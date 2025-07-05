<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>공지사항 수정</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/notice.css">
</head>
<body>
	<h1>공지사항 수정</h1>
	<div class="card">
		<div class="card-body">
			<form:form modelAttribute="board" method="post">
				<div>
					<div class="form-control">
						<label class="label" for="brdTitlNm">제목</label>
						<form:input path="brdTitlNm" placeholder="제목을 입력하세요" cssClass="" />
						<form:errors path="brdTitlNm" cssClass="text-danger" />
					</div>
					<div class="form-control">
						<label class="label" for="brdCode">공지유형</label>
						<form:checkbox path="brdCode" value="공지사항" cssClass="" />공지사항
						<form:checkbox path="brdCode" value="FAQ" cssClass="" />FAQ
						<form:checkbox path="brdCode" value="QnA" cssClass="" />QnA
						<form:errors path="brdCode" cssClass="text-danger" />			
					</div>
					<c:forEach var="noticeItem" items="${board.notice}" varStatus="status">
						<div class="form-control">
							<label class="label" for="noticeTop">상단고정</label>
							<form:radiobutton path="notice[${status.index}].noticeTop" value="Y" cssClass=""  />사용
							<form:radiobutton path="notice[${status.index}].noticeTop" value="N" cssClass="" />안함
							<form:errors path="notice[${status.index}].noticeTop" cssClass="text-danger" />			
						</div>
					</c:forEach>
					<div class="form-control">
						<label class="label" for="brdCont">내용</label>
						<form:textarea path="brdCont" placeholder="내용을 입력하세요" cssClass="" />
						<form:errors path="brdCont" cssClass="text-danger" />			
					</div>
				</div>
			</form:form>
		</div>
	</div>
		<div class="card-footer">
			<div class="button-group">
				<button type="button" class="btn btn-outline-success" onclick="location.href='${pageContext.request.contextPath}/admin/notice/list'">등록</button>
				<button type="reset" class="btn btn-outline-danger">취소</button>
			</div>
		</div>
</body>
</html>