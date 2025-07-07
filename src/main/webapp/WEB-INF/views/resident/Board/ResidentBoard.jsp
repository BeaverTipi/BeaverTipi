<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
  <title>입주민 게시판</title>
  <style>
    .search-bar { margin-bottom: 20px; }
    .btn-write { float: right; margin-bottom: 10px; }
    table { width: 100%; border-collapse: collapse; }
    th, td { border: 1px solid #ccc; padding: 8px; text-align: center; }
    th { background-color: #f4f4f4; }
  </style>

</head>
<body>

<h1>입주민 게시판</h1>


<!-- 🔍 검색/필터 폼 -->
<form method="get" action="${pageContext.request.contextPath}/resident/board" class="search-bar">
  <label>건물:
    <select name="bldgId">
      <c:forEach var="unit" items="${unitList}">
        <option value="${unit.bldgId}" ${unit.bldgId == selectedBldgId ? 'selected' : ''}>
          ${unit.building.bldgNm}
        </option>
      </c:forEach>
    </select>
  </label>

  <label>카테고리:
    <select name="brdCode">
      <option value="">전체</option>
      <option value="R0001" ${search.brdCode == 'R0001' ? 'selected' : ''}>자유게시판</option>
      <option value="R0002" ${search.brdCode == 'R0002' ? 'selected' : ''}>공지사항</option>
    </select>
  </label>

  <label>검색:
    <select name="searchType">
      <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
      <option value="writer" ${search.searchType == 'writer' ? 'selected' : ''}>작성자</option>
    </select>
    <input type="text" name="searchWord" value="${search.searchWord}" />
  </label>

  <button type="submit">검색</button>
</form>

<!-- ✏️ 글쓰기 버튼 -->
<c:if test="${not empty unitList}">
  <div class="btn-write">
    <a href="${pageContext.request.contextPath}/resident/board/form">글쓰기</a>
  </div>
</c:if>

<!-- 📋 게시글 목록 -->
<table>
  <thead>
    <tr>
      <th>번호</th>
      <th>제목</th>
      <th>작성자</th>
      <th>작성일</th>
      <th>조회수</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="board" items="${boardList}" varStatus="status">
      <tr>
        <td>${pagingInfo.totalRecordCount - (pagingInfo.currentPageNo - 1) * pagingInfo.pageSize - status.index}</td>
        <td>
          <a href="${pageContext.request.contextPath}/resident/board/detail?rsdBrdId=${board.rsdBrdId}">
            ${board.rsdBrdTitl}
          </a>
        </td>
        <td>${board.mbrNnm}</td>
        <td><fmt:formatDate value="${board.rsdBrdPblsDate}" pattern="yyyy-MM-dd" /></td>
        <td>${board.rsdBrdCnt}</td>
      </tr>
    </c:forEach>
    <c:if test="${empty boardList}">
      <tr><td colspan="5">게시글이 없습니다.</td></tr>
    </c:if>
  </tbody>
</table>

<!-- 📄 페이징 -->
<div class="pagination">
  ${pagingHTML}
</div>

</body>
</html>
