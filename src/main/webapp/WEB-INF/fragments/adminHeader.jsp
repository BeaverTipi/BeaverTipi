<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<nav class="navbar navbar-expand-lg px-4 py-2" style="background-color: transparent; justify-content: end; box-shadow: none;">
	<div class="container-fluid d-flex justify-content-end align-items-center">
		<!-- 🔵 우측 메뉴 -->
		<ul class="navbar-nav flex-row gap-4 align-items-center mb-0">
			<li class="nav-item"><a class="nav-link" href="#">공지사항</a></li>
			<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/main/map">지도</a></li>
			<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/member/product/add">매물등록</a></li>

			<security:authorize access="isAuthenticated()">
				<security:authentication property="principal" var="principal"/>

				<li class="nav-item">
					<a class="nav-link" href="${pageContext.request.contextPath}/account/read">마이페이지</a>
				</li>
				<li class="nav-item">
					<a class="nav-link" href="${pageContext.request.contextPath}/resident/chat" id="chatSidebarLink">채팅</a>
				</li>

				<!-- 🔔 알림 -->
				<!-- 🔔 알림 -->
<li class="nav-item dropdown">
	<a class="nav-link text-dark notification-bell dropdown-toggle" id="notificationDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
		<span class="position-relative d-inline-block">
			<svg class="icon icon-sm text-dark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="20" height="20">
				<path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6zM10 18a3 3 0 01-3-3h6a3 3 0 01-3 3z"/>
			</svg>
			<c:if test="${unreadCount > 0}">
				<span class="position-absolute top-0 start-100 translate-middle badge rounded-pill bg-danger">
					${unreadCount}
				</span>
			</c:if>
		</span>
	</a>

	<div class="dropdown-menu dropdown-menu-lg dropdown-menu-end mt-2 py-0">
		<div class="list-group list-group-flush">
			<a href="#" class="text-center text-notification fw-bold border-bottom border-silver py-3">Notifications</a>

			<c:choose>
				<c:when test="${not empty notifications}">
					<c:forEach var="notification" items="${notifications}" varStatus="staus">
						<a href="/notification/read/${notification.notifId}" class="list-group-item list-group-item-action border-bottom">
							<div class="row align-items-center">
								<div class="col-auto">
									<img src="${pageContext.request.contextPath}/volt/assets/img/team/profile-picture-1.jpg" class="avatar-md rounded" alt="알림">
								</div>
								<div class="col ps-0 ms-2">
									<div class="d-flex justify-content-between align-items-center">
										<h4 class="h6 mb-0 text-small">${notification.notifTitle}</h4>
										<small class="text-muted">${notification.notifDt}</small>
									</div>
									<p class="font-small mt-1 mb-0">${notification.notifMsg}</p>
								</div>
							</div>
						</a>
						    <c:if test="${status.last}">
						        <!-- 🔽 마지막 알림 이후에만 보더 라인 추가 -->
						        <div class="border-silver"></div>
   							 </c:if>
					</c:forEach>
				</c:when>
				<c:otherwise>
					<div class="text-center py-3 text-muted border-silver">알림이 없습니다.</div>
				</c:otherwise>
			</c:choose>

			<a href="#" class="dropdown-item text-center fw-bold rounded-bottom py-3"
			   data-bs-toggle="modal" data-bs-target="#notificationModal">
				<svg class="icon icon-xxs text-gray-400 me-1" fill="currentColor" viewBox="0 0 20 20">
					<path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
					<path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
				</svg>
				전체 알림 보기
			</a>
		</div>
	</div>
</li>


				<!-- 👤 사용자 드롭다운 -->
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle d-flex align-items-center gap-2" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<span class="text-dark">${principal.realUser.mbrNm}</span>
						<c:choose>
								<c:when test="${not empty principal.realUser.memberFile}">
									<img class="avatar rounded-circle" alt="Profile"
										src="${principal.realUser.memberFile.filePathUrl}"
										height="32" width="32">
								</c:when>
								<c:otherwise>
									<img class="avatar rounded-circle" alt="Default Profile"
										src="${pageContext.request.contextPath}/volt/assets/img/team/beaver.png"
										height="32" width="32">
								</c:otherwise>
							</c:choose>
					</a>
					<ul class="dropdown-menu dropdown-menu-end mt-2" aria-labelledby="userDropdown">
						<li><a class="dropdown-item" href="${pageContext.request.contextPath}/resident/myhouse">마이하우스(입주민)</a></li>
						<li><a class="dropdown-item" href="${pageContext.request.contextPath}/building/myhouse">마이하우스(임대인)</a></li>
						<li><a class="dropdown-item" href="#" id="brokerOfficeLink">마이오피스(중개인)</a></li>
						<li><a class="dropdown-item" href="${pageContext.request.contextPath}/admin/main">시스템 관리자</a></li>
						<li><a class="dropdown-item" href="${pageContext.request.contextPath}/subscribe/subscription">구독/승인</a></li>
						<li><hr class="dropdown-divider"></li>
						<li><a class="dropdown-item text-danger" id="logout" href="${pageContext.request.contextPath}/account/logout">로그아웃</a></li>
					</ul>
				</li>
			</security:authorize>

			<security:authorize access="isAnonymous()">
				<li class="nav-item"><a class="nav-link" href="${pageContext.request.contextPath}/member/register">회원가입</a></li>
				<li class="nav-item">
					<a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#loginModal">
						<i class="bi bi-person-circle fs-4"></i>
					</a>
				</li>
			</security:authorize>
		</ul>
	</div>
</nav>
<!-- ✅ 전체 알림 모달 with AJAX 페이징 -->
<div class="modal fade" id="notificationModal" tabindex="-1" aria-labelledby="notificationModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-scrollable modal-lg modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="notificationModalLabel">전체 알림</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
      </div>
      <div class="modal-body" id="notificationModalContent">
        <!-- 알림 목록이 AJAX로 로드됩니다. -->
      </div>
    </div>
  </div>
</div>
<security:authorize access="!isAuthenticated()">
  <script>
    location.href = '${pageContext.request.contextPath}/';
  </script>
</security:authorize>