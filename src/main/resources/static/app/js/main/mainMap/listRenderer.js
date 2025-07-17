window.renderListPage = function(data, map, page = 1, perPage = 5) {
	const listContainer = document.getElementById('listing-list');
	const paginationContainer = document.getElementById('pagination');
	const getSaleTypeText = (code) => {
		switch (code) {
			case '001': return '전세';
			case '002': return '월세';
			case '003': return '매매';
			default: return '기타';
		}
	};

	const getDepositText = (item) => {
		const type = String(item.lstgTypeSale);
		const lease = item.lstgLease || 0;
		const leaseM = item.lstgLeaseM || 0;

		switch (type) {
			case '001': // 전세
				return `전세금: ${lease}`;
			case '002': // 월세
				return `보증금: ${lease} / 월세: ${leaseM}`;
			case '003': // 매매
				return `매매가: ${lease}`;
			default:
				return '';
		}
	};

	listContainer.innerHTML = '';
	if (paginationContainer) paginationContainer.innerHTML = '';

	const sliced = data.slice((page - 1) * perPage, page * perPage);

	sliced.forEach(item => {
		const lat = parseFloat(item.lstgLat);
		const lng = parseFloat(item.lstgLng);
		const position = new kakao.maps.LatLng(lat, lng);

		const div = document.createElement('div');
		div.className = 'list-item';
		div.innerHTML = `
			<div class="list-content">
				<div class="list-title">${item.lstgNm || item.bldgNm}</div>
				<div class="list-body">
					<div>도로명: ${item.lstgAdd}</div>
					<div>면적: ${item.lstgExArea || '-'}㎡    거래유형 : ${getSaleTypeText(item.lstgTypeSale)}</div>
					<div>${getDepositText(item)}</div>
				</div>
			</div>
		`;

		div.addEventListener('click', () => {
			map.panTo(position);
			openDetailModal(item.lstgId);
		});

		/*div.addEventListener('click', () => {
			map.panTo(position);
			const mbrCdParam = window.loggedInUserId || '';
			fetch(`/map/api/detail?lstgId=${item.lstgId}&mbrCd=${encodeURIComponent(mbrCdParam)}`)
				.then(res => res.json())
				.then(detail => showDetailModal(detail));
		});*/

		listContainer.appendChild(div); showDetailModal
	});

	// ✅ 페이지네이션은 여기만!
	if (paginationContainer) {
		const totalPages = Math.ceil(data.length / perPage);

		const first = document.createElement('a');
		first.innerText = '«';
		first.className = 'page-link';
		if (page === 1) {
			first.classList.add('disabled');
		} else {
			first.addEventListener('click', e => {
				e.preventDefault();
				renderListPage(data, map, 1, perPage);
			});
		}
		paginationContainer.appendChild(first);

		const prev = document.createElement('a');
		prev.innerText = '‹';
		prev.className = 'page-link';
		if (page === 1) {
			prev.classList.add('disabled');
		} else {
			prev.addEventListener('click', e => {
				e.preventDefault();
				renderListPage(data, map, page - 1, perPage);
			});
		}
		paginationContainer.appendChild(prev);

		for (let i = 1; i <= totalPages; i++) {
			const a = document.createElement('a');
			a.href = '#';
			a.innerText = i;
			a.classList.add('page-link');
			if (i === page) {
				a.classList.add('on');
			} else {
				a.addEventListener('click', e => {
					e.preventDefault();
					renderListPage(data, map, i, perPage);
				});
			}
			paginationContainer.appendChild(a);
		}

		const next = document.createElement('a');
		next.innerText = '›';
		next.className = 'page-link';
		if (page === totalPages) {
			next.classList.add('disabled');
		} else {
			next.addEventListener('click', e => {
				e.preventDefault();
				renderListPage(data, map, page + 1, perPage);
			});
		}
		paginationContainer.appendChild(next);

		const last = document.createElement('a');
		last.innerText = '»';
		last.className = 'page-link';
		if (page === totalPages) {
			last.classList.add('disabled');
		} else {
			last.addEventListener('click', e => {
				e.preventDefault();
				renderListPage(data, map, totalPages, perPage);
			});
		}
		paginationContainer.appendChild(last);
	}
};

window.openDetailModal = function(lstgId) {
	const mbrCd = window.loggedInUserId || '';
	const url = `/map/api/detail?lstgId=${lstgId}&mbrCd=${encodeURIComponent(mbrCd)}`;
	
	fetch(url)
		.then(res => res.json())
		.then(data => {
			showDetailModal(data);
		});
};


window.showDetailModal = function(data) {
	const modal = document.getElementById('side-detail-modal');
	const body = document.getElementById('sideModalBody');
	modal.classList.add('active');
	
	const getDealType = (code) => ({
		'001': '전세',
		'002': '월세',
		'003': '매매'
	}[code] || '미정');


	const getDepositText = (item) => {
		const type = String(item.LSTG_TYPE_SALE);
		const lease = item.LSTG_LEASE || 0;
		const leaseM = item.LSTG_LEASE_M || 0;

		switch (type) {
			case '001': return `전세금: ${lease}`;
			case '002': return `보증금: ${lease} / 월세: ${leaseM}`;
			case '003': return `매매가: ${lease}`;
			default: return '-';
		}
	};

	const renderFacilityOptions = (options = []) => {
		if (!Array.isArray(options) || options.length === 0) return '<p>선택된 옵션 없음</p>';

		return `
	    <ul class="facility-options">
	    ${options.map(opt => `
	      <li><strong>${opt.facOptNm}</strong></li>
	    `).join('')}
	  </ul>
 	 `;
	};

	const renderImageSlider = () => {
		const imageUrls = [
			'/volt/assets/img/images/room1.png',
			'/volt/assets/img/images/room2.png',
			'/volt/assets/img/images/room3.png'
		];

		return `
    <div class="image-slider">
      ${imageUrls.map(url => `
        <div class="image-item">
          <img src="${url}" alt="매물 이미지" />
        </div>
      `).join('')}
    </div>
  `;
	};

	body.innerHTML = `
	${renderImageSlider()}
	
	  <div class="detail-modal">
	    <!-- 상단 헤더 -->
	    <div class="header">
	      <h2 class="listing-title">${data.LSTG_NM || '-'}</h2>
	      <p class="listing-address">${data.LSTG_ADD || ''} ${data.LSTG_ADD2 || ''}</p>
	    </div>
	
	    <!-- 📦 가격 정보 박스 -->
	   <div class="deal-section">
		  <h4>가격 정보</h4>
		  <ul>
		    <li><strong>거래유형:</strong> ${getDealType(data.LSTG_TYPE_SALE)}</li>
		    <li><strong>${data.LSTG_TYPE_SALE === '002' ? '보증금:' : '전세금:'}</strong> ${getDepositText(data)}</li>
		    ${data.LSTG_TYPE_SALE === '002' ? `<li><strong>월세:</strong> ${data.LSTG_MONTH_PRICE || 0}만원</li>` : ''}
		    <li><strong>관리비:</strong> ${data.LSTG_MGMT_PRICE ? `${data.LSTG_MGMT_PRICE}만원` : '없음'}</li>
		    <li><strong>면적:</strong> ${data.LSTG_EX_AREA}㎡ / ${data.LSTG_GR_AREA}㎡</li>
		    <li><strong>방 개수:</strong> ${data.LSTG_ROOM_CNT}개</li>
		    <li><strong>층수:</strong> ${data.LSTG_FLOOR}</li>
		    <li><strong>주차:</strong> ${data.LSTG_PARK_YN === 'Y' ? '가능' : '불가능'}</li>
		  </ul>
		</div>
	
	    <!-- 시설 옵션 -->
	    <div class="option-section">
	      <h4>시설 옵션</h4>
	      ${renderFacilityOptions(data.facilityOptions)}
	    </div>
	    
	    <!-- 중개사 정보 -->
	    <div class="broker-section">
	      <h4>중개사 정보</h4>
	      <ul>
	        <li><strong>중개사명:</strong> ${data.BROK_NM || '-'}</li>
	        <li><strong>대표자명:</strong> ${data.REPR_NM || '-'}</li>
	        <li><strong>연락처:</strong> ${data.REPR_TEL_NO || '-'}</li>
	      </ul>
	    </div>
		
		<div class="detail-actions">
			<button id="inquiryBtn" data-lstg-id=${data.LSTG_ID || data.lstgId}>문의하기</button> 
			<img id="heartIcon"
		     src="/volt/assets/img/heart-svgrepo-com.svg"
		     data-active="false"
		     data-lstg-id="${data.LSTG_ID || data.lstgId}" />
		</div>
		<div id="wishlist-count-text"></div>
	</div>
	
	<div id="galleryModal" class="gallery-modal" style="display: none;">
	    <div class="gallery-overlay" onclick="closeGalleryModal()"></div>
	    <div class="gallery-content">
	      <span class="gallery-close" onclick="closeGalleryModal()">×</span>
	      <img id="galleryImage" src="" alt="확대 이미지">
	      <div class="gallery-nav">
	        <button id="prevBtn" onclick="changeGalleryImage(-1)">〈</button>
	        <button id="nextBtn" onclick="changeGalleryImage(1)">〉</button>
	      </div>
	    </div>
	 </div>
	`;

	setupGalleryViewer();

	setupHeartClickEvent(data);
	
	console.log('받은 상세 데이터:', data);
	console.log('찜 여부 판단 결과:', isWishlisted);

};


window.setupModalCloseBtn = function() {
	document.getElementById('sideModalClose')?.addEventListener('click', () => {
		document.getElementById('side-detail-modal')?.classList.remove('active');
	});
};
