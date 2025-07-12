document.addEventListener('DOMContentLoaded', () => {
  document.querySelectorAll('.giro-notice-card').forEach(card => {
    const unitId = card.dataset.unitId;
    const month  = card.dataset.month;

    fetch(`/resident/payment/detail?unitId=${unitId}&chargeMonth=${month}`)
      .then(res => res.json())
      .then(data => {
        if (!data || data.length === 0) return;

        const bill = data[0];
        const grouped = {};
        data.forEach(item => {
          grouped[item.energyTypeName] = {
            usage: item.energyUsageQty,
            fee: item.energyChargeAmount
          };
        });

        const receipt = card.querySelector('.giro-receipt');
        receipt.innerHTML = `
          <p>지로 번호: ${bill.chgbillId}</p>
          <p>대표입주자 명: ${bill.residentName}</p>
          <p>총 관리비: <b>${formatCurrency(bill.intgFeeAmount)}</b></p>
          <hr/>
          <b>에너지 사용량</b><br/>
          ${grouped['전기'] ? `- 전기: ${grouped['전기'].usage} kWh (${formatCurrency(grouped['전기'].fee)})<br/>` : ''}
          ${grouped['가스'] ? `- 가스: ${grouped['가스'].usage} ㎥ (${formatCurrency(grouped['가스'].fee)})<br/>` : ''}
          ${grouped['수도'] ? `- 수도: ${grouped['수도'].usage} ㎥ (${formatCurrency(grouped['수도'].fee)})<br/>` : ''}
        `;
      });
  });

  function formatCurrency(value) {
    if (value == null) return '-';
    return new Intl.NumberFormat('ko-KR').format(value) + '원';
  }
});