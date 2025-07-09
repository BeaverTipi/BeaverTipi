<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>채팅방</title>
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background-color: #f0f0f0;
      margin: 0;
      padding: 0;
    }

    .header {
      display: flex;
      align-items: center;
      padding: 10px;
      background-color: #ffffff;
      border-bottom: 1px solid #ccc;
    }

    .chatimg {
      width: 160px;
      height: 40px;
      margin-right: 10px;
    }

    .header h1 {
      font-size: 20px;
      margin: 0;
    }

    #chatbox {
      height: 400px;
      overflow-y: auto;
      border: 1px solid #ccc;
      padding: 10px;
      display: flex;
      flex-direction: column;
      background-color: #ffffff;
      margin: 10px auto;
      max-width: 600px;
      border-radius: 8px;
    }

    .message {
      margin: 5px 0;
      max-width: 80%;
      word-wrap: break-word;
      padding: 10px 14px;
      border-radius: 16px;
      font-size: 14px;
      line-height: 1.5;
    }

    .my-message {
      align-self: flex-end;
      background-color: #d1e7dd;
    }

    .other-message {
      align-self: flex-start;
      background-color: #f8d7da;
    }

    .sender-name {
      font-weight: bold;
      font-size: 13px;
      margin-bottom: 2px;
      color: #333;
      align-self: flex-start;
    }

    .input-container {
      display: flex;
      align-items: center;
      max-width: 600px;
      margin: 0 auto 20px;
      padding: 0 10px;
    }

    #messageInput {
      flex: 1;
      padding: 10px;
      resize: none;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 5px;
      margin-right: 8px;
      height: 60px;
    }

    button {
      width: 80px;
      height: 60px;
      background-color: #3572ef;
      color: white;
      border: none;
      border-radius: 5px;
      font-weight: bold;
      font-size: 14px;
      cursor: pointer;
      transition: background 0.3s ease;
    }

    button:hover {
      background-color: #6a93ff;
    }
  </style>
</head>
<body>

  <c:if test="${param.popup eq 'true'}">
    <div class="header">
      <img src="/volt/assets/img/brand/dark.png" class="chatimg" alt="Logo">
    </div>

    <div id="chatbox">
      <c:forEach var="msg" items="${messages}">
        <c:choose>
          <c:when test="${msg.mbrCd eq mbrCd}">
            <div class="message my-message">${msg.rcmCont}</div>
          </c:when>
          <c:otherwise>
            <div class="sender-name">${msg.unitRoom}호 ${msg.mbrNnm}</div>
            <div class="message other-message">${msg.rcmCont}</div>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>

    <div class="input-container">
      <textarea id="messageInput" placeholder="메시지를 입력하세요" rows="3"></textarea>
      <button onclick="sendMessage()">전송</button>
    </div>
  </c:if>

  <script>
    const residentChatRoomId = "${residentChatRoomId}";
    const loginMbrCd = "${mbrCd}";
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
      console.warn(`🔌 WebSocket 종료 (코드: ${event.code}, 이유: ${event.reason})`);
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
  </script>
</body>
</html>