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
.search-item select {
  min-width: 110px;
  max-width: 110px;
  text-align: center;
  text-align-last: center;
  padding-left: 0;  
  padding-right: 0;  
}

.form-control {
  padding: 8px 10px;
  font-size: 14px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: white;
  text-align: center;     
  text-align-last: center;  
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
      background-color: #E17100;
      color: white;
}
.btn-search:hover{
	 background-color: #973C00;
}
.btn-reset,.btn-search {
      height: 38px;
      padding: 0 16px;
      border: none;
      border-radius: 4px;
      font-size: 14px;
      font-weight: bold;
      cursor: pointer;
      white-space: nowrap;
}
    .btn-reset {
      background-color: #ccc;
      color: #333;
    }
.btn-reset:hover {
      background-color: #999;
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
input {
  height: 38px;
  padding: 6px 10px;
  font-size: 14px;
  line-height: 1.2;
  box-sizing: border-box;
  min-width: 200px;
  max-width: 200px;
}

#bldgselect {
  height: 38px;
  padding: 0 10px;
  font-size: 14px;
  line-height: 38px;     /* ✅ 줄 높이를 height와 동일하게 */
  box-sizing: border-box;
  min-width: 597px;
  max-width: 597px;
}

#bldgselect option {
  text-align: center;     
}
td .type-icon {
  font-size: 18px;
  display: inline-block;
  text-align: center;
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
                <select name="bldgIdParam" class="form-control" id="bldgselect">
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
                <select name="noticeType" class="form-control" id="noticeType">
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
              <button type="submit" class="btn-search">검색</button>
              <button type="button" class="btn-reset" onclick="location.href='${pageContext.request.contextPath}/resident/notice?page=1'">초기화</button>
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
      <security:authorize access="hasRole('TENANCY')">
        <a href="/resident/notice/form" class="btn-success">글쓰기</a>
      </security:authorize>
    </div>
  </main>
</div>


<script>
document.addEventListener("DOMContentLoaded", () => {
	  const selector = document.querySelector('select[name="bldgIdParam"]');
	  const savedBldgId = localStorage.getItem("selectedBuildingId");

	  // 🟧 1. 초기 selectedBuildingId 없으면 첫 옵션으로 설정
	  if ((!savedBldgId || savedBldgId === "null") && selector && selector.options.length > 0) {
	    const defaultBldgId = selector.options[0].value;
	    localStorage.setItem("selectedBuildingId", defaultBldgId);
	    selector.value = defaultBldgId;
	  }

	  // 🟧 2. 건물 선택기 활성화
	  setupGlobalBuildingSelector({
	    param: "bldgIdParam",
	    storageKey: "selectedBuildingId",
	    onChange: (bldgId, page) => {
	      loadNotices(page);
	    },
	    pageParam: "page",
	    pageSize: 10
	  });

	  // 🟧 3. 최초 진입 시에도 공지사항 목록 로딩
	  const initialBldgId = localStorage.getItem("selectedBuildingId");
	  if (initialBldgId && initialBldgId !== "null") {
	    loadNotices(1); // ✅ 직접 첫 페이지 로드
	  }
	});
</script>
<!-- ✅ axios CDN 추가 (필수) -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/commonBuildingSelect.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/residentNotice.js"></script>

</body>
</html>
