<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
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
  </style>
</head>
<body>

<h2>📢 공지사항</h2>

<form method="get" action="/resident/notice" id="noticeSearchForm">
  <!-- 🔽 건물 선택 -->
  <select name="bldgIdParam">
    <c:forEach var="unit" items="${unitList}">
      <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
        ${unit.building.bldgNm}
      </option>
    </c:forEach>
  </select>

  <!-- 🔽 공지 유형 -->
  <select name="search.noticeType">
    <option value="">-- 유형 전체 --</option>
    <c:forEach var="code" items="${noticeTypeList}">
      <option value="${code.codeValue}" <c:if test="${search.noticeType eq code.codeValue}">selected</c:if>>
        ${code.codeName}
      </option>
    </c:forEach>
  </select>

  <!-- 🔽 검색 대상 -->
  <select name="search.searchType">
    <option value="">-- 전체 --</option>
    <option value="title" <c:if test="${search.searchType eq 'title'}">selected</c:if>>제목</option>
    <option value="content" <c:if test="${search.searchType eq 'content'}">selected</c:if>>내용</option>
    <option value="title+content" <c:if test="${search.searchType eq 'title+content'}">selected</c:if>>제목+내용</option>
  </select>

  <!-- 🔍 검색어 -->
  <input type="text" name="search.searchWord" value="${search.searchWord}" placeholder="검색어 입력" />
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
    <c:set var="generalIndex" value="1" scope="page"/>
    <c:forEach var="notice" items="${boardList}" varStatus="status">
      <tr>
        <td>
          <c:choose>
            <c:when test="${notice.noticeType eq '002' || notice.noticeType eq '003' || notice.noticeType eq '004'}">
              &#128204;
            </c:when>
            <c:otherwise>
              ${generalIndex}
              <c:set var="generalIndex" value="${generalIndex + 1}" scope="page"/>
            </c:otherwise>
          </c:choose>
        </td>
        <td><c:out value="${notice.noticeTypeCode.codeName}"/></td>
        <td>
          <a href="/resident/notice/detail?noticeNo=${notice.noticeNo}">
            <c:choose>
              <c:when test="${fn:length(notice.brdTitlNm) > 30}">
                <c:out value="${fn:substring(notice.brdTitlNm, 0, 30)}"/>...
              </c:when>
              <c:otherwise>
                <c:out value="${notice.brdTitlNm}"/>
              </c:otherwise>
            </c:choose>
          </a>
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

</body>
</html>