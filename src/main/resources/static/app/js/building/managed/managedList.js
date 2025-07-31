document.addEventListener('DOMContentLoaded', () => {
	const resetBtn = document.querySelector('#resetBtn');
	const tableBody = document.querySelector('#buildingTableBody')
	// 초기화 버튼
	resetBtn.addEventListener('click', () => {
		const form = resetBtn.closest('form');
		form.querySelectorAll('input[type="text"], input[type="number"], select').forEach(el => {
			el.value = '';
		});
		form.querySelector('input[name="page"]').value = "1";
		form.submit();
	});

	// 호수 리스트 토글
	tableBody.addEventListener('click', async (e) => {
		if (!e.target.classList.contains('building-infom-link')) return;
		e.preventDefault();

		const row = e.target.closest('tr');
		const address = row.dataset.address;
		const safeAddress = address.replace(/[^\w]/g, '').slice(0, 40);
		const detailRowId = `detail-${safeAddress}`;
		const existingDetailRow = document.getElementById(detailRowId);

		if (existingDetailRow) {
			existingDetailRow.remove();
			return;
		}

		const detailRow = document.createElement('tr');
		detailRow.setAttribute('id', detailRowId);

		const td = document.createElement('td');
		td.setAttribute('colspan', '8');
		td.className = 'bg-light border-top border-bottom text-secondary';
		td.innerHTML = '<div class="py-2">불러오는 중...</div>';

		detailRow.appendChild(td);
		row.insertAdjacentElement('afterend', detailRow);

		try {
			const response = await axios.post(`/ajax/building/listing/rooms`, { address });
			const list = response.data;

			if (Array.isArray(list) && list.length > 0) {
				td.classList.remove('text-secondary');
				td.innerHTML = list.map(room => `
          <div class="mb-2 px-3 py-2 border rounded bg-white shadow-sm">
            <div><strong>호수:</strong> ${room.lstgRoomNum || '-'}</div>
            <div>
              <strong>전세금:</strong> ${room.lstgLease || '-'} &nbsp; 
              <strong>월세:</strong> ${room.lstgLeaseM || '-'} &nbsp; 
              <strong>보증금:</strong> ${room.lstgLeaseAmt || '-'}
            </div>
          </div>
        `).join('');
			} else {
				td.innerHTML = '<div class="py-2 text-muted">해당 주소의 다른 호수가 없습니다.</div>';
			}
		} catch (err) {
			console.error(err);
			td.innerHTML = '<div class="py-2 text-danger">호수 정보를 불러오는 중 오류가 발생했습니다.</div>';
		}
	});
});
