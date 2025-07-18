<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<head>
<meta charset="UTF-8">
<title>게시판</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
<script src="${pageContext.request.contextPath}/app/js/admin/board/boardToggle.js"></script>
</head>
<body>

	<h1>게시판</h1>
	
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
	    <!-- FAQ, QNA는 기존 테이블 구조 유지 -->
	    <table class="table table-striped table-hover">
	      <thead>
	        <tr>
	          <th>No</th>
	          <th>제목</th>
	          <th>게시일자</th>
	          <th>카테고리</th>
	          <th>조회수</th>
	          <th>댓글 허용 여부</th>
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
	                <td>
	                  <c:choose>
	                    <c:when test="${board.boardCartegory.brdCode.startsWith('F')}">FAQ</c:when>
	                    <c:when test="${board.boardCartegory.brdCode.startsWith('Q')}">QNA</c:when>
	                    <c:otherwise>기타</c:otherwise>
	                  </c:choose>
	                </td>
	                <td>${board.brdVwCnt}</td>
	                <td>${board.boardCartegory.brdCmntYn}</td>
	              </tr>
	              <tr style="display: none;">
	                <td colspan="6"><div><p>${board.brdCont}</p></div></td>
	              </tr>
	            </c:forEach>
	          </c:when>
	          <c:otherwise>
	            <tr><td colspan="6">게시글이 없습니다.</td></tr>
	          </c:otherwise>
	        </c:choose>
	      </tbody>
	    </table>
	  </c:otherwise>
	</c:choose>

	
	<!-- ✅ 페이징 영역 -->
	<c:if test="${paging.totalPageCount > 1}">
	  <ul class="pagination">
	
	    <!-- << 맨 처음 페이지로 -->
	    <li class="page-item ${paging.currentPageNo == 1 ? 'disabled' : ''}">
	      <a class="page-link" href="?page=1&tab=${activeTab}">&laquo;</a>
	    </li>
	
	    <!-- < 이전 페이지 한 칸 -->
	    <li class="page-item ${paging.currentPageNo == 1 ? 'disabled' : ''}">
	      <a class="page-link" href="?page=${paging.currentPageNo - 1}&tab=${activeTab}">&lt;</a>
	    </li>
	
	    <!-- 숫자 페이지 목록 -->
	    <c:forEach var="pageNo" begin="${paging.firstPageNoOnPageList}" end="${paging.lastPageNoOnPageList}">
	      <li class="page-item ${pageNo == paging.currentPageNo ? 'active' : ''}">
	        <a class="page-link" href="?page=${pageNo}&tab=${activeTab}">${pageNo}</a>
	      </li>
	    </c:forEach>
	
	    <!-- > 다음 페이지 한 칸 -->
	    <li class="page-item ${paging.currentPageNo == paging.totalPageCount ? 'disabled' : ''}">
	      <a class="page-link" href="?page=${paging.currentPageNo + 1}&tab=${activeTab}">&gt;</a>
	    </li>
	
	    <!-- >> 마지막 페이지로 -->
	    <li class="page-item ${paging.currentPageNo == paging.totalPageCount ? 'disabled' : ''}">
	      <a class="page-link" href="?page=${paging.totalPageCount}&tab=${activeTab}">&raquo;</a>
	    </li>
	
	  </ul>
	</c:if>


	
	<!-- ✅ 버튼 영역 -->
	<div class="button-group">
		<button class="btn btn-outline-primary"
			onclick="location.href='${pageContext.request.contextPath}/admin/notice/write?tab=${activeTab}'">추가</button>
		<button class="btn btn-outline-danger"
			onclick="location.href='${pageContext.request.contextPath}/admin/notice/delete?tab=${activeTab}'">삭제</button>
		<button class="btn btn-outline-warning"
			onclick="location.href='${pageContext.request.contextPath}/admin/notice/update?tab=${activeTab}'">수정</button>
	</div>

</body>
