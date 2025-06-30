<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<nav class="navbar navbar-top navbar-expand navbar-dashboard navbar-dark ps-0 pe-2 pb-0">
  <div class="container-fluid px-0">
    <div class="d-flex justify-content-between align-items-center w-100">

      <!-- 좌측 로고 -->
      <div class="d-flex align-items-center">
        <a class="navbar-brand text-white fw-bold" href="${pageContext.request.contextPath}/">beavertipi</a>
      </div>

      <!-- 우측 메뉴 -->
      <ul class="navbar-nav d-flex align-items-center flex-wrap">

        <!-- 공통 메뉴 -->
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html">
            <span class="font-large text-gray-800">공지사항</span>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/map">
            <span class="font-large text-gray-800">지도</span>
          </a>
        </li>
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/product/register">
            <span class="font-large text-gray-800">상품등록</span>
          </a>
        </li>

        <!-- 권한별 메뉴 -->
        <security:authorize access="hasRole('ROLE_RESIDENT')">
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/resident/myhouse">
              <span class="font-large text-gray-800">마이하우스</span>
            </a>
          </li>
        </security:authorize>
        <security:authorize access="hasRole('ROLE_OWNER')">
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/building/myhouse">
              <span class="font-large text-gray-800">마이하우스</span>
            </a>
          </li>
        </security:authorize>
        <security:authorize access="isAnonymous()">
          <li class="nav-item">
            <a class="nav-link" href="${pageContext.request.contextPath}/register">
              <span class="font-large text-gray-800">회원가입</span>
            </a>
          </li>
        </security:authorize>

        <!-- 내 정보 -->
        <li class="nav-item">
          <a class="nav-link" href="${pageContext.request.contextPath}/user/profile">
            <span class="font-large text-gray-800">내 정보</span>
          </a>
        </li>

        <!-- 🔔 알림 드롭다운 -->
        <li class="nav-item dropdown">
          <a class="nav-link text-dark dropdown-toggle" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <svg class="icon icon-sm text-gray-900" fill="currentColor" viewBox="0 0 20 20" xmlns="http://www.w3.org/2000/svg">
              <path d="M10 2a6 6 0 00-6 6v3.586l-.707.707A1 1 0 004 14h12a1 1 0 00.707-1.707L16 11.586V8a6 6 0 00-6-6zM10 18a3 3 0 01-3-3h6a3 3 0 01-3 3z"></path>
            </svg>
          </a>
          <ul class="dropdown-menu dropdown-menu-end mt-2">
            <li class="dropdown-header text-primary fw-bold px-3">🔔 Notifications</li>
            <li><hr class="dropdown-divider"></li>
            <li class="px-3">새로운 알림이 없습니다.</li>
          </ul>
        </li>

        <!-- 👤 사용자 프로필 드롭다운 -->
        <li class="nav-item dropdown ms-lg-3">
          <a class="nav-link dropdown-toggle pt-1 px-0" href="#" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <div class="d-flex align-items-center">
              <div class="d-none d-lg-block text-dark ms-2">
                <span class="fw-bold text-gray-900">사용자</span>
              </div>
              <img class="avatar rounded-circle ms-2" alt="Profile" src="${pageContext.request.contextPath}/volt/assets/img/team/profile-picture-3.jpg" style="width: 32px; height: 32px;">
            </div>
          </a>
          <ul class="dropdown-menu dropdown-menu-end mt-2 py-1">
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/profile">👤 내 프로필</a></li>
            <li><a class="dropdown-item" href="${pageContext.request.contextPath}/user/settings">⚙ 설정</a></li>
            <li><hr class="dropdown-divider"></li>
            <li>
              <a class="dropdown-item text-danger" href="${pageContext.request.contextPath}/logout">🚪 로그아웃</a>
            </li>
          </ul>
        </li>

      </ul>
    </div>
  </div>
</nav>
