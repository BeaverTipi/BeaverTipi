window._currentMarkerState = {
	overlay: null,
	marker: null
};

window.renderMarkers = function(data, map, clusterer) {
	const groupedMap = new Map();
	const markers = [];

	const SALE_TYPE_MAP = {
		'001': '전세',
		'002': '월세',
		'003': '매매'
	};

	const TYPE_CODE2_MAP = {
		'001': '원룸/투룸/다세대',
		'002': '단독주택',
		'003': '다가구주택',
		'004': '상가주택'
	};


	clusterer.clear();

	data.forEach(item => {
		const key = `${item.lstgLat}_${item.lstgLng}`;
		if (!groupedMap.has(key)) groupedMap.set(key, []);
		groupedMap.get(key).push(item);
	});

	const formatDeal = (item) => {
		const lease = item.lstgLease || 0;
		const leaseM = item.lstgLeaseM || 0;
		const leaseAmt = item.lstgLeaseAmt || 0;

		switch (item.lstgTypeSale) {
			case '001':
				return lease ? `전세 ${lease.toLocaleString()}만원` : '';
			case '002':
				return (leaseAmt || leaseM) ? `보증 ${leaseAmt.toLocaleString()} / 월 ${leaseM.toLocaleString()}만원` : '';
			case '003':
				return '매매';
			default:
				return '';
		}
	};

	const makeOverlayContent = (group) => {
		const maxItems = Math.min(group.length, 5);
		const [main, ...rest] = group.slice(0, maxItems);

		const dealText = formatDeal(main);
		const saleType = SALE_TYPE_MAP[main.lstgTypeSale] || '';
		const itemType = TYPE_CODE2_MAP[main.lstgTypeCode2] || '';

		// 임시 대표 이미지 (VO 없이 정적 경로)
		const imageUrl = '/volt/assets/img/images/room1.png';

		let html = `
			<div class="overlaybox">
				<div class="boxtitle">
					${group && group.length > 1
						? `${main.lstgNm || main.bldgNm} 외 ${group.length - 1}건`
						: `${main.lstgNm || main.bldgNm}`
					}
					<div class="close" style="cursor:pointer; float:right;">X</div>
				</div>
		
				<div class="overlay-content-horizontal">
			      <div class="overlay-text-block">
			        <div class="triangle text">1</div>
			        <div class="movietitle text">
			          ${main.lstgAdd}<br>
			          <span class="listing-info">
			            ${saleType} / ${itemType}
			          </span>
			        </div>
			      </div>
			      <div class="overlay-image-block">
			        <img src="${imageUrl}" alt="대표 이미지" class="overlay-thumbnail"/>
			      </div>
			    </div>
			
			    <ul>`;

		rest.forEach((item, i) => {
			html += `
		<li>
			<span class="number">${i + 2}</span>
			<span class="desc">
				${SALE_TYPE_MAP[item.lstgTypeSale] || '-'} /
				${TYPE_CODE2_MAP[item.lstgTypeCode2] || '-'}
			</span>
		</li>`;
		});

		html += `</ul></div>`;
		return html;
	};



	groupedMap.forEach(group => {
		const lat = parseFloat(group[0].lstgLat);
		const lng = parseFloat(group[0].lstgLng);
		if (isNaN(lat) || isNaN(lng)) return;

		const position = new kakao.maps.LatLng(lat, lng);
		const marker = new kakao.maps.Marker({
			position: position,
			title: group[0].lstgNm || group[0].bldgNm
		});
		markers.push(marker);

		const content = document.createElement('div');
		content.innerHTML = makeOverlayContent(group);

		const overlay = new kakao.maps.CustomOverlay({
			content: content,
			position: position,
			yAnchor: 1.3
		});

		// 닫기 버튼
		content.querySelector('.close')?.addEventListener('click', () => {
			overlay.setMap(null);
			if (window._currentMarkerState.overlay === overlay) {
				window._currentMarkerState.overlay = null;
				window._currentMarkerState.marker = null;
			}
		});

		// 마커 클릭 시 toggle
		kakao.maps.event.addListener(marker, 'click', () => {
			if (window._currentMarkerState.overlay && window._currentMarkerState.marker === marker) {
				window._currentMarkerState.overlay.setMap(null);
				window._currentMarkerState.overlay = null;
				window._currentMarkerState.marker = null;
			} else {
				if (window._currentMarkerState.overlay) window._currentMarkerState.overlay.setMap(null);
				overlay.setMap(map);
				window._currentMarkerState.overlay = overlay;
				window._currentMarkerState.marker = marker;
			}
		});
	});

	clusterer.addMarkers(markers);
};
