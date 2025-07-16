<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 게시판</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css">
  <style type="text/css">
    /* 검색 영역 스타일 */
    .search-area {
      margin-bottom: 30px;
      border: 1px solid #ddd;
      padding: 20px;
      border-radius: 8px;
      background-color: #fff;
    }

    /* 검색 폼 레이아웃 */
    .search-form {
      display: grid;
      grid-template-columns: repeat(4, 1fr); /* 4개의 열로 나누기 */
      gap: 20px;
      margin-bottom: 30px;
    }

    /* 검색 항목 */
    .search-item {
      display: flex;
      flex-direction: column;
    }

    /* 레이블 */
    .search-item label {
      font-weight: bold;
      margin-bottom: 8px;
      font-size: 14px;
    }

    /* 입력 필드 */
    .select-field,
    .input-field {
      padding: 8px;
      font-size: 14px;
      border: 1px solid #ddd;
      border-radius: 4px;
      margin-bottom: 10px;
      width: 100%;
    }

    /* 버튼 */
    .search-button {
      background-color: var(--main-color-orange, #ff7f00) !important;
      color: white;
      padding: 12px 20px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
      font-weight: bold;
      transition: background-color 0.3s ease;
      width: 100%;
    }

    .search-button:hover {
      background-color: #e67e22;
    }

    /* 날짜 선택 칸 */
    .date-wrapper {
      display: flex;
      gap: 10px;
    }

    .date-wrapper input {
      width: 45%;
    }

    /* 화면 크기 768px 이하에서의 레이아웃 조정 */
    @media (max-width: 768px) {
      .search-form {
        grid-template-columns: 1fr 1fr; /* 작은 화면에서는 2개 열로 나누기 */
      }

      .search-button {
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
  <h2 class="board-title">입주민 게시판</h2>
  <div class="container-wrapper">
    <main class="container">
      <!-- 🔍 검색 영역 -->
      <div class="search-area">
        <form method="get" action="${pageContext.request.contextPath}/resident/board" class="search-form">
          <div class="search-conditions">
            <div class="search-item">
              <label for="bldgIdParam">건물</label>
              <select name="bldgIdParam" class="select-field">
                <option value="">건물 선택</option>
                <c:forEach var="unit" items="${unitList}">
                  <option value="${unit.bldgId}" ${unit.bldgId == selectedBldgId ? 'selected' : ''}>${unit.building.bldgNm}</option>
                </c:forEach>
              </select>
            </div>

            <div class="search-item">
              <label for="searchStartDate">일자</label>
              <div class="date-wrapper">
                <input type="date" name="searchStartDate" class="input-field" value="${search.searchStartDate}">
                ~
                <input type="date" name="searchEndDate" class="input-field" value="${search.searchEndDate}">
              </div>
            </div>

            <div class="search-item">
              <label for="searchType">조건</label>
              <select name="searchType" class="select-field">
                <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
                <option value="writer" ${search.searchType == 'writer' ? 'selected' : ''}>작성자</option>
              </select>
            </div>

            <div class="search-item-group">
              <label for="searchWord">검색어</label>
              <input type="text" name="searchWord" value="${search.searchWord}" placeholder="검색어 입력" class="input-field">
            </div>

            <div class="search-item-group">
              <button type="submit" class="search-button">검색</button>
            </div>
          </div>
        </form>
        <form method="get" action="${pageContext.request.contextPath}/resident/board" style="display:inline;">
		    <input type="hidden" name="page" value="1" />
		    <button type="submit" class="btn-reset">초기화</button>
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
      </form>
    </main>
  </div>

  <script>
    function fnPaging(pageNo) {
      const form = document.getElementById('searchForm');
      form.page.value = pageNo;
      form.submit();
    }
  </script>

  <script>
    document.addEventListener("DOMContentLoaded", () => {
      const currentPage = "${pagingInfo.currentPageNo}";
      document.querySelectorAll(".pagination-wrapper a").forEach(a => {
        if (a.textContent.trim() === currentPage) {
          a.classList.add("bg-primary");
        }
      });
    });
  </script>

  <script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
