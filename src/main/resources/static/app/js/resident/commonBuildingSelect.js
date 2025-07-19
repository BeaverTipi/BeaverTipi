function setupGlobalBuildingSelector({ param, storageKey, onChange }) {
  const urlParams = new URLSearchParams(window.location.search);
  let selectedBldgId = urlParams.get(param);

  const selector = document.querySelector(`select[name="${param}"]`);
  if (!selector) return;

  if (selectedBldgId) {
    localStorage.setItem(storageKey, selectedBldgId);
  }

  selectedBldgId = selectedBldgId || localStorage.getItem(storageKey);

  if (selectedBldgId) {
    selector.value = selectedBldgId;

    const alreadyLoaded = sessionStorage.getItem('alreadyLoaded') === 'true';
    if (!alreadyLoaded && typeof onChange === 'function') {
      onChange(selectedBldgId); // ✅ 초기 로딩
      sessionStorage.setItem('alreadyLoaded', 'true');
    }
  }

  selector.addEventListener('change', (e) => {
    const newVal = e.target.value;
    localStorage.setItem(storageKey, newVal);
    sessionStorage.removeItem('alreadyLoaded');

    const newUrl = new URL(location.href);
    newUrl.searchParams.set(param, newVal);
    window.history.pushState({}, '', newUrl); // ✅ 새로고침 없이 URL 변경
    if (typeof onChange === 'function') {
      onChange(newVal); // ✅ Ajax 로드
    }
  });
}

window.setupGlobalBuildingSelector = setupGlobalBuildingSelector;
