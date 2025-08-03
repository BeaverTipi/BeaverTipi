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
          <form:form modelAttribute="searchBuildingForm"
            action="${pageContext.request.contextPath}/building/product/tabList"
            method="get" class="search-section">
            <input type="hidden" name="page" value="${buildingPagingVO.currentPageNo}" id="currentPageNoInput" />


            <div class="search-grid-container">
              <div class="search-grid-left">
                <div class="search-grid-row">
                  <div class="search-item">
                    <label>건물명</label>
                    <form:input path="searchBuildingName" cssClass="form-control" placeholder="건물명 입력" />
                  </div>

                  <div class="search-item">
                    <label>기본 주소</label>
                    <form:input path="searchBuildingAddress" cssClass="form-control" placeholder="주소 입력"/>
                    </div>
                 
                  <div class="search-item  full-width">
                    <label>등록 기간</label>
                    <div class="d-flex">
                      <form:input type="date" path="searchBuildingRegDateFrom" cssClass="form-control mr-1" />
                      <span class="date-separator">~</span>
                      <form:input type="date" path="searchBuildingRegDateTo" cssClass="form-control" />
                    </div>
                  </div>
                 <input type="hidden" name="activeTab" id="activeTabInput" value="1" />
                </div>
              </div>
            <div class="search-grid-right">
              <div class="button-area">
                <button type="submit" class="btn-search">검색</button>
                <button type="reset" class="btn-reset" id="resetBuildingBtn">초기화</button>
              </div>
            </div>
            </div>
          </form:form>

          <div class="table-responsive">
            <table class="table table-bordered table-hover text-center">
              <thead class="thead-light">
                <tr>
                  <th>순번</th>
                  <th>건물명</th>
                  <th>기본 주소</th>
                  <th>층수</th>
                  <th>호실수</th>
                  <th>등록 일자</th>
                </tr>
              </thead>
             <tbody id="buildingTableBody">
			  <c:choose>
			    <c:when test="${empty buildingList}">
			      <tr><td colspan="6">등록된 건물이 없습니다.</td></tr>
			    </c:when>
			    <c:otherwise>
			      <c:forEach var="building" items="${buildingList}" varStatus="status">
			        <tr>
			          <td>${status.index + 1}</td>
			          <td class="text-start">
			            <!-- 건물명 클릭시 상세 펼침 (페이지 이동X) -->
			            <a href="#" 
			               class="building-detail-toggle font-weight-bold text-primary font-weight-bold"
			               data-bldg-id="${building.bldgId}">
			              ${building.bldgNm}
			            </a>
			            &nbsp;
			            <a href="#"
			               class="building-infom-link"
			               data-bldg-id="${building.bldgId}"
			               data-address="${building.bldgAddr}${building.bldgDtlAddr}">
			          </a>
			          </td>
			          <td>${building.bldgAddr} ${building.bldgDtlAddr}</td>
			          
			          <td>${building.bldgFlrCnt}</td>
			          <td>${building.bldgUnitCnt}</td>
			          <td>${building.bldgCmpltnDt}</td>
			        </tr>
			      </c:forEach>
			    </c:otherwise>
			  </c:choose>
			</tbody>

            </table>
            <div class="pagination-wrapper mt-4">
            <nav aria-label="Page navigation">
            <ul class="pagination justify-content-center">${buildingPagingHTML}</ul>
                  </nav>
          </div>
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
            action="${pageContext.request.contextPath}/building/product/tabList"
            method="get" class="search-section">
            <div class="search-grid-container">
              <div class="search-grid-left">
                <div class="search-grid-row">
                  <div class="search-item">
                    <label>건물명</label>
                    <form:input path="searchListingName" cssClass="form-control" placeholder="건물명 입력" />
                  </div>

                  <div class="search-item">
                    <label>상태</label>
                    <form:select path="searchStatus" cssClass="form-control">
                      <c:forEach var="code" items="${statusCodeList}">
                        <form:option value="${code.codeValue}">${code.codeName}</form:option>
                      </c:forEach>
                    </form:select>
                    </div>
                  <div class="search-item ">
                    <label>거래 유형</label>
                    <form:select path="searchType" id="searchTypeSelect" cssClass="form-control">
                      <c:forEach var="typeCode" items="${typeSaleCodeList}">
                        <form:option value="${typeCode.codeValue}">${typeCode.codeName}</form:option>
                      </c:forEach>
                    </form:select>
                  </div>
                  <div class="search-item  full-width">
                    <label>등록 기간</label>
                    <div class="d-flex">
                      <form:input type="date" path="searchRegDateFrom" cssClass="form-control mr-1" />
                      <span class="date-separator">~</span>
                      <form:input type="date" path="searchRegDateTo" cssClass="form-control" />
                    </div>
                  </div>
                 <input type="hidden" name="activeTab" id="activeTabInput" value="2" />
                  <input type="hidden" name="page" value="${pagingVO.currentPageNo}" />
                </div>
                 
              </div>
            <div class="search-grid-right">
              <div class="button-area">
                <button type="submit" class="btn-search">검색</button>
                <button type="reset" class="btn-reset" id="resetBtn">초기화</button>
              </div>
            </div>
            </div>
          </form:form>
          <div class="table-responsive">
            <table class="table table-bordered table-hover text-center">
              <thead class="thead-light">
                <tr>
                  <th>순번</th>
                  <th>건물명</th>
                  <th>중개사</th>
                  <th>상태</th>
                  <th>거래 유형</th>
                  <th>등록 일자</th>
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
                        <td class="text-start">
                          <a href="#" class="building-name-link font-weight-bold text-primary" data-lstg-id="${listing.lstgId}">
								  ${listing.lstgNm}
						</a>

                        </td>
                        <td>
                        ${listing.mbrNm }
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
                        <td>${formattedDateList[status.index]}</td>
                       
                        
                      </tr>
                    </c:forEach>
                  </c:otherwise>
                </c:choose>
              </tbody>
            </table>
            <div id="listingQuickView" style="display:none;"></div>
            
          </div>
          <div class="pagination-wrapper mt-4">
            <nav aria-label="Page navigation">
              <ul class="pagination justify-content-center">${pagingHTML}</ul>
            </nav>
          </div>
                    <div style="text-align:right; margin-top:20px;">
            <a href="/building/product/add" class="btn btn-success">매물 등록</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</div>

<script src="${pageContext.request.contextPath}/app/js/building/product/listing-management.js"></script>


</body>
</html>
