<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags"
	prefix="security"%>

<nav class="navbar navbar-expand-lg bg-white px-4 py-2">
	<div
		class="container-fluid d-flex justify-content-between align-items-center">
		<a class="navbar-brand d-flex align-items-center"
			href="${pageContext.request.contextPath}/"> <img
			src="${pageContext.request.contextPath}/volt/assets/img/brand/dark.png"
			alt="Logo" class="me-2">
		</a>
		<ul class="navbar-nav flex-row gap-4 align-items-center mb-0">
			<li class="nav-item"><a class="nav-link" href="#">공지사항</a></li>
			<security:authorize access="isAuthenticated()">
				<li class="nav-item"><a class="nav-link fw-bold"
					href="${pageContext.request.contextPath}/account/read">마이페이지</a></li>
			</security:authorize>
			<security:authorize access="isAnonymous()">
				<li class="nav-item"><a class="nav-link "
					href="${pageContext.request.contextPath}/member/register">회원가입</a>
				</li>

			</security:authorize>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/main/map">지도</a></li>
			<li class="nav-item"><a class="nav-link"
				href="${pageContext.request.contextPath}/member/product/add">상품등록</a></li>

			<security:authorize access="isAuthenticated()">
				<li class="nav-item dropdown"><a
					class="nav-link dropdown-toggle" href="#" id="userDropdown"
					role="button" data-bs-toggle="dropdown" aria-expanded="false">
						<i class="bi bi-person-circle fs-4"></i>
				</a>
					<ul class="dropdown-menu dropdown-menu-end mt-2"
						aria-labelledby="userDropdown">
						<li><a class="dropdown-item d-flex align-items-center"
							href="${pageContext.request.contextPath}/resident/myhouse">마이하우스(입주민)</a></li>
						<li><a class="dropdown-item d-flex align-items-center"
							href="${pageContext.request.contextPath}/building/myhouse">마이하우스(임대인)</a></li>
						<li><a class="dropdown-item d-flex align-items-center"
							href="http://localhost:81/broker/myoffice">마이오피스(중개인)</a></li>
						<li><a class="dropdown-item d-flex align-items-center"
							href="${pageContext.request.contextPath}/admin/main">시스템 관리자</a></li>
						<li><a class="dropdown-item d-flex align-items-center"
							href="${pageContext.request.contextPath}/subscribe/subscription">구독/승인</a></li>

						<li><hr class="dropdown-divider"></li>
						<li><a
							class="dropdown-item d-flex align-items-center text-danger"
							id="logout" href="/account/logout">로그아웃</a></li>
					</ul></li>
			</security:authorize>

			<security:authorize access="isAnonymous()">
				<li class="nav-item"><a class="nav-link" href="#"
					data-bs-toggle="modal" data-bs-target="#loginModal"> <i
						class="bi bi-person-circle fs-4"></i>
				</a></li>
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