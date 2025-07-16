<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 15.     		김재윤           최초 생성
 *
-->

<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>브로커 채팅방</title>
  <link rel="stylesheet" href="/app/css/chat/chatRoom.css">
</head>
<body>
  <c:if test="${param.popup eq 'true'}">
    <div class="header">
      <img src="/volt/assets/img/brand/dark.png" class="chatimg" alt="Logo">
      <button id="leaveChatRoomBtn" class="sidebar-toggle-btn-small">🚪</button>
    </div>

    <div id="chatbox">
      <c:forEach var="msg" items="${messages}">
        <c:choose>
          <c:when test="${msg.mbrCd eq mbrCd}">
            <div class="message my-message">${msg.cmCont}</div>
          </c:when>
          <c:otherwise>
            <c:if test="${not empty msg.member and not empty msg.member.mbrNnm}">
              <div class="sender-name">${msg.member.mbrNnm}</div>
            </c:if>
            <div class="message other-message">${msg.cmCont}</div>
          </c:otherwise>
        </c:choose>
      </c:forEach>
    </div>

    <div class="input-container">
      <textarea id="messageInput" placeholder="메시지를 입력하세요" rows="3"></textarea>
      <button onclick="sendMessage()">전송</button>
    </div>
  </c:if>

  <div id="leaveModal" class="modal">
    <p>정말 채팅방을 나가시겠습니까?</p>
    <div class="modal-buttons">
      <button id="confirmLeaveBtn">예</button>
      <button id="cancelLeaveBtn">아니오</button>
    </div>
  </div>

  <script>
    window.chatInfo = {
      crId: "${bcVO.crId}",
      loginMbrCd: "${mbrCd}"
    };
  </script>
  <script src="${pageContext.request.contextPath}/app/js/main/chat/brokerChatRoom.js"></script>
</body>
</html>