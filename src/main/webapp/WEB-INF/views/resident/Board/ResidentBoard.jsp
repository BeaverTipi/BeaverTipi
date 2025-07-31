<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 게시판</title>
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

.search-area {
  margin-bottom: 30px;
  border: 1px solid #ddd;
  padding: 20px;
  border-radius: 8px;
  background-color: #f5f5f5;
}

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

.search-item {
  display: flex;
  flex-direction: column;
}

.search-item label {
  font-weight: 600;
  margin-bottom: 6px;
  font-size: 14px;
}

.search-item select {
  min-width: 110px;
  max-width: 110px;
  text-align: center;
  text-align-last: center;
  padding-left: 0;
  padding-right: 0;
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
  margin-top: 6px;
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
  line-height: 38px;
  box-sizing: border-box;
  min-width: 597px;
  max-width: 597px;
}

#bldgselect option {
  text-align: center;
}

/* 버튼 */
.btn-search {
  background-color: #E17100;
  color: white;
}
.btn-search:hover {
  background-color: #973C00;
}
.btn-reset, .btn-search {
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

/* 게시판 제목 hover 효과 */
.board-title a {
  color: #333;
  text-decoration: none;
}
.board-title a:hover {
  text-decoration: underline;
  color: #E17100;
}
.checkbox-wrapper {
  display: flex;
  align-items: center;
  justify-content: flex-start;
  gap: 6px;
  height: 100%;
  cursor: pointer;
}

.checkbox-wrapper input[type="checkbox"] {
  width: 18px;
  height: 18px;
  transform: scale(1.4); /* ✅ 살짝 키움 */
  cursor: pointer;
  accent-color: #E17100; /* ✅ 브라우저 지원 시 색상 지정 */
}

.checkbox-wrapper span {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  user-select: none;
}
  </style>
</head>
<body>
  <h2 class="board-title">🏠 입주민 게시판</h2>
  <div class="container-wrapper">
    <main class="container">
      <!-- 🔍 검색 영역 -->
<!-- 🔍 검색영역 -->
<div class="search-area">
  <form method="get" action="${pageContext.request.contextPath}/resident/board" id="boardSearchForm">
    <input type="hidden" name="page" value="${param.page}" />

    <div class="search-grid-container">
      <!-- 왼쪽 필드 -->
      <div class="search-grid-left">
        <!-- 건물 선택 -->
        <div class="search-grid-row">
          <div class="search-item" style="grid-column: span 4;">
            <label for="bldgIdParam">건물명</label>
            <select name="bldgIdParam" class="form-control" id="bldgselect">
              <c:forEach var="unit" items="${unitList}">
                <option value="${unit.bldgId}" 
                  <c:if test="${unit.bldgId eq param.bldgIdParam or unit.bldgId eq selectedBldgId}">selected</c:if>>
                  ${unit.building.bldgNm}
                </option>
              </c:forEach>
            </select>
          </div>
        </div>

        <!-- 검색 조건 -->
        <div class="search-grid-row">
        
    
          
          <div class="search-item">
            <label for="searchStartDate">작성일</label>
            <div class="input-range">
              <input type="date" name="searchStartDate" value="${search.searchStartDate}" class="form-control" />
              <span class="date-separator">~</span>
              <input type="date" name="searchEndDate" value="${search.searchEndDate}" class="form-control" />
            </div>
          </div>
	
	      <div class="search-item">
            <label for="myPostsOnly">내 게시글 보기</label>
             <div class="checkbox-wrapper">
            <label style="font-weight: normal;">
              <input type="checkbox" id="myPostsOnly" name="myPostsOnly" value="Y"
                ${param.myPostsOnly == 'Y' ? 'checked' : ''} />
            </label>
            </div>
          </div>
          
          <div class="search-item">
            <label for="searchType">조건</label>
            <select name="searchType" class="form-control" id="searchType">
              <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
              <option value="writer" ${search.searchType == 'writer' ? 'selected' : ''}>작성자</option>
            </select>
          </div>


          <div class="search-item">
            <label for="searchWord">검색어</label>
            <input type="text" name="searchWord" value="${search.searchWord}" class="form-control" placeholder="검색어 입력" />
          </div>
        </div>
      </div>

      <!-- 오른쪽 버튼 -->
      <div class="search-grid-right">
        <div class="button-area">
          <button type="submit" class="btn-search">검색</button>
          <button type="button" class="btn-reset" onclick="location.href='${pageContext.request.contextPath}/resident/board'">초기화</button>
        </div>
      </div>
    </div>
  </form>
</div>

      

      <!-- 📋 게시글 목록 -->
      <div class="table-wrapper">
      <table class="table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
          </tr>
        </thead>
        <tbody id="boardTableBody">
        </tbody>
      </table>
     <div id="tableLoading" class="table-loading-overlay" style="display: none;">로딩 중입니다...</div>
</div>
	<div class="pagination-wrapper"></div> 
	
      <!-- ✏️ 글쓰기 및 휴지통 버튼 -->
      <c:if test="${not empty unitList && not empty selectedBldgId}">
        <div class="write-buttons">
          <a href="${pageContext.request.contextPath}/resident/board/form?bldgId=${selectedBldgId}" class="btn-success">글쓰기</a>
<%--           <a href="${pageContext.request.contextPath}/resident/board/trash?bldgIdParam=${selectedBldgId}" class="btn-dark">휴지통</a> --%>
        </div>
      </c:if>
    </main>
  </div>

 <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/commonBuildingSelect.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/residentBuilding.js"></script>

<script>
  // 📌 건물 선택 초기화 및 게시글 자동 로딩
  document.addEventListener('DOMContentLoaded', () => {
    setupGlobalBuildingSelector({
      param: 'bldgIdParam',
      storageKey: 'selectedBuildingId',
      onChange: loadPosts
    });

    // 📌 첫 진입 시 자동 게시글 로딩
    const selectedBldgId = localStorage.getItem('selectedBuildingId');
    if (selectedBldgId) {
      loadPosts(selectedBldgId, 1);
    }
    const myPostsOnlyCheckbox = document.querySelector('#myPostsOnly');
    if (myPostsOnlyCheckbox) {
      myPostsOnlyCheckbox.addEventListener('change', () => {
        const selectedBldgId = document.querySelector('select[name="bldgIdParam"]').value;
        if (selectedBldgId) {
          loadPosts(selectedBldgId, 1);
        }
      });
    }
  });
 
  // 📌 검색 폼 제출 시 비동기 게시글 로딩
  document.querySelector('.search-form').addEventListener('submit', function (e) {
    e.preventDefault(); // 폼 전송 막고
    const selectedBldgId = document.querySelector('select[name="bldgIdParam"]').value;
    if (selectedBldgId) {
      sessionStorage.removeItem('alreadyLoaded'); // 필요시 초기화
      loadPosts(selectedBldgId, 1); // 검색 조건 포함해 다시 로드
    }
  });
</script>



</body>
</html>
