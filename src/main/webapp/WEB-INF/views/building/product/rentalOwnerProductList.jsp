<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" language="java"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>
<%@ page isELIgnored="false" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>내 건물/매물 관리</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link href="/app/css/building/product/listing-manage.css" rel="stylesheet">
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8fbfe;
      margin: 0;
      padding: 30px;
    }
    .tab-wrap {
      margin: 0 auto 20px auto;
      max-width: 900px;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      padding: 0 0 30px 0;
    }
    .tabs {
      display: flex;
      border-bottom: 2px solid #e0e0e0;
      margin-bottom: 24px;
    }
    .tab {
      flex: 1;
      padding: 18px 0 12px 0;
      text-align: center;
      font-size: 17px;
      font-weight: 600;
      color: #bbb;
      background: transparent;
      border: none;
      outline: none;
      cursor: pointer;
      transition: color 0.18s;
      border-bottom: 2px solid transparent;
    }
    .tab.active {
      color: #00aaff;
      border-bottom: 2.5px solid #00aaff;
      background: #f8fbfe;
    }
    h2 { margin-bottom: 20px; font-size: 24px; color: #333;}
    .building-link {
      color: #007bff;
      text-decoration: underline;
      font-weight: bold;
      cursor: pointer;
    }
    .building-link:hover {
      color: #0056b3;
      text-decoration: underline;
    }
    .btn-success {
      background-color: #00aaff; color: #fff; border: none; padding: 8px 18px; border-radius: 5px; font-weight: 600; cursor: pointer;
    }
    .btn-success:hover { background-color: #008fcc;}
    .empty-message {
      text-align: center;
      padding: 30px;
      color: #888;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 5px;
    }
  </style>
</head>
<body>
<div class="tab-wrap">

  <!-- 탭 헤더 -->
  <div class="tabs">
    <button class="tab active" id="tab1Btn" onclick="showTab(1)">내 건물 관리</button>
    <button class="tab" id="tab2Btn" onclick="showTab(2)">내 매물 관리</button>
  </div>

  <!-- 탭1: 내 건물 관리 (검색폼 추가) -->
  <div id="tab1Panel">
    <div class="container mt-4">
      <div class="card">
        <div class="card-body">
          <h2 class="mb-4">내 건물 관리</h2>

          <!-- 내 매물관리와 동일한 검색 폼 -->
          <form:form modelAttribute="searchForm"
            action="${pageContext.request.contextPath}/building/managed/list"
            method="get" class="search-section">
            <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput" />

            <div class="search-grid-container">
              <div class="search-grid-left">
                <div class="search-grid-row">
                  <div class="search-item">
                    <label>건물명</label>
                    <form:input path="searchBuildingName" cssClass="form-control" placeholder="건물명 입력" />
                  </div>
                  <div class="search-item">
                    <label>호수</label>
                    <form:input path="searchRoomNum" cssClass="form-control" placeholder="예: 101호" />
                  </div>
                  <div class="search-item">
                    <label>상태</label>
                    <form:select path="searchStatus" cssClass="form-control">
                      <c:forEach var="code" items="${statusCodeList}">
                        <form:option value="${code.codeValue}">${code.codeName}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>
                 
                </div>
                <div class="search-grid-row">
                  <div class="search-item ">
                    <label>유형</label>
                    <form:select path="searchType" id="searchTypeSelect" cssClass="form-control">
                      <c:forEach var="typeCode" items="${typeSaleCodeList}">
                        <form:option value="${typeCode.codeValue}">${typeCode.codeName}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>
                  <div class="search-item  full-width">
                    <label>등록일</label>
                    <div class="d-flex">
                      <form:input type="date" path="searchRegDateFrom" cssClass="form-control mr-1" />
                      <span class="date-separator">~</span>
                      <form:input type="date" path="searchRegDateTo" cssClass="form-control" />
                    </div>
                  </div>
                </div>
                 
              </div>
            </div>
            <div class="search-grid-right">
              <div class="button-area">
                <button type="submit" class="btn-search">검색</button>
                <button type="reset" class="btn-reset" id="resetBtn">초기화</button>
              </div>
            </div>
          </form:form>

          <div class="table-responsive">
            <table class="table table-bordered table-hover text-center">
              <thead class="thead-light">
                <tr>
                  <th>순번</th>
                  <th>건물명</th>
                  <th>주소</th>
                  <th>유형</th>
                  <th>층수</th>
                  <th>호실수</th>
                </tr>
              </thead>
              <tbody>
                <c:choose>
                  <c:when test="${empty buildingList}">
                    <tr><td colspan="6">등록된 건물이 없습니다.</td></tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="building" items="${buildingList}" varStatus="status">
                      <tr>
                        <td>${status.index + 1}</td>
                        <td>
                          <a href="/building/managed/detail?bldgId=${building.bldgId}" class="building-link">
                            ${building.bldgNm}
                          </a>
                        </td>
                        <td>${building.bldgAddr} ${building.bldgDtlAddr}</td>
                        <td>${building.bldgTypeCode}</td>
                        <td>${building.bldgFlrCnt}</td>
                        <td>${building.bldgUnitCnt}</td>
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>
          </div>
          <div style="text-align:right; margin-top:20px;">
            <a href="/building/managed/add" class="btn btn-success">새 건물등록</a>
          </div>
        </div>
      </div>
    </div>
  </div>

  <!-- 탭2: 내 매물 관리 (기존 그대로) -->
  <div id="tab2Panel" style="display: none;">
    <div class="container mt-4">
      <div class="card">
        <div class="card-body">
          <h2 class="mb-4">내 매물 관리</h2>
          <form:form modelAttribute="searchForm"
            action="${pageContext.request.contextPath}/building/product/list"
            method="get" class="search-section">
            <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput" />
            <div class="search-grid-container">
              <div class="search-grid-left">
                <div class="search-grid-row">
                  <div class="search-item">
                    <label>건물명</label>
                    <form:input path="searchBuildingName" cssClass="form-control" placeholder="건물명 입력" />
                  </div>
                  <div class="search-item">
                    <label>호수</label>
                    <form:input path="searchRoomNum" cssClass="form-control" placeholder="예: 101호" />
                  </div>
                  <div class="search-item">
                    <label>상태</label>
                    <form:select path="searchStatus" cssClass="form-control">
                      <c:forEach var="code" items="${statusCodeList}">
                        <form:option value="${code.codeValue}">${code.codeName}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>

                </div>
                <div class="search-grid-row">
                  <div class="search-item ">
                    <label>유형</label>
                    <form:select path="searchType" id="searchTypeSelect" cssClass="form-control">
                      <c:forEach var="typeCode" items="${typeSaleCodeList}">
                        <form:option value="${typeCode.codeValue}">${typeCode.codeName}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>
					<div class="search-item  full-width">
                    <label>등록일</label>
                    <div class="d-flex">
                      <form:input type="date" path="searchRegDateFrom" cssClass="form-control mr-1" />
                      <span class="date-separator">~</span>
                      <form:input type="date" path="searchRegDateTo" cssClass="form-control" />
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div class="search-grid-right">
              <div class="button-area">
                <button type="submit" class="btn-search">검색</button>
                <button type="reset" class="btn-reset" id="resetBtn">초기화</button>
              </div>
            </div>
          </form:form>
          <div class="table-responsive">
            <table class="table table-bordered table-hover text-center">
              <thead class="thead-light">
                <tr>
                  <th>순번</th>
                  <th>건물명</th>
                  <th>상태</th>
                  <th>거래유형</th>
                  
                  <th>등록일자</th>
                </tr>
              </thead>
              <tbody id="listingTableBody">
                <c:choose>
                  <c:when test="${empty listingProductList}">
                    <tr>
                      <td colspan="8">등록된 매물이 없습니다.</td>
                    </tr>
                  </c:when>
                  <c:otherwise>
                    <c:forEach var="listing" items="${listingProductList}" varStatus="status">
                      <tr class="listing-row" data-address="${listing.lstgAdd}">
                        <td>${status.index + 1}</td>
                        <td>
                          <a href="#" class="building-name-link font-weight-bold text-primary">${listing.lstgNm}</a>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${listing.lstgProdStat == 1}">활성</c:when>
                            <c:when test="${listing.lstgProdStat == 2}">비활성</c:when>
                            <c:when test="${listing.lstgProdStat == 3}">숨기기</c:when>
                            <c:otherwise>미정</c:otherwise>
                          </c:choose>
                        </td>
                        <td>
                          <c:choose>
                            <c:when test="${listing.lstgTypeSale == 1}">전세</c:when>
                            <c:when test="${listing.lstgTypeSale == 2}">월세</c:when>
                            <c:when test="${listing.lstgTypeSale == 3}">매매</c:when>
                            <c:otherwise>기타</c:otherwise>
                          </c:choose>
                        </td>
                        <td>${empty listing.lstgRegDate ? '-' : listing.lstgRegDate}</td>
                       
                        
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>
          </div>
          <div class="pagination-wrapper mt-4">
            <nav aria-label="Page navigation">
              <ul class="pagination justify-content-center">${pagingHTML}</ul>
            </nav>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script>
  // 탭 전환
  function showTab(tabNum) {
    document.getElementById("tab1Panel").style.display = tabNum === 1 ? "" : "none";
    document.getElementById("tab2Panel").style.display = tabNum === 2 ? "" : "none";
    document.getElementById("tab1Btn").classList.toggle("active", tabNum === 1);
    document.getElementById("tab2Btn").classList.toggle("active", tabNum === 2);
  }
  window.onload = function() {
    showTab(1);
  }
</script>
<script src="${pageContext.request.contextPath}/app/js/building/product/listing-management.js"></script>
</body>
</html>
