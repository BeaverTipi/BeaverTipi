window.renderListPage = function(data, map, page = 1, perPage = 5) {
	const listContainer = document.getElementById('listing-list');
	const paginationContainer = document.getElementById('pagination');
	const getSaleTypeText = (code) => {
		switch (code) {
			case 1: return '전세';
			case 2: return '월세';
			case 3: return '매매';
			default: return '기타';
		}
	};

	const getDepositText = (item) => {
		const type = String(item.lstgTypeSale);
		const lease = item.lstgLease || 0;
		const leaseM = item.lstgLeaseM || 0;

		switch (type) {
			case '1': // 전세
				return `전세금: ${lease}`;
			case '2': // 월세
				return `보증금: ${lease} / 월세: ${leaseM}`;
			case '3': // 매매
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
			fetch(`/map/api/detail?lstgId=${item.lstgId}`)
				.then(res => res.json())
				.then(detail => showDetailModal(detail));
		});

		listContainer.appendChild(div);
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

window.showDetailModal = function(data) {
	const modal = document.getElementById('side-detail-modal');
	const body = document.getElementById('sideModalBody');
	modal.classList.add('active');

	const getDealType = (code) => ({ 1: '전세', 2: '월세', 3: '매매' }[code] || '미정');

	window.getDepositText = function(item) {
		const type = String(item.lstgTypeSale);
		const lease = item.lstgLease || 0;
		const leaseM = item.lstgLeaseM || 0;

		switch (type) {
			case '1': return `전세금: ${lease}`;
			case '2': return `보증금: ${lease} / 월세: ${leaseM}`;
			case '3': return `매매가: ${lease}`;
			default: return '-';
		}
	};


	body.innerHTML = `
    <h3>${data.lstgNm || '-'}</h3>
	    <p><strong>주소:</strong> ${data.lstgAdd || ''} ${data.lstgAdd2 || ''}</p>
	    <p><strong>면적:</strong> ${data.lstgExArea || '-'}㎡</p>
	    <p><strong>방 개수:</strong> ${data.lstgRoomCnt || '-'}개</p>
	    <p><strong>거래유형:</strong> ${getDealType(data.lstgTypeSale)}</p>
	  	<p><strong>${getDepositText(data)}</strong></p>
	    <p><strong>층수:</strong> ${data.lstgFloor || '-'}</p>
	    <p><strong>주차 가능:</strong> ${data.lstgParkYn === 'Y' ? '가능' : '불가능'}</p>
  `;
};

window.setupModalCloseBtn = function() {
	document.getElementById('sideModalClose')?.addEventListener('click', () => {
		document.getElementById('side-detail-modal')?.classList.remove('active');
	});
};
