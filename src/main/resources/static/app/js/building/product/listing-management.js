document.addEventListener('DOMContentLoaded', () => {
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
