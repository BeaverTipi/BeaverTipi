/**
 * 
 */    
document.addEventListener("DOMContentLoaded",()=>{
	
	document.querySelector('#selectAll').addEventListener('change', function() {
      const checkboxes = document.querySelectorAll('.rowCheckbox');
      checkboxes.forEach(cb => cb.checked = this.checked);
    });
})

document.addEventListener('DOMContentLoaded', function () {
  console.log("DOM fully loaded and parsed"); // DOMContentLoaded가 실행된 후 로그

  // 단위 포맷 함수
  function formatUsage(type, value) {
    if (value == null) return '-';
    const unitMap = {
      elec: 'kWh',
      gas: '㎥',
      water: '㎥'
    };
    return `${value} ${unitMap[type] || ''}`;
  }

  function formatCurrency(value) {
    if (value == null) return '-';
    return new Intl.NumberFormat('ko-KR').format(value) + '원';
  }

  // 상세보기 버튼 이벤트
  document.querySelectorAll('.detail-btn').forEach(function (btn) {
    btn.addEventListener('click', function () {
      const month = this.dataset.month;
      const unitId = this.dataset.unitId; // 서버에서 단건으로 처리 중
      console.log(`Detail button clicked for month: ${month}, unitId: ${unitId}`);

      fetch(`/resident/payment/detail?unitId=${unitId}&chargeMonth=${month}`)
        .then(response => response.json())
        .then(data => {
          console.log('Data received:', data); // 데이터를 받았을 때 확인
          if (!data || data.length === 0) {
            console.log('No data received');
            return;
          }

          const bill = data[0]; // 단건이라 가정
          const modalBody = document.getElementById('modalBody');

          modalBody.innerHTML = `
            <h3>${month} 청구서 상세 내역</h3>
            <h4>📌 사용자 정보</h4>
            <ul>
              <li>지로번호: ${bill.chgbillId}</li>
              <li>건물명: ${bill.buildingName || '-'}</li>
              <li>임대인명: ${bill.landlordName || '-'}</li>
              <li>입주민명: ${bill.residentName || '-'}</li>
            </ul>

            <h4>💡 에너지 사용량</h4>
            <ul>
              <li>전기 사용량: ${formatUsage('elec', bill.elecUsage)}</li>
              <li>전기요금: ${formatCurrency(bill.elecFee)}</li>
              <li>가스 사용량: ${formatUsage('gas', bill.gasUsage)}</li>
              <li>가스요금: ${formatCurrency(bill.gasFee)}</li>
              <li>수도 사용량: ${formatUsage('water', bill.waterUsage)}</li>
              <li>수도요금: ${formatCurrency(bill.waterFee)}</li>
            </ul>

            <h4>🧾 공동 관리비 내역</h4>
            <ul>
              <li>총 관리비: ${formatCurrency(bill.sharedFee)}</li>
            </ul>

            <h4>📍 청구내역</h4>
            <ul>
              <li>청구금액: ${formatCurrency(bill.chgbillAmount)}</li>
              <li>납부상태: ${bill.chgbillStatus}</li>
              <li>납부마감일자: ${bill.chgbillDueDate}</li>
            </ul>
          `;
          console.log('Modal body updated'); // 모달 본문이 업데이트 됐는지 확인

          // 모달 표시
          document.getElementById('detailModal').style.display = 'block';
          console.log('Modal is displayed'); // 모달이 표시되는지 확인
        })
        .catch(err => {
          console.error('Error fetching detail:', err); // fetch 요청 오류 확인
        });
    });
  });

  // 모달 닫기
  document.querySelector('.modal .close').addEventListener('click', function () {
    console.log('Close button clicked');
    const modal = document.getElementById('detailModal');
    modal.style.opacity = '0'; // 부드럽게 사라짐
    setTimeout(() => {
      modal.style.display = 'none'; // 완전히 숨김
      console.log('Modal is hidden');
    }, 300); // 0.3초 후 숨김
  });
});


