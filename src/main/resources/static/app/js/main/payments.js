/**
 * 
 */
function requestPayment() {
	const tossPayments = TossPayments("test_ck_DLJOpm5QrlxRJLBQ0xqLrPNdxbWn"); // 토스에서 받은 clientKey로 교체
	const solId = document.querySelector('input[name="solution"]:checked').value.trim();
	const currentUrl = window.location.href;
const payMethod = document.querySelector('input[name="paymentMethod"]:checked');
	const codeGroup = payMethod.dataset.group;
	if (!solId) {
		Swal.fire({
			icon: 'warning',
			title: '알림',
			text: '요금제를 선택해주세요.',
			confirmButtonText: '확인'
		})
		return;
	}
	// 서버에서 주문번호, 금액, 고객 정보 등 결제 정보를 받아옵니다
	fetch("/ajax/payment/ready", {
		method: "POST",
		headers: {
			"Content-Type": "application/json"
		},
		body: JSON.stringify({
			solId: solId
		})
	})
		.then(response => response.json())
		.then(data => {
			return tossPayments.requestPayment(payMethod.value.trim(), {
				amount: data.amount,
				orderId: data.orderId,
				orderName: data.orderName,
				customerName: data.customerName,
				successUrl: data.successUrl,
				failUrl: currentUrl + "?fail=true"
			});
		})
		.then(data =>{
			console.log("payments" , data)
		})
		.catch(err => {
			Swal.fire({
			icon: 'error',
			title: '결제 요청 중 오류 발생',
			text: err.message,
			confirmButtonText: '확인'
		})
		});
}