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
	kakao.maps.event.addListener(map, 'idle', () => {
		const bounds = map.getBounds();
		const url = buildUrl(bounds, window.currentCategory);
		fetch(url).then(res => res.json()).then(data => {
			renderMarkers(data, map, clusterer);
			renderListPage(data, map);
		});
	});
};

window.setupAutoFilterTrigger = function(map, clusterer) {
	const trigger = () => {
		const bounds = map.getBounds();
		const url = buildUrl(bounds, window.currentCategory);
		fetch(url).then(res => res.json()).then(data => {
			renderMarkers(data, map, clusterer);
			renderListPage(data, map);
		});
	};

	['#saleTypeFilter', '#listingTypeFilter', '#areaFilter', '#keywordFilter', '#parkingYn', '#minFloor', '#maxFloor']
		.forEach(sel => document.querySelector(sel)?.addEventListener('input', trigger));

	document.querySelectorAll('.facilityOpt').forEach(cb => cb.addEventListener('change', trigger));

	document.getElementById("openAdvancedFilter")?.addEventListener("click", () => {
		document.getElementById("filterModal")?.classList.add("show");
	});

	document.getElementById("closeFilterModal")?.addEventListener("click", () => {
		document.getElementById("filterModal")?.classList.remove("show");
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
	const filterParams = getFilterParams();
	const queryString = new URLSearchParams(filterParams).toString();
	if (queryString) url += `&${queryString}`;
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

