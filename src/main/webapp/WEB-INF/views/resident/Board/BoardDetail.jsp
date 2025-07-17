<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>${board.rsdBrdTitl}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_residentDetail.css" />
</head>
<body>

  <div class="detail-container">
    <!-- 🔸 제목 -->
    <h2>${board.rsdBrdTitl}</h2>

    <!-- 🔸 작성 정보 -->
    <div class="detail-info">
      <p><span class="label">건물:</span>${board.bldgNm}</p>
      <p><span class="label">작성자:</span>${board.mbrNnm}</p>
      <p><span class="label">작성일:</span>
        <fmt:formatDate value="${board.rsdBrdPblsDate}" pattern="yyyy-MM-dd HH:mm" />
      </p>
      <p><span class="label">조회수:</span>${board.rsdBrdCnt}</p>
    </div>

    <hr/>

    <!-- 🔸 본문 내용 -->
    <div class="detail-content">
      <p><c:out value="${board.rsdBrdCont}" escapeXml="false" /></p>
    </div>

    <!-- 🔸 버튼 영역 -->
    <div class="btn-group">
      <a href="<c:url value='/resident/board'>
                 <c:param name='bldgIdParam' value='${selectedBldgId}' />
                 <c:param name='page' value='${page}' />
               </c:url>">목록으로</a>

      <c:if test="${board.mbrCd eq loginUser.mbrCd}">
        <a href="<c:url value='/resident/board/form'>
                   <c:param name='rsdBrdId' value='${board.rsdBrdId}' />
                   <c:param name='bldgIdParam' value='${selectedBldgId}' />
                 </c:url>">수정</a>

        <form method="post" action="${pageContext.request.contextPath}/resident/board/delete" style="display:inline;">
          <input type="hidden" name="rsdBrdId" value="${board.rsdBrdId}" />
          <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
          <button type="submit" class="delete-btn" onclick="return confirm('정말 삭제하시겠습니까?');">삭제</button>
        </form>
      </c:if>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
