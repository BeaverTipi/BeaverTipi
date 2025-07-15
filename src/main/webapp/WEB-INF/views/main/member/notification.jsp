<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/notification.css">
<div id="notificationListFragment">
  <c:choose>
    <c:when test="${not empty notifications}">
      <c:forEach var="noti" items="${notifications}">
        <a href="/notification/read/${noti.notifId}"
           class="list-group-item list-group-item-action border-bottom 
                  ${noti.notifReadYn eq 'N' ? 'fw-bold' : ''}">
          <div class="row align-items-center">
            <div class="col-auto">
              <svg xmlns="http://www.w3.org/2000/svg" width="30" height="30" fill="currentColor" class="bi bi-alarm" viewBox="0 0 16 16">
				  <path d="M8.5 5.5a.5.5 0 0 0-1 0v3.362l-1.429 2.38a.5.5 0 1 0 .858.515l1.5-2.5A.5.5 0 0 0 8.5 9z"/>
				  <path d="M6.5 0a.5.5 0 0 0 0 1H7v1.07a7.001 7.001 0 0 0-3.273 12.474l-.602.602a.5.5 0 0 0 .707.708l.746-.746A6.97 6.97 0 0 0 8 16a6.97 6.97 0 0 0 3.422-.892l.746.746a.5.5 0 0 0 .707-.708l-.601-.602A7.001 7.001 0 0 0 9 2.07V1h.5a.5.5 0 0 0 0-1zm1.038 3.018a6 6 0 0 1 .924 0 6 6 0 1 1-.924 0M0 3.5c0 .753.333 1.429.86 1.887A8.04 8.04 0 0 1 4.387 1.86 2.5 2.5 0 0 0 0 3.5M13.5 1c-.753 0-1.429.333-1.887.86a8.04 8.04 0 0 1 3.527 3.527A2.5 2.5 0 0 0 13.5 1"/>
				</svg>
            </div>
            <div class="col ps-0 ms-2">
              <div class="d-flex justify-content-between align-items-center">
               <h4 class="h6 mb-0 text-small notif-title-wrapper">
				  <span>${noti.notifTitle}</span>
				  <c:if test="${noti.notifReadYn eq 'N'}">
				    <span class="notif-badge">새로 도착</span>
				  </c:if>
				</h4>
                <small class="text-muted">${noti.notifDt}</small>
              </div>
              <p class="font-small mt-1 mb-0">${noti.notifMsg}</p>
            </div>
          </div>
        </a>
      </c:forEach>
    </c:when>
    <c:otherwise>
      <div class="text-center text-muted py-3">알림이 없습니다.</div>
    </c:otherwise>
  </c:choose>

  <div class="mt-3 text-center pagination-wrapper">
    ${pagingHTML}
  </div>
</div>
