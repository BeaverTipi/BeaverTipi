<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
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
    <!-- 제목 및 메타 정보 -->
    <h2><c:out value="${notice.brdTitlNm}"/></h2>
    <div class="meta">
      작성자: <c:out value="${notice.member.mbrNnm}"/> |
      조회수: <c:out value="${notice.brdVwCnt}"/> |
      작성일: <fmt:formatDate value="${convertedDate}" pattern="yyyy-MM-dd HH:mm"/>
    </div>
    <hr/>

    <!-- 내용 -->
    <div class="content">
      <pre><c:out value="${notice.brdCont}"/></pre>
    </div>
	
    <!-- 권한 기반 수정/삭제 버튼 -->
  <!-- 권한 체크 -->
		<c:set var="isAdmin" value="false"/>
		<c:set var="isAuthor" value="${notice.mbrCd == mbrCd}" />
		
		<c:forEach var="role" items="${memRoleList}">
		  <c:if test="${role.userRoleId == 'ADMIN'}">
		    <c:set var="isAdmin" value="true"/>
		  </c:if>
		</c:forEach>
		
    <c:if test="${isAdmin or isAuthor}">
      <div class="action-buttons">
        <!-- 수정 버튼 -->
        <c:url var="formUrl" value="/resident/notice/form">
		  <c:param name="noticeNo"     value="${notice.noticeNo}" />
		  <c:param name="bldgIdParam"  value="${bldgIdParam}"  />
		  <c:param name="page"         value="${page}"         />
		  <c:param name="noticeType"   value="${noticeType}"   />
		  <c:param name="searchType"   value="${searchType}"   />
		  <c:param name="searchWord"   value="${searchWord}"   />
		</c:url>
		<a href="${formUrl}" class="btn btn-primary">수정</a>

        <!-- 삭제 버튼 -->
        <form method="post" action="<c:url value='/resident/notice/delete'/>"
              onsubmit="return confirm('정말 삭제하시겠습니까?');" style="display:inline;">
          <input type="hidden" name="noticeNo"    value="${notice.noticeNo}" />
          <input type="hidden" name="bldgIdParam" value="${bldgIdParam}" />
          <input type="hidden" name="page"        value="${page}" />
          <input type="hidden" name="noticeType"  value="${noticeType}" />
          <input type="hidden" name="searchType"  value="${searchType}" />
          <input type="hidden" name="searchWord"  value="${searchWord}" />
          <button type="submit" class="btn btn-danger">삭제</button>
        </form>
      </div>
    </c:if>

    <!-- 목록으로 돌아가기 -->
    <c:url var="listUrl" value="/resident/notice">
      <c:param name="bldgIdParam" value="${bldgIdParam}"/>
      <c:param name="noticeType"  value="${noticeType}"/>
      <c:param name="page"        value="${page}"/>
      <c:param name="searchType"  value="${searchType}"/>
      <c:param name="searchWord"  value="${searchWord}"/>
    </c:url>
    <a href="${listUrl}" class="btn btn-default">목록으로</a>
  </div>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>