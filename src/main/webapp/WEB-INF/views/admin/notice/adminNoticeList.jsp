<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
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
		<li class="nav-item ${activeTab eq 'notice' ? 'active' : ''}"><a
			class="nav-link" href="?tab=notice">공지사항</a></li>
		<li class="nav-item ${activeTab eq 'faq' ? 'active' : ''}"><a
			class="nav-link" href="?tab=faq">FAQ</a></li>
		<li class="nav-item ${activeTab eq 'qna' ? 'active' : ''}"><a
			class="nav-link" href="?tab=qna">QNA</a></li>
	</ul>

	<!-- ✅ 탭에 따른 리스트 JSP include -->
	<c:choose>
		<c:when test="${activeTab eq 'notice'}">
			<jsp:include page="/WEB-INF/views/admin/notice/adminBoardNotice.jsp" />
		</c:when>
		<c:when test="${activeTab eq 'faq'}">
			<jsp:include page="/WEB-INF/views/admin/notice/adminFaqNotice.jsp" />
		</c:when>
		<c:when test="${activeTab eq 'qna'}">
			<jsp:include page="/WEB-INF/views/admin/notice/adminQnaNotice.jsp" />
		</c:when>
	</c:choose>

	<!-- ✅ 페이징 영역 -->
	<c:if test="${paging != null and paging.totalPageCount > 0}">
		<div class="pagination-wrapper">
			<a href="?page=1&tab=${activeTab}"
				class="${paging.currentPageNo == 1 ? 'disabled' : ''}">&laquo;</a> <a
				href="?page=${paging.currentPageNo - 1}&tab=${activeTab}"
				class="${paging.currentPageNo == 1 ? 'disabled' : ''}">&lt;</a>

			<c:forEach var="pageNo" begin="${paging.firstPageNoOnPageList}"
				end="${paging.lastPageNoOnPageList}">
				<c:choose>
					<c:when test="${pageNo == paging.currentPageNo}">
						<span class="bg-primary">${pageNo}</span>
					</c:when>
					<c:otherwise>
						<a href="?page=${pageNo}&tab=${activeTab}">${pageNo}</a>
					</c:otherwise>
				</c:choose>
			</c:forEach>

			<a href="?page=${paging.currentPageNo + 1}&tab=${activeTab}"
				class="${paging.currentPageNo == paging.totalPageCount ? 'disabled' : ''}">&gt;</a>
			<a href="?page=${paging.totalPageCount}&tab=${activeTab}"
				class="${paging.currentPageNo == paging.totalPageCount ? 'disabled' : ''}">&raquo;</a>
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
