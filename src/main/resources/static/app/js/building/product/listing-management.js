// 탭 상태 전환 함수
function showTab(tabNum) {
  const tab1Panel = document.querySelector("#tab1Panel");
  const tab2Panel = document.querySelector("#tab2Panel");
  const tab1Btn = document.querySelector("#tab1Btn");
  const tab2Btn = document.querySelector("#tab2Btn");

  if (!tab1Panel || !tab2Panel || !tab1Btn || !tab2Btn) return;

  tab1Panel.style.display = tabNum === 1 ? "" : "none";
  tab2Panel.style.display = tabNum === 2 ? "" : "none";

  tab1Btn.classList.toggle("active", tabNum === 1);
  tab2Btn.classList.toggle("active", tabNum === 2);

  // 폼에 hidden input 있을 경우 값 설정
  const tabInputs = document.querySelectorAll("input[name='activeTab']");
  tabInputs.forEach(input => input.value = tabNum);
}

// 초기 로딩 시 탭 유지
document.addEventListener("DOMContentLoaded", function () {
  const params = new URLSearchParams(location.search);
  const activeTab = parseInt(params.get("activeTab")) || 1;
  showTab(activeTab);

  document.querySelector("#tab1Btn")?.addEventListener("click", () => showTab(1));
  document.querySelector("#tab2Btn")?.addEventListener("click", () => showTab(2));

  // === 초기화 버튼 ===
  const resetBtn = document.querySelector('#resetBtn');
  if (resetBtn) {
    resetBtn.addEventListener('click', () => {
      const form = resetBtn.closest('form');
      form.querySelectorAll('input[type="text"], input[type="number"], select').forEach(el => {
        el.value = '';
      });
      form.querySelector('input[name="page"]').value = "1";
      form.submit();
    });
  }
  
  const  resetBuildingBtn = document.querySelector('#resetBuildingBtn');
  if (resetBuildingBtn) {
    resetBuildingBtn.addEventListener('click', () => {
      const form = resetBtn.closest('form');
      form.querySelectorAll('input[type="text"], input[type="number"], select').forEach(el => {
        el.value = '';
      });
      form.querySelector('input[name="page"]').value = "1";
      form.submit();
    });
  }
  // === 매물 퀵뷰(상세 JSP) ===
  const listingTableBody = document.getElementById('listingTableBody');
  if (listingTableBody) {
    listingTableBody.addEventListener('click', async (e) => {
      const link = e.target.closest('.building-name-link');
      if (!link) return;
      e.preventDefault();

      const row = link.closest('tr');
      const lstgId = link.dataset.lstgId;
      const detailRowId = `listing-detail-${lstgId}`;
      const existingDetailRow = document.getElementById(detailRowId);

      // 이미 열려있으면 닫기
      if (existingDetailRow) {
        existingDetailRow.remove();
        return;
      }

      // 단일 오픈: 열려있는 퀵뷰 닫기
      listingTableBody.querySelectorAll('.listing-quick-detail-row').forEach(r => r.remove());

      // 새 퀵뷰 row 삽입
      const detailRow = document.createElement('tr');
      detailRow.setAttribute('id', detailRowId);
      detailRow.className = 'listing-quick-detail-row';

      const detailTd = document.createElement('td');
      detailTd.colSpan = row.children.length;
      detailTd.innerHTML = '<div class="text-muted py-2">불러오는 중…</div>';
      detailRow.appendChild(detailTd);
      row.insertAdjacentElement('afterend', detailRow);

      try {
        const html = await fetch(`/building/product/detail/quick/${lstgId}`).then(r => r.text());
        detailTd.innerHTML = html;
      } catch (err) {
        console.error(err);
        detailTd.innerHTML = '<div class="text-danger py-2">상세 정보를 불러오는 데 실패했습니다.</div>';
      }
    });
  }

  // === 건물 퀵뷰(상세 JSP) ===
  const buildingTableBody = document.getElementById('buildingTableBody');
  if (buildingTableBody) {
    buildingTableBody.addEventListener('click', async (e) => {
      const link = e.target.closest('.building-detail-toggle');
      if (!link) return;
      e.preventDefault();

      const tr = link.closest('tr');
      const openRow = buildingTableBody.querySelector('.quick-detail-row');
      if (openRow) {
        if (tr.nextElementSibling === openRow) {
          openRow.remove();
          return;
        } else {
          openRow.remove();
        }
      }

      const bldgId = link.dataset.bldgId;
      const detailTr = document.createElement('tr');
      detailTr.className = 'quick-detail-row';
      const detailTd = document.createElement('td');
      detailTd.colSpan = 6;
      detailTd.innerHTML = '<div class="text-muted py-2">불러오는 중…</div>';
      detailTr.appendChild(detailTd);
      tr.after(detailTr);

      try {
        const html = await fetch(`/building/managed/detail/quick/${bldgId}`).then(r => r.text());
        detailTd.innerHTML = html;
      } catch (err) {
        console.error(err);
        detailTd.innerHTML = '<div class="text-danger py-2">상세 정보를 불러오는 데 실패했습니다.</div>';
      }
    });
  }
});
