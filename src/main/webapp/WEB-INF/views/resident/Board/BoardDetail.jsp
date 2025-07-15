<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt"  %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>게시글 상세</title>
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background-color: #f9f9f9;
      margin: 0;
      color: #333;
    }

    .detail-container {
      max-width: 800px;
      margin: 40px auto;
      background: #fff;
      border-radius: 10px;
      box-shadow: 0 4px 10px rgba(0, 0, 0, 0.08);
      padding: 30px 40px;
    }

    h2 {
      font-size: 24px;
      margin-bottom: 20px;
      color: #E67E22;
    }

    .detail-info p {
      margin: 8px 0;
      font-size: 15px;
    }

    .label {
      display: inline-block;
      width: 80px;
      font-weight: bold;
      color: #555;
    }

    .detail-content {
      max-height: 400px;
      overflow-y: auto;
      padding: 15px;
      margin-top: 20px;
      border: 1px solid #eee;
      border-radius: 6px;
      background: #fdfdfd;
      box-sizing: border-box;
    }

    .detail-content p {
      white-space: pre-wrap;
      word-break: break-word;
      margin: 0;
      line-height: 1.6;
    }

    .btn-group {
      margin-top: 30px;
      display: flex;
      justify-content: flex-end;
      gap: 12px;
    }

    .btn-group a,
    .btn-group button {
      padding: 10px 20px;
      border: none;
      border-radius: 6px;
      font-weight: 600;
      font-size: 14px;
      text-decoration: none;
      transition: background-color 0.2s ease;
      cursor: pointer;
    }

    .btn-group a {
      background-color: #4a90e2;
      color: white;
    }

    .btn-group a:hover {
      background-color: #357ab8;
    }

    .delete-btn {
      background-color: #e74c3c;
      color: white;
    }

    .delete-btn:hover {
      background-color: #c0392b;
    }
  </style>
</head>
<body>

  <div class="detail-container">
    <h2>${board.rsdBrdTitl}</h2>

    <div class="detail-info">
      <p><span class="label">작성자:</span>${board.mbrNnm}</p>
      <p><span class="label">작성일:</span><fmt:formatDate value="${board.rsdBrdPblsDate}" pattern="yyyy-MM-dd HH:mm" /></p>
      <p><span class="label">조회수:</span>${board.rsdBrdCnt}</p>
    </div>

    <hr/>
    <div class="detail-content">
      <p><c:out value="${board.rsdBrdCont}" escapeXml="true"/></p>
    </div>

    <div class="btn-group">
      <a href="<c:url value='/resident/board'>
                 <c:param name='bldgIdParam' value='${selectedBldgId}'/>
                 <c:param name='page' value='${page}'/>
               </c:url>">목록으로</a>

      <c:if test="${board.mbrCd eq loginUser.mbrCd}">
        <a href="<c:url value='/resident/board/form'>
                   <c:param name='rsdBrdId' value='${board.rsdBrdId}'/>
                   <c:param name='bldgIdParam' value='${selectedBldgId}'/>
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
