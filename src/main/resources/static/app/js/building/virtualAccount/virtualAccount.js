
const bankCodeToName = {
  "88": "신한은행",
  "04": "국민은행",
  "20": "우리은행",
  "11": "NH농협"
};


const accountTypeToLabel = {
  "SUBSCRIPTION": "정기구독",
  "MONTHLY": "월세/관리비",
  "ONETIME": "단건결제"
};

// 리스트
async function fetchList() {
  const res = await fetch('/virtualAccount/list');
  const table = document.getElementById('accountTable');
  const empty = document.getElementById('emptyMessage');
  const tbody = document.getElementById('vaTableBody');

  if (res.ok) {
    const list = await res.json();

    if (!list || list.length === 0) {
      table.style.display = 'none';
      empty.style.display = 'block';
      return;
    }

    table.style.display = 'table';
    empty.style.display = 'none';

    tbody.innerHTML = list.map(va => `
      <tr>
        <td>${bankCodeToName[va.bankCode] || '-'}</td>
        <td>${va.accountNumber || '-'}</td>
        <td>${va.customerName || '-'}</td>
        <td>${va.virtualAccountAmount?.toLocaleString() || '-'}</td>
        <td>${va.dueDate || '-'}</td>
        <td>${accountTypeToLabel[va.accountType] || '-'}</td>
        <td>${va.settlementStatus || '-'}</td>
        <td>
          <button class="delete-btn" onclick="deleteVA('${va.virtualAccountId}')">삭제</button>
        </td>
      </tr>
    `).join('');
  } else {
    console.error("가상계좌 목록 불러오기 실패");
  }
}

// 삭제
function deleteVA(vaId) {
  if (confirm('정말 삭제하시겠습니까?')) {
    fetch(`/virtualAccount/delete/${vaId}`, {
      method: 'POST'
    }).then(() => fetchList());
  }
}

// 모달 오픈
function openModal() {
  document.getElementById('accountModal').style.display = 'flex';
}

// 모달 닫기
function closeModal() {
  document.getElementById('accountModal').style.display = 'none';
}

// 등록
document.addEventListener('DOMContentLoaded', () => {
  fetchList();

  const form = document.getElementById('vaForm');
  form.addEventListener('submit', async (e) => {
    e.preventDefault();
    const formData = new FormData(form);

    const res = await fetch('/virtualAccount/register', {
      method: 'POST',
      body: formData
    });

    if (res.ok) {
      closeModal();
      fetchList();
      form.reset();
    } else {
      alert("가상계좌 등록 실패");
    }
  });
});
