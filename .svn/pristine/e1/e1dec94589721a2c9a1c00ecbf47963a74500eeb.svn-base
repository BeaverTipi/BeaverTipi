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

		// 지도 표시할 div 선택
		const mapContainer = document.getElementById('map');
		// 지도 옵션 설정
		const mapOption = {
			center: new kakao.maps.LatLng(36.324994522, 127.408980639),
			level: 3
		}

		// 지도 객체 생성
		const map = new kakao.maps.Map(mapContainer, mapOption);

		// 클러스터러 생성
		const clusterer = new kakao.maps.MarkerClusterer({
			map: map,
			averageCenter: false,
			minLevel: 5,
			disableClickZoom: true
		});

		const modalCloseBtn = document.getElementById('sideModalClose');
		if (modalCloseBtn) {
			modalCloseBtn.addEventListener('click', () => {
				document.getElementById('side-detail-modal').classList.remove('active');
			});
		}

		kakao.maps.event.addListener(clusterer, 'clusterclick', function(cluster) {
			const level = map.getLevel() - 1;
			map.setLevel(level, { anchor: cluster.getCenter() });
		});

		// 마커 배열 저장(숨김/보이기 용)
		const markers = [];

		// 현재 열린 오버레이 저장용
		let currentOverlay = null;

		// 카테고리
		let currentCategory = null;

		// 페이징
		let currentListData = [];

		// 지도 컨트롤 추가
		const mapTypeControl = new kakao.maps.MapTypeControl();
		map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

		const zoomControl = new kakao.maps.ZoomControl();
		map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);

		// renderMarkers 함수 정의
		function renderMarkers(data) {
			// 기존 오버레이 제거
			if (currentOverlay) {
				currentOverlay.setMap(null);
				currentOverlay = null;
			}
			/*// 기존 마커 제거
			markers.forEach(m => m.setMap(null));
			markers.length = 0;*/

			clusterer.clear(); // 클러스터 초기화
			markers.length = 0;

			// 목록 초기화
			const listContainer = document.getElementById('listing-list');
			listContainer.innerHTML = "";

			currentListData = data;
			renderListPage(1);

			const groupedMap = new Map();

			data.forEach(item => {
				const key = `${item.lstgLat}_${item.lstgLng}`;
				if (!groupedMap.has(key)) {
					groupedMap.set(key, []);
				}
				groupedMap.get(key).push(item);
			});

			groupedMap.forEach((group, key) => {
				const lat = parseFloat(group[0].lstgLat);
				const lng = parseFloat(group[0].lstgLng);

				if (!isNaN(lat) && !isNaN(lng) &&
					lat >= -90 && lat <= 90 &&
					lng >= -180 && lng <= 180
				) {

					const position = new kakao.maps.LatLng(lat, lng);
					const marker = new kakao.maps.Marker({
						position: position,
						title: group[0].lstgNm || group[0].bldgNm,
						postal: group[0].lstgPostal
					});

					markers.push(marker);

					const titleText = group.length === 1
						? `${group[0].lstgNm || group[0].bldgNm}`
						: `${group[0].lstgNm || group[0].bldgNm} 외 ${group.length - 1}건`;

					const formatDeal = (item) => {
						const saleType = item.lstgTypeSale;
						const lease = item.lstgLease || 0;
						const leaseM = item.lstgLeaseM || 0;
						const leaseAmt = item.lstgLeaseAmt || 0;

						switch (saleType) {
							case 1: // 전세
								return `전세 ${lease.toLocaleString()}만원`;
							case 2: // 월세
								return `보증금 ${leaseAmt.toLocaleString()} / 월 ${leaseM.toLocaleString()}만원`;
							case 3: // 매매
								return `매매 ${leaseAmt.toLocaleString()}만원`;
							default:
								return `정보 없음`;
						}
					};


					const content = document.createElement('div');
					content.className = 'wrap';
					content.innerHTML = `
					<div class="info">
						<div class="title">
							${titleText}
							<div class="close" title="닫기" style="cursor:pointer;"></div>
						</div>
						<div class="body">
							<div class="desc">
								${group.map(item => `
									<div><strong>${item.lstgNm || item.bldgNm}</strong> - 보증금 : ${formatDeal(item)}</div>
								`).join('')}
							</div>
						</div>
					</div>
					`;

					const overlay = new kakao.maps.CustomOverlay({
						content: content,
						position: position,
						yAnchor: 1
					});

					content.querySelector('.close').addEventListener('click', () => {
						overlay.setMap(null);
						currentOverlay = null;
					});

					kakao.maps.event.addListener(marker, 'click', () => {
						if (currentOverlay) currentOverlay.setMap(null);
						overlay.setMap(map);
						currentOverlay = overlay;
					});

				} else {
					console.warn(`잘못된 좌표값 : ID=${item.id}, lat: ${lat}, lng: ${lng}`);
				}
			}); /* data.foreach 끝 */

			clusterer.addMarkers(markers);

		} /* renderMarkers 끝 */

		const ITEMS_PER_PAGE = 5;

		function renderListPage(page) {
			const listContainer = document.getElementById('listing-list');
			const paginationContainer = document.getElementById('pagination');
			listContainer.innerHTML = '';
			if (paginationContainer) paginationContainer.innerHTML = '';

			const start = (page - 1) * ITEMS_PER_PAGE;
			const end = start + ITEMS_PER_PAGE;

			const sliced = currentListData.slice(start, end);

			sliced.forEach(item => {
				const lat = parseFloat(item.lstgLat);
				const lng = parseFloat(item.lstgLng);
				const position = new kakao.maps.LatLng(lat, lng);

				const listItem = document.createElement('div');
				listItem.className = 'list-item';
				listItem.innerHTML = `
						<div class="list-content">
							<div class="list-title">
								${item.lstgNm || item.bldgNm}
							</div>
							<div class="list-body">
								<div class="list-desc">
									<div>도로명 : ${item.lstgAdd || item.bldgAddr}</div>
									<div>우편주소 : ${item.lstgPostal}</div>
									<div>면적: ${item.lstgExArea || '-'} ㎡</div>
									<div>보증금: ${item.lstgLease || 0} / 월세: ${item.lstgLeaseM || 0}</div>
								</div>
							</div>
						</div>
						`;

				listItem.style.cursor = 'pointer';

				// 목록 항목 클릭 시 지도 이동
				listItem.addEventListener('click', () => {
					map.panTo(position);
					const lstgId = item.lstgId;

					fetch(`/map/api/detail?lstgId=${lstgId}`)
						.then(res => res.json())
						.then(detail => {
							showDetailModal(detail);
						})
						.catch(err => {
							console.error("상세 정보 로딩 실패:", err);
							showDetailModal({});
						});
				}); /* listItem.addEventListener 끝*/

				listContainer.appendChild(listItem);

			}); /* slice.forEach 끝 */

			function getDealType(code) {
				switch (code) {
					case 1: return '전세';
					case 2: return '월세';
					case 3: return '매매';
					default: return '미정';
				}
			}

			function showDetailModal(data) {
				const modal = document.getElementById('side-detail-modal');
				const body = document.getElementById('sideModalBody');

				modal.classList.add('active');

				body.innerHTML = `
				    <h3>${data.lstgNm || '-'}</h3>
				    <p><strong>주소:</strong> ${data.lstgAdd || '-'} ${data.lstgAdd2 || ''}</p>
				    <p><string>우편주소:</strong> ${data.lstgPostal}</p>
				    <p><strong>면적:</strong> ${data.lstgExArea || '-'}㎡</p>
				    <p><strong>방 개수:</strong> ${data.lstgRoomCnt || '-'}개</p>
				    <p><strong>거래유형:</strong> ${getDealType(data.lstgTypeSale)}</p>
				    <p><strong>보증금:</strong> ${data.lstgLease || 0} / <strong>월세:</strong> ${data.lstgLeaseM || 0}</p>
				    <p><strong>매매가:</strong> ${data.lstgLeaseAmt || 0}</p>
				    <p><strong>층수:</strong> ${data.lstgFloor || '-'}</p>
				    <p><strong>주차 가능:</strong> ${data.lstgParkYn === 'Y' ? '가능' : '불가능'}</p>
				  `;

			}


			if (sliced.length === 0) {
				const noItem = document.createElement('div');
				noItem.className = 'no-item';
				noItem.innerText = '해당 범위에 매물이 없습니다.';
				listContainer.appendChild(noItem);
			}


			// 페이지네이션 생성
			if (paginationContainer) {
				const totalPages = Math.ceil(currentListData.length / ITEMS_PER_PAGE);
				for (let i = 1; i <= totalPages; i++) {
					const pageBtn = document.createElement('a');
					pageBtn.href = '#';
					pageBtn.innerText = i;
					pageBtn.classList.add('page-link');
					if (i === page) pageBtn.classList.add('on'); // 현재 페이지 스타일 지정

					pageBtn.addEventListener('click', (e) => {
						e.preventDefault();
						renderListPage(i);
					});

					paginationContainer.appendChild(pageBtn);
				}
			}
		} /* renderListPage 끝 */

		// idle 이벤트는 단 한 번 등록
		kakao.maps.event.addListener(map, 'idle', () => {
			const bounds = map.getBounds();
			const sw = bounds.getSouthWest();
			const ne = bounds.getNorthEast();

			// URL에 currentCategory가 있을 경우 파라미터로 추가
			let url = `/map/api/mark?swLat=${sw.getLat()}&swLng=${sw.getLng()}&neLat=${ne.getLat()}&neLng=${ne.getLng()}`;
			if (currentCategory) {
				url += `&category=${currentCategory}`;
			}

			fetch(url)
				.then(res => res.json())
				.then(data => renderMarkers(data))
				.catch(err => console.error("범위 마커 불러오기 실패:", err));
		});


		document.querySelectorAll('.category-btn').forEach(btn => {
			btn.addEventListener('click', (e) => {
				e.preventDefault();

				// 같은 카테고리를 다시 클릭하면 전체 보기로 되돌림
				if (currentCategory === btn.dataset.category) {
					currentCategory = null;
					document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
				} else {
					currentCategory = btn.dataset.category;
					document.querySelectorAll('.category-btn').forEach(b => b.classList.remove('active'));
					btn.classList.add('active');
				}

				// 현재 지도 범위 가져오기
				const bounds = map.getBounds();
				const sw = bounds.getSouthWest();
				const ne = bounds.getNorthEast();

				// category 여부에 따라 URL 분기
				let url = `/map/api/mark?swLat=${sw.getLat()}&swLng=${sw.getLng()}&neLat=${ne.getLat()}&neLng=${ne.getLng()}`;
				if (currentCategory) {
					url += `&category=${currentCategory}`;
				}

				// 마커 가져오기
				fetch(url)
					.then(res => res.json())
					.then(data => renderMarkers(data))
					.catch(err => console.error("마커 불러오기 실패:", err));
			});
		});


	});
});
