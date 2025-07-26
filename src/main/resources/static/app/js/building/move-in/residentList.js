

document.addEventListener('DOMContentLoaded', () => {
const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.get("success") === "true") {
    Swal.fire({
      icon: 'success',
      title: '결제가 완료되었습니다!',
      text: '이용해 주셔서 감사합니다.',
      confirmButtonText: '확인'
    })
  }
  console.log("✅ residentList.js 실행됨");

  // 🏢 건물 선택 셀렉터
  const selector = document.querySelector('select[name="bldgIdParam"]');
  const savedBldgId = localStorage.getItem("selectedBuildingId");

  // 🔄 납부 버튼 이벤트 등록
  const payButton = document.querySelector('.pay-button');
  if (payButton) {
    payButton.addEventListener('click', requestPayment);
  }

  // 초기 로드 시 로컬에 저장된 건물 아이디가 있으면 select에 반영
  if (selector && savedBldgId) {
    selector.value = savedBldgId;
  }

  // 건물 선택 변경 시 로컬 저장 후 폼 제출
  if (selector) {
    selector.addEventListener("change", () => {
      console.log("🔧 건물 선택 변경됨:", selector.value);
      localStorage.setItem("selectedBuildingId", selector.value);
      const form = selector.closest("form");
      if (form) form.submit();
    });
  }
});

function requestPayment() {
  const tossPayments = TossPayments("test_ck_DLJOpm5QrlxRJLBQ0xqLrPNdxbWn"); // 실제 clientKey로 교체
  const payButton = document.querySelector('.pay-button');
  const payMethodRadio = document.querySelector('input[name="payment_method"]:checked');

  const payMethod = payMethodRadio ? payMethodRadio.value : null;
  const orderName = payButton?.dataset.name?.trim();
  const amountStr = payButton?.dataset.pay?.trim();
  const amount = parseInt(amountStr, 10);
  const currentUrl = window.location.href;

  const selectedBldgId = localStorage.getItem("selectedBuildingId");

  if (!selectedBldgId) {
    Swal.fire({
      icon: 'warning',
      title: '알림',
      text: '세대 정보가 없습니다. 다시 시도해주세요.',
      confirmButtonText: '확인'
    });
    return;
  }

  if (!payMethod) {
    Swal.fire({
      icon: 'warning',
      title: '알림',
      text: '결제 방식을 선택해주세요.',
      confirmButtonText: '확인'
    });
    return;
  }

  if (isNaN(amount)) {
    Swal.fire({
      icon: 'error',
      title: '오류',
      text: '결제 금액이 잘못되었습니다.',
      confirmButtonText: '확인'
    });
    return;
  }

  fetch("/ajax/payment/resident", {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      orderName: orderName,
      amount: amount
    })
  })
  .then(res => res.json())
  .then(data => {
    return tossPayments.requestPayment(payMethod, {
      amount: data.amount,
      orderId: data.orderId,
      orderName: data.orderName,
      customerName: data.customerName,
      successUrl: data.successUrl +"?success=true",
      failUrl: currentUrl + "?fail=true"
    });
  })
  .then(result => {
    console.log("✅ 결제 완료 후 반환값:", result);
  })
  .catch(err => {
    Swal.fire({
      icon: 'error',
      title: '결제 실패',
      text: err.message,
      confirmButtonText: '확인'
    });
  });
}
