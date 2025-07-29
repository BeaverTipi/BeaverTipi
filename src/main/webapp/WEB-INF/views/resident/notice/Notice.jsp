<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags"  %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>📢 공지사항</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css">
<style>
.container-wrapper {
  background-color: #ffffff;
  border: 1px solid #ddd;
  border-radius: 10px;
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.05);
  margin: 0 auto 40px;
  width: 95%;
  max-width: 1400px;
}
.search-area{
	  margin-bottom: 30px;
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 8px;
  background-color:  #f5f5f5;
}
/* ✅ 전체 검색영역 layout */
.search-section {
  background-color: var(--color-bg-light, #f9f9f9);
  padding: 20px;
  border: 1px solid var(--color-border, #ccc);
  border-radius: 6px;
  margin-bottom: 24px;
}

/* ✅ 2단 그리드: 왼쪽 조건, 오른쪽 버튼 */
.search-grid-container {
  display: grid;
  grid-template-columns: 4fr 1fr;
  gap: 20px;
}

.search-grid-left {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.search-grid-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
}

.search-grid-right {
  display: flex;
  align-items: flex-end;
  justify-content: flex-end;
}

.button-area {
  display: flex;
  gap: 10px;
}

/* ✅ 필드 레이아웃 및 폼 스타일 */
.search-item {
  display: flex;
  flex-direction: column;
}

.search-item label {
  font-weight: 600;
  margin-bottom: 6px;
  font-size: 14px;
}
.search-item.full-width {
  flex: 1;
  min-width: 260px;
  
}
.form-control {
  padding: 8px 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: white;
}

.search-item.full-width {
  grid-column: span 2;
}

.input-range {
  display: flex;
  align-items: center;
  gap: 6px;
}

.date-separator {
  padding: 0 6px;
  font-weight: bold;
  color: #666;
  margin-top : 6px;
}

/* ✅ 버튼 */
.btn-search {
  background-color: #333;
  color: #fff;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
}

.btn-reset {
  background-color: #ffc107;
  color: #000;
  border: none;
  padding: 10px 20px;
  border-radius: 6px;
}

/* 제목에 마우스를 올렸을 때 밑줄 효과 */
.notice-title {
  color: #333;
  text-decoration: none;
  cursor: pointer;
}

.notice-title:hover {
  text-decoration: underline;
  color: #E17100;
}


</style>

</head>
<body>

<h2 class="board-title">📢 공지사항</h2>

<div class="container-wrapper">
  <main class="container">
    <!-- 🔍 검색영역 -->
    <div class="search-area">
      <form method="get" action="${pageContext.request.contextPath}/resident/notice" id="noticeSearchForm">
        <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />

        <div class="search-grid-container">
          <!-- 왼쪽 필드 -->
          <div class="search-grid-left">
            <!-- 1단: 건물 -->
            <div class="search-grid-row">
              <div class="search-item" style="grid-column: span 4;">
                <label for="bldgIdParam">건물</label>
                <select name="bldgIdParam" class="form-control">
                  <c:forEach var="unit" items="${unitList}">
                    <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
                      ${unit.building.bldgNm}
                    </option>
                  </c:forEach>
                </select>
              </div>
            </div>

            <!-- 2단: 유형 / 조건 / 날짜 / 검색어 -->
            <div class="search-grid-row">
              <div class="search-item">
                <label for="noticeType">유형</label>
                <select name="noticeType" class="form-control">
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
                <div class="input-range">
                  <input type="date" name="searchStartDate" value="${simpleSearch.searchStartDate}" class="form-control" />
                  <span class="date-separator">~</span>
                  <input type="date" name="searchEndDate" value="${simpleSearch.searchEndDate}" class="form-control" />
                </div>
              </div>

              <div class="search-item">
                <label for="searchType">조건</label>
                <select name="searchType" class="form-control">
                  <option value="">-- 전체 --</option>
                  <option value="title" <c:if test="${simpleSearch.searchType eq 'title'}">selected</c:if>>제목</option>
                  <option value="content" <c:if test="${simpleSearch.searchType eq 'content'}">selected</c:if>>내용</option>
                  <option value="title+content" <c:if test="${simpleSearch.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
                </select>
              </div>
              <div class="search-item">
                <label for="searchWord">검색어</label>
                <input type="text" name="searchWord" value="${simpleSearch.searchWord}" class="form-control" placeholder="검색어 입력" />
              </div>
            </div>
          </div>

          <!-- 오른쪽 버튼 -->
          <div class="search-grid-right">
            <div class="button-area">
              <button type="button" class="btn-reset" onclick="location.href='${pageContext.request.contextPath}/resident/notice?page=1'">초기화</button>
              <button type="submit" class="btn-search">검색</button>
            </div>
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
      <security:authorize access="hasRole('ADMIN') or hasRole('TENANCY')">
        <a href="/resident/notice/form" class="btn-success">글쓰기</a>
      </security:authorize>
    </div>
  </main>
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
