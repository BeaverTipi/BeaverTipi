<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>공지사항</title>
  <style>
    .notice-table {
      width: 100%;
      border-collapse: collapse;
    }
    .notice-table th, .notice-table td {
      border: 1px solid #ccc;
      padding: 8px;
      text-align: center;
    }
    .notice-table th {
      background-color: #f5f5f5;
    }
    .clickable-row {
  cursor: pointer;
}
.clickable-row:hover {
  background-color: #f1f1f1;
}
.pinned-row {
  background-color: #e9f3ff;
  border-left: 4px solid #007bff;
}
.entire-row{
	background-color: #fff8e1;
}
  </style>
</head>
<body>

<h2>📢 공지사항</h2>

<form method="get" action="/resident/notice" id="noticeSearchForm">
	<input type="hidden" name="page" value="${pagingInfo.currentPageNo}">
  <!-- 🔽 건물 선택 -->
  <select name="bldgIdParam" onchange="this.form.submit()">
    <c:forEach var="unit" items="${unitList}">
      <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
        ${unit.building.bldgNm}
      </option>
    </c:forEach>
  </select>

  <!-- 🔽 공지 유형 -->
  <select name="noticeType">
    <option value="">-- 유형 전체 --</option>
    <c:forEach var="code" items="${noticeTypeList}">
      <option value="${code.codeValue}"
       <c:if test="${simpleSearch.noticeType eq code.codeValue}">selected</c:if>>
        ${code.codeName}
      </option>
    </c:forEach>
  </select>

  <!-- 🔽 검색 대상 -->
  <select name="searchType">
	  <option value="">-- 전체 --</option>
	  <option value="title" 
	    <c:if test="${simpleSearch.searchType eq 'title'}">selected</c:if>>제목</option>
	  <option value="content" 
	    <c:if test="${simpleSearch.searchType eq 'content'}">selected</c:if>>내용</option>
	  <option value="title+content" 
	    <c:if test="${simpleSearch.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
	</select>


  <!-- 🔍 검색어 -->
  <input type="text" name="searchWord" value="${simpleSearch.searchWord}" placeholder="검색어 입력" />
  <button type="submit">검색</button>
</form>

<!-- 📋 공지 목록 테이블 -->
<table class="notice-table">
  <thead>
    <tr>
      <th>번호</th>
      <th>유형</th>
      <th>제목</th>
      <th>작성자</th>
      <th>게시일</th>
      <th>조회수</th>
    </tr>
  </thead>
  <tbody>
    <c:forEach var="notice" items="${boardList}" varStatus="status">
      <c:url var="detailUrl" value="/resident/notice/detail">
        <c:param name="noticeNo"      value="${notice.noticeNo}" />
        <c:param name="bldgIdParam"   value="${selectedBldgId}" />
        <c:param name="noticeType"    value="${simpleSearch.noticeType}" />
        <c:param name="page"          value="${pagingInfo.currentPageNo}" />
        <c:param name="searchType"    value="${simpleSearch.searchType}" />
        <c:param name="searchWord"    value="${simpleSearch.searchWord}" />
      </c:url>

		<tr class="clickable-row ${notice.noticeType == '002' || notice.noticeType == '003' || notice.noticeType == '004' ? 'pinned-row' : ''}" 
    		data-href="${detailUrl}">
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
        <td><c:out value="${notice.noticeTypeCode.codeName}"/></td>
        <td>
          <c:choose>
            <c:when test="${fn:length(notice.brdTitlNm) > 30}">
              <c:out value="${fn:substring(notice.brdTitlNm, 0, 30)}"/>...
            </c:when>
            <c:otherwise>
              <c:out value="${notice.brdTitlNm}"/>
            </c:otherwise>
          </c:choose>
        </td>
        <td><c:out value="${notice.member.mbrNnm}"/></td>
        <td>${notice.formattedBrdPblsDtm}</td>
        <td><c:out value="${notice.brdVwCnt}"/></td>
      </tr>
    </c:forEach>

    <c:if test="${empty boardList}">
      <tr><td colspan="6">등록된 공지사항이 없습니다.</td></tr>
    </c:if>
  </tbody>
</table>

<!-- ➕ 등록 버튼 (권한 체크) -->
<sec:authorize access="hasAuthority('ADMIN') or hasAuthority('TENANCY')">
  <a href="/resident/notice/form">
    <button type="button">공지 등록</button>
  </a>
</sec:authorize>

<sec:authorize access="!hasAuthority('ADMIN') and !hasAuthority('TENANCY')">
  <div style="margin-top: 10px; color: #d9534f; font-weight: bold;">
    ⚠️ 공지사항 등록은 <span style="color: #007bff;">관리자</span> 또는 <span style="color: #007bff;">임대인</span>만 가능합니다.
  </div>
</sec:authorize>

<!-- 📄 페이징 -->
<div class="pagination">
  <c:out value="${pagingHTML}" escapeXml="false"/>
</div>

<script>
  document.addEventListener('DOMContentLoaded', function () {
    document.querySelectorAll('.clickable-row').forEach(row => {
      row.addEventListener('click', function () {
        const targetUrl = row.dataset.href;
        if (targetUrl) {
          window.location.href = targetUrl;
        }
      });
    });
  });
</script>

<script>
  function fnPaging(pageNo) {
    const form = document.getElementById('noticeSearchForm');
    form.page.value = pageNo;
    form.submit();
  }
</script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/residentList.js"></script>


</body>
</html>