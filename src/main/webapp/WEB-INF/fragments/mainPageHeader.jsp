<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>

 <nav class="navbar navbar-top navbar-expand navbar-dashboard navbar-dark ps-0 pe-2 pb-0">
  <div class="container-fluid px-0">
    <div class="d-flex justify-content-between w-100" id="navbarSupportedContent">
      <div class="d-flex align-items-center">
       
      </div>
      <!-- Navbar links -->
      <ul class="navbar-nav align-items-center">
         <li class="nav-item " style="margin-right:20px;">
              <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html">
                <span class="font-large text-gray-800">공지사항</span>
              </a>
            </li>
         <li class="nav-item " style="margin-right:20px;">
              <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html">
                <span class="font-large text-gray-800">마이페이지</span>
              </a>
            </li>
         <li class="nav-item " style="margin-right:20px;">
              <a class="nav-link" href="${pageContext.request.contextPath}/admin/pages/tables/bootstrap-tables.html">
                <span class="font-large text-gray-800">지도</span>
              </a>
            </li>
         <li class="nav-item " style="margin-right:20px;">
              <a class="nav-link" href="${pageContext.request.contextPath}/resident/myhouse">
                <span class="font-large text-gray-800">상품등록</span>
              </a>
            </li>
         <li class="nav-item">
            <a href="${pageContext.request.contextPath}/member/register">
            	<img src="${pageContext.request.contextPath}/volt/assets/img/profile-1341-svgrepo-com.svg"
            		alt = "User Icon"
            		style="width: 24px; height: 24px;"
            	>
            </a>
         </li>
        
      </ul>
    </div>
  </div>
</nav>
