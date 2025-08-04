window.setupCategoryButtonHandler = function(map, clusterer) {
	document.querySelectorAll('.category-btn').forEach(btn => {
		btn.addEventListener('click', (e) => {
			e.preventDefault();
			const current = window.currentCategory;
			const selected = btn.dataset.category;

			window.currentCategory = current === selected ? null : selected;
			document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
			if (window.currentCategory) btn.classList.add('active');

			document.querySelectorAll('.popup-option[data-type="saleDetailType"]').forEach(option => {
				const isAll = option.dataset.value === "";
				const parent = option.dataset.parent;

				if (isAll || parent === window.currentCategory) {
					option.style.display = "inline-block";
				} else {
					option.style.display = "none";
					option.classList.remove("active");
				}
			});

			const saleDetailBtn = document.getElementById('saleDetailTypeBtn');
			const allOptions = document.querySelectorAll('.popup-option[data-type="saleDetailType"]');

			if (window.currentCategory === '001') {
				allOptions.forEach(option => {
					const parent = option.dataset.parent;
					const isAll = option.dataset.value === "";

					if (isAll || parent === '001') {
						option.style.display = 'inline-block';
					} else {
						option.style.display = 'none';
					}
				});

				saleDetailBtn.classList.remove('disabled');
				saleDetailBtn.disabled = false;

			} else {
				allOptions.forEach(option => {
					const isAll = option.dataset.value === "";
					option.style.display = isAll ? 'inline-block' : 'none';
					option.classList.remove('active');
				});

				saleDetailBtn.classList.add('disabled');
				saleDetailBtn.disabled = true;
				closeFilterPopup('saleDetailType');
			}

			const detailFilter = document.getElementById('saleDetailTypeFilter');
			if (detailFilter) detailFilter.value = "";

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

window.setupFacilityOptionListener = function(map, clusterer) {
	document.querySelectorAll('.facilityOpt').forEach(checkbox => {
		checkbox.addEventListener('change', () => {
			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url)
				.then(res => res.json())
				.then(data => {
					renderMarkers(data, map, clusterer);
					renderListPage(data, map);
				});
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
	window.currentCategory = category || null;

	document.querySelectorAll('.category-btn').forEach(btn => {
		btn.classList.toggle('active', btn.dataset.category === category);
	});

	const saleDetailBtn = document.getElementById('saleDetailTypeBtn');
	const allOptions = document.querySelectorAll('.popup-option[data-type="saleDetailType"]');

	if (window.currentCategory === '001') {
		allOptions.forEach(option => {
			const parent = option.dataset.parent;
			const isAll = option.dataset.value === "";

			option.style.display = (isAll || parent === '001') ? 'inline-block' : 'none';
		});
		saleDetailBtn.classList.remove('disabled');
		saleDetailBtn.disabled = false;
	} else {
		allOptions.forEach(option => {
			const isAll = option.dataset.value === "";
			option.style.display = isAll ? 'inline-block' : 'none';
			option.classList.remove('active');
		});
		saleDetailBtn.classList.add('disabled');
		saleDetailBtn.disabled = true;
		closeFilterPopup('saleDetailType');
	}

	const detailFilter = document.getElementById('saleDetailTypeFilter');
	if (detailFilter) detailFilter.value = "";

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

			document.querySelectorAll(`.filter-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			btn.classList.add('active');

			const hiddenSelect = document.getElementById(type + 'Filter');
			if (hiddenSelect) hiddenSelect.value = value;

			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url).then(res => res.json()).then(data => {
				renderMarkers(data, map, clusterer);
				renderListPage(data, map);
			});
		});
	});
};

window.toggleFilterPopup = function(type, btn) {
	const popup = document.getElementById('popup-' + type);
	const allPopups = document.querySelectorAll('.filter-popup');

	if (type === 'saleDetailType' && (btn.classList.contains('disabled') || btn.disabled)) {
		return;
	}

	allPopups.forEach(p => {
		if (p !== popup) p.style.display = 'none';
	});

	if (popup.style.display === 'block') {
		popup.style.display = 'none';
	} else {
		popup.style.display = 'block';
	}
};

window.resetAllMapFilters = function () {
	document.querySelectorAll('.facilityOpt:checked').forEach(chk => chk.checked = false);
	
	const saleDetailBtn = document.getElementById('saleDetailTypeBtn');
	if (saleDetailBtn) {
	  saleDetailBtn.classList.add('disabled');
	  saleDetailBtn.disabled = true;
	}
	
	const hiddenInputs = [
		'#saleTypeFilter', '#listingTypeFilter', '#saleDetailTypeFilter', '#areaFilter',
		'#minFloor', '#maxFloor', '#minArea', '#maxArea', '#parkingYn'
	];
	hiddenInputs.forEach(id => {
		const el = document.querySelector(id);
		if (el) el.value = '';
	});

	if (typeof window.setDefaultFilterValues === 'function') {
		window.setDefaultFilterValues();
	}

	window.currentCategory = null;
	document.querySelectorAll('.category-btn').forEach(btn => btn.classList.remove('active'));

	if (window._map && window._map.getBounds && window._clusterer) {
		const bounds = window._map.getBounds();
		const url = buildUrl(bounds, null);

		fetch(url)
			.then(res => res.json())
			.then(data => {
				renderMarkers(data, window._map, window._clusterer);
				renderListPage(data, window._map);
			});
	} else {
		console.warn('window._map or window._clusterer is not defined.');
	}
};

document.getElementById('resetFilters')?.addEventListener('click', window.resetAllMapFilters);
document.getElementById('resetFiltersTop')?.addEventListener('click', window.resetAllMapFilters);


document.addEventListener('click', function(e) {
	if (e.target?.id === 'toggle-unit-btn') {
		e.stopPropagation();

		const display = document.getElementById('area-display');
		if (!display) return;

		const grArea = parseFloat(display.dataset.grArea || '0');
		const currentUnit = display.dataset.unit;

		if (currentUnit === 'm2') {
			const grPy = Math.round(grArea / 3.3);
			display.textContent = `${grPy}평`;
			display.dataset.unit = 'py';
			e.target.textContent = '평 → ㎡';
		} else {
			display.textContent = `${grArea}㎡`;
			display.dataset.unit = 'm2';
			e.target.textContent = '㎡ → 평';
		}
	}
});

window.closeFilterPopup = function(type) {
	const popup = document.getElementById('popup-' + type);
	if (popup) popup.style.display = 'none';
};

function setupPriceSelectEvents(map, clusterer) {
	const selects = document.querySelectorAll('#priceFilterSection select');
	selects.forEach(select => {
		select.addEventListener('change', () => {
			const bounds = map.getBounds();
			const url = buildUrl(bounds, window.currentCategory);
			fetch(url).then(res => res.json()).then(data => {
				renderMarkers(data, map, clusterer);
				renderListPage(data, map);
			});
		});
	});
}

window.setupPopupOptionClick = function(map, clusterer) {
	document.querySelectorAll('.popup-option').forEach(btn => {
		btn.addEventListener('click', () => {
			const type = btn.dataset.type;
			const value = btn.dataset.value;

			document.querySelectorAll(`.popup-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			btn.classList.add('active');

			const hidden = document.getElementById(type + 'Filter');
			if (hidden) hidden.value = value;

			if (type === 'saleType') {
				const priceFilterSection = document.getElementById('priceFilterSection');
				let html = '';

				if (value === '001') { // 전세
					html = `
					<div class="price-filter-line">
						<label>전세금 (만원)</label>
						<div class="price-range">
							<select name="jeonseMin">
							  <option value="100000000">최소(1억)</option>
							  <option value="200000000">2억</option>
							  <option value="300000000">3억</option>
							  <option value="400000000">4억</option>
							</select>
							<span class="range-separator">~</span>
							<select name="jeonseMax">
							  <option value="">최대</option>
							  <option value="1000000000">10억</option>
							  <option value="2000000000">20억</option>
							  <option value="3000000000">30억</option>
							</select>
						</div>
					</div>
				`;
				} else if (value === '002') { // 월세
					html = `
					<div class="price-filter-line">
						<label>보증금 (만원)</label>
						<div class="price-range">
							<select name="depositMin">
							  <option value="">최소</option>
							  <option value="500">500</option>
							  <option value="1000">1000</option>
							</select>
							<span class="range-separator">~</span>
							<select name="depositMax">
							  <option value="">최대</option>
							  <option value="3000">3000</option>
							  <option value="5000">5000</option>
							</select>
						</div>
					</div>
					<div class="price-filter-line">
						<label>월세 (만원)</label>
						<div class="price-range">
							<select name="monthlyMin">
							  <option value="">최소</option>
							  <option value="30">30</option>
							  <option value="50">50</option>
							</select>
							<span class="range-separator">~</span>
							<select name="monthlyMax">
							  <option value="">최대</option>
							  <option value="100">100</option>
							  <option value="150">150</option>
							</select>
						</div>
					</div>
				`;
				} else if (value === '003') { // 매매
					html = `
					<div class="price-filter-line">
						<label>매매가 (만원)</label>
						<div class="price-range">
							<select name="saleMin">
							  <option value="">최소</option>
							  <option value="10000">1억</option>
							  <option value="20000">2억</option>
							</select>
							<span class="range-separator">~</span>
							<select name="saleMax">
							  <option value="">최대</option>
							  <option value="30000">3억</option>
							  <option value="50000">5억</option>
							</select>
						</div>
					</div>
				`;
				}


				if (priceFilterSection) {
					priceFilterSection.innerHTML = html;
					priceFilterSection.style.display = html ? 'block' : 'none';

					setupPriceSelectEvents(map, clusterer);
				}
			}

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
	const defaultFilters = [
		'saleType',
		'listingType',
		'saleDetailType',
		'area'
	];

	defaultFilters.forEach(type => {
		const allBtn = document.querySelector(`.popup-option[data-type="${type}"][data-value=""]`);
		const hidden = document.getElementById(`${type}Filter`);

		if (allBtn) {
			document.querySelectorAll(`.popup-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			allBtn.classList.add('active');
		}

		if (hidden) hidden.value = "";
	});
}

function refreshDetailModal(lstgId) {
	fetch(`/map/api/detail?lstgId=${lstgId}`)
		.then(res => res.json())
		.then(detailData => {
			if (Array.isArray(detailData) && detailData.length > 0) {
				const data = detailData[0];
				showDetailModal(data);
				setupHeartClickEvent(data);
				if (typeof bindDetailModalEvents === 'function') {
					bindDetailModalEvents(data);
				}
			}
		});
}

window.setupHeartClickEvent = function(data) {
	const heartIcon = document.getElementById("heartIcon");
	if (!heartIcon) return;

	const isWishlisted = data.IS_WISHLISTED === 1 || data.IS_WISHLISTED === '1' || data.isWishlisted === true;
	heartIcon.dataset.active = isWishlisted ? "true" : "false";
	heartIcon.src = isWishlisted
		? "/volt/assets/img/heart-filled.svg"
		: "/volt/assets/img/heart-svgrepo-com.svg";

	const clonedHeartIcon = heartIcon.cloneNode(true);
	heartIcon.parentNode.replaceChild(clonedHeartIcon, heartIcon);
	const newHeartIcon = document.getElementById("heartIcon");

	newHeartIcon.addEventListener("click", () => {
		const lstgId = newHeartIcon.dataset.lstgId;
		const mbrCd = window.loggedInUserId;

		if (!mbrCd || mbrCd === "null" || mbrCd === "") {
			Swal.fire({
				icon: 'warning',
				title: '로그인이 필요합니다',
				text: '찜 기능은 로그인 후 이용 가능합니다.',
				confirmButtonText: '확인'
			});
			return;
		}

		fetch("/map/api/wishlist/toggle", {
			method: "POST",
			headers: { "Content-Type": "application/x-www-form-urlencoded" },
			body: `lstgId=${encodeURIComponent(lstgId)}`
		})
			.then(res => {
				if (!res.ok) throw new Error("서버 오류");
				return res.json();
			})
			.then(count => {
				const nowActive = newHeartIcon.dataset.active === "true";
				newHeartIcon.dataset.active = !nowActive ? "true" : "false";
				newHeartIcon.src = !nowActive
					? "/volt/assets/img/heart-filled.svg"
					: "/volt/assets/img/heart-svgrepo-com.svg";

				const tooltip = document.getElementById("wishlist-count-text");
				if (tooltip) {
					tooltip.innerText = `${count}명이 관심을 가지고 있습니다.`;
					tooltip.style.display = "block";
					setTimeout(() => {
						tooltip.style.display = "none";
					}, 2000);
				}

				refreshDetailModal(lstgId);
			})
			.catch(console.error);
	});
};

let galleryImages = [];
let currentIndex = 0;

window.setupGalleryViewer = function() {
	const mainImg = document.querySelector('.main-image img'); // 대표 이미지
	const thumbImgs = document.querySelectorAll('.thumbnail-grid .image-item img'); // 썸네일들

	galleryImages = [];

	if (mainImg) {
		galleryImages.push(mainImg.getAttribute('src'));
	}

	thumbImgs.forEach(imgEl => {
		const src = imgEl.getAttribute('src');
		if (src && !src.includes('no-image.png')) {
			galleryImages.push(src);
		} else {
			galleryImages.push('/volt/assets/img/illustrations/no-image.png');
		}
	});

	[mainImg, ...thumbImgs].forEach((imgEl, index) => {
		imgEl?.addEventListener('click', () => {
			currentIndex = index;
			openGalleryModal(index);
		});
	});
};


function openGalleryModal(index) {
	const modal = document.getElementById('galleryModal');
	const imgEl = document.getElementById('galleryImage');
	const fallback = '/volt/assets/img/illustrations/no-image.png';

	if (typeof index !== 'number' || index < 0 || index >= galleryImages.length) {
		index = 0;
	}

	currentIndex = index;

	imgEl.onerror = null; // 기존 onerror 초기화
	imgEl.src = galleryImages[currentIndex];
	imgEl.onerror = function() {
		if (imgEl.src !== fallback) {
			imgEl.src = fallback;
		}
	};

	modal.style.display = 'flex';
}

window.changeGalleryImage = function(delta) {
	currentIndex += delta;
	if (currentIndex < 0) currentIndex = galleryImages.length - 1;
	if (currentIndex >= galleryImages.length) currentIndex = 0;

	const imgEl = document.getElementById('galleryImage');
	const fallback = '/volt/assets/img/illustrations/no-image.png';

	imgEl.onerror = null; // 이전 핸들러 제거
	imgEl.src = galleryImages[currentIndex];
	imgEl.onerror = function() {
		if (imgEl.src !== fallback) {
			imgEl.src = fallback;
		}
	};
};

window.closeGalleryModal = function() {
	document.getElementById('galleryModal').style.display = 'none';
};

window.bindDetailModalEvents = function(detailData) {

	const warningIcon = document.getElementById('warningIcon');
	if (warningIcon) {
		warningIcon.addEventListener('click', () => {
			const lstgIdToReport = detailData.LSTG_ID || detailData.lstgId;
			const rptTargetNmToReport = detailData.LSTG_NM || detailData.lstgNm;
			if (lstgIdToReport) {
				window.location.href = `${contextPath}/member/report/createForm?targetId=${lstgIdToReport}&type=LSTG&rptTargetNm=${encodeURIComponent(rptTargetNmToReport)}`;
			} else {
				console.error("신고할 매물 ID를 찾을 수 없습니다.");
				alert("신고할 매물 정보를 가져올 수 없습니다. 다시 시도해 주세요.");
			}
		});
	}
};
