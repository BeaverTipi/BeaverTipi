document.addEventListener('DOMContentLoaded', function () {
  function setupGlobalBuildingSelector({ param, storageKey, onChange, pageParam = 'page', pageSize = 10 }) {
    const urlParams = new URLSearchParams(window.location.search);
    let selectedBldgId = urlParams.get(param);

    const selector = document.querySelector(`select[name="${param}"]`);
    if (!selector) {
      console.error(`선택한 ${param} 요소를 찾을 수 없습니다.`);
      return;
    }

    if (selectedBldgId) {
      localStorage.setItem(storageKey, selectedBldgId);
    }

    selectedBldgId = selectedBldgId || localStorage.getItem(storageKey);

    console.log("선택된 건물 ID:", selectedBldgId);
    const currentPage = 1;

    if (selectedBldgId) {
      selector.value = selectedBldgId;

      const alreadyLoaded = sessionStorage.getItem('alreadyLoaded') === 'true';
      if (!alreadyLoaded && typeof onChange === 'function') {
        onChange(selectedBldgId, currentPage, pageSize);
        sessionStorage.setItem('alreadyLoaded', 'true');
      }
    }

    selector.addEventListener('change', (e) => {
      const newVal = e.target.value;
      localStorage.setItem(storageKey, newVal);
      sessionStorage.removeItem('alreadyLoaded');

      const newUrl = new URL(location.href);
      newUrl.searchParams.set(param, newVal);
      newUrl.searchParams.set(pageParam, 1); // 페이지 번호를 1로 설정하여 URL 업데이트
      window.history.pushState({}, '', newUrl);

      console.log("건물 변경됨:", newVal);

      if (typeof onChange === 'function') {
        onChange(newVal, 1, pageSize); // 첫 번째 페이지로 로드
      }
    });

    // 페이지 이동 함수들
    function loadPage(pageNumber) {
      const currentBuildingId = selector.value;
      const newUrl = new URL(location.href);
      newUrl.searchParams.set(param, currentBuildingId);
      newUrl.searchParams.set(pageParam, pageNumber); // 페이지 번호 업데이트
      window.history.pushState({}, '', newUrl);

      console.log("페이지 로딩 중:", pageNumber, "건물:", currentBuildingId);

      if (typeof onChange === 'function') {
        onChange(currentBuildingId, pageNumber, pageSize); // 페이지 이동 시 데이터 로드
      }
    }

    // prev-page, next-page 요소가 존재하는지 확인 후 이벤트 리스너 추가
    const prevPageButton = document.querySelector('.prev-page');
    const nextPageButton = document.querySelector('.next-page');

    if (prevPageButton) {
      prevPageButton.addEventListener('click', () => {
        const currentPage = parseInt(urlParams.get(pageParam) || 1);
        if (currentPage > 1) {
          loadPage(currentPage - 1);
        }
      });
    }

    if (nextPageButton) {
      nextPageButton.addEventListener('click', () => {
        const currentPage = parseInt(urlParams.get(pageParam) || 1);
        loadPage(currentPage + 1);
      });
    }
  }

  // setupGlobalBuildingSelector 함수 호출
  setupGlobalBuildingSelector({
    param: 'bldgIdParam',
    storageKey: 'selectedBuildingId',
    onChange: loadPosts
  });
});
