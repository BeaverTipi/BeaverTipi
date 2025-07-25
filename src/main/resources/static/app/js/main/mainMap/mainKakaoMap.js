/**
 * 
 * <pre>
 * << 개정이력 (Modifcation    Information) >>
 *  수정일          수정자         수정내용
 * ----------------  ----------------- -----------------------------
 * 2025. 7. 7.         윤현식         생성
 */
document.addEventListener("DOMContentLoaded", () => {
	if (typeof kakao === "undefined" || !kakao.maps || !kakao.maps.load) {
		console.error("❌ kakao 객체가 없음 (SDK 로드 실패 또는 차단됨)");
		return; // kakao가 없으므로 이후 지도 로직 중단
	}

	kakao.maps.load(() => {
		const map = initMap();
		const clusterer = initClusterer(map);
		setupMapControls(map);

		window._map = map;
		window._clusterer = clusterer;

		setupModalCloseBtn();
		setupManualFilterTrigger(map, clusterer);
		setupCategoryButtonHandler(map, clusterer);
		setupKeywordSearch(map, clusterer);
		setupPopupOptionClick(map, clusterer);
		setupClusterClick(map, clusterer);
		setupFacilityOptionListener(map, clusterer);
		initCategoryFromParam(map, clusterer);
		setupIdleEvent(map, clusterer);

		let firstLoaded = false;
		kakao.maps.event.addListener(map, 'tilesloaded', () => {
			if (firstLoaded) return;
			firstLoaded = true;

			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url)
				.then(res => res.json())
				.then(data => {
					renderMarkers(data, map, clusterer);
					renderListPage(data, map);
				});
		});

		setDefaultFilterValues();
	});
});


