<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8"/>
  <title>공지사항 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>"/>
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>"/>
</head>
<body>
  <div class="container">
    <!-- 제목, 작성자, 조회수 -->
    <h2><c:out value="${notice.brdTitlNm}"/></h2>
    <div class="meta">
      작성자: <c:out value="${notice.member.mbrNnm}"/> |
      조회수: <c:out value="${notice.brdVwCnt}"/> |
      작성일: <c:out value="${notice.brdPblsDtm}"/>
    </div>
    <hr/>

    <!-- 내용 -->
    <div class="content">
      <pre><c:out value="${notice.brdCont}"/></pre>
    </div>

    <!-- 목록으로 -->
    <c:url var="listUrl" value="/resident/notice">
	  <c:param name="bldgIdParam" value="${bldgIdParam}"/>
	  <c:param name="noticeType"  value="${noticeType}"/>
	  <c:param name="page"        value="${page}"/>
	  <c:param name="searchType"  value="${searchType}"/>
	  <c:param name="searchWord"  value="${searchWord}"/>
	</c:url>
      <a href="${listUrl}" class="btn btn-default">목록으로</a>
  </div>
</body>
</html>

