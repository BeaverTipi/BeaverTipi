<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 7.     		 윤현식            생성
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<link rel="styleSheet"
	href="${pageContext.request.contextPath }/app/css/main/mainMap/kakaoMap.css">


<div id="wrap">
	<!-- 헤더 등 상단 영역이 있다면 여기 포함 -->

		<!-- 1. 사이드바 -->
		<nav class="sidebar">
			<%@ include file="/WEB-INF/fragments/mainPageMapSidebar.jsp"%>
		</nav>
		
		<div class="map-container">
			<div class="filter-bar">
				<div class="filter-group">
				<button class="filter-toggle-btn" onclick="toggleFilterPopup('saleType', this)">거래유형</button>
					<div class="filter-popup" id="popup-saleType">
						  <div class="popup-header">
						    거래유형
						    <button class="close-btn" onclick="closeFilterPopup('saleType')">×</button>
						  </div>
						  <div class="popup-body">
						    <button class="popup-option" data-type="saleType" data-value="">전체</button>
						    <button class="popup-option" data-type="saleType" data-value="1">월세</button>
						    <button class="popup-option" data-type="saleType" data-value="2">전세</button>
						    <button class="popup-option" data-type="saleType" data-value="3">매매</button>
						  </div>
						</div>
					<input type="hidden" id="saleTypeFilter" />
				<button class="filter-toggle-btn" onclick="toggleFilterPopup('listingType', this)">매물유형</button>
					<div class="filter-popup" id="popup-listingType">
						  <div class="popup-header">
						    매물유형
						    <button class="close-btn" onclick="closeFilterPopup('listingType')">×</button>
						  </div>
						  <div class="popup-body">
						    <button class="popup-option" data-type="listingType" data-value="">전체</button>
						    <button class="popup-option" data-type="listingType" data-value="1">아파트</button>
						    <button class="popup-option" data-type="listingType" data-value="2">빌라</button>
						    <button class="popup-option" data-type="listingType" data-value="3">오피스텔</button>
						    <button class="popup-option" data-type="listingType" data-value="4">단독주택</button>
						    <button class="popup-option" data-type="listingType" data-value="5">상가주택</button>
						    <button class="popup-option" data-type="listingType" data-value="6">상가</button>
						    <button class="popup-option" data-type="listingType" data-value="7">오피스빌딩/사무실</button>
						    <button class="popup-option" data-type="listingType" data-value="8">기타</button>
						  </div>
						</div>
					<input type="hidden" id="listingTypeFilter" />
				<button class="filter-toggle-btn" onclick="toggleFilterPopup('saleDetailType', this)">매물상세유형</button>
					<div class="filter-popup" id="popup-saleDetailType">
						  <div class="popup-header">
						    매물상세유형
						    <button class="close-btn" onclick="closeFilterPopup('saleDetailType')">×</button>
						  </div>
						  <div class="popup-body">
						    <button class="popup-option" data-type="saleDetailType" data-value="">전체</button>
						    <button class="popup-option" data-type="saleDetailType" data-value="1">원룸/투룸/다세대</button>
						    <button class="popup-option" data-type="saleDetailType" data-value="2">단독주택</button>
						    <button class="popup-option" data-type="saleDetailType" data-value="3">다가구주택</button>
						    <button class="popup-option" data-type="saleDetailType" data-value="4">상가주택</button>
						  </div>
						</div>
					<input type="hidden" id="saleDetailTypeFilter" />
				<button class="filter-toggle-btn" onclick="toggleFilterPopup('area', this)">방크기</button>
					<div class="filter-popup" id="popup-area">
						  <div class="popup-header">
						    방크기
						    <button class="close-btn" onclick="closeFilterPopup('area')">×</button>
						  </div>
						  <div class="popup-body">
						    <button class="popup-option" data-type="area" data-value="">전체</button>
						    <button class="popup-option" data-type="area" data-value="1">10평 이하</button>
						    <button class="popup-option" data-type="area" data-value="2">10~20평</button>
						    <button class="popup-option" data-type="area" data-value="3">20~30평</button>
						    <button class="popup-option" data-type="area" data-value="4">30평 이상</button>
						  </div>
						</div>
					<input type="hidden" id="areaFilter" />
				<button class="filter-toggle-btn" onclick="toggleFilterPopup('addfilter', this)">추가옵션</button>
					<div class="filter-popup" id="popup-addfilter">
						  <div class="popup-header">
						    추가옵션
						    <button class="close-btn" onclick="closeFilterPopup('addfilter')">×</button>
						  </div>
						  <div class="popup-body">
						  	<div class="popup-section">
								<span class="popup-label">주차 가능</span>
								<label><input type="checkbox" id="parkingYn"> 가능</label>
							</div>
							<div class="popup-section">
								<span class="popup-label">층수</span>
								<input type="number" id="minFloor" placeholder="최소" min="1" style="width: 70px;">
								~
								<input type="number" id="maxFloor" placeholder="최대" min="1" style="width: 70px;">
							</div>
						    <div class="popup-section">
								<span class="popup-label">시설 옵션</span><br>
								<label><input type="checkbox" class="facilityOpt" value="1"> 엘리베이터</label>
								<label><input type="checkbox" class="facilityOpt" value="2"> CCTV</label>
								<label><input type="checkbox" class="facilityOpt" value="3"> 가스레인지</label>
							</div>
						    <div style="margin-top: 10px; text-align: right;">
								<button id="resetFilters" class="btn btn-sm btn-secondary">초기화</button>
							</div>
						</div>
					</div>
					<input type="text" id="keywordFilter" placeholder="키워드 검색" /> 
					<button id="keywordSearchBtn">검색하기</button>
				</div>
			</div>
			
			<!-- 2. 리스트 -->
			<div class="main-section">
				<div class="list-panel">
					<div id="listing-list" class="listing-list"></div>
					<div id="pagination" class="pagination-container"></div>
				</div>
	
				<!-- 3. 상세 모달 -->
				<div id="side-detail-modal" class="side-modal">
					<div class="side-modal-header">
						<button id="sideModalClose" class="modal-close-btn">×</button>
					</div>
					<div id="sideModalBody" class="side-modal-body"></div>
				</div>
		
				<!-- 4. 지도 -->
				<div class="map-area">
					<div id="map"></div>
				</div>
			</div>
	</div>
</div>



<script
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=83437aacc12e4951bd2a5acb4a512ff0&libraries=services,clusterer,drawing"></script>
<%-- <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${jsApiKey}&libraries=services,clusterer,drawing"></script> --%>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/mapCore.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/filterUtils.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/markerRenderer.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/listRenderer.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/eventBinder.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/mainKakaoMap.js"></script>