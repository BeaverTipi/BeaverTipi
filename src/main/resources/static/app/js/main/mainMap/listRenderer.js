window.renderListPage = function(data, map, page = 1, perPage = 5) {
	const listContainer = document.getElementById('listing-list');
	const paginationContainer = document.getElementById('pagination');

	if (!data || data.length === 0) {
		listContainer.innerHTML = "<p style='text-align:center; padding:20px;'>조회된 매물이 없습니다.</p>";
		if (paginationContainer) paginationContainer.innerHTML = "";
		return;
	}


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
		const leaseAmt = item.lstgLeaseAmt || 0;
	
		const format = (num) => !isNaN(num) ? Math.round(Number(num)).toLocaleString() + '원' : '-';
	
		switch (type) {
			case '001':
				return `전세금: ${format(lease)}`;
			case '002':
				return `보증금: ${format(leaseAmt)} / 월세: ${format(leaseM)}`;
			case '003':
				return `매매가: ${format(leaseAmt)}`;
			default:
				return '이건 잘못되었습니다!';
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
					<div>
					  면적: 
					  <span>
					    ${item.lstgGrArea || 0}㎡
					  </span>
					</div>
					<div>거래유형 : ${getSaleTypeText(item.lstgTypeSale)}</div>
					<div>${getDepositText(item)}</div>
				</div>
			</div>
		`;
		div.addEventListener('click', () => {
			map.panTo(position);
			openDetailModal(item.lstgId);
		});
		listContainer.appendChild(div);
	});

	if (paginationContainer) {
		const totalPages = Math.ceil(data.length / perPage);
		const maxVisiblePages = 5;

		const currentBlock = Math.floor((page - 1) / maxVisiblePages);
		const startPage = currentBlock * maxVisiblePages + 1;
		let endPage = startPage + maxVisiblePages - 1;
		if (endPage > totalPages) endPage = totalPages;

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

		for (let i = startPage; i <= endPage; i++) {
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
	
	fetch('/map/api/viewLog', {
		method: 'POST',
		headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
		body: new URLSearchParams({
			lstgId: lstgId,
			mbrCd: mbrCd
		})
	}).catch(console.warn);
	
	const url = `/map/api/detail?lstgId=${lstgId}&mbrCd=${encodeURIComponent(mbrCd)}`;
	fetch(url)
		.then(res => res.json())
		.then(data => {
			showDetailModal(data);
			if (typeof bindDetailModalEvents === 'function') {
                bindDetailModalEvents(data);
            }
		});
};



window.showDetailModal = function(data) {
	const modal = document.getElementById('side-detail-modal');
	const body = document.getElementById('sideModalBody');
	const broker = {
	  brokNm: data.brokNm || '-',
	  reprNm: data.reprNm || '-',
	  reprTelNo: data.reprTelNo || '-'
	};
	
	const nameCardImageUrl = data.brokerInfo?.businessCardUrl || null;
	
	data.imageUrlList = Array.isArray(data.listingFiles)
		  ? data.listingFiles.map(f => f.filePathUrl).filter(Boolean)
		  : [];

	modal.classList.add('active');

	const getDealType = (code) => ({
		'001': '전세',
		'002': '월세',
		'003': '매매'
	}[code] || '미정');

	const getDepositText = (item) => {
		const type = String(item.lstgTypeSale);
		const lease = item.lstgLease || 0;
		const leaseM = item.lstgLeaseM || 0;
		const leaseAmt = item.lstgLeaseAmt || 0;
	
		const format = (num) => !isNaN(num) ? Math.round(Number(num)).toLocaleString() + '원' : '-';
	
		switch (type) {
			case '001':
				return `전세금: ${format(lease)}`;
			case '002':
				return `보증금: ${format(leaseAmt)} / 월세: ${format(leaseM)}`;
			case '003':
				return `매매가: ${format(leaseAmt)}`;
			default:
				return '-';
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

	const renderImageGallery = () => {
		const fallback = '/volt/assets/img/illustrations/no-image.png';

		const imageUrls = data.imageUrlList?.length > 0
			? data.imageUrlList
			: [fallback];

		const totalImages = imageUrls.length;
		const thumbnailsToShow = imageUrls.slice(1, 5);
		const hiddenCount = totalImages > 5 ? totalImages - 5 : 0;

		return `
	<div class="image-gallery">
		<div class="main-image image-item">
			<img src="${imageUrls[0]}" alt="대표 이미지" onerror="this.src='${fallback}'" />
		</div>
		<div class="thumbnail-grid">
			${thumbnailsToShow.map((url, i) => {
				const isLast = (i === thumbnailsToShow.length - 1);
				const imageTag = `<img src="${url}" alt="썸네일" onerror="this.src='${fallback}'" />`;

				if (isLast && hiddenCount > 0) {
					return `
						<div class="image-item thumbnail-more">
							${imageTag}
							<div class="more-count">+${hiddenCount}</div>
						</div>
					`;
				}
				return `<div class="image-item">${imageTag}</div>`;
			}).join('')}
		</div>
	</div>
		`;
	};

	body.innerHTML = `
	${renderImageGallery()}
	
	<div class="detail-modal">
	    <div class="header">
	      <h2 class="listing-title">${data.lstgNm || '-'}</h2>
	      <p class="listing-address">${data.lstgAdd || ''} ${data.lstgAdd2 || ''}</p>
	    </div>
	
	    <div class="deal-section">
		  <h4>가격 정보</h4>
		  <ul>
		    <li><strong>거래유형:</strong> ${getDealType(data.lstgTypeSale)}</li>
		    <li><strong>금액:</strong> ${getDepositText(data)}</li>
		    <li><strong>관리비:</strong> ${data.lstgFee ? `${data.lstgFee}만원` : '없음'}</li>
		    <li>
		      <strong>면적:</strong>
		      <span id="area-display" data-unit="m2" data-gr-area="${data.lstgGrArea}">
		        ${data.lstgGrArea}㎡
		      </span>
		      <button id="toggle-unit-btn" class="btn-toggle-unit">㎡ → 평</button>
		    </li>
		    <li><strong>방 개수:</strong> ${data.lstgRoomCnt}개</li>
		    <li><strong>층수:</strong> ${data.lstgFloor}</li>
		    <li><strong>주차:</strong> ${data.lstgParkYn === 'Y' ? '가능' : '불가능'}</li>
		  </ul>
		</div>

	
	    <div class="option-section">
	      <h4>시설 옵션</h4>
	     ${renderFacilityOptions(data.facOptions)}
	    </div>	
	    
	    <div class="broker-section">
		  <h4>중개사 정보</h4>
		  <ul>
		    <li><strong>중개사명:</strong> ${broker.brokNm || '-'}</li>
		    <li><strong>대표자명:</strong> ${broker.reprNm || '-'}</li>
		    <li><strong>연락처:</strong> ${broker.reprTelNo || '-'}</li>
		  </ul>
		  ${nameCardImageUrl ? `<div class="namecard-wrapper">
		  <img src="${nameCardImageUrl}" alt="명함 이미지" style="max-width: 100%; margin-top: 10px;" /></div>` : ''}
		</div>
		
		<div class="detail-actions">
			<button id="inquiryBtn" data-lstg-id=${data.LSTG_ID || data.lstgId}>문의하기</button> 
			<img id="heartIcon"
		     src="/volt/assets/img/heart-svgrepo-com.svg"
		     data-active="false"
		     data-lstg-id="${data.lstgId}" />
			<img id="warningIcon" src="/volt/assets/img/icons/warning-svgrepo-com.svg">
			<div id="wishlist-count-text" class="wishlist-tooltip"></div>
		</div>
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

	if (window._map) {
		const center = window._map.getCenter();
		window._map.relayout();
		window._map.setCenter(center);
	}

	console.log('받은 상세 데이터:', data);
	console.log('📦 brokerInfo:', data.brokerInfo);
	console.log('📦 brokerInfo.files:', data.brokerInfo?.files);
};



window.setupModalCloseBtn = function() {
	document.getElementById('sideModalClose')?.addEventListener('click', () => {
		document.getElementById('side-detail-modal')?.classList.remove('active');

		if (window._map) {
			const center = window._map.getCenter();  // 현재 중심 저장
			window._map.relayout();                  // 지도 컨테이너 리사이즈 반영
			window._map.setCenter(center);           // 중심 복원
		}
	});
};
