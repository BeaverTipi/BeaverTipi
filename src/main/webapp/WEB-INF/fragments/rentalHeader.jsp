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
				<li class="nav-item dropdown">
					<a class="nav-link text-dark notification-bell unread dropdown-toggle" id="notificationDropdown" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
  <span class="position-relative d-inline-block">
    <svg class="icon icon-sm text-dark" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 20 20" width="20" height="20">
      <path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6zM10 18a3 3 0 01-3-3h6a3 3 0 01-3 3z"/>
    </svg>
  </span>
</a>

					<div class="dropdown-menu dropdown-menu-lg dropdown-menu-end mt-2 py-0">
						<div class="list-group list-group-flush">
							<a href="#" class="text-center text-primary fw-bold border-bottom border-light py-3">Notifications</a>
							<c:forEach var="i" begin="1" end="5">
								<a href="#" class="list-group-item list-group-item-action border-bottom">
									<div class="row align-items-center">
										<div class="col-auto">
											<img src="${pageContext.request.contextPath}/volt/assets/img/team/profile-picture-${i}.jpg" class="avatar-md rounded" alt="알림">
										</div>
										<div class="col ps-0 ms-2">
											<div class="d-flex justify-content-between align-items-center">
												<h4 class="h6 mb-0 text-small">User ${i}</h4>
												<small class="text-muted">${i} hrs ago</small>
											</div>
											<p class="font-small mt-1 mb-0">메시지 예시 ${i}</p>
										</div>
									</div>
								</a>
							</c:forEach>
							<a href="#" class="dropdown-item text-center fw-bold rounded-bottom py-3">
								<svg class="icon icon-xxs text-gray-400 me-1" fill="currentColor" viewBox="0 0 20 20">
									<path d="M10 12a2 2 0 100-4 2 2 0 000 4z"/>
									<path fill-rule="evenodd" d="M.458 10C1.732 5.943 5.522 3 10 3s8.268 2.943 9.542 7c-1.274 4.057-5.064 7-9.542 7S1.732 14.057.458 10zM14 10a4 4 0 11-8 0 4 4 0 018 0z" clip-rule="evenodd"/>
								</svg> View all
							</a>
						</div>
					</div>
				</li>

				<!-- 👤 사용자 드롭다운 -->
				<li class="nav-item dropdown">
					<a class="nav-link dropdown-toggle d-flex align-items-center gap-2" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<span class="text-dark">${principal.realUser.mbrNm}</span>
						<img class="avatar rounded-circle" alt="Profile" src="${pageContext.request.contextPath}/volt/assets/img/team/profile-picture-3.jpg" height="32" width="32">
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

<security:authorize access="!isAuthenticated()">
  <script>
    location.href = '${pageContext.request.contextPath}/';
  </script>
</security:authorize>