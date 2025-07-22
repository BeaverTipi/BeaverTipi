// 공통 건물 선택자 + 페이지 이동 컨트롤
function setupGlobalBuildingSelector({ param, storageKey, onChange, pageParam = 'page', pageSize = 10 }) {
  const urlParams = new URLSearchParams(window.location.search);
  let selectedBldgId = urlParams.get(param);
  const selector = document.querySelector(`select[name="${param}"]`);
  if (!selector) return;

  if (selectedBldgId) localStorage.setItem(storageKey, selectedBldgId);
  selectedBldgId = selectedBldgId || localStorage.getItem(storageKey);

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
    newUrl.searchParams.set(pageParam, 1);
    window.history.pushState({}, '', newUrl);

    if (typeof onChange === 'function') {
      onChange(newVal, 1, pageSize);
    }
  });

  const prevPageButton = document.querySelector('.prev-page');
  const nextPageButton = document.querySelector('.next-page');

  function loadPage(pageNumber) {
    const currentBuildingId = selector.value;
    const newUrl = new URL(location.href);
    newUrl.searchParams.set(param, currentBuildingId);
    newUrl.searchParams.set(pageParam, pageNumber);
    window.history.pushState({}, '', newUrl);

    if (typeof onChange === 'function') {
      onChange(currentBuildingId, pageNumber, pageSize);
    }
  }

  if (prevPageButton) {
    prevPageButton.addEventListener('click', () => {
      const currentPage = parseInt(urlParams.get(pageParam) || 1);
      if (currentPage > 1) loadPage(currentPage - 1);
    });
  }

  if (nextPageButton) {
    nextPageButton.addEventListener('click', () => {
      const currentPage = parseInt(urlParams.get(pageParam) || 1);
      loadPage(currentPage + 1);
    });
  }
}
