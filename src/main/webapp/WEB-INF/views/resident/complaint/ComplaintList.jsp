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
    .search-area {
      margin-bottom: 30px;
      border: 1px solid #ddd;
      padding: 20px;
      border-radius: 8px;
      background-color: #fff;
    }
    
.search-form {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  column-gap: 24px;
  row-gap: 16px;
}

    .search-item {
      display: flex;
      flex-direction: column;
    }

    .search-item label {
      font-weight: bold;
      font-size: 14px;
      margin-bottom: 8px;
    }

    .select-field,
    .input-field {
      padding: 8px;
      font-size: 14px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    .select-field.short {
      width: 120px;
    }

    .input-field.short {
      width: 160px;
    }

    .date-wrapper {
      display: flex;
      gap: 10px;
    }

    .date-wrapper input {
      width: 45%;
    }

    .search-buttons {
      display: flex;
      justify-content: flex-start;
      gap: 10px;
      align-items: center;
      margin-top: 4px;
    }

    .search-button {
      background-color: #E17100;
      color: white;
      padding: 10px 20px;
      border: none;
      border-radius: 4px;
      font-weight: bold;
      font-size: 14px;
      cursor: pointer;
      height: 38px;
    }

    .search-button:hover {
      background-color: #973C00;
    }

    .btn-reset {
      background-color: #f0f0f0;
      color: #333;
      padding: 10px 20px;
      border: 1px solid #ccc;
      border-radius: 4px;
      font-weight: bold;
      font-size: 14px;
      height: 38px;
      text-decoration: none;
      display: flex;
      align-items: center;
      justify-content: center;
    }

    .btn-reset:hover {
      background-color: #ddd;
      color: #000;
    }

    @media (max-width: 768px) {
      .search-form {
        grid-template-columns: repeat(2, 1fr);
      }

      .search-buttons {
        flex-direction: column;
        align-items: stretch;
      }

      .search-button,
      .btn-reset {
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

    <!-- 검색 영역 -->
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
		
		<!-- 검색조건 + 검색어 같이 정렬 -->
		<div class="search-item search-row search-keyword-group" style="grid-column: span 2; display: flex; gap: 10px;">
		  <div style="flex: 0 0 140px;">
		    <label for="searchType">검색조건</label>
		    <select name="searchType" class="select-field short">
		      <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
		      <option value="content" ${search.searchType == 'content' ? 'selected' : ''}>내용</option>
		    </select>
		  </div>
		  <div style="flex: 1;">
		    <label for="searchWord">검색어</label>
		    <input type="text" name="searchWord" value="${search.searchWord}" class="input-field" placeholder="검색어를 입력하세요" />
		  </div>
		</div>

        <!-- 버튼 -->
		<div class="search-item search-buttons" style="grid-column: span 2; display: flex; justify-content: flex-end; align-items: end;">
		  <button type="submit" class="search-button">검색</button>
		  <a href="${pageContext.request.contextPath}/resident/complaint" class="btn-reset">초기화</a>
		</div>
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
                    <c:when test="${code.codeValue == '001'}"><span class="badge badge-orange">${code.codeName}</span></c:when>
                    <c:when test="${code.codeValue == '002'}"><span class="badge badge-green">${code.codeName}</span></c:when>
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
      <a class="btn-success" href="${pageContext.request.contextPath}/resident/complaint/form?bldgIdParam=${selectedBldgId}">글쓰기</a>
    </div>

  </main>
</div>

<script>
  function fnPaging(pageNo) {
    const form = document.getElementById('searchForm');
    form.page.value = pageNo;
    form.submit();
  }
</script>

<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
