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
    .search-area {
      margin-bottom: 30px;
      border: 1px solid #ddd;
      padding: 20px;
      border-radius: 8px;
      background-color: #fff;
    }

    .search-form {
      display: flex;
      justify-content: space-between;
      align-items: flex-start;
      flex-wrap: wrap;
      gap: 20px;
    }

.search-description {
  flex-shrink: 0;
  min-width: 240px; /* ✅ 너비 확보 */
  font-size: 16px;
  font-weight: 500;
  color: #444;
  align-self: center;
}

    .search-fields {
      display: grid;
      grid-template-columns: repeat(4, auto);
      gap: 20px;
    }

    .search-item {
      display: flex;
      flex-direction: column;
      min-width: auto;
      width: auto;
    }

    .search-item label {
      font-weight: bold;
      margin-bottom: 8px;
      font-size: 14px;
    }

    .date-wrapper {
      display: flex;
      gap: 10px;
    }

    .date-wrapper input {
      width: 45%;
    }

    .search-item-group {
      display: flex;
      flex: 1 1 auto;
      gap: 8px;
      align-items: center;
    }

    .search-item-group select,
    .search-item-group input {
      height: 38px;
      padding: 0 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-size: 14px;
    }

    .search-item-group input {
      flex: 1 1 auto;
      min-width: 140px;
    }

    .search-button,
    .btn-reset {
      height: 38px;
      padding: 0 16px;
      border: none;
      border-radius: 4px;
      font-size: 14px;
      font-weight: bold;
      cursor: pointer;
      white-space: nowrap;
    }

    .search-button {
      background-color: #E17100;
      color: white;
    }

    .search-button:hover {
      background-color: #973C00;
    }

    .btn-reset {
      background-color: #ccc;
      color: #333;
    }

    .btn-reset:hover {
      background-color: #999;
    }

/* 조건 select 전용 그룹 */
.condition-group {
  width: 80px; /* 줄여서 정렬 맞추기 */
}

.select-type {
  max-width: 100px;
  /* 그룹 내에서 꽉 차게 */
}
.post-title-link {
  color: #333;
  text-decoration: none;
  cursor: pointer;
}

.post-title-link:hover {
  text-decoration: underline;
  color: #E17100;
}
.loading-row, .error-row {
  text-align: center;
  font-style: italic;
  color: #999;
}
.table-loading-overlay {
  position: absolute;
  background-color: rgba(255,255,255,0.8);
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: 10;
  display: flex;
  justify-content: center;
  align-items: center;
  font-style: italic;
  color: #888;
}
.table-wrapper {
  position: relative;
}
.fade-out {
  opacity: 0;
  transition: opacity 0.2s ease-out;
}
.fade-in {
  opacity: 1;
  transition: opacity 0.2s ease-in;
}

    @media (max-width: 768px) {
      .search-form {
        flex-direction: column;
      }

      .search-fields {
        grid-template-columns: 1fr 1fr;
      }

      .date-wrapper {
        flex-direction: column;
      }

      .date-wrapper input {
        width: 100%;
      }

      .search-item-group {
        flex-direction: column;
        align-items: stretch;
      }

      .search-item-group input,
      .search-button,
      .btn-reset {
        width: 100%;
      }
    }
  </style>
</head>
<body>
  <h2 class="board-title">입주민 게시판</h2>
  <div class="container-wrapper">
    <main class="container">
      <!-- 🔍 검색 영역 -->
      <div class="search-area">
        <form method="get" action="${pageContext.request.contextPath}/resident/board" class="search-form">
          <div class="search-description">
            아름다운 우리 집 게시글 함께 만들어갑시다
          </div>
			<div class="post-list"></div>
			<div class="search-fields">
			  <!-- 건물 -->
			  <div class="search-item">
			    <label for="bldgIdParam">건물</label>
			    <select name="bldgIdParam" class="select-field">
				  <option value="">건물 선택</option>
				  <c:forEach var="unit" items="${unitList}">
				    <option value="${unit.bldgId}" 
				      <c:if test="${unit.bldgId eq param.bldgIdParam or unit.bldgId eq selectedBldgId}">selected</c:if>>
				      ${unit.building.bldgNm}
				    </option>
				  </c:forEach>
				</select>
				
	<script>
  document.querySelector('select[name="bldgIdParam"]').addEventListener('change', function(event) {
    console.log("Selected Building ID:", event.target.value);  // 로그 추가
  });
</script>
	
  </div>

  <!-- 일자 -->
  <div class="search-item">
    <label for="searchStartDate">일자</label>
    <div class="date-wrapper">
      <input type="date" name="searchStartDate" value="${search.searchStartDate}">
      ~
      <input type="date" name="searchEndDate" value="${search.searchEndDate}">
    </div>
  </div>

	  <!-- 조건 -->
	<div class="search-item">
	  <label for="searchType">조건</label>
	  <div class="condition-group">
	    <select name="searchType" class="select-field select-type">
	      <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
	      <option value="writer" ${search.searchType == 'writer' ? 'selected' : ''}>작성자</option>
	    </select>
	  </div>
	</div>
	
	<!-- 내 글만 보기 -->
	<div class="search-item">
	  <label for="myPostsOnly"> </label>
	  <div class="search-item-group">
	    <label style="font-weight: normal;">
	      <input type="checkbox" id="myPostsOnly" name="myPostsOnly" value="Y"
	        ${param.myPostsOnly == 'Y' ? 'checked' : ''} />
	      내 글만 보기
	    </label>
	  </div>
	</div>
	
	  <!-- 검색어 (입력 + 버튼) -->
	  <div class="search-item">
	    <label for="searchWord">검색어</label>
	    <div class="search-item-group">
	      <input type="text" name="searchWord" value="${search.searchWord}" placeholder="검색어 입력" />
	      <button type="submit" class="search-button">검색</button>
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
          <a href="${pageContext.request.contextPath}/resident/board/trash?bldgIdParam=${selectedBldgId}" class="btn-dark">휴지통</a>
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
