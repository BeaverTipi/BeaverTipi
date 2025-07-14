<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<nav class="navbar navbar-expand-lg bg-white px-4 py-2">
	<div class="container-fluid d-flex justify-content-between align-items-center">
		<!-- 🔵 좌측 로고 -->
		<a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/">
			<img src="${pageContext.request.contextPath}/volt/assets/img/brand/dark.png" alt="Logo" class="me-2" style="height: 32px;">
		</a>

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
							<a href="#" class="text-center text-notification fw-bold border-bottom border-silver py-3 text-main">Notifications</a>
				
							<c:choose>
								<c:when test="${not empty notifications}">
									<c:forEach var="notification" items="${notifications}" varStatus="status" >
										<a href="${notification.url}" class="list-group-item list-group-item-action border-bottom">
											<div class="row align-items-center">
												<div class="col-auto">
													<img src="${pageContext.request.contextPath}/volt/assets/img/team/profile-picture-1.jpg" class="avatar-md rounded" alt="알림">
												</div>
												<div class="col ps-0 ms-2">
													<div class="d-flex justify-content-between align-items-center">
														<h4 class="h6 mb-0 text-small">${notification.title}</h4>
														<small class="text-muted">${notification.createdAt}</small>
													</div>
													<p class="font-small mt-1 mb-0">${notification.message}</p>
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
				
							<a href="#" class="dropdown-item text-notification text-center fw-bold rounded-bottom py-3"
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

<!-- 🔐 로그인 모달 -->
<div class="modal fade" id="loginModal" tabindex="-1"
	aria-labelledby="loginModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-dialog-centered">
		<div class="modal-content">
			<form method="post" id="loginForm"
				action="${pageContext.request.contextPath}/account/login">
				<security:csrfInput />
				<div class="modal-header">
					<h5 class="modal-title" id="loginModalLabel">로그인</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="닫기"></button>
				</div>
				<div class="modal-body">

					<!-- 역할 선택 -->
					<div class="mb-3">
						<label class="form-label">로그인 유형 선택</label>
						<div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="loginRole"
									id="roleLandlord" value="landlord"> <label
									class="form-check-label" for="roleLandlord">임대인</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="loginRole"
									id="roleBroker" value="broker"> <label
									class="form-check-label" for="roleBroker">공인중개사</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="loginRole"
									id="roleAdmin" value="admin"> <label
									class="form-check-label" for="roleAdmin">시스템 관리자</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="loginRole"
									id="roleResident" value="resident"> <label
									class="form-check-label" for="roleResident">입주민</label>
							</div>
							<div class="form-check form-check-inline">
								<input class="form-check-input" type="radio" name="loginRole"
									id="roleMember" value="member" checked> <label
									class="form-check-label" for="roleMember">일반회원</label>
							</div>
						</div>
					</div>

					<div class="mb-3">
						<label for="username" class="form-label">아이디</label> <input
							type="text" class="form-control" name="mbrId" id="username">
					</div>
					<div class="mb-3">
						<label for="password" class="form-label">비밀번호</label> <input
							type="password" class="form-control" name="mbrPw" id="password">
					</div>
					<div id="login-error-msg" class="alert alert-danger"
						style="display: none;">아이디 또는 비밀번호가 올바르지 않습니다.</div>
					<button type="submit" class="btn btn-primary w-100">로그인</button>

					<div class="text-center my-3 small-link-group">
						<a href="#" class="small-link">아이디 찾기</a> | <a href="#"
							class="small-link">비밀번호 찾기</a> | <a
							href="${pageContext.request.contextPath}/member/register"
							class="small-link">회원가입</a>
					</div>

					<div class="d-grid gap-2">
						<a
							href="${pageContext.request.contextPath}/oauth2/authorization/google-login"
							class="btn social-btn google-btn"> <img
							src="${pageContext.request.contextPath}/volt/assets/img/authentication/google.svg"
							alt="Google"> <span class="d-none d-sm-inline-block">Google
								로그인</span>
						</a> <a
							href="${pageContext.request.contextPath}/oauth2/authorization/kakao"
							class="btn social-btn kakao-btn"> <img
							src="${pageContext.request.contextPath }/volt/assets/img/authentication/kakao.png"
							alt="img">
						</a>
					</div>

				</div>
			</form>
		</div>
	</div>
</div>
<!-- 🔔 전체 알림 모달 -->
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

<!-- ✅ JS는 별도 파일(notification-modal.js)로 분리됨 -->
