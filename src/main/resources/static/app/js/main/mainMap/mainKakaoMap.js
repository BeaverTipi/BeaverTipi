/**
 * 
 * <pre>
 * << 개정이력 (Modifcation 	Information) >>
 *  수정일 			수정자			수정내용
 * ----------------  ----------------- -----------------------------
 * 2025. 7. 7.			윤현식			생성
 */
document.addEventListener("DOMContentLoaded", () => {
	kakao.maps.load(() => {
		const map = initMap();
		const clusterer = initClusterer(map);
		setupMapControls(map);

		setupModalCloseBtn();
		setupManualFilterTrigger(map, clusterer);
		setupCategoryButtonHandler(map, clusterer);
		setupKeywordSearch(map, clusterer);
		setupPopupOptionClick(map, clusterer);
		setupClusterClick(map, clusterer);

		// init 함수나 initMap 안에서 호출
		setupFacilityOptionListener(map, clusterer);

		initCategoryFromParam(map, clusterer);
		setupIdleEvent(map, clusterer);

		setupIdleEvent(map, clusterer);
		setDefaultFilterValues();
	});
});

