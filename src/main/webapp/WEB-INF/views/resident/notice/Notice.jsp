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
  .btn-view {
    background-color: var(--main-color-orange, #ff7f00);
    color: white;
    padding: 4px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  td{
  	  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  max-width: 300px; /* 셀 너비 지정 */
}
  	
  }
</style>
	
</head>
<body>

<h2 class="board-title">📢 공지사항</h2>
<div class="container">
<main class="container-wrapper">

<!-- 🔍 검색 영역 -->
<div class="search-area">
  <form method="get" action="${pageContext.request.contextPath}/resident/notice" id="noticeSearchForm" class="search-form">
    <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />

    <div class="search-conditions">
      
      <!-- 건물 -->
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

      <!-- 공지 유형 -->
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

      <!-- 검색 조건 -->
      <div class="search-item">
        <label for="searchType">조건</label>
        <select name="searchType" class="select-field">
          <option value="">-- 전체 --</option>
          <option value="title" <c:if test="${simpleSearch.searchType eq 'title'}">selected</c:if>>제목</option>
          <option value="content" <c:if test="${simpleSearch.searchType eq 'content'}">selected</c:if>>내용</option>
          <option value="title+content" <c:if test="${simpleSearch.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
        </select>
      </div>

      <!-- 검색어 -->
      <div class="search-item">
        <label for="searchWord">검색어</label>
        <input type="text" name="searchWord" value="${simpleSearch.searchWord}" class="input-field" placeholder="검색어 입력"/>
      </div>

      <!-- 검색 버튼 -->
      <div class="search-item">
        <button type="submit" class="search-button">검색</button>
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
        <c:when test="${fn:length(notice.brdTitlNm) > 30}">
          ${fn:substring(notice.brdTitlNm, 0, 30)}...
        </c:when>
        <c:otherwise>
          ${notice.brdTitlNm}
        </c:otherwise>
      </c:choose>
    </td>
    <td>${notice.member.mbrNnm}</td>
    <td>${notice.formattedBrdPblsDtm}</td>
    <td>${notice.brdVwCnt}</td>

    <!-- ✅ 보기 버튼 추가 -->
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
      <tr><td colspan="6" class="no-data-center">등록된 공지사항이 없습니다.</td></tr>
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
    <a href="/resident/notice/form" class="btn-success">공지 등록</a>
  </sec:authorize>
</div>

</main>
</div>

<!-- ✅ JS -->
<script>
  function fnPaging(pageNo) {
    const form = document.getElementById('noticeSearchForm');
    form.page.value = pageNo;
    form.submit();
  }
</script>
<script>
  document.addEventListener("DOMContentLoaded", () => {
    const bldgSelect = document.querySelector('select[name="bldgIdParam"]');
    bldgSelect?.addEventListener("change", () => {
      document.getElementById("noticeSearchForm").submit();
    });
  });
</script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
