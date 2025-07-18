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
    <select name="search.bldgId" class="select-field" onchange="loadPosts(this.value)">
      <option value="">건물 선택</option>
      <c:forEach var="unit" items="${unitList}">
      	<option value="${unit.bldgId}" 
		  <c:if test="${unit.bldgId eq search.bldgId or (empty search.bldgId and unit.bldgId eq selectedBldgId)}">selected</c:if>>
		  ${unit.building.bldgNm}
		</option>
      </c:forEach>
    </select>
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
      <table class="table">
        <thead>
          <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
            <th>보기</th>
          </tr>
        </thead>
        <tbody>
          <c:forEach var="board" items="${boardList}" varStatus="status">
            <tr>
              <td>${pagingInfo.firstRecordIndex + status.index}</td>
              <td><c:out value="${board.rsdBrdTitl}" /></td>
              <td>${board.mbrNnm}</td>
              <td><fmt:formatDate value="${board.rsdBrdPblsDate}" pattern="yyyy-MM-dd" /></td>
              <td>${board.rsdBrdCnt}</td>
              <td>
                <form method="get" action="${pageContext.request.contextPath}/resident/board/detail" style="display:inline;">
                  <input type="hidden" name="rsdBrdId" value="${board.rsdBrdId}" />
                  <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
                  <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />
                  <button type="submit" class="btn-view">보기</button>
                </form>
              </td>
            </tr>
          </c:forEach>
          <c:if test="${empty boardList}">
            <tr>
              <td colspan="6" class="no-data-center">
                <c:choose>
                  <c:when test="${not empty search.searchWord}">검색 결과가 없습니다.</c:when>
                  <c:otherwise>건물을 선택하면 게시글이 표시됩니다.</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:if>
        </tbody>
      </table>

      <!-- 📄 페이징 -->
      <div class="pagination-wrapper">${pagingHTML}</div>

      <!-- ✏️ 글쓰기 및 휴지통 버튼 -->
      <c:if test="${not empty unitList && not empty selectedBldgId}">
        <div class="write-buttons">
          <a href="${pageContext.request.contextPath}/resident/board/form?bldgId=${selectedBldgId}" class="btn-success">글쓰기</a>
          <a href="${pageContext.request.contextPath}/resident/board/trash?bldgIdParam=${selectedBldgId}" class="btn-dark">휴지통</a>
        </div>
      </c:if>

      <!-- 🔁 페이징용 히든 폼 -->
      <form id="searchForm" method="get" action="${pageContext.request.contextPath}/resident/board">
        <input type="hidden" name="page" value="1">
        <input type="hidden" name="bldgIdParam" value="${selectedBldgId}">
        <input type="hidden" name="searchType" value="${search.searchType}">
        <input type="hidden" name="searchWord" value="${search.searchWord}">
        <input type="hidden" name="searchStartDate" value="${search.searchStartDate}">
        <input type="hidden" name="searchEndDate" value="${search.searchEndDate}">
        <input type="hidden" name="search.bldgId" value="${empty search.bldgId ? selectedBldgId : search.bldgId}" />
      </form>
    </main>
  </div>

  <script>
    function fnPaging(pageNo) {
      const form = document.getElementById('searchForm');
      form.page.value = pageNo;
      form.submit();
    }

    document.addEventListener("DOMContentLoaded", () => {
      const currentPage = "${pagingInfo.currentPageNo}";
      document.querySelectorAll(".pagination-wrapper a").forEach(a => {
        if (a.textContent.trim() === currentPage) {
          a.classList.add("bg-primary");
        }
      });
    });
  </script>
 <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
<script src="${pageContext.request.contextPath}/app/js/resident/residentBuilding.js"></script>
</body>
</html>
