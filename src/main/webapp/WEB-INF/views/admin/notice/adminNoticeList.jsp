<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
<script src="${pageContext.request.contextPath}/app/js/admin/board/boardToggle.js"></script>
</head>
<body>

	<h1 class="board-title">게시판</h1>
	
	<!-- ✅ 탭 메뉴 -->
	<ul class="nav nav-tabs customtab" role="tablist">
		<li class="nav-item ${activeTab eq 'notice' ? 'active' : ''}">
			<a class="nav-link" href="?tab=notice">공지사항</a>
		</li>
		<li class="nav-item ${activeTab eq 'faq' ? 'active' : ''}">
			<a class="nav-link" href="?tab=faq">FAQ</a>
		</li>
		<li class="nav-item ${activeTab eq 'qna' ? 'active' : ''}">
			<a class="nav-link" href="?tab=qna">QNA</a>
		</li>
	</ul>
	
	<!-- 게시판 탭 선택에 따라 다른 템플릿 로딩 -->
	<c:choose>
	  <c:when test="${activeTab eq 'notice'}">
	    <jsp:include page="${pageContext.request.contextPath}/WEB-INF/views/admin/notice/adminBoardNotice.jsp" />
	  </c:when>
	  <c:otherwise>
	    <c:choose>
	      <c:when test="${activeTab eq 'faq'}">
	        <table class="table faq-table">
	      </c:when>
	      <c:when test="${activeTab eq 'qna'}">
	        <table class="table qna-table">
	      </c:when>
	      <c:otherwise>
	        <table class="table">
	      </c:otherwise>
	    </c:choose>
	      <thead>
	        <tr>
	          <th>No</th>
	          <th>제목</th>
	          <th>게시일자</th>
	          <th>조회수</th>
	          <th>댓글 허용 여부</th>
	          <th><input type="checkbox" id="selectAllCheckbox" /></th>
	        </tr>
	      </thead>
	      <tbody>
	        <c:choose>
	          <c:when test="${not empty boardList}">
	            <c:forEach items="${boardList}" var="board" varStatus="status">
	              <tr>
	                <td> 
	                	<c:out value="${(paging.currentPageNo - 1) * paging.pageSize + status.index + 1}" />
	                </td>
	                <td><a href="#" class="toggle-detail">${board.brdTitlNm}</a></td>
	                <td>${board.brdPblsDtmFormatted}</td>
	                <td>${board.brdVwCnt}</td>
	                <td>${board.boardCartegory.brdCmntYn}</td>
	                <td><input type="checkbox" class="rowCheckbox"></td>
	              </tr>
	              <tr style="display: none;">
	                <td colspan="7"><div><p>${board.brdCont}</p></div></td>
	              </tr>
	            </c:forEach>
	          </c:when>
	          <c:otherwise>
	            <tr><td colspan="7">게시글이 없습니다.</td></tr>
	          </c:otherwise>
	        </c:choose>
	      </tbody>
	    </table>
	  </c:otherwise>
	</c:choose>

	
	<!-- ✅ 페이징 영역 -->
	<c:if test="${paging.totalPageCount > 1}">
	  <div class="pagination-wrapper">
	    <a href="?page=1&tab=${activeTab}" class="${paging.currentPageNo == 1 ? 'disabled' : ''}">&laquo;</a>
	    <a href="?page=${paging.currentPageNo - 1}&tab=${activeTab}" class="${paging.currentPageNo == 1 ? 'disabled' : ''}">&lt;</a>
	
	    <c:forEach var="pageNo" begin="${paging.firstPageNoOnPageList}" end="${paging.lastPageNoOnPageList}">
	      <c:choose>
	        <c:when test="${pageNo == paging.currentPageNo}">
	          <span class="bg-primary">${pageNo}</span>
	        </c:when>
	        <c:otherwise>
	          <a href="?page=${pageNo}&tab=${activeTab}">${pageNo}</a>
	        </c:otherwise>
	      </c:choose>
	    </c:forEach>
	
	    <a href="?page=${paging.currentPageNo + 1}&tab=${activeTab}" class="${paging.currentPageNo == paging.totalPageCount ? 'disabled' : ''}">&gt;</a>
	    <a href="?page=${paging.totalPageCount}&tab=${activeTab}" class="${paging.currentPageNo == paging.totalPageCount ? 'disabled' : ''}">&raquo;</a>
	  </div>
	</c:if>

	<!-- ✅ 버튼 영역 -->
	<div class="write-buttons">
	  <button class="btn-success"
	    onclick="location.href='${pageContext.request.contextPath}/admin/notice/write?tab=${activeTab}'">추가</button>
	  <button class="btn-danger"
	    onclick="location.href='${pageContext.request.contextPath}/admin/notice/delete?tab=${activeTab}'">삭제</button>
	  <button class="btn-warning"
	    onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?tab=${activeTab}'">수정</button>
	</div>


</body>
