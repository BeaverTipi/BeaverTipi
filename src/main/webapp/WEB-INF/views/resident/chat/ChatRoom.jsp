<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>채팅방</title>
<link rel ="stylesheet" href = "/app/css/chat/chatRoom.css">
</head>
<body>
  <c:if test="${param.popup eq 'true'}">
    <div class="header">
      <img src="/volt/assets/img/brand/dark.png" class="chatimg" alt="Logo">
      <button id="leaveChatRoomBtn" class="sidebar-toggle-btn-small">🚪</button>
      <button id="toggleSidebarBtn" class="sidebar-toggle-btn">👥</button>
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

  <div id="chatSidebar" class="chat-sidebar">
    <div class="sidebar-header">
      <h3>참여자 목록</h3>
      <button id="closeSidebarBtn">✖</button>
    </div>
    <ul id="participantList" class="participant-list"></ul>
    <button id="inviteBtn" class="invite-btn">+ 초대하기</button>
  </div>

  <!-- ✅ 채팅방 나가기 확인 모달 -->
	<div id="leaveModal" class="modal">
	  <p>정말 채팅방을 나가시겠습니까?</p>
	  <div class="modal-buttons">
	    <button id="confirmLeaveBtn">예</button>
	    <button id="cancelLeaveBtn">아니오</button>
	  </div>
	</div>
	<script>
  window.chatInfo = {
    residentChatRoomId: "${residentChatRoomId}",
    loginMbrCd: "${mbrCd}",
    bldgId: "${bldgId}"
  };
</script>
  <script src="${pageContext.request.contextPath}/app/js/chat/chatRoom.js"></script>
</body>
</html>