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

	// ✅ 카테고리 버튼 active 클래스 적용
	document.querySelectorAll('.category-btn').forEach(btn => {
		btn.classList.toggle('active', btn.dataset.category === category);
	});

	// ✅ 상세유형 필터 표시/숨김 처리 (카테고리 클릭 시 로직과 동일)
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

	// ✅ 상세 필터 초기화
	const detailFilter = document.getElementById('saleDetailTypeFilter');
	if (detailFilter) detailFilter.value = "";

	// ✅ 마커/리스트 로딩
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

window.toggleFilterPopup = function(type, btn) {
	const popup = document.getElementById('popup-' + type);
	const allPopups = document.querySelectorAll('.filter-popup');

	if (type === 'saleDetailType' && (btn.classList.contains('disabled') || btn.disabled)) {
		return;
	}

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

document.getElementById('resetFilters')?.addEventListener('click', () => {
	// 1. 시설 옵션 체크박스 해제
	document.querySelectorAll('.facilityOpt:checked').forEach(chk => chk.checked = false);

	// 2. 숨겨진 input/select 초기화
	const hiddenInputs = [
		'#saleTypeFilter', '#listingTypeFilter', '#saleDetailTypeFilter', '#areaFilter',
		'#minFloor', '#maxFloor', '#minArea', '#maxArea', '#parkingYn'
	];
	hiddenInputs.forEach(id => {
		const el = document.querySelector(id);
		if (el) el.value = '';
	});

	// 3. '전체' 버튼 활성화
	if (typeof window.setDefaultFilterValues === 'function') {
		window.setDefaultFilterValues();
	}

	// 4. 카테고리 버튼 초기화
	window.currentCategory = null;
	document.querySelectorAll('.category-btn').forEach(btn => btn.classList.remove('active'));

	// ✅ 5. 마커/리스트 강제 재로드
	if (window._map && window._map.getBounds && window._clusterer) {
		const bounds = window._map.getBounds();
		const url = buildUrl(bounds, null); // 필터 초기화 후 기준으로 URL 생성

		fetch(url)
			.then(res => res.json())
			.then(data => {
				renderMarkers(data, window._map, window._clusterer);
				renderListPage(data, window._map);
			});
	} else {
		console.warn('window._map or window._clusterer is not defined.');
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

			// 기존 선택 해제
			document.querySelectorAll(`.popup-option[data-type="${type}"]`)
				.forEach(b => b.classList.remove('active'));

			// 선택한 버튼 활성화
			btn.classList.add('active');

			// 숨겨진 select 태그에도 값 반영
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
							  <option value="">최소</option>
							  <option value="500">500</option>
							  <option value="1000">1000</option>
							  <option value="2000">2000</option>
							</select>
							<span class="range-separator">~</span>
							<select name="jeonseMax">
							  <option value="">최대</option>
							  <option value="3000">3000</option>
							  <option value="5000">5000</option>
							  <option value="10000">10000</option>
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

function refreshDetailModal(lstgId) {
	fetch(`/map/api/detail?lstgId=${lstgId}`)
		.then(res => res.json())
		.then(detailData => {
			if (Array.isArray(detailData) && detailData.length > 0) {
				const data = detailData[0];
				showDetailModal(data);       // 모달 다시 열기
				setupHeartClickEvent(data);  // ✅ 하트 상태 및 이벤트 적용
				if (typeof bindDetailModalEvents === 'function') {
                    bindDetailModalEvents(data);
                }
			}
		});
}

window.setupHeartClickEvent = function(data) {
	const heartIcon = document.getElementById("heartIcon");
	if (!heartIcon) return;

	// 🟢 상태 반영
	const isWishlisted = data.IS_WISHLISTED === 1 || data.IS_WISHLISTED === '1' || data.isWishlisted === true;
	heartIcon.dataset.active = isWishlisted ? "true" : "false";
	heartIcon.src = isWishlisted
		? "/volt/assets/img/heart-filled.svg"
		: "/volt/assets/img/heart-svgrepo-com.svg";

	// 🟢 기존 이벤트 제거 후 새로 등록
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

				// 🟡 최신 상태로 다시 렌더링
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

	// 클릭 이벤트 바인딩 (대표 + 썸네일)
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
            if (lstgIdToReport) {
                // `contextPath`는 JSP에서 정의된 전역 변수여야 합니다.
                // 이 변수가 없으면 오류가 발생합니다.
                window.location.href = `${contextPath}/main/report/createForm?targetId=${lstgIdToReport}&type=LSTG`;
            } else {
                console.error("신고할 매물 ID를 찾을 수 없습니다.");
                alert("신고할 매물 정보를 가져올 수 없습니다. 다시 시도해 주세요.");
            }
        });
    }
};
