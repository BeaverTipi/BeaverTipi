<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
</head>
<body>
<h1>공지사항</h1>
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
						<c:forEach items="${boardList }" var="board"> 
							<tr>
								<td>${board.brdNo }</td>
								<td>
									<a href="#" class="toggle-detail"  >${board.brdTitlNm }</a>
								</td>
								<td>${board.brdPblsDtm }</td>
								<td>${board.boardCartegory.brdCode }</td>
								<td>${board.brdVmCnt }</td>
								<td>${board.boardCartegory.brdCmntYn }</td>
							</tr>
							<tr style="display: none;">
								<td colspan="6">
									<div>
										<p>${board.brdCont }</p>
									</div>
								</td>
							</tr>
						</c:forEach> 
					</c:when>
			<c:otherwise> 
				<tr> 
 					<td colspan="6"> 
 						새로운 공지 사항 등록하세요! 
 					</td> 
 				</tr> 
			</c:otherwise> 
 			</c:choose>
		</tbody>
	</table>
	<div class="button-group">
		<button class="btn btn-outline-primary" onclick="location.href='${pageContext.request.contextPath}/admin/notice/write'">추가</button>
		<button class="btn btn-outline-danger" onclick="location.href='${pageContext.request.contextPath}/admin/notice/delete'">삭제</button>
		<button class="btn btn-outline-warning" onclick="location.href='${pageContext.request.contextPath}/admin/notice/update'">수정</button>
	</div>
</body>
</html>