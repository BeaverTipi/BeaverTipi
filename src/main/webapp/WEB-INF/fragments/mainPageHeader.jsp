<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>

<!-- <<<<<<< HEAD -->
<!--  <nav class="navbar navbar-top navbar-expand navbar-dashboard navbar-dark ps-0 pe-2 pb-0"> -->
<!--   <div class="container-fluid px-0"> -->
<!--     <div class="d-flex justify-content-between w-100" id="navbarSupportedContent"> -->
<!--       <div class="d-flex align-items-center"> -->
       
<!--       </div> -->
<!--       Navbar links -->
<!--       <ul class="navbar-nav align-items-center"> -->
<!--          <li class="nav-item " style="margin-right:20px;"> -->
<%--               <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html"> --%>
<!--                 <span class="font-large text-gray-800">공지사항</span> -->
<!--               </a> -->
<!--             </li> -->
<!--          <li class="nav-item " style="margin-right:20px;"> -->
<%--               <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html"> --%>
<!--                 <span class="font-large text-gray-800">마이페이지</span> -->
<!--               </a> -->
<!--             </li> -->
<!--          <li class="nav-item " style="margin-right:20px;"> -->
<%--               <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html"> --%>
<!--                 <span class="font-large text-gray-800">지도</span> -->
<!--               </a> -->
<!--             </li> -->
<!--          <li class="nav-item " style="margin-right:20px;"> -->
<%--               <a class="nav-link" href="${pageContext.request.contextPath}/resident/myhouse"> --%>
<!--                 <span class="font-large text-gray-800">상품등록</span> -->
<!--               </a> -->
<!--             </li> -->
<!--          <li class="nav-item"> -->
<%--             <a href="${pageContext.request.contextPath}/member/register"> --%>
<%--             	<img src="${pageContext.request.contextPath}/volt/assets/img/profile-1341-svgrepo-com.svg" --%>
<!--             		alt = "User Icon" -->
<!--             		style="width: 24px; height: 24px;" -->
<!--             	> -->
<!--             </a> -->
<!--          </li> -->
        
<!--       </ul> -->
<!--     </div> -->
<!-- ======= -->
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
      <a class="nav-link " href="${pageContext.request.contextPath}/member/register">회원가입</a>
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
<!-- >>>>>>> branch 'main' of https://github.com/arin903/BeaverTipi.git -->
  </div>
</nav>
<!-- <<<<<<< HEAD -->
<!-- ======= -->

<!-- >>>>>>> branch 'main' of https://github.com/arin903/BeaverTipi.git -->
