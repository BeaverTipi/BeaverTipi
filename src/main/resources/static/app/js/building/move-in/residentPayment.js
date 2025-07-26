
// 📦 residentPayment.js - Toss 결제 성공 후 /ajax/payment/confirm 호출 처리

document.addEventListener('DOMContentLoaded', () => {
  const urlParams = new URLSearchParams(window.location.search);

  if (urlParams.get("success") === "true") {
    const paymentKey = urlParams.get("paymentKey");
    const orderId = urlParams.get("orderId");
    const amount = urlParams.get("amount");
    const unitId = document.querySelector('#unitSelect')?.value;
    const bldgId = localStorage.getItem("selectedBuildingId");

    if (!paymentKey || !orderId || !amount || !unitId || !bldgId) {
      console.error("❌ 결제 확인에 필요한 정보가 부족합니다.");
      return;
    }

    // chargeMonth 추출 예: "ORD202507251532000M123..." → 202507
    const chgbillChargeMonth = orderId.substring(3, 9);

    fetch("/ajax/payment/confirm", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        chgbillChargeMonth: chgbillChargeMonth,
        rentalPtyId: "",  // 🔧 필요한 경우 서버에서 조회하거나 hidden input으로 넘겨주세요
        unitId: unitId,
        bldgId: bldgId,
        amount: parseInt(amount),
        chgbillId: orderId,
        paymentKey: paymentKey,
        method: "TOSS",
        methodGrpCd: "CARD"
      })
    })
    .then(res => {
      if (!res.ok) throw new Error("서버 오류 또는 납부 실패");
      return res.json();
    })
    .then(data => {
      Swal.fire({
        icon: 'success',
        title: '납부 성공',
        text: data.message || "성공적으로 결제 처리되었습니다.",
        confirmButtonText: '확인'
      }).then(() => {
        location.href = "/resident/payment"; // ✅ 목록 새로고침
      });
    })
    .catch(err => {
      Swal.fire({
        icon: 'error',
        title: '납부 처리 실패',
        text: err.message || "서버 응답 처리 중 문제가 발생했습니다.",
        confirmButtonText: '확인'
      });
    });
  }
});
