<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
  <title>민원 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_residentDetail.css" />
</head>
<body>

  <div class="detail-container">
    <h2>${complaint.rsdBrdTitl}</h2>

    <div class="detail-info">
      <p><span class="label">작성자:</span> ${complaint.mbrNnm}</p>
      <p><span class="label">게시일:</span>
        <fmt:formatDate value="${complaint.rsdBrdPblsDate}" pattern="yyyy-MM-dd HH:mm"/>
      </p>
      <p><span class="label">수정일:</span>
        <fmt:formatDate value="${complaint.rsdBrdModDate}" pattern="yyyy-MM-dd HH:mm"/>
      </p>
      <p><span class="label">공개여부:</span> ${complaint.openYn}</p>
      <p><span class="label">처리상태:</span> ${complaint.reqStatus}</p>
    </div>

    <hr/>

    <div class="detail-content">
      <p><c:out value="${complaint.rsdBrdCont}" escapeXml="true"/></p>
    </div>

    <div class="btn-group">
      <c:if test="${loginMember.mbrCd == complaint.mbrCd}">
        <a class="button button-success"
           href="${pageContext.request.contextPath}/resident/complaint/form?rsdBrdId=${complaint.rsdBrdId}&bldgIdParam=${complaint.bldgId}">수정</a>

        <form action="${pageContext.request.contextPath}/resident/complaint/delete" method="post" style="display:inline;">
          <input type="hidden" name="rsdBrdId" value="${complaint.rsdBrdId}"/>
          <input type="hidden" name="bldgIdParam" value="${complaint.bldgId}"/>
          <button class="button button-danger" type="submit"
                  onclick="return confirm('정말 삭제하시겠습니까?')">삭제</button>
        </form>
      </c:if>

      <a class="button button-primary"
         href="${pageContext.request.contextPath}/resident/complaint?bldgIdParam=${complaint.bldgId}">목록</a>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
