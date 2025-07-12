window.renderMarkers = function (data, map, clusterer) {
  const groupedMap = new Map();
  const markers = [];
  let currentOverlay = null;

  clusterer.clear();

  data.forEach(item => {
    const key = `${item.lstgLat}_${item.lstgLng}`;
    if (!groupedMap.has(key)) groupedMap.set(key, []);
    groupedMap.get(key).push(item);
  });

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

    const formatDeal = (item) => {
      const lease = item.lstgLease || 0;
      const leaseM = item.lstgLeaseM || 0;
      const leaseAmt = item.lstgLeaseAmt || 0;
      switch (item.lstgTypeSale) {
        case 1: return `전세 ${lease}만원`;
        case 2: return `보증금 ${leaseAmt} / 월 ${leaseM}만원`;
        case 3: return `매매 ${leaseAmt}만원`;
        default: return '정보 없음';
      }
    };

    const content = document.createElement('div');
    content.className = 'wrap';
    content.innerHTML = `
      <div class="info">
        <div class="title">${group[0].lstgNm || group[0].bldgNm} 외 ${group.length - 1}건
          <div class="close" style="cursor:pointer;"></div>
        </div>
        <div class="body">
          <div class="desc">
            ${group.map(item => `<div><strong>${item.lstgNm || item.bldgNm}</strong> - ${formatDeal(item)}</div>`).join('')}
          </div>
        </div>
      </div>
    `;

    const overlay = new kakao.maps.CustomOverlay({ content, position, yAnchor: 1 });
    content.querySelector('.close').addEventListener('click', () => overlay.setMap(null));
    kakao.maps.event.addListener(marker, 'click', () => {
      if (currentOverlay) currentOverlay.setMap(null);
      overlay.setMap(map);
      currentOverlay = overlay;
    });
  });

  clusterer.addMarkers(markers);
};
