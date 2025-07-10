<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>
    민원
    <c:choose>
      <c:when test="${empty complaint.rsdBrdId}">등록</c:when>
      <c:otherwise>수정</c:otherwise>
    </c:choose>
  </title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>" />
</head>
<body>
  <h2> 
  	<c:choose>
      <c:when test="${empty complaint.rsdBrdId}">등록</c:when>
      <c:otherwise>수정</c:otherwise>
    </c:choose>
  </h2>

  <form action="${pageContext.request.contextPath}/resident/complaint/save"
        method="post">
    <!-- 키 값들 -->
    <input type="hidden" name="rsdBrdId"       value="${complaint.rsdBrdId}" />
    <input type="hidden" name="bldgId"          value="${complaint.bldgId}" />
    <input type="hidden" name="bldgIdParam"     value="${selectedBldgId}" />

    <table class="table table-striped">
      <tr>
        <th>제목</th>
        <td>
          <input type="text" name="rsdBrdTitl"
                 value="${complaint.rsdBrdTitl}" required />
        </td>
      </tr>
      <tr>
        <th>내용</th>
        <td>
          <textarea name="rsdBrdCont"
                    rows="8" cols="60"
                    required>${complaint.rsdBrdCont}</textarea>
        </td>
      </tr>
      <tr>
        <th>공개여부</th>
        <td>
          <select name="openYn">
            <c:forEach var="c" items="${openYnList}">
              <option value="${c.code}"
                <c:if test="${c.code == complaint.openYn}">selected</c:if>>
                ${c.name}
              </option>
            </c:forEach>
          </select>
        </td>
      </tr>
      <tr>
        <th>처리상태</th>
        <td>
          <select name="reqStatus">
            <c:forEach var="c" items="${reqStatusList}">
              <option value="${c.code}"
                <c:if test="${c.code == complaint.reqStatus}">selected</c:if>>
                ${c.name}
              </option>
            </c:forEach>
          </select>
        </td>
      </tr>
    </table>
	<div class="btn-group">
	    <button class="btn btn-success" type="submit">저장</button>
	    <a class="btn btn-primary" href="${pageContext.request.contextPath}/resident/complaint?bldgIdParam=${selectedBldgId}">
	      취소
	    </a>
    </div>
  </form>
</body>
</html>