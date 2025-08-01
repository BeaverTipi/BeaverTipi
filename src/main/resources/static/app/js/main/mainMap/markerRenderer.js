window._currentMarkerState = {
	overlay: null,
	marker: null
};

window.renderMarkers = function(data, map, clusterer) {
	const groupedMap = new Map();
	const markers = [];

	clusterer.clear();

	// 그룹핑
	data.forEach(item => {
		const key = `${item.lstgLat}_${item.lstgLng}`;
		if (!groupedMap.has(key)) groupedMap.set(key, []);
		groupedMap.get(key).push(item);
	});

	// 금액 한글 단위 포맷 (만원 기준 → 억/만원 출력)
	function formatMoneyKor(amount) {
		if (!amount || isNaN(amount)) return '';
		const man = Math.floor(amount / 10000);
		const uk = Math.floor(man / 10000);
		const manRest = man % 10000;
	
		if (uk > 0 && manRest > 0) {
			return `${uk.toLocaleString()}억 ${manRest.toLocaleString()}만원`;
		} else if (uk > 0) {
			return `${uk.toLocaleString()}억`;
		} else {
			return `${man.toLocaleString()}만원`;
		}
	}

	function formatDeal(item) {
	  const formatCurrency = (val) => {
	    const num = Number(val || 0);
	    if (num >= 100000000) return (num / 100000000).toFixed(0) + '억';
	    if (num >= 10000) return (num / 10000).toFixed(0) + '만';
	    return num.toLocaleString() + '원';
	  };
	
	  const lease = item.lstgLease;           // 전세금
	  const leaseM = item.lstgLeaseM;         // 월세금
	  const leaseAmt = item.lstgLeaseAmt;     // 보증금 or 매매금
	
	  switch (item.lstgTypeSale) {
	    case '001': // 전세
	      return `${formatCurrency(lease)}원`;
	    case '002': // 월세
	      return `보증 ${formatCurrency(leaseAmt)} / 월 ${formatCurrency(leaseM)}`;
	    case '003': // 매매
	      return `${formatCurrency(leaseAmt)}원`;
	    default:
	      return '-';
	  }
	}



	const makeOverlayContent = (group) => {
		const fallback = '/volt/assets/img/illustrations/no-image.png';
	
		const renderMainContent = (item, index) => {
			const dealText = formatDeal(item);
			const saleType = item.lstgSaleNm || '';
			const imageUrl = item.filePathUrl || fallback;
	
			return `
				<div class="overlay-text-block">
					<div class="triangle text">${index + 1}</div>
					<div class="movietitle text">
						${item.lstgAdd || ''}<br/>
						<span class="listing-info">${saleType}</span><br/>
						<span class="listing-deal">${dealText}</span>
					</div>
				</div>
				<div class="overlay-image-block">
					<img src="${imageUrl}" alt="대표 이미지" class="overlay-thumbnail"
						onerror="this.src='${fallback}'" />
				</div>
			`;
		};
	
		const titleText = (group.length > 1)
			? `${group[0].lstgNm || group[0].bldgNm} 외 ${group.length - 1}건`
			: `${group[0].lstgNm || group[0].bldgNm}`;
	
		const wrapper = document.createElement('div');
		wrapper.className = 'overlaybox';
	
		wrapper.innerHTML = `
			<div class="boxtitle">
				${titleText}
				<div class="close" style="cursor:pointer; float:right;">X</div>
			</div>
			<div class="overlay-content-horizontal" id="overlay-main-content">
				${renderMainContent(group[0], 0)}
			</div>
			<ul class="overlay-list">
			  ${group.map((item, i) => {
			    const saleType = item.lstgSaleNm || '-';
			    const dealText = formatDeal(item)?.replace(/<br\/?>/g, ' / ') || '';
			
			    return `
			      <li class="overlay-list-item" data-index="${i}">
			        <span class="number">${i + 1}</span>
			        <span class="desc">${saleType} / ${dealText}</span>
			      </li>`;
			  }).join('')}
			</ul>
			`;
	
		wrapper.querySelectorAll('.overlay-list-item').forEach(li => {
			li.addEventListener('click', () => {
				const idx = parseInt(li.dataset.index);
				const contentArea = wrapper.querySelector('#overlay-main-content');
				if (group[idx]) {
					contentArea.innerHTML = renderMainContent(group[idx], idx);
				}
			});
		});
	
		return wrapper;
	};



	// 마커 + 오버레이 처리
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

		const content = makeOverlayContent(group);

		const overlay = new kakao.maps.CustomOverlay({
			content: content,
			position: position,
			yAnchor: 1.3
		});

		content.querySelector('.close')?.addEventListener('click', () => {
			overlay.setMap(null);
			if (window._currentMarkerState.overlay === overlay) {
				window._currentMarkerState.overlay = null;
				window._currentMarkerState.marker = null;
			}
		});

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
