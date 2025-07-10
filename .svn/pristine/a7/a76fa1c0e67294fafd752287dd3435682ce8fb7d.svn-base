<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>휴지통</title>
  <style>
    body { font-family: 'Segoe UI', sans-serif; background: #f9f9f9; margin: 0; padding: 0; }
    main.container { max-width: 1000px; margin: 40px auto; background: #fff; padding: 20px; border-radius: 6px; box-shadow: 0 0 10px rgba(0,0,0,0.05); }
    h1 { margin-bottom: 20px; }
    table { width: 100%; border-collapse: collapse; margin-bottom: 20px; }
    th, td { border: 1px solid #ccc; padding: 12px; text-align: center; }
    th { background-color: #f4f4f4; }
    .btn { padding: 6px 12px; border: none; border-radius: 4px; cursor: pointer; font-size: 14px; }
    .btn-restore { background-color: #17a2b8; color: white; }
    .btn-delete { background-color: #dc3545; color: white; }
    .pagination-wrapper { text-align: center; margin-top: 20px; }
  </style>
</head>
<body>
<main class="container">

  <h1>🗑️ 휴지통</h1>

  <table>
    <thead>
      <tr>
        <th>번호</th>
        <th>제목</th>
        <th>작성자</th>
        <th>삭제일</th>
        <th>삭제</th>
        <th>복구</th>
      </tr>
    </thead>
    <tbody>
		  <c:forEach var="board" items="${boardList}" varStatus="status">
		    <tr>
		      <td>${pagingInfo.firstRecordIndex + status.index}</td>
		      <td>${board.rsdBrdTitl}</td>
		      <td>${board.mbrNnm}</td>
		      <td>
		        <fmt:formatDate value="${board.rsdBrdModDate}" pattern="yyyy-MM-dd HH:mm" />
		      </td>
		
		      <!-- 🔴 영구 삭제 버튼 -->
		      <td>
		        <form method="post"
		              action="${pageContext.request.contextPath}/resident/board/permanent"
		              onsubmit="return confirm('정말 삭제하시겠습니까?');">
		          <input type="hidden" name="rsdBrdId" value="${board.rsdBrdId}" />
		          <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
		          <button type="submit" class="btn btn-delete">영구 삭제</button>
		        </form>
		      </td>
		
		      <!-- 🔄 복구 버튼 -->
		      <td>
		        <form method="post"
		              action="${pageContext.request.contextPath}/resident/board/restore">
		          <input type="hidden" name="rsdBrdId" value="${board.rsdBrdId}" />
		          <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
		          <button type="submit" class="btn btn-restore">복구</button>
		        </form>
		      </td>
		    </tr>
		  </c:forEach>
      <c:if test="${empty boardList}">
        <tr>
          <td colspan="6">삭제된 게시글이 없습니다.</td>
        </tr>
      </c:if>
    </tbody>
  </table>
	  <!-- 페이징 -->
	  <div class="pagination-wrapper">
	    ${pagingHTML}
	  </div>
	<div style="text-align: right; margin-top: 10px;">
	  <a href="${pageContext.request.contextPath}/resident/board?bldgIdParam=${selectedBldgId}"
	     class="btn"
	     style="background-color: #6c757d; color: white; text-decoration: none;">
	    목록
	  </a>
	</div>
</main>
</body>
</html>