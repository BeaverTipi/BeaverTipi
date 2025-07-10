/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 10.     	     김재윤            최초작성
 *
 * </pre>
 */

const residentChatRoomId = window.chatInfo.residentChatRoomId;
const loginMbrCd = window.chatInfo.loginMbrCd;
const bldgId = window.chatInfo.bldgId;
const socket = new WebSocket("ws://" + location.host + "/ws/chat?residentChatRoomId=" + residentChatRoomId);

    socket.onopen = function () {
      console.log(" WebSocket 연결 성공");
    };

    socket.onmessage = function (event) {
      try {
        const msg = JSON.parse(event.data);
        console.log(" 메시지 수신:", msg);
        const isMine = msg.mbrCd === loginMbrCd;

        const chatbox = document.getElementById("chatbox");

        if (!isMine) {
          const nameDiv = document.createElement("div");
          nameDiv.classList.add("sender-name");
          nameDiv.textContent = msg.unitRoom + "호 " + msg.mbrNnm;
          chatbox.appendChild(nameDiv);
        }

        const messageDiv = document.createElement("div");
        messageDiv.classList.add("message", isMine ? "my-message" : "other-message");
        messageDiv.innerHTML = msg.rcmCont.replace(/\n/g, "<br>");
        chatbox.appendChild(messageDiv);

        chatbox.scrollTop = chatbox.scrollHeight;
      } catch (e) {
        console.error(" 메시지 파싱 오류:", e);
        console.log("수신된 원시 메시지:", event.data);
      }
    };

    socket.onerror = function (error) {
      console.error(" WebSocket 오류:", error);
    };

    socket.onclose = function (event) {
      console.warn(` WebSocket 종료 (코드: ${event.code}, 이유: ${event.reason})`);
    };

    function sendMessage() {
      const input = document.getElementById("messageInput");
      const content = input.value.trim();
      if (!content) return;

      const msg = {
        residentChatRoomId: residentChatRoomId,
        mbrCd: loginMbrCd,
        rcmCont: content.replace(/\n/g, "\\n")
      };

      if (socket.readyState === WebSocket.OPEN) {
        socket.send(JSON.stringify(msg));
        console.log(" 메시지 전송:", msg);
        displayMessage("", content, true);
      } else {
        console.warn("WebSocket이 열려 있지 않음. 메시지 전송 실패");
      }

      input.value = "";
    }

    function displayMessage(senderNickname, message, isMyMessage) {
      const chatbox = document.getElementById("chatbox");

      if (!isMyMessage && senderNickname) {
        const nameDiv = document.createElement("div");
        nameDiv.classList.add("sender-name");
        nameDiv.textContent = senderNickname;
        chatbox.appendChild(nameDiv);
      }

      const messageDiv = document.createElement("div");
      messageDiv.classList.add("message", isMyMessage ? "my-message" : "other-message");
      messageDiv.innerHTML = message.replace(/\n/g, "<br>");
      chatbox.appendChild(messageDiv);

      chatbox.scrollTop = chatbox.scrollHeight;
    }

    document.getElementById("messageInput").addEventListener("keydown", function (event) {
      if (event.key === "Enter" && !event.shiftKey) {
        sendMessage();
        event.preventDefault();
      }
    });

    // ✅ 사이드바 열기 기능
    const sidebar = document.getElementById("chatSidebar");
    const toggleBtn = document.getElementById("toggleSidebarBtn");
    const closeBtn = document.getElementById("closeSidebarBtn");
    const participantList = document.getElementById("participantList");

    toggleBtn.addEventListener("click", () => {
      sidebar.classList.add("visible");
      loadParticipants();
    });

    closeBtn.addEventListener("click", () => {
      sidebar.classList.remove("visible");
    });

    function loadParticipants() {
      fetch(`/chat/participants?residentChatRoomId=${residentChatRoomId}`)
        .then(res => res.json())
        .then(data => {
          participantList.innerHTML = "";
          data.forEach(p => {
            const li = document.createElement("li");
            li.textContent = `${p.unitRoom}호 ${p.mbrNnm}`;
            participantList.appendChild(li);
          });
        })
        .catch(err => {
          console.error("참여자 목록 로딩 실패:", err);
        });
    }

    // ✅ 채팅방 나가기 버튼 클릭 → 모달 표시
    document.getElementById("leaveChatRoomBtn").addEventListener("click", () => {
      document.getElementById("leaveModal").style.display = "block";
    });

    // ✅ 아니오 클릭 → 모달 닫기
    document.getElementById("cancelLeaveBtn").addEventListener("click", () => {
      document.getElementById("leaveModal").style.display = "none";
    });

	// ✅ 예 클릭 → 탈퇴 처리
	document.getElementById("confirmLeaveBtn").addEventListener("click", () => {
	  // ✅ 서버에 residentChatRoomId만 전송
	  const formData = new URLSearchParams();
	  formData.append("residentChatRoomId", residentChatRoomId);
	
	  fetch("/resident/chat/room/leave", {
	    method: "POST",
	    headers: { "Content-Type": "application/x-www-form-urlencoded" },
	    body: formData.toString()
	  })
	  .then(res => {
	    if (res.ok) {
	      alert("채팅방에서 나갔습니다.");
	
	      // ✅ WebSocket 명시적으로 종료 (1000 코드)
	      if (socket.readyState === WebSocket.OPEN || socket.readyState === WebSocket.CONNECTING) {
	        socket.close(1000, "사용자가 퇴장함");
	      }
	
	      window.close();
	    } else {
	      alert("채팅방 나가기 실패");
	    }
	  })
	  .catch(err => {
	    console.error("나가기 요청 오류:", err);
	  });
	
	  // ✅ 모달 닫기
	  document.getElementById("leaveModal").style.display = "none";
	});
	
	document.getElementById("inviteBtn")?.addEventListener("click", () => {
  const bldgId = window.chatInfo?.bldgId; 
  const residentChatRoomId = window.chatInfo?.residentChatRoomId;

  const popupUrl = `/resident/chat/residentList?bldgId=${bldgId}&popup=true&mode=invite`;
  window.open(popupUrl, "inviteResidentPopup", "width=700,height=600");
});