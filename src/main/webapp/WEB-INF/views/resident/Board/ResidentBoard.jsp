<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>입주민 게시판</title>
  <style>
    html, body {
      margin: 0; padding: 0;
      font-size: 16px;
      font-family: 'Segoe UI', sans-serif;
      background-color: #f9f9f9;
    }
    main.container {
      max-width: 1000px; margin: 40px auto; padding: 0 20px;
      background: #fff; border: 1px solid #eee;
      box-shadow: 0 0 10px rgba(0,0,0,0.03);
      border-radius: 6px;
    }
    h1 { margin: 30px 0 20px; }
    .top-bar {
      display: flex; justify-content: flex-end;
      margin-bottom: 10px;
    }
    .search-form {
      display: flex; gap: 10px; align-items: center;
    }
    .search-form select, .search-form input[type="text"] {
      padding: 6px 10px;
      border: 1px solid #ccc; border-radius: 4px;
      font-size: 14px;
    }
    .btn-search {
      padding: 6px 14px; background-color: #007bff;
      color: #fff; border: none; border-radius: 4px;
      cursor: pointer; font-size: 14px;
    }
    .btn-search:hover { background-color: #0056b3; }
    table {
      width: 100%; border-collapse: collapse;
      margin-bottom: 10px;
    }
    th, td {
      border: 1px solid #ccc;
      padding: 14px 10px;
      font-size: 15px;
      text-align: center;
    }
    th { background-color: #f4f4f4; }
    tbody tr:nth-child(even) { background-color: #fafafa; }
    td:nth-child(2), th:nth-child(2) {
      text-align: left; padding-left: 20px; width: 40%;
    }
    td a {
      color: #333; font-weight: 500;
      text-decoration: none;
    }
    .footer-bar {
      display: flex; justify-content: flex-end;
      margin-bottom: 10px;
    }
    .btn-write {
      padding: 8px 16px;
      background-color: #28a745;
      color: #fff; border-radius: 4px;
      font-weight: bold; font-size: 16px;
      text-decoration: none;
    }
    .btn-write:hover { background-color: #218838; }
    .pagination-wrapper {
      display: flex; justify-content: center;
      margin-bottom: 30px;
    }
    .pagination { display: flex; gap: 6px; }
    .pagination a {
      display: inline-block; padding: 8px 14px;
      border: 1px solid #ddd; border-radius: 4px;
      color: #007bff; background: #fff;
      text-decoration: none; font-size: 14px;
      transition: background-color 0.2s ease;
    }
    .pagination a:hover {
      background-color: #e9f2ff; border-color: #007bff;
    }
    .pagination a.active {
      background-color: #007bff; color: #fff;
      border-color: #007bff; font-weight: bold;
      pointer-events: none;
    }
  </style>
</head>
<body>
<main class="container">

  <h1>입주민 게시판</h1>

  <!-- 🔍 검색 영역 -->
  <div class="top-bar">
    <form method="get"
          action="${pageContext.request.contextPath}/resident/board"
          class="search-form">

      <!-- 1️⃣ name="bldgIdParam" -->
      <select name="bldgIdParam" onchange="this.form.submit()">
        <option value="">건물 선택</option>
        <c:forEach var="unit" items="${unitList}">
          <option value="${unit.bldgId}"
                  ${unit.bldgId == selectedBldgId ? 'selected' : ''}>
            ${unit.building.bldgNm}
          </option>
        </c:forEach>
      </select>

      <select name="searchType">
        <option value="title"
          ${search.searchType == 'title' ? 'selected' : ''}>
          제목
        </option>
        <option value="writer"
          ${search.searchType == 'writer' ? 'selected' : ''}>
          작성자
        </option>
      </select>

      <input type="text"
             name="searchWord"
             value="${search.searchWord}"
             placeholder="검색어 입력" />

      <button type="submit" class="btn-search">검색</button>
    </form>
  </div>

  <!-- 📋 게시글 목록 -->
  <table>
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
            <!-- 2️⃣ 상세보기: rsdBrdId만 전달 -->
           <a href="<c:url value='/resident/board/detail'>
			           <c:param name='rsdBrdId'    value='${board.rsdBrdId}'/>
			           <c:param name='bldgIdParam' value='${selectedBldgId}'/>
			         </c:url>">
			  ${board.rsdBrdTitl}
		   </a>
          </td>
          <td>${board.mbrNnm}</td>
          <td>
            <fmt:formatDate value="${board.rsdBrdPblsDate}"
                            pattern="yyyy-MM-dd" />
          </td>
          <td>${board.rsdBrdCnt}</td>
        </tr>
      </c:forEach>
      <c:if test="${empty boardList}">
        <tr>
          <td colspan="5">
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

  <!-- ✏️ 글쓰기 버튼 -->
  <c:if test="${not empty unitList}">
    <div class="footer-bar">
      <a href="${pageContext.request.contextPath}/resident/board/form?bldgId=${selectedBldgId}"
         class="btn-write">
        글쓰기
      </a>
      <!-- 🗑️ 휴지통 버튼 (입주민도 사용 가능) -->
	  <a href="${pageContext.request.contextPath}/resident/board/trash?bldgIdParam=${selectedBldgId}"
	     class="btn-write"
	     style="background-color: #6c757d; margin-left: 10px;">
	    휴지통
	  </a>
    </div>
  </c:if>

  <!-- 📄 페이징 -->
  <div class="pagination-wrapper">
    <div class="pagination">${pagingHTML}</div>
  </div>

  <!-- 3️⃣ 페이징용 히든 폼 -->
  <form id="searchForm"
        method="get"
        action="${pageContext.request.contextPath}/resident/board">
    <input type="hidden" name="page"       value="1" />
    <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
    <input type="hidden" name="searchType"  value="${search.searchType}" />
    <input type="hidden" name="searchWord"  value="${search.searchWord}" />
  </form>

</main>


<script>
  function fnPaging(page) {
    const form = document.getElementById("searchForm");
    form.page.value = page;
    form.requestSubmit();
  }

  document.addEventListener("DOMContentLoaded", () => {
    const currentPage = "${pagingInfo.currentPageNo}";
    document.querySelectorAll(".pagination a").forEach(a => {
      if (a.textContent.trim() === currentPage) {
        a.classList.add("active");
      }
    });
  });
</script>

</body>
</html>