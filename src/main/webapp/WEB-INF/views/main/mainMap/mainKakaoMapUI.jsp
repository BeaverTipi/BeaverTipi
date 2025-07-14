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

	<div class="map-container">
		<div class="filter-bar">
			<div class="filter-group">
				<select id="saleTypeFilter">
					<option value="">거래유형</option>
					<option value="1">월세</option>
					<option value="2">전세</option>
					<option value="3">매매</option>
				</select> <select id="roomSizeFilter">
					<option value="">방 크기</option>
					<option value="1">1룸</option>
					<option value="2">2룸</option>
					<option value="3">3룸 이상</option>
				</select> <select id="approvalDateFilter">
					<option value="">사용승인일</option>
					<option value="2020">2020년 이후</option>
					<option value="2010">2010년 이후</option>
				</select> <input type="text" id="keywordFilter" placeholder="키워드 검색" />
			</div>
		</div>

		<!-- 1. 사이드바 -->
		<nav class="sidebar">
			<%@ include file="/WEB-INF/fragments/mainPageMapSidebar.jsp"%>
		</nav>

		<!-- 2. 리스트 -->
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



<script
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=83437aacc12e4951bd2a5acb4a512ff0&libraries=services,clusterer,drawing"></script>
<%-- <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${jsApiKey}&libraries=services,clusterer,drawing"></script> --%>
<script
	src="${pageContext.request.contextPath }/app/js/main/mainMap/mainKakaoMap.js"></script>