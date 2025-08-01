document.addEventListener('DOMContentLoaded', () => {
  const listingTableBody = document.getElementById('listingTableBody');
  if (!listingTableBody) return;

  listingTableBody.addEventListener('click', async (e) => {
    const link = e.target.closest('.building-name-link');
    if (!link) return;
    e.preventDefault();

    const tr = link.closest('tr');
    const openRow = listingTableBody.querySelector('.quick-detail-row');
    if (openRow) {
      if (tr.nextElementSibling === openRow) {
        openRow.remove();
        return;
      } else {
        openRow.remove();
      }
    }

    const lstgId = link.dataset.lstgId;
    const detailTr = document.createElement('tr');
    detailTr.className = 'quick-detail-row';
    const detailTd = document.createElement('td');
    detailTd.colSpan = tr.children.length;
    detailTd.innerHTML = '<div class="text-muted py-2">불러오는 중…</div>';
    detailTr.appendChild(detailTd);
    tr.after(detailTr);

    try {
      const html = await fetch(`/building/product/detail/quick/${lstgId}`).then(r => r.text());
      detailTd.innerHTML = html;
    } catch (err) {
      detailTd.innerHTML = '<div class="text-danger py-2">상세 정보를 불러오는 데 실패했습니다.</div>';
    }
  });
});
