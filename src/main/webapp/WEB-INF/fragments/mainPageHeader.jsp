<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<!-- 🔷 Header -->
<nav class="navbar navbar-expand-lg bg-white px-4 py-2">
  <div class="container-fluid d-flex justify-content-between align-items-center">
    <a class="navbar-brand d-flex align-items-center" href="${pageContext.request.contextPath}/">
      <img src="${pageContext.request.contextPath}/volt/assets/img/brand/dark.png" alt="Logo" class="me-2">
    </a>
    <ul class="navbar-nav flex-row gap-4 align-items-center mb-0">
      <li class="nav-item"><a class="nav-link" href="#">공지사항</a></li>
      <security:authorize access="isAuthenticated()">
      <li class="nav-item"><a class="nav-link fw-bold" href="#">마이페이지</a></li>
      </security:authorize>
      <security:authorize access="isAnonymous()">
      <li class="nav-item">
      <a class="nav-link " href="#">회원가입</a>
      </li>
          
      </security:authorize>
      <li class="nav-item"><a class="nav-link" href="#">지도</a></li>
      <li class="nav-item"><a class="nav-link" href="#">상품등록</a></li>

      <security:authorize access="isAuthenticated()">
        <li class="nav-item dropdown">
          <a class="nav-link dropdown-toggle" href="#" id="userDropdown" role="button" data-bs-toggle="dropdown" aria-expanded="false">
            <i class="bi bi-person-circle fs-4"></i>
          </a>
          <ul class="dropdown-menu dropdown-menu-end mt-2" aria-labelledby="userDropdown">
            <li><a class="dropdown-item" href="#">내 정보</a></li>
            <li><a class="dropdown-item" href="#">알림</a></li>
            <li><hr class="dropdown-divider"></li>
            <li><a class="dropdown-item text-danger" href="<c:url value='/logout'/>">로그아웃</a></li>
          </ul>
        </li>
      </security:authorize>

      <security:authorize access="isAnonymous()">
        <li class="nav-item">
          <a class="nav-link" href="#" data-bs-toggle="modal" data-bs-target="#loginModal">
            <i class="bi bi-person-circle fs-4"></i>
          </a>
        </li>
      </security:authorize>
    </ul>
  </div>
</nav>

