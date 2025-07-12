window.renderListPage = function (data, map, page = 1, perPage = 5) {
  const listContainer = document.getElementById('listing-list');
  const paginationContainer = document.getElementById('pagination');
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
          <div>면적: ${item.lstgExArea || '-'}㎡</div>
          <div>보증금: ${item.lstgLease || 0} / 월세: ${item.lstgLeaseM || 0}</div>
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

  if (paginationContainer) {
    const totalPages = Math.ceil(data.length / perPage);
    for (let i = 1; i <= totalPages; i++) {
      const a = document.createElement('a');
      a.href = '#';
      a.innerText = i;
      a.className = i === page ? 'page-link on' : 'page-link';
      a.addEventListener('click', e => {
        e.preventDefault();
        renderListPage(data, map, i, perPage);
      });
      paginationContainer.appendChild(a);
    }
  }
};

window.showDetailModal = function (data) {
  const modal = document.getElementById('side-detail-modal');
  const body = document.getElementById('sideModalBody');
  modal.classList.add('active');

  const getDealType = (code) => ({ 1: '전세', 2: '월세', 3: '매매' }[code] || '미정');

  body.innerHTML = `
    <h3>${data.lstgNm || '-'}</h3>
    <p><strong>주소:</strong> ${data.lstgAdd || ''} ${data.lstgAdd2 || ''}</p>
    <p><strong>면적:</strong> ${data.lstgExArea || '-'}㎡</p>
    <p><strong>방 개수:</strong> ${data.lstgRoomCnt || '-'}개</p>
    <p><strong>거래유형:</strong> ${getDealType(data.lstgTypeSale)}</p>
    <p><strong>보증금:</strong> ${data.lstgLease || 0} / 월세: ${data.lstgLeaseM || 0}</p>
    <p><strong>매매가:</strong> ${data.lstgLeaseAmt || 0}</p>
    <p><strong>층수:</strong> ${data.lstgFloor || '-'}</p>
    <p><strong>주차 가능:</strong> ${data.lstgParkYn === 'Y' ? '가능' : '불가능'}</p>
  `;
};

window.setupModalCloseBtn = function () {
  document.getElementById('sideModalClose')?.addEventListener('click', () => {
    document.getElementById('side-detail-modal')?.classList.remove('active');
  });
};
