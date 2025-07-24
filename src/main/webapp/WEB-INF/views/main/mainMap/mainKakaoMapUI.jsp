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
<link rel="styleSheet" href="${pageContext.request.contextPath }/app/css/main/mainMap/kakaoMap.css">
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<title>지도</title>
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
						    <c:forEach var="item" items="${saleTypeList}">
					  <c:if test="${not empty item.codeValue && item.codeName ne '전체'}">
					    <button class="popup-option"
					            data-type="saleType"
					            data-value="${item.codeValue}">
					      ${item.codeName}
					    </button>
					  </c:if>
					</c:forEach>
						  </div>
						</div>
					<input type="hidden" id="saleTypeFilter" />
					<button id="saleDetailTypeBtn" class="filter-toggle-btn" onclick="toggleFilterPopup('saleDetailType', this)">매물상세유형</button>
						<div class="filter-popup" id="popup-saleDetailType">
							  <div class="popup-header">
							    매물상세유형
							    <button class="close-btn" onclick="closeFilterPopup('saleDetailType')">×</button>
							  </div>
							  <div class="popup-body">
							    <button class="popup-option" data-type="saleDetailType" data-value="">전체</button>
							    <c:forEach var="item" items="${saleDetailTypeList}">
								 <c:if test="${(not empty item.codeValue) and (item.codeName ne '전체')}">
									  <button class="popup-option"
									          data-type="saleDetailType"
									          data-value="${item.codeValue}"
									          data-parent="${item.parentCodeValue}">
									    ${item.codeName}
								  </button>
								  </c:if>
								</c:forEach>
							  </div>
						</div>
					<input type="hidden" id="saleDetailTypeFilter" />
					
				<!-- 여긴 하드 코딩(방 크기)  -->
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
						  	<div class="popup-section parking-floor-group">
								<label><input type="checkbox" id="parkingYn"> 주차 가능</label>
								<span style="margin-left: 20px;">층수</span>
								<input type="number" id="minFloor" placeholder="최소" min="1" style="width: 60px;"> ~
								<input type="number" id="maxFloor" placeholder="최대" min="1" style="width: 60px;">
							</div>
						    <div class="popup-section">
								<!-- <span class="popup-label">시설 옵션</span> -->
								<!-- 가구류 -->
								<div class="option-group">
									<strong>가구류</strong><br>
									<label><input type="checkbox" class="facilityOpt" value="FAC01000001">침대</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC01000002">책상</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC01000003">옷장</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC01000004">식탁</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC01000005">소파</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC01000006">신발장</label>
								</div>
							
								<!-- 가전류 -->
								<div class="option-group">
									<strong>가전류</strong><br>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000007"> 냉장고</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000008"> 세탁기</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000009"> 건조기</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000010"> 식기세척기</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000011"> 가스레인지</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000012"> 인덕션</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000013"> 전자레인지</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000014"> 오븐</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC02000015"> TV</label>
								</div>
							
								<!-- 보안 및 건물시설 -->
								<div class="option-group">
									<strong>보안 및 건물시설</strong><br>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000016"> 붙박이장</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000017"> CCTV</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000018"> 비디오폰</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000019"> 경비원</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000020"> 인터폰</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000021"> 카드키</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000022"> 사설경비</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000023"> 현관보안</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000024"> 방범창</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000025"> 화재경보기</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000026"> 테라스</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000027"> 베란다</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000028"> 마당</label>
									<label><input type="checkbox" class="facilityOpt" value="FAC03000029"> 무인택배함</label>
								</div>
							</div>
						    <div style="margin-top: 10px; text-align: right;">
								<button id="resetFilters" class="btn btn-sm btn-secondary">초기화</button>
							</div>
						</div>
					</div>
					<input type="text" id="keywordFilter" placeholder="매물명 검색" /> 
					<button id="keywordSearchBtn">검색하기</button>
				</div>
			</div>
			
			<!-- 2. 리스트 -->
			<div class="main-section">
				<div class="list-panel">
					<div id="listing-list" class="listing-list"></div>
					<div class="pagination-container" id="pagination"></div>
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
</div>



 <<!-- script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=83437aacc12e4951bd2a5acb4a512ff0&libraries=services,clusterer,drawing"></script> -->
<script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${jsApiKey}&autoload=false&libraries=services,clusterer,drawing"></script>

<script>
	window.loggedInUserId = "${loginMember != null ? loginMember.mbrCd : ''}";
</script>

<script src="${pageContext.request.contextPath }/app/js/main/mainMap/mapCore.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/filterUtils.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/markerRenderer.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/listRenderer.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/eventBinder.js"></script>
<script src="${pageContext.request.contextPath }/app/js/main/mainMap/mainKakaoMap.js"></script>

<script src="${pageContext.request.contextPath }/app/js/main/chat/chatCreate.js"></script>