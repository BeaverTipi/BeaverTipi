<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 삭제</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
</head>
<body>
<h1>공지사항 삭제</h1>
	
	<table class="table table-striped table-hover">
		<thead>
			<tr>
				<th>No</th>
				<th>제목</th>
				<th>게시일자</th>
				<th>종료일자</th>
				<th>상단고정</th>
				<th>카테고리</th>
				<th>선택</th>
			</tr>
		</thead>
		<tbody>
			<%-- <c:choose> 
					<c:when test="${not empty boardList}">
						<c:forEach items="${boardList }" var="board"> 
					<tr>
						<td>${board.brdNo }</td>
						<td>
							<a href="javascript:void(0);" onclick="showDetail(this)">${board.brdTitlNm }</a>
						</td>
						<td>${board.brdPblsDtm }</td>
						<td>${board.brdModDtm }</td>
						<td>${board.brd }</td>
						<td>${board.brd }</td>
						<td><input type="checkbox" name="chkBox"></td>
					</tr>
					<tr style="display: none;">
						<td colspan="7">
							<div>
								<p>${board.brdCont }</p>
							</div>
						</td>
					</tr>
				</c:forEach> 
				</c:when>
			<c:otherwise> 
				<tr> 
 					<td colspan="7"> 
 						새로운 공지 사항 등록하세요! 
 					</td> 
 				</tr> 
			</c:otherwise> 
 			</c:choose>  --%> 
 				 <tr>
 					<th>Dummy</th>
					<th>Dummy</th>
					<th>Dummy</th>
					<th>Dummy</th>
					<th>Dummy</th>
					<th>Dummy</th>
					<td><input type="checkbox" name="chkBox"></td>
 				</tr> 
 		
		</tbody>
	</table>
	<div class="button-group">
			<button class="btn btn-outline-danger" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list'">삭제</button>
	</div>
	
</body>
</html>