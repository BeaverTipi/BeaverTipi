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
      background-color: var(--main-color-orange, #ff7f00);
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

<h2 class="board-title">📮 민원 목록</h2>
<div class="container-wrapper">
  <main class="container">

    <!-- 🔍 검색 영역 -->
    <div class="search-area">
      <form id="searchForm" method="get" action="${pageContext.request.contextPath}/resident/complaint" class="search-form">
        <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />
        <input type="hidden" name="brdCode" value="M0001" />

        <!-- 건물 선택 -->
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

        <!-- 검색조건 -->
        <div class="search-item">
          <label for="searchType">조건</label>
          <select name="searchType" class="select-field">
            <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
            <option value="content" ${search.searchType == 'content' ? 'selected' : ''}>내용</option>
          </select>
        </div>

        <!-- 검색어 -->
        <div class="search-item">
          <label for="searchWord">검색어</label>
          <input type="text" name="searchWord" value="${search.searchWord}" class="input-field" placeholder="검색어 입력" />
        </div>

        <!-- 검색 버튼 -->
        <button type="submit" class="search-button">검색</button>

        <!-- 초기화 버튼 -->
        <a href="${pageContext.request.contextPath}/resident/complaint" class="btn-reset">초기화</a>
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
      <tbody>
        <c:forEach var="vo" items="${boardList}">
          <tr>
            <td>${vo.mbrNnm}</td>
            <td><c:out value="${vo.rsdBrdTitl}" /></td>
            <td>
              <c:choose>
                <c:when test="${vo.openYn == 'Y'}"><span class="badge badge-blue">공개</span></c:when>
                <c:otherwise><span class="badge badge-dark">비공개</span></c:otherwise>
              </c:choose>
            </td>
            <td>
              <c:forEach var="code" items="${reqStatusList}">
                <c:if test="${code.codeValue eq vo.reqStatus}">
                  <c:choose>
                    <c:when test="${code.codeValue == '001'}">
                      <span class="badge badge-orange">${code.codeName}</span>
                    </c:when>
                    <c:when test="${code.codeValue == '002'}">
                      <span class="badge badge-green">${code.codeName}</span>
                    </c:when>
                  </c:choose>
                </c:if>
              </c:forEach>
            </td>
            <td><fmt:formatDate value="${vo.rsdBrdPblsDate}" pattern="yyyy-MM-dd"/></td>
            <td>
              <form method="get" action="${pageContext.request.contextPath}/resident/complaint/view" style="display:inline;">
                <input type="hidden" name="rsdBrdId" value="${vo.rsdBrdId}" />
                <button type="submit" class="btn-view">보기</button>
              </form>
            </td>
          </tr>
        </c:forEach>
        <c:if test="${empty boardList}">
          <tr><td colspan="6" class="no-data-center">검색 결과가 없습니다.</td></tr>
        </c:if>
      </tbody>
    </table>

    <!-- 페이징 -->
    <div class="pagination-wrapper">
      <c:out value="${pagingHtml}" escapeXml="false"/>
    </div>

    <!-- 글쓰기 버튼 -->
    <div class="write-buttons">
      <a class="btn-success" href="${pageContext.request.contextPath}/resident/complaint/form?bldgIdParam=${selectedBldgId}">등록</a>
    </div>

  </main>
</div>

<script>
  function fnPaging(pageNo){
    const form = document.getElementById('searchForm');
    form.page.value = pageNo;
    form.submit();
  }
</script>

<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
