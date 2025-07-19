<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>📢 공지사항</title>
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
  display: grid;
  grid-template-columns: repeat(4, 1fr); /* 4열 균등 분할 */
  row-gap: 16px;
  column-gap:24px;
  align-items: center;
}

.search-item {
  display: flex;
  align-items: center;
  gap: 6px;
}
.search-input-with-btn {
  display: flex;
  flex: 1;
  gap: 8px;
}
.search-item label {
  font-weight: bold;
  width: 60px;
  flex-shrink: 0;
  text-align: left;
  margin-right: 2px;
}

.input-field {
  flex: 1;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.select-field,
.input-field,
.date-field {
  flex: 1;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ddd;
  border-radius: 4px;
}

.date-wrapper {
  display: flex;
  align-items: center;
  gap: 6px;
  width: 100%;
}

.search-actions {
  grid-column: span 4;
  display: flex;
  align-items: center;
  gap: 12px;
  flex-wrap: wrap;
}
.search-actions label {
  font-weight: bold;
  width: 60px;
  flex-shrink: 0;
}

.search-input-with-btn input.input-field {
  flex: 1 1 65%;  /* 너비 적당히 제한 */
  min-width: 240px;
  max-width: 500px;
  padding: 8px;
}
.search-input-with-btn {
  display: flex;
  flex: 1;
  align-items: center;
  gap: 8px;
  flex-wrap: nowrap;
}

.search-button,
.btn-reset {
  height: 42px;
  white-space: nowrap;
}

.search-button {
  background-color: #E17100;
  color: white;
  padding: 10px 16px;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  font-weight: bold;
  height: 42px;
  transition: background-color 0.3s ease;
}

.search-button:hover {
  background-color: #973C00;
}

.btn-reset {
  height: 42px;
  background: white;
  border: 1px solid #aaa;
  padding: 0 16px;
  border-radius: 4px;
  font-weight: bold;
  cursor: pointer;
}

</style>

</head>
<body>

<h2 class="board-title">📢 공지사항</h2>

<div class="container-wrapper">
<main class="container">

  <!-- 🔍 검색 영역 -->
  <div class="search-area">
    <form method="get" action="${pageContext.request.contextPath}/resident/notice" id="noticeSearchForm" class="search-form">
      <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />

      <div class="search-item">
        <label for="bldgIdParam">건물</label>
        <select name="bldgIdParam" class="select-field">
          <c:forEach var="unit" items="${unitList}">
            <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
              ${unit.building.bldgNm}
            </option>
          </c:forEach>
        </select>
      </div>

      <div class="search-item">
        <label for="noticeType">유형</label>
        <select name="noticeType" class="select-field">
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
        <div class="date-wrapper">
          <input type="date" name="searchStartDate" value="${simpleSearch.searchStartDate}" class="date-field" />
          <span>~</span>
          <input type="date" name="searchEndDate" value="${simpleSearch.searchEndDate}" class="date-field" />
        </div>
      </div>

      <div class="search-item">
        <label for="searchType">조건</label>
        <select name="searchType" class="select-field">
          <option value="">-- 전체 --</option>
          <option value="title" <c:if test="${simpleSearch.searchType eq 'title'}">selected</c:if>>제목</option>
          <option value="content" <c:if test="${simpleSearch.searchType eq 'content'}">selected</c:if>>내용</option>
          <option value="title+content" <c:if test="${simpleSearch.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
        </select>
      </div>

    <div class="search-actions">
	  <label for="searchWord">검색어</label>
	  <div class="search-input-with-btn">
	    <input type="text" name="searchWord" value="${simpleSearch.searchWord}" class="input-field" placeholder="검색어 입력" />
	    <button type="submit" class="search-button">검색</button>
	    <button type="button" class="btn-reset" onclick="location.href='${pageContext.request.contextPath}/resident/notice?page=1'">초기화</button>
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
        <th>보기</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="notice" items="${boardList}" varStatus="status">
        <tr>
          <td>
            <c:choose>
              <c:when test="${notice.noticeType=='002' || notice.noticeType=='003' || notice.noticeType=='004'}">
                &#128204;
              </c:when>
              <c:otherwise>
                ${pagingInfo.firstRecordIndex + status.index}
              </c:otherwise>
            </c:choose>
          </td>
          <td>${notice.noticeTypeCode.codeName}</td>
          <td title="${notice.brdTitlNm}">
            <c:choose>
              <c:when test="${fn:length(notice.brdTitlNm) > 30}">${fn:substring(notice.brdTitlNm, 0, 30)}...</c:when>
              <c:otherwise>${notice.brdTitlNm}</c:otherwise>
            </c:choose>
          </td>
          <td>${notice.member.mbrNnm}</td>
          <td>${notice.formattedBrdPblsDtm}</td>
          <td>${notice.brdVwCnt}</td>
          <td>
            <form method="get" action="${pageContext.request.contextPath}/resident/notice/detail" style="display:inline;">
              <input type="hidden" name="noticeNo" value="${notice.noticeNo}" />
              <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
              <input type="hidden" name="noticeType" value="${simpleSearch.noticeType}" />
              <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />
              <input type="hidden" name="searchType" value="${simpleSearch.searchType}" />
              <input type="hidden" name="searchWord" value="${simpleSearch.searchWord}" />
              <button type="submit" class="btn-view">보기</button>
            </form>
          </td>
        </tr>
      </c:forEach>

      <c:if test="${empty boardList}">
        <tr><td colspan="7" class="no-data-center">등록된 공지사항이 없습니다.</td></tr>
      </c:if>
    </tbody>
  </table>

  <!-- 📄 페이징 -->
  <div class="pagination-wrapper">
    <c:out value="${pagingHTML}" escapeXml="false"/>
  </div>

  <!-- ✏️ 등록 버튼 -->
  <div class="write-buttons">
    <sec:authorize access="hasAuthority('ADMIN') or hasAuthority('TENANCY')">
      <a href="/resident/notice/form" class="btn-success">글쓰기</a>
    </sec:authorize>
  </div>

</main>
</div>

<script>
  function fnPaging(pageNo) {
    const form = document.getElementById('noticeSearchForm');
    form.page.value = pageNo;
    form.submit();
  }

  document.addEventListener("DOMContentLoaded", () => {
    const bldgSelect = document.querySelector('select[name="bldgIdParam"]');
    bldgSelect?.addEventListener("change", () => {
      document.getElementById("noticeSearchForm").submit();
    });
  });
</script>

<script src="${pageContext.request.contextPath}/app/js/resident/commonBuildingSelect.js"></script>
</body>
</html>
