<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>


<html>
<head>
  <title>민원 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>" />
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background-color: #f9f9f9;
      margin: 0;
      color: #333;
    }
	
	.complaint-table {
	  width: 100%;
	  max-width: 1000px;
	  margin: 0 auto 30px;
	  border-collapse: collapse;
	  background: #fff;
	  border: 1px solid #ddd;
	  border-radius: 8px;
	  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
	}
	.complaint-table th,
	.complaint-table td {
	  padding: 12px 16px;
	  border-bottom: 1px solid #eee;
	  text-align: left;
	}
	.complaint-table th {
	  background-color: #f2f2f2;
	  color: #333;
	  width: 120px;
	}
	
    h2 {
      max-width: 1000px;
      margin: 40px auto 20px;
      font-size: 24px;
      color: #E67E22;
    }

    table {
      width: 100%;
      max-width: 1000px;
      margin: 0 auto 30px;
      border-collapse: collapse;
      background: #fff;
      border: 1px solid #ddd;
      border-radius: 8px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.05);
    }

    th, td {
      padding: 12px 16px;
      border-bottom: 1px solid #eee;
      text-align: left;
    }

    th {
      background-color: #f2f2f2;
      color: #333;
      width: 120px;
    }

    .btn {
      display: inline-block;
      padding: 8px 16px;
      border-radius: 4px;
      text-decoration: none;
      font-weight: bold;
      font-size: 14px;
      margin-left: 10px;
    }
	
	.btn-group {
	  max-width: 1000px;
	  margin: 0 auto;
	  display: flex;
	  justify-content: flex-end;
	  gap: 12px;
	}
	
    .btn-success {
      background-color: #28a745;
      color: white;
    }

    .btn-danger {
      background-color: #dc3545;
      color: white;
      border: none;
    }

    .btn-primary {
      background-color: #007bff;
      color: white;
    }

    .btn:hover {
      opacity: 0.9;
    }

    form {
      display: inline;
    }
  </style>
</head>
<body>
  <h2>민원 상세</h2>

  <table class="complaint-table">
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
	<div class="btn-group">
	<c:if test="${loginMember.mbrCd == complaint.mbrCd}">
	  <a class="btn btn-success" href="${pageContext.request.contextPath}/resident/complaint/form?rsdBrdId=${complaint.rsdBrdId}&bldgIdParam=${complaint.bldgId}">
	    수정
	  </a>
	  <form action="${pageContext.request.contextPath}/resident/complaint/delete" method="post">
	    <input type="hidden" name="rsdBrdId" value="${complaint.rsdBrdId}"/>
	    <input type="hidden" name="bldgIdParam" value="${complaint.bldgId}"/>
	    <button class="btn btn-danger" type="submit" onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
	  </form>
	</c:if>
  <a class="btn btn-primary" href="${pageContext.request.contextPath}/resident/complaint?bldgIdParam=${complaint.bldgId}">목록</a>
</div>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>