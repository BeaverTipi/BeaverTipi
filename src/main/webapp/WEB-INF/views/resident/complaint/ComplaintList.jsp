<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>민원 목록</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css" />
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  <style>
/* ✅ 컨테이너 기본 스타일 */
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
  grid-template-columns: repeat(6, 1fr);
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

.search-item {
  display: flex;
  flex-direction: column;
  gap: 4px;
  justify-content: flex-start;
}

.search-item label {
  font-weight: 600;
  font-size: 14px;
  color: #333;
  display: inline-block;
}

/* ✅ select/input 공통 정렬 + 너비 */
.form-control {
  width: 100%;
  height: 38px;
  font-size: 14px;
  padding: 8px 10px;
  border: 1px solid #ccc;
  border-radius: 4px;
  background-color: white;
  box-sizing: border-box;
  text-align: center;
  -webkit-appearance: none;
  appearance: none;
  text-align-last: center;
}

/* ✅ 날짜 범위 */
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
.search-item.checkbox-inline {
  flex-direction: row;
  align-items: center;
  gap: 8px;
  justify-content: flex-start;
  padding-top: 22px; /* ✅ 버튼과 높이 맞춤 */
}
/* ✅ 체크박스 정렬 */
.search-item input[type="checkbox"] {
  transform: scale(1.2);
  margin-top: 4px;
  align-self: flex-start;
}
.search-item label[for="searchWord"] + label {
  display: flex;
  align-items: center;
  gap: 6px;
}

/* ✅ 버튼 */
.btn-search, .btn-reset {
  height: 42px;
  padding: 0 20px;
  font-size: 14px;
  font-weight: bold;
  border: none;
  border-radius: 4px;
  white-space: nowrap;
  cursor: pointer;
}

.btn-search {
  background-color: #E17100;
  color: #fff;
}
.btn-search:hover {
  background-color: #973C00;
}

.btn-reset {
  background-color: #ccc;
  color: #333;
}
.btn-reset:hover {
  background-color: #999;
}

.notice-title {
  color: #333;
  text-decoration: none;
  cursor: pointer;
}
.notice-title:hover {
  text-decoration: underline;
  color: #E17100;
}
    .badge {
  display: inline-block;
  padding: 4px 10px;
  border-radius: 12px;
  font-size: 13px;
  font-weight: bold;
  text-align: center;
}

/* 각각 색상 지정 */
.badge-blue {
  background-color: #e0f0ff;
  color: #007acc;
}

.badge-dark {
  background-color: #f0f0f0;
  color: #333;
}

.badge-orange {
  background-color: #ffe3c3;
  color: #e17100;
}

.badge-green {
  background-color: #d5f5dc;
  color: #2a8a43;
}
.bldgSelect {
  height: 38px;
  padding: 0 10px;
  font-size: 14px;
  line-height: 38px;     /* ✅ 줄 높이를 height와 동일하게 */
  box-sizing: border-box;
  min-width: 597px;
  max-width: 597px;
}
.search-item.my-posts {
  display: flex;
  flex-direction: column;
  justify-content: flex-start;
}

.search-item.my-posts input[type="checkbox"] {
  transform: scale(1.4);            /* 크기 확대 */
  accent-color: #E17100;           /* 체크 시 색상 (지원 브라우저 한정) */
  width: 18px;
  height: 18px;
  cursor: pointer;
  margin-top: 6px;                 /* 수직 정렬 보정 */
}

/* ✅ 반응형 대응 */
@media (max-width: 992px) {
  .search-grid-row {
    grid-template-columns: repeat(2, 1fr);
  }
}

@media (max-width: 576px) {
  .search-grid-container {
    grid-template-columns: 1fr;
  }

  .search-grid-row {
    grid-template-columns: 1fr;
  }

  .search-grid-right {
    justify-content: flex-start;
  }
}

  </style>
</head>
<body>

<!-- 상단 선언부 생략 -->
<h2 class="board-title">📮 민원 목록</h2>
<div class="container-wrapper">
  <main class="container">

<!-- ✅ 새 검색영역 -->
<div class="search-area">
  <form id="searchForm" method="get">
    <input type="hidden" name="brdCode" value="M0001" />
    
    <div class="search-grid-container">
      <div class="search-grid-left">

        <!-- 🔶 1단: 건물 (전폭) -->
        <div class="search-grid-row">
          <div class="search-item" style="grid-column: span 6;">
            <label for="bldgIdParam">건물명</label>
            <select name="bldgIdParam" class="form-control" id="bldgSelect">
              <c:forEach var="unit" items="${unitList}">
                <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
                  ${unit.building.bldgNm}
                </option>
              </c:forEach>
            </select>
          </div>
        </div>

        <!-- 🔶 2단: 6칸 정렬 -->
        <div class="search-grid-row">
          <div class="search-item">
            <label>처리상태</label>
            <select name="reqStatus" class="form-control">
              <option value="">-- 전체 --</option>
              <c:forEach var="code" items="${reqStatusList}">
                <option value="${code.codeValue}" <c:if test="${search.reqStatus eq code.codeValue}">selected</c:if>>
                  ${code.codeName}
                </option>
              </c:forEach>
            </select>
          </div>

          <div class="search-item">
            <label>공개여부</label>
            <select name="openYn" class="form-control">
              <option value="">-- 전체 --</option>
              <c:forEach var="code" items="${openYnList}">
                <option value="${code.codeValue}" <c:if test="${search.openYn eq code.codeValue}">selected</c:if>>
                  ${code.codeName}
                </option>
              </c:forEach>
            </select>
          </div>

          <div class="search-item">
            <label>일자</label>
            <div class="input-range">
              <input type="date" name="searchStartDate" value="${search.searchStartDate}" class="form-control" />
              <span class="date-separator">~</span>
              <input type="date" name="searchEndDate" value="${search.searchEndDate}" class="form-control" />
            </div>
          </div>

         <div class="search-item my-posts">
		  <label for="myPostsOnly">내 게시글 보기</label>
		  <input type="checkbox" id="myPostsOnly" name="myPostsOnly" value="Y"
		    <c:if test="${param.myPostsOnly eq 'Y'}">checked</c:if> />
		</div>
		          
          <div class="search-item">
            <label for="searchType">조건</label>
            <select name="searchType" class="form-control">
              <option value="">-- 전체 --</option>
              <option value="title" <c:if test="${search.searchType eq 'title'}">selected</c:if>>제목</option>
              <option value="content" <c:if test="${search.searchType eq 'content'}">selected</c:if>>내용</option>
              <option value="title+content" <c:if test="${search.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
            </select>
          </div>

          <div class="search-item">
            <label for="searchWord">검색어</label>
            <input type="text" name="searchWord" value="${search.searchWord}" class="form-control" placeholder="검색어 입력" />
          </div>

        </div>
      </div>

      <!-- 🔶 버튼 -->
      <div class="search-grid-right">
        <div class="button-area">
          <button type="submit" class="btn-search">검색</button>
          <button type="button" class="btn-reset" onclick="clearForm(event)">초기화</button>
        </div>
      </div>
    </div>
  </form>
</div>


    <!-- 민원 목록 테이블 -->
    <table class="table">
      <thead>
        <tr>
          <th>번호</th>
          <th>작성자</th>
          <th>제목</th>
          <th>공개여부</th>
          <th>처리상태</th>
          <th>게시일</th>
        </tr>
      </thead>
     <tbody id="boardTableBody" class="post-list">

  <c:if test="${empty boardList}">
    <tr><td colspan="6" class="no-data-center">검색 결과가 없습니다.</td></tr>
  </c:if>
</tbody>
    </table>

    <!-- 페이징 -->
	<div class="pagination-wrapper"></div>
    <!-- 글쓰기 버튼 -->
	<c:if test="${not empty selectedBldgId}">
	  <div class="write-buttons">
	  	<a class="btn-success" id="writeBtn" href="${pageContext.request.contextPath}/resident/complaint/form?bldgIdParam=${selectedBldgId}">글쓰기</a>
	  </div>
	</c:if>

  </main>
</div>
<!-- ✅ axios CDN 추가 (필수) -->
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/commonBuildingSelect.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/residentComplaint.js"></script>

<script>
  function clearForm(e) {
    e.preventDefault();

    const form = document.getElementById("searchForm");
    if (!form) return;

    // input, select, radio 초기화
    form.reset();

    // 로컬스토리지에 저장된 selectedBuildingId는 유지하되, 검색 조건만 초기화
    loadComplaints(currentBuildingId, 1); // 검색 조건 비운 상태로 목록 재조회
  }
</script>

<script>

const select = document.querySelector("#bldgSelect");
const writeBtn = document.querySelector("#writeBtn");

select.addEventListener("change", () => {
  //const selectedId = localStorage.getItem("selectedBuildingId");
  const selectedId = select.value;
    console.log(selectedId);
    writeBtn.href = `/resident/complaint/form?bldgIdParam=\${selectedId}`;
    console.log("writeBtn.href : ",writeBtn.href)

});
  function showPrivateAlert() {
    Swal.fire({
      icon: 'warning',
      title: '비공개 글입니다',
      text: '작성자만 확인할 수 있습니다.',
      confirmButtonColor: '#E17100'
    });
  }
</script>
<script>
  setupGlobalBuildingSelector({
    param: 'bldgIdParam',
    storageKey: 'selectedBuildingId',
    onChange: (bldgId, pageNo) => {
      loadComplaints(bldgId, pageNo);  // ✅ 새로고침 없이 AJAX 호출
    }
  });
</script>


</body>
</html>
