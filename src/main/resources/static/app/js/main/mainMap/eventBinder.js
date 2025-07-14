window.setupCategoryButtonHandler = function(map, clusterer) {
	document.querySelectorAll('.category-btn').forEach(btn => {
		btn.addEventListener('click', (e) => {
			e.preventDefault();
			const current = window.currentCategory;
			const selected = btn.dataset.category;

			window.currentCategory = current === selected ? null : selected;
			document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
			if (window.currentCategory) btn.classList.add('active');

			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url).then(res => res.json()).then(data => {
				renderMarkers(data, map, clusterer);
				renderListPage(data, map);
			});
		});
	});
};

window.setupIdleEvent = function(map, clusterer) {
	let currentOverlay = null;

	kakao.maps.event.addListener(map, 'idle', () => {

		if (window._currentMarkerState.overlay) {
			window._currentMarkerState.overlay.setMap(null);
			window._currentMarkerState.overlay = null;
			window._currentMarkerState.marker = null;
		}

		const bounds = map.getBounds();
		const url = buildUrl(bounds, window.currentCategory);
		fetch(url).then(res => res.json()).then(data => {
			renderMarkers(data, map, clusterer);
			renderListPage(data, map);
		});

	});
};

window.setupManualFilterTrigger = function(map, clusterer) {
	document.getElementById('applyFilterBtn')?.addEventListener('click', () => {
		const bounds = map.getBounds();
		const url = buildUrl(bounds, window.currentCategory);
		fetch(url).then(res => res.json()).then(data => {
			renderMarkers(data, map, clusterer);
			renderListPage(data, map);
		});
	});
};


window.setupKeywordSearch = function(map, clusterer) {
	document.getElementById('keywordSearchBtn')?.addEventListener('click', () => {
		const bounds = map.getBounds();
		const url = buildUrl(bounds, window.currentCategory);
		fetch(url).then(res => res.json()).then(data => {
			renderMarkers(data, map, clusterer);
			renderListPage(data, map);
		});
	});
};

window.setupClusterClick = function(map, clusterer) {
	kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
		const level = map.getLevel() - 1;
		map.setLevel(level, { anchor: cluster.getCenter() });
	});
};


window.initCategoryFromParam = function(map, clusterer) {
	const category = new URLSearchParams(location.search).get('category');
	if (!category) return;
	window.currentCategory = category;

	document.querySelectorAll('.category-btn').forEach(btn => {
		btn.classList.toggle('active', btn.dataset.category === category);
	});

	const bounds = map.getBounds();
	const url = buildUrl(bounds, window.currentCategory);
	fetch(url).then(res => res.json()).then(data => {
		renderMarkers(data, map, clusterer);
		renderListPage(data, map);
	});
};

window.buildUrl = function(bounds, category) {
	const sw = bounds.getSouthWest();
	const ne = bounds.getNorthEast();
	let url = `/map/api/mark?swLat=${sw.getLat()}&swLng=${sw.getLng()}&neLat=${ne.getLat()}&neLng=${ne.getLng()}`;
	if (category) url += `&category=${category}`;

	const params = getFilterParams();

	for (const key in params) {
		const value = params[key];
		if (Array.isArray(value)) {
			value.forEach(v => {
				url += `&${encodeURIComponent(key)}=${encodeURIComponent(v)}`;
			});
		} else {
			url += `&${encodeURIComponent(key)}=${encodeURIComponent(value)}`;
		}
	}

	return url;
};


window.setupFilterOptionClick = function(map, clusterer) {
	document.querySelectorAll('.filter-option').forEach(btn => {
		btn.addEventListener('click', (e) => {
			const type = btn.dataset.type;
			const value = btn.dataset.value;

			// 기존 선택 해제
			document.querySelectorAll(`.filter-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			// 현재 선택된 버튼 표시
			btn.classList.add('active');

			// 숨겨진 select 박스에도 값 반영 (getFilterParams에서 사용되도록)
			const hiddenSelect = document.getElementById(type + 'Filter');
			if (hiddenSelect) hiddenSelect.value = value;

			// 필터 적용
			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url).then(res => res.json()).then(data => {
				renderMarkers(data, map, clusterer);
				renderListPage(data, map);
			});
		});
	});
};

// 모달창 열기 / 닫기 함수
window.toggleFilterPopup = function(type, btn) {
	const popup = document.getElementById('popup-' + type);
	const allPopups = document.querySelectorAll('.filter-popup');

	// 다른 팝업 닫기
	allPopups.forEach(p => {
		if (p !== popup) p.style.display = 'none';
	});

	// 현재 팝업 토글
	if (popup.style.display === 'block') {
		popup.style.display = 'none';
	} else {
		popup.style.display = 'block';
	}
};

window.closeFilterPopup = function(type) {
	const popup = document.getElementById('popup-' + type);
	if (popup) popup.style.display = 'none';
};

window.setupPopupOptionClick = function(map, clusterer) {
	document.querySelectorAll('.popup-option').forEach(btn => {
		btn.addEventListener('click', (e) => {
			const type = btn.dataset.type;
			const value = btn.dataset.value;

			// 기존 선택 해제
			document.querySelectorAll(`.popup-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			// 선택한 버튼 활성화
			btn.classList.add('active');

			// 숨겨진 select 태그에도 값 반영
			const hidden = document.getElementById(type + 'Filter');
			if (hidden) hidden.value = value;

			// 필터 fetch 트리거
			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url).then(res => res.json()).then(data => {
				renderMarkers(data, map, clusterer);
				renderListPage(data, map);
			});
		});
	});
};

window.setDefaultFilterValues = function() {
	// 기본 필터들에 대해 '전체' 버튼 클릭 상태 유지
	const defaultFilters = [
		'saleType',          // 거래유형
		'listingType',       // 매물유형
		'saleDetailType',     // 매물상세유형
		'area'
	];

	defaultFilters.forEach(type => {
		// 버튼들 중 data-value="" 가 있는 "전체"를 찾아 활성화
		const allBtn = document.querySelector(`.popup-option[data-type="${type}"][data-value=""]`);
		const hidden = document.getElementById(`${type}Filter`);

		if (allBtn) {
			// 기존 active 제거
			document.querySelectorAll(`.popup-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			allBtn.classList.add('active');  // 전체 활성화
		}

		if (hidden) hidden.value = "";  // 숨은 input 값도 초기화
	});
}
