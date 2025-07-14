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
              <img src="${pageContext.request.contextPath}/volt/assets/img/alert-icon.png"
                   class="avatar-md rounded" alt="알림">
            </div>
            <div class="col ps-0 ms-2">
              <div class="d-flex justify-content-between align-items-center">
                <h4 class="h6 mb-0 text-small">
                  ${noti.notifTitle}
                  <c:if test="${noti.notifReadYn eq 'N'}">
                    <span class="badge bg-primary ms-2">새로 도착</span>
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
