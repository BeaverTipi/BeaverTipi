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
		setupAutoFilterTrigger(map, clusterer);
		setupCategoryButtonHandler(map, clusterer);
		setupIdleEvent(map, clusterer);
		setupFilterOptionClick(map, clusterer);
		initCategoryFromParam(map, clusterer);
	});
});

