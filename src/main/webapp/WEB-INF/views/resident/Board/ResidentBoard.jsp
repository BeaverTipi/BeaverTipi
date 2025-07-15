<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 게시판</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css">
<style type="text/css">
	.btn-view {
	  background-color: var(--main-color-orange, #ff7f00);
	  color: white;
	  padding: 4px 10px;
	  border: none;
	  border-radius: 4px;
	  cursor: pointer;
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
            <div class="search-item row-type">
              <label for="searchType">조건</label>
              <select name="searchType" class="select-field select-type">
                <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
                <option value="writer" ${search.searchType == 'writer' ? 'selected' : ''}>작성자</option>
              </select>
            </div>
            <div class="search-item-group">
              <input type="text" name="searchWord" value="${search.searchWord}" placeholder="검색어 입력" class="input-field">
              <button type="submit" class="search-button">검색</button>
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
