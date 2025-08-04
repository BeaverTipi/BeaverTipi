<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일               수정자           수정내용
 *  ============      ============== =======================
 *  2025. 7. 15.           김재윤           최초 생성
 *  2025. 7. 27.		   김남혁			  신고 기능 추가
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
     
     <%-- 현재 로그인한 사용자(mbrCd)와 bcVO의 sellerCd, inquirerCd를 비교하여 상대방 ID를 찾음 --%>
    <c:set var="reportTargetMbrId" value=""/> <%-- 초기화 --%>
    <c:choose>
        <c:when test="${mbrCd eq bcVO.sellerCd}">
            <%-- 내가 판매자라면, 상대방은 문의자 --%>
            <c:set var="reportTargetMbrId" value="${inquirerMemberVO.mbrId}"/>
        </c:when>
        <c:otherwise>
            <%-- 내가 문의자라면 상대방은 판매자 --%>
            <c:set var="reportTargetMbrId" value="${sellerMemberVO.mbrId}"/>
        </c:otherwise>
    </c:choose>
  
    <div class="header">
      <img src="/volt/assets/img/brand/dark.png" class="chatimg" alt="Logo">
      <button id="leaveChatRoomBtn" class="sidebar-toggle-btn-small">🚪</button>
      <img id="warningIcon" src="/volt/assets/img/icons/warning-svgrepo-com.svg" alt="경고 아이콘">
      
      <%-- 계산된 상대방 ID를 hidden 필드에 설정 --%>
      <input type="hidden" id="reportTargetIdValue" value="${reportTargetMbrId}">
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
     var contextPath = '${pageContext.request.contextPath}';
  
    window.chatInfo = {
      crId: "${bcVO.crId}",
      loginMbrCd: "${mbrCd}"
    };
    
    // 경고 아이콘 클릭 이벤트 처리
    document.getElementById('warningIcon').addEventListener('click', function() {
        var reportTargetId = document.getElementById('reportTargetIdValue').value;
        var reportType = "MEMB"; // 신고 유형은 '회원'을 의미하는 코드

        // 신고 대상 ID가 비어있으면 오류
        if (!reportTargetId || reportTargetId.trim() === '') {
            alert('신고 대상을 식별할 수 없습니다.');
            return;
        }

        // 신고 작성 페이지 URL 생성
        var reportUrl = contextPath + "/member/report/createForm?targetId=" + reportTargetId + "&type=" + reportType + "&popup=true";
		
        // 채팅하던 페이지에서 신고 작성 페이지 열기
//         window.location.href = reportUrl;
        // 새로운 창으로 신고 작성 페이지 열기 (팝업 형태로)
		window.open(reportUrl, '_blank', 'width=800,height=600,scrollbars=yes,resizable=yes');
    });
    
    
  </script>
  <script src="${pageContext.request.contextPath}/app/js/main/chat/brokerChatRoom.js"></script>
</body>
</html>