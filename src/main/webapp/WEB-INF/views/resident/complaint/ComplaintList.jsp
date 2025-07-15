<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>민원 목록</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_resident.css" />
	<style>
  .btn-view {
    background-color: var(--main-color-orange, #ff7f00);
    color: white;
    padding: 4px 10px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  .badge {
  padding: 4px 8px;
  border-radius: 6px;
  font-size: 0.85rem;
  font-weight: bold;
  color: white;
}

.badge-orange { background-color: #FF9800; }
.badge-green  { background-color: #4CAF50; }
.badge-gray   { background-color: #9E9E9E; }

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
        <input type="hidden" name="search.brdCode" value="M0001" />
        <input type="hidden" name="bldgIdParam" id="bldgIdParamInput" value="${selectedBldgId}" />

        <div class="search-conditions">
          <!-- 건물 -->
          <div class="search-item row-type">
            <label for="search.bldgId" class="select-field">건물</label>
            <select name="bldgIdParam" class="select-field" id="bldgSelect">
              <c:forEach var="unit" items="${unitList}">
                <option value="${unit.bldgId}" ${unit.bldgId == selectedBldgId ? 'selected' : ''}>
                  ${unit.building.bldgNm}
                </option>
              </c:forEach>
            </select>
          </div>

          <!-- 공개 여부 -->
          <div class="search-item row-type">
            <label for="search.openYn">공개여부</label>
            <select name="search.openYn" class="select-field">
              <option value="">전체</option>
              <c:forEach var="code" items="${openYnList}">
                <option value="${code.code}" ${code.code == search.openYn ? 'selected' : ''}>${code.name}</option>
              </c:forEach>
            </select>
          </div>
			
			<div class="search-item row-type">
			  <label for="search.searchStartDate">일자</label>
			  <div class="date-wrapper">
			    <input type="date" name="search.searchStartDate" class="input-field" value="${search.searchStartDate}">
			    ~
			    <input type="date" name="search.searchEndDate" class="input-field" value="${search.searchEndDate}">
			  </div>
			</div>
			
          <!-- 처리상태 -->
          <div class="search-item row-type">
            <label for="search.reqStatus">처리상태</label>
            <select name="search.reqStatus" class="select-field">
              <option value="">전체</option>
              <c:forEach var="code" items="${reqStatusList}">
                <option value="${code.code}" ${code.code == search.reqStatus ? 'selected' : ''}>${code.name}</option>
              </c:forEach>
            </select>
          </div>
			
          <!-- 검색 조건 -->
          <div class="search-item row-type select-cond">
            <label for="search.searchType">조건</label>
            <select name="search.searchType" class="select-type">
              <option value="title" ${search.searchType == 'title' ? 'selected' : ''}>제목</option>
              <option value="content" ${search.searchType == 'content' ? 'selected' : ''}>내용</option>
            </select>
          </div>

          <!-- 검색어 -->
          <div class="search-item-group">
			  <div class="search-item double-width">
			    <label for="search.searchWord">검색어</label>
			    <input type="text" name="search.searchWord" value="${search.searchWord}" class="input-field" placeholder="검색어 입력" />
			  </div>
			
			  <button type="submit" class="search-button">검색</button>
			</div>
      </form>
    </div>

    <!-- 📋 민원 목록 테이블 -->
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
				    <c:when test="${vo.ynfg == 'Y'}"><span class="badge badge-blue">공개</span></c:when>
				    <c:otherwise><span class="badge badge-dark">비공개</span></c:otherwise>
				  </c:choose>
				</td>
		      <td>
				  <c:choose>
				    <c:when test="${vo.reqStatus == '001'}">
				      <span class="badge badge-orange">처리중</span>
				    </c:when>
				    <c:when test="${vo.reqStatus == '002'}">
				      <span class="badge badge-green">처리완료</span>
				    </c:when>
				    <c:otherwise>
				      <span class="badge badge-gray">접수</span>
				    </c:otherwise>
				  </c:choose>
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
          <tr><td colspan="5" class="no-data-center">검색 결과가 없습니다.</td></tr>
        </c:if>
      </tbody>
    </table>

    <!-- 📄 페이징 -->
    <div class="pagination-wrapper">
      <c:out value="${pagingHtml}" escapeXml="false"/>
    </div>

    <!-- ✏️ 등록 버튼 -->
    <div class="write-buttons">
      <a class="btn-success" href="${pageContext.request.contextPath}/resident/complaint/form?bldgIdParam=${selectedBldgId}">등록</a>
    </div>

  </main>
</div>

<!-- ✅ 페이징 JS -->
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
