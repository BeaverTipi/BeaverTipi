/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 15.     	     김재윤            최초작성
 *
 * </pre>
 */
const { crId, loginMbrCd } = window.chatInfo;
const socket = new WebSocket("ws://" + location.host + "/ws/brokerChat?crId=" + crId);

socket.onopen = () => console.log("✅ WebSocket 연결 성공");

socket.onmessage = (event) => {
  try {
    const msg = JSON.parse(event.data);
    const isMine = msg.mbrCd === loginMbrCd;
    const nickname = msg.member?.mbrNnm ?? "";
    const content = (msg.cmCont || "").replace(/\n/g, "<br>");

    displayMessage(nickname, content, isMine);
  } catch (err) {
    console.error("❌ 메시지 파싱 오류:", err);
    console.log("⛔ 원시 데이터:", event.data);
  }
};

socket.onerror = (error) => console.error("❌ WebSocket 오류:", error);
socket.onclose = (event) => console.warn(`⚠️ WebSocket 종료 (코드: ${event.code}, 이유: ${event.reason})`);

function sendMessage() {
  const input = document.getElementById("messageInput");
  const content = input.value.trim();
  if (!content) return;

  const msg = {
    crId,
    mbrCd: loginMbrCd,
    cmCont: content.replace(/\n/g, "\\n")
  };

  if (socket.readyState === WebSocket.OPEN) {
    socket.send(JSON.stringify(msg));
    console.log("✉️ 메시지 전송:", msg);

    displayMessage("", content, true); // 내 메시지는 직접 렌더링
  } else {
    console.warn("⚠️ WebSocket 닫힘 상태 → 메시지 전송 실패");
  }

  input.value = "";
}

function displayMessage(senderNickname, message, isMine) {
  const chatbox = document.getElementById("chatbox");

  if (!isMine && senderNickname) {
    const nameDiv = document.createElement("div");
    nameDiv.classList.add("sender-name");
    nameDiv.textContent = senderNickname;
    chatbox.appendChild(nameDiv);
  }

  const messageDiv = document.createElement("div");
  messageDiv.classList.add("message", isMine ? "my-message" : "other-message");
  messageDiv.innerHTML = message;
  chatbox.appendChild(messageDiv);

  chatbox.scrollTop = chatbox.scrollHeight;
}

document.getElementById("messageInput").addEventListener("keydown", (e) => {
  if (e.key === "Enter" && !e.shiftKey) {
    sendMessage();
    e.preventDefault();
  }
});

document.getElementById("leaveChatRoomBtn").addEventListener("click", () => {
  document.getElementById("leaveModal").style.display = "block";
});

document.getElementById("cancelLeaveBtn").addEventListener("click", () => {
  document.getElementById("leaveModal").style.display = "none";
});

document.getElementById("confirmLeaveBtn").addEventListener("click", () => {
  const formData = new URLSearchParams();
  formData.append("crId", crId);

  fetch("/broker/chat/room/leave", {
    method: "POST",
    headers: { "Content-Type": "application/x-www-form-urlencoded" },
    body: formData.toString()
  })
  .then(res => {
    if (res.ok) {
      alert("채팅방에서 나갔습니다.");
      window.opener?.refreshChatRoomList?.();
      window.close();
    } else {
      alert("채팅방 나가기 실패");
    }
  });

  document.getElementById("leaveModal").style.display = "none";
});