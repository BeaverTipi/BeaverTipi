<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
<head>
  <title>민원 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>" />
</head>
<body>
  <h2>민원 상세</h2>

  <table class="table table-striped">
    <tr><th>제목</th>
        <td>${complaint.rsdBrdTitl}</td></tr>
    <tr><th>내용</th>
        <td>${complaint.rsdBrdCont}</td></tr>
    <tr><th>작성자</th>
        <td>${complaint.mbrNnm}</td></tr>
    <tr><th>게시일</th>
        <td><fmt:formatDate value="${complaint.rsdBrdPblsDate}"
                pattern="yyyy-MM-dd HH:mm"/></td></tr>
    <tr><th>수정일</th>
        <td><fmt:formatDate value="${complaint.rsdBrdModDate}"
                pattern="yyyy-MM-dd HH:mm"/>
		</td></tr>
    <tr><th>공개여부</th>
        <td>${complaint.openYn}</td></tr>
    <tr><th>처리상태</th>
        <td>${complaint.reqStatus}</td></tr>
  </table>
	
	<a class="btn btn-success" href="${pageContext.request.contextPath}/resident/complaint/form?rsdBrdId=${complaint.rsdBrdId}&bldgIdParam=${complaint.bldgId}">
	    수정
	</a>

  <form action="${pageContext.request.contextPath}/resident/complaint/delete" method="post" style="display:inline">
    <input type="hidden" name="rsdBrdId" value="${complaint.rsdBrdId}"/>
    <input type="hidden" name="bldgIdParam" value="${complaint.bldgId}"/>
    <button class="btn btn-danger" type="submit">삭제</button>
  </form>

  <a class="btn btn-primary" href="${pageContext.request.contextPath}/resident/complaint?bldgIdParam=${complaint.bldgId}">목록</a>
</body>
</html>