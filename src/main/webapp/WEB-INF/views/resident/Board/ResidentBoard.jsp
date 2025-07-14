<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 게시판</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css">
</head>
<body>
<h2 class="board-title">입주민 게시판</h2>
<div class="container-wrapper">

<main class="container">

<!-- 🔍 검색 영역 -->
<div class="search-area">
  <form method="get" action="${pageContext.request.contextPath}/resident/board" class="search-form">

    <!-- ✅ 좌측: 검색 조건 필드 -->
    <div class="search-conditions">
      <div class="search-item">
        <label for="bldgIdParam">건물</label>
        <select name="bldgIdParam" class="select-field">
          <option value="">건물 선택</option>
          <c:forEach var="unit" items="${unitList}">
            <option value="${unit.bldgId}" ${unit.bldgId == selectedBldgId ? 'selected' : ''}>
              ${unit.building.bldgNm}
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="search-item double-width">
        <label for="searchWord">일자</label> 
        <div class="date-wrapper">
        <input type="date" name="searchStartDate" class="input-field" />
        ~
        <input type="date" name="searchEndDate" class="input-field" />
        </div>
      </div>
      <div class="search-item">
        <label for="searchType">조건</label>
        <select name="searchType" class="select-field">
          <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
          <option value="writer" ${search.searchType == 'writer' ? 'selected' : ''}>작성자</option>
        </select>
      </div>
      <!-- ✅ 검색어를 double-width로 지정하고 정렬 -->
<div class="search-item double-width align-to-top">
  <label for="searchWord">검색어</label>
  <input type="text" name="searchWord" value="${search.searchWord}" placeholder="검색어 입력" class="input-field" />
</div>



    </div>

    <!-- ✅ 우측: 검색 버튼 -->
    <div class="search-button-area">
      <button type="submit">검색</button>
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
      </tr>
    </thead>
    <tbody>
      <c:forEach var="board" items="${boardList}" varStatus="status">
        <tr>
          <td>${pagingInfo.firstRecordIndex + status.index}</td>
          <td>
            <a href="<c:url value='/resident/board/detail'>
                       <c:param name='rsdBrdId' value='${board.rsdBrdId}'/>
                       <c:param name='bldgIdParam' value='${selectedBldgId}'/>
                     </c:url>">
              ${board.rsdBrdTitl}
            </a>
          </td>
          <td>${board.mbrNnm}</td>
          <td><fmt:formatDate value="${board.rsdBrdPblsDate}" pattern="yyyy-MM-dd" /></td>
          <td>${board.rsdBrdCnt}</td>
        </tr>
      </c:forEach>
      <c:if test="${empty boardList}">
        <tr>
          <td colspan="5" class="no-data-center">
            <c:choose>
              <c:when test="${not empty search.searchWord}">
                검색 결과가 없습니다.
              </c:when>
              <c:otherwise>
                건물을 선택하면 게시글이 표시됩니다.
              </c:otherwise>
            </c:choose>
          </td>
        </tr>
      </c:if>
    </tbody>
  </table>

  <!-- 📄 페이징 -->
  <div class="pagination-wrapper">
    ${pagingHTML}
  </div>

  <!-- ✏️ 글쓰기 및 휴지통 버튼 (페이징 아래로 이동) -->
<!-- ✏️ 글쓰기 및 휴지통 버튼 (오른쪽 정렬) -->
<c:if test="${not empty unitList}">
  <div class="write-buttons">
    <a href="${pageContext.request.contextPath}/resident/board/form?bldgId=${selectedBldgId}" class="btn-success">글쓰기</a>
    <a href="${pageContext.request.contextPath}/resident/board/trash?bldgIdParam=${selectedBldgId}" class="btn-dark" style="margin-left: 10px;">휴지통</a>
  </div>
</c:if>


  <!-- 🔁 페이징용 히든 폼 -->
  <form id="searchForm" method="get" action="${pageContext.request.contextPath}/resident/board">
    <input type="hidden" name="page" value="1" />
    <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
    <input type="hidden" name="searchType" value="${search.searchType}" />
    <input type="hidden" name="searchWord" value="${search.searchWord}" />
  </form>
</main>
</div>
<script>
  document.addEventListener("DOMContentLoaded", () => {
    const currentPage = "${pagingInfo.currentPageNo}";
    document.querySelectorAll(".pagination-wrapper a").forEach(a => {
      if (a.textContent.trim() === currentPage) {
        a.classList.add("bg-primary");
      }
    });

    // 🔽 검색 조건 개수 체크해서 버튼 위치 조정
    const conditionItems = document.querySelectorAll(".search-row .search-item");
    const buttonWrap = document.querySelector(".search-buttons-in-row");

    if (conditionItems.length > 3) {
      buttonWrap.classList.add("dynamic-margin");
    } else {
      buttonWrap.classList.remove("dynamic-margin");
    }
  });
</script>


</body>
</html>
