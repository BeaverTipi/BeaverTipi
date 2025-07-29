<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"
	language="java"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>내 매물 관리</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<link href="/app/css/building/product/listing-manage.css"
	rel="stylesheet">
</head>
<body>

	<div class="container mt-4">
		<div class="card">
			<div class="card-body">
				<h2 class="mb-4">내 매물 관리</h2>

				<form:form modelAttribute="searchForm"
					action="${pageContext.request.contextPath}/building/product/list"
					method="get" class="search-section">
					<input type="hidden" name="page" value="${pagingVO.currentPageNo}"
						id="currentPageNoInput" />

					<!-- ✅ 전체를 하나의 grid로 구성 -->
					<div class="search-grid-container">

						<!-- ✅ 왼쪽 영역: 검색 조건 -->
						<div class="search-grid-left">

							<!-- 1. 건물명 / 호수 / 상태 / 등록일 -->
							<div class="search-grid-row">
								<div class="search-item">
									<label>건물명</label>
									<form:input path="searchBuildingName" cssClass="form-control"
										placeholder="건물명 입력" />
								</div>
								<div class="search-item">
									<label>호수</label>
									<form:input path="searchRoomNum" cssClass="form-control"
										placeholder="예: 101호" />
								</div>
								<div class="search-item">
									<label>상태</label>
									<form:select path="searchStatus" cssClass="form-control">
										<c:forEach var="code" items="${statusCodeList}">
											<form:option value="${code.codeValue}">${code.codeName}</form:option>
										</c:forEach>
									</form:select>
								</div>
								<div class="search-item  full-width">
									<label>등록일</label>
									<div class="d-flex">
										<form:input type="date" path="searchRegDateFrom"
											cssClass="form-control mr-1" />
										<span class="date-separator">~</span>
										<form:input type="date" path="searchRegDateTo"
											cssClass="form-control" />
									</div>
								</div>
							</div>

							<!-- 2. 거래유형 -->
							<div class="search-grid-row">
								<div class="search-item ">
									<label>거래유형</label>
									<form:select path="searchType" id="searchTypeSelect"
										cssClass="form-control">
										<c:forEach var="typeCode" items="${typeSaleCodeList}">
											<form:option value="${typeCode.codeValue}">${typeCode.codeName}</form:option>
										</c:forEach>
									</form:select>
								</div>
								<!-- 조건부 필드 -->
								<div id="conditionalFields" style="display: none;">
									<!-- 전세금 -->
									<div class="search-item full-width row-deposit">
										<label id="depositLabel">전세금</label>
										<div class="input-range">
											<form:input path="searchDepositMin" cssClass="form-control"
												placeholder="최소" />
											<span class="date-separator">~</span>
											<form:input path="searchDepositMax" cssClass="form-control"
												placeholder="최대" />
										</div>
									</div>

									<!-- 월세 -->
									<div class="search-item full-width row-monthly">
										<label>월세</label>
										<div class="input-range">
											<form:input path="searchMonthlyMin" cssClass="form-control"
												placeholder="최소" />
											<span class="date-separator">~</span>
											<form:input path="searchMonthlyMax" cssClass="form-control"
												placeholder="최대" />
										</div>
									</div>


									<!-- 매매가 -->
									<div class="search-item full-width row-sale"
										style="display: none;">
										<label>매매가</label>
										<div class="d-flex">
											<form:input path="searchSaleMin" cssClass="form-control mr-1"
												placeholder="최소" />
											<span class="date-separator">~</span>
											<form:input path="searchSaleMax" cssClass="form-control"
												placeholder="최대" />
										</div>
									</div>
								</div>
							</div>
						</div>



					</div>
						<!-- ✅ 오른쪽 버튼 영역 -->
						<div class="search-grid-right">
							<div class="button-area">
								<button type="submit" class="btn-search">검색</button>
								<button type="reset" class="btn-reset" id="resetBtn">초기화</button>
							</div>
						</div>
					<!-- search-grid-container 끝 -->
				</form:form>








				<div class="table-responsive">
					<table class="table table-bordered table-hover text-center">
						<thead class="thead-light">
							<tr>
								<th>순번</th>
								<th>건물명</th>
								<th>상태</th>
								<th>거래유형</th>
								<th>전세금</th>
								<th>보증금</th>
								<th>월세</th>
								<th>매매가</th>
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
									<c:forEach var="listing" items="${listingProductList}"
										varStatus="status">
										<tr class="listing-row" data-address="${listing.lstgAdd}">
											<td>${status.index + 1}</td>
											<td><a href="#"
												class="building-name-link font-weight-bold text-primary">${listing.lstgNm}</a></td>
											<td><c:choose>
													<c:when test="${listing.lstgProdStat == 1}">활성</c:when>
													<c:when test="${listing.lstgProdStat == 2}">비활성</c:when>
													<c:when test="${listing.lstgProdStat == 3}">숨기기</c:when>
													<c:otherwise>미정</c:otherwise>
												</c:choose></td>
											<td><c:choose>
													<c:when test="${listing.lstgTypeSale == 1}">전세</c:when>
													<c:when test="${listing.lstgTypeSale == 2}">월세</c:when>
													<c:when test="${listing.lstgTypeSale == 3}">매매</c:when>
													<c:otherwise>기타</c:otherwise>
												</c:choose></td>
											<td>${empty listing.lstgLease ? '-' : listing.lstgLease}</td>
											<td>${empty listing.lstgLeaseM ? '-' : listing.lstgLeaseM}</td>
											<td>${empty listing.lstgLeaseAmt ? '-' : listing.lstgLeaseAmt}</td>
											<td>-</td>
										</tr>
									</c:forEach>
								</c:otherwise>
							</c:choose>
						</tbody>
					</table>
				</div>

				<div class="pagination-wrapper mt-4">
					<nav aria-label="Page navigation">
						<ul class="pagination justify-content-center">${pagingHTML}
						</ul>
					</nav>
				</div>
			</div>
		</div>
	</div>
	<script
		src="${pageContext.request.contextPath}/app/js/building/product/listing-management.js"></script>



</body>
</html>
