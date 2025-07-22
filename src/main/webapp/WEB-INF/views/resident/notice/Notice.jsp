<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>📢 공지사항</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css">
<style>
.search-area {
  margin-bottom: 30px;
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 8px;
  background-color: #fff;
}

.search-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4열 균등 분할 */
  row-gap: 16px;
  column-gap:24px;
  align-items: center;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 6px;
}
.search-input-with-btn {
  display: flex;
  flex: 1;
  gap: 8px;
}
.search-item label {
  font-weight: bold;
  width: 60px;
  flex-shrink: 0;
  text-align: left;
  margin-right: 2px;
}

.input-field {
  flex: 1;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.select-field,
.input-field,
.date-field {
  flex: 1;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.date-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
  width: 100%;
}

.search-actions {
  grid-column: span 4;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
.search-actions label {
  font-weight: bold;
  width: 60px;
  flex-shrink: 0;
}

.search-input-with-btn input.input-field {
  flex: 1 1 65%;  /* 너비 적당히 제한 */
  min-width: 240px;
  max-width: 500px;
  padding: 8px;
}
.search-input-with-btn {
  display: flex;
  flex: 1;
  align-items: center;
  gap: 8px;
  flex-wrap: nowrap;
}

.search-button,
.btn-reset {
  height: 42px;
  white-space: nowrap;
}

.search-button {
  background-color: #E17100;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  height: 42px;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #973C00;
}

.btn-reset {
  height: 42px;
  background: white;
  border: 1px solid #aaa;
  padding: 0 16px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

</style>

</head>
<body>

<h2 class="board-title">📢 공지사항</h2>

<div class="container-wrapper">
<main class="container">

  <!-- 🔍 검색 영역 -->
  <div class="search-area">
    <form method="get" action="${pageContext.request.contextPath}/resident/notice" id="noticeSearchForm" class="search-form">
      <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />

      <div class="search-item">
        <label for="bldgIdParam">건물</label>
        <select name="bldgIdParam" class="select-field">
          <c:forEach var="unit" items="${unitList}">
            <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
              ${unit.building.bldgNm}
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="search-item">
        <label for="noticeType">유형</label>
        <select name="noticeType" class="select-field">
          <option value="">-- 전체 --</option>
          <c:forEach var="code" items="${noticeTypeList}">
            <option value="${code.codeValue}" <c:if test="${simpleSearch.noticeType eq code.codeValue}">selected</c:if>>
              ${code.codeName}
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="search-item">
        <label>검색일자</label>
        <div class="date-wrapper">
          <input type="date" name="searchStartDate" value="${simpleSearch.searchStartDate}" class="date-field" />
          <span>~</span>
          <input type="date" name="searchEndDate" value="${simpleSearch.searchEndDate}" class="date-field" />
        </div>
      </div>

      <div class="search-item">
        <label for="searchType">조건</label>
        <select name="searchType" class="select-field">
          <option value="">-- 전체 --</option>
          <option value="title" <c:if test="${simpleSearch.searchType eq 'title'}">selected</c:if>>제목</option>
          <option value="content" <c:if test="${simpleSearch.searchType eq 'content'}">selected</c:if>>내용</option>
          <option value="title+content" <c:if test="${simpleSearch.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
        </select>
      </div>

    <div class="search-actions">
	  <label for="searchWord">검색어</label>
	  <div class="search-input-with-btn">
	    <input type="text" name="searchWord" value="${simpleSearch.searchWord}" class="input-field" placeholder="검색어 입력" />
	    <button type="submit" class="search-button">검색</button>
	    <button type="button" class="btn-reset" onclick="location.href='${pageContext.request.contextPath}/resident/notice?page=1'">초기화</button>
	  </div>
	</div>
    </form>
  </div>

<!-- 📋 공지 목록 -->
<table class="table">
  <thead>
    <tr>
      <th>번호</th>
      <th>유형</th>
      <th>제목</th>
      <th>작성자</th>
      <th>게시일</th>
      <th>조회수</th>
      <th>보기</th>
    </tr>
  </thead>
  <tbody id="noticeTableBody">
    <!-- 비동기 렌더링 영역 -->
  </tbody>
</table>

<!-- 📄 페이징 -->
<div class="pagination-wrapper">
  <!-- 비동기 페이징 영역 -->
</div>

<!-- ✏️ 등록 버튼 -->
<div class="write-buttons">
  <sec:authorize access="hasAuthority('ADMIN') or hasAuthority('TENANCY')">
    <a href="/resident/notice/form" class="btn-success">글쓰기</a>
  </sec:authorize>
</div>

<script>
  document.addEventListener("DOMContentLoaded", () => {
    setupGlobalBuildingSelector({
      param: "bldgIdParam",
      storageKey: "selectedBuildingId",
      onChange: (bldgId, page) => {
        loadNotices(page);
      },
      pageParam: "page",
      pageSize: 10
    });
  });
</script>
<!-- ✅ axios CDN 추가 (필수) -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/commonBuildingSelect.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/residentNotice.js"></script>

</body>
</html>
