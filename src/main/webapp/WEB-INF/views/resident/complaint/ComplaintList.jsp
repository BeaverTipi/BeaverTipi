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
    .search-area {
      margin-bottom: 30px;
      border: 1px solid #ddd;
      padding: 20px;
      border-radius: 8px;
      background-color: #fff;
    }
    
.search-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  column-gap: 24px;
  row-gap: 16px;
}

    .search-item {
      display: flex;
      flex-direction: column;
    }

    .search-item label {
      font-weight: bold;
      font-size: 14px;
      margin-bottom: 8px;
    }

    .select-field,
    .input-field {
      padding: 8px;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    .select-field.short {
      width: 120px;
    }

    .input-field.short {
      width: 160px;
    }

    .date-wrapper {
      display: flex;
      gap: 10px;
    }

    .date-wrapper input {
      width: 45%;
    }

    .search-buttons {
      display: flex;
      justify-content: flex-start;
      gap: 10px;
      align-items: center;
      margin-top: 4px;
    }

    .search-button {
      background-color: #E17100;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      font-weight: bold;
      font-size: 14px;
      cursor: pointer;
      height: 38px;
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
    
    

    .search-button:hover {
      background-color: #973C00;
    }

    .btn-reset {
      background-color: #f0f0f0;
      color: #333;
      padding: 10px 20px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-weight: bold;
      font-size: 14px;
      height: 38px;
      text-decoration: none;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .btn-reset:hover {
      background-color: #ddd;
      color: #000;
    }
.radio-group {
  display: flex;
  flex-direction: row;       /* 🔄 가로 정렬로 변경 */
  gap: 16px;                 /* 라디오 사이 간격 */
  align-items: center;
  flex-wrap: wrap;           /* 필요 시 다음 줄 허용 */
}

.radio-group label {
  display: flex;
  flex-direction: row;     /* 가로 정렬 */
  align-items: center;
  gap: 6px;                /* 라디오와 텍스트 간격 */
  white-space: nowrap;     /* 줄바꿈 방지 */
}

    @media (max-width: 768px) {
      .search-form {
        grid-template-columns: repeat(2, 1fr);
      }

      .search-buttons {
        flex-direction: column;
        align-items: stretch;
      }

      .search-button,
      .btn-reset {
        width: 100%;
      }

      .date-wrapper {
        flex-direction: column;
      }

      .date-wrapper input {
        width: 100%;
      }
    }
  </style>
</head>
<body>

<h2 class="board-title">📮 민원 목록</h2>
<div class="container-wrapper">
  <main class="container">

    <!-- 검색 영역 -->
    <div class="search-area">
    	<form id="searchForm" method="get" class="search-form">
        <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />
        <input type="hidden" name="brdCode" value="M0001" />

        <!-- 건물 선택 -->
        <div class="search-item">
          <label for="bldgIdParam">건물</label>
          <select name="bldgIdParam" id="bldgSelect" class="select-field">
            <c:forEach var="unit" items="${unitList}">
              <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
                ${unit.building.bldgNm}
              </option>
            </c:forEach>
          </select>
        </div>

        <!-- 공개 여부 -->
        <div class="search-item">
          <label>공개여부</label>
          <div class="radio-group">
            <label><input type="radio" name="openYn" value="" ${empty search.openYn ? 'checked' : ''}/> 전체</label>
            <c:forEach var="code" items="${openYnList}">
              <label>
                <input type="radio" name="openYn" value="${code.codeValue}" ${code.codeValue == search.openYn ? 'checked' : ''}/>
                ${code.codeName}
              </label>
            </c:forEach>
          </div>
        </div>

        <!-- 날짜 -->
        <div class="search-item">
          <label for="searchStartDate">일자</label>
          <div class="date-wrapper">
            <input type="date" name="searchStartDate" class="input-field" value="${search.searchStartDate}">
            ~
            <input type="date" name="searchEndDate" class="input-field" value="${search.searchEndDate}">
          </div>
        </div>

        <!-- 처리상태 -->
        <div class="search-item">
          <label>처리상태</label>
          <div class="radio-group">
            <label><input type="radio" name="reqStatus" value="" ${empty search.reqStatus ? 'checked' : ''}/> 전체</label>
            <c:forEach var="code" items="${reqStatusList}">
              <label>
                <input type="radio" name="reqStatus" value="${code.codeValue}" ${code.codeValue == search.reqStatus ? 'checked' : ''}/>
                ${code.codeName}
              </label>
            </c:forEach>
          </div>
        </div>
<div class="search-item search-combined-group" style="grid-column: span 2;">
  <label for="searchType">검색</label>
  <div class="combined-search-row">
    
    <!-- 검색조건 -->
    <div class="search-type-group">
      <select name="searchType" class="select-field short">
        <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
        <option value="content" ${search.searchType == 'content' ? 'selected' : ''}>내용</option>
      </select>
    </div>

    <!-- 검색어 -->
    <div class="search-word-group">
      <input type="text" name="searchWord" value="${search.searchWord}" class="input-field" placeholder="검색어를 입력하세요" />
    </div>

    <!-- 내 글만 보기 -->
    <div class="my-posts-group">
      <label>
        <input type="checkbox" id="myPostsOnly" name="myPostsOnly" value="Y"
               ${param.myPostsOnly == 'Y' ? 'checked' : ''} />
        내 글만 보기
      </label>
    </div>

  </div>
</div>


        <!-- 버튼 -->
		<div class="search-item search-buttons" style="grid-column: span 2; display: flex; justify-content: flex-end; align-items: end;">
		  <button type="submit" class="search-button">검색</button>
		  <a href="#" class="btn-reset" onclick="clearForm(event)">초기화</a>
		</div>
      </form>
    </div>

    <!-- 민원 목록 테이블 -->
    <table class="table">
      <thead>
        <tr>
          <th>작성자</th>
          <th>제목</th>
          <th>공개여부</th>
          <th>처리상태</th>
          <th>게시일</th>
          <th>보기</th>
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
  function fnPaging(pageNo) {
    const form = document.getElementById('searchForm');
    form.page.value = pageNo;
    form.submit();
  }
</script>
<script>
  function clearForm(e) {
    e.preventDefault();

    const form = document.getElementById("searchForm");
    if (!form) return;

    // input, select, radio 초기화
    form.reset();

    // hidden 필드(page 등)는 기본값으로 수동 초기화
    form.page.value = 1;

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
