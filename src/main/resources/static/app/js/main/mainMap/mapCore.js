window.initMap = function () {
  const mapContainer = document.getElementById('map');
  const mapOption = {
    center: new kakao.maps.LatLng(36.324994522, 127.408980639),
    level: 3
  };
  return new kakao.maps.Map(mapContainer, mapOption);
};

window.initClusterer = function (map) {
  return new kakao.maps.MarkerClusterer({
    map: map,
    averageCenter: false,
    minLevel: 5,
    disableClickZoom: true
  });
};

window.setupMapControls = function (map) {
  const mapTypeControl = new kakao.maps.MapTypeControl();
  map.addControl(mapTypeControl, kakao.maps.ControlPosition.TOPRIGHT);

  const zoomControl = new kakao.maps.ZoomControl();
  map.addControl(zoomControl, kakao.maps.ControlPosition.RIGHT);
};
