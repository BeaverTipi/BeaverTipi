<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<link rel="stylesheet" href="/app/css/building/managed/managedList.css">


<c:choose>
	<c:when test="${empty listingProductList}">
		<p>등록된 매물이 없습니다.</p>
	</c:when>
	<c:otherwise>
		<h2>내매물정보</h2>
		<div class="lead">
			<code>매물관리 > 내매물정보</code>
		</div>
		<div class="row">
			<c:forEach var="listing" items="${listingProductList}">
			<div class="col-md-6 col-xs-12">
				<div class="card mt-3">
					<div class="card-header border-primary">
						<div class="card-title">${listing.lstgNm}</div>
					</div>
					<div class="card-body">
						<div class="row">
							<div class="col-md-12">
								<table class="table table-bordered" widt="100%">
									<tr>
										<td rowspan="6">
<%-- 											<img src="${pageContext.request.contextPath }/images/no-image.jpg" alt="${listing.lstgNm}" class="building-img">									 --%>
											<img src="https://cdn.pixabay.com/photo/2016/12/03/17/38/building-1880261_960_720.jpg" alt="${listing.lstgNm}" class="building-img">									
										</td>
									</tr>
									<tr>
										<td><strong>건물 이름</strong></td>
										<td>${listing.lstgNm}</td>
									</tr>
									<tr>
										<td><strong>매물명</strong></td>
										<td>${listing.lstgNm}</td>
									</tr>
									<tr>
										<td><strong>매물 상태</strong></td>
										<td><c:choose>
												<c:when test="${listing.lstgProdStat == 1}">활성</c:when>
												<c:when test="${listing.lstgProdStat == 2}">비활성</c:when>
												<c:when test="${listing.lstgProdStat == 3}">숨김</c:when>
												<c:otherwise>미정</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td><strong>거래유형</strong></td>
										<td><c:choose>
												<c:when test="${listing.lstgTypeSale == 1}">전세</c:when>
												<c:when test="${listing.lstgTypeSale == 2}">월세</c:when>
												<c:when test="${listing.lstgTypeSale == 3}">매매</c:when>
												<c:otherwise>기타</c:otherwise>
											</c:choose>
										</td>
									</tr>
									<tr>
										<td><strong>설명 요약</strong></td>
										<td>
											<c:choose>
												<c:when test="${fn:length(listing.lstgDst) > 100}">
													<c:out value="${fn:substring(listing.lstgDst, 0, 100)}" />...
		                      					</c:when>
												<c:otherwise>
													<c:out value="${listing.lstgDst}" />
												</c:otherwise>
											</c:choose>
										</td>
									</tr>
								</table>
							</div>
						</div>
					</div>
					<div class="card-footer">
						<div class="button-box">
							<button class="btn btn-edit"
								onclick="location.href='/building/product/detail?lstgId=${listing.lstgId}'">상세보기</button>
							<button class="btn btn-edit"
								onclick="location.href='/building/product/update?lstgId=${listing.lstgId}'">수정</button>
							<button class="btn btn-delete"
								 onclick="f_delete()">삭제</button>
						</div>
					</div>
				</div>
			</div>
			</c:forEach>
		</div>
	</c:otherwise>
</c:choose>
<script type="text/javascript">
function f_delete(){
	// onclick="location.href='/building/product/delete?lstgId=${listing.lstgId}'"
	if(confirm("정말로 삭제하시겠습니까?")){
		location.href='/building/product/delete?lstgId=${listing.lstgId}'
	}
}
</script>