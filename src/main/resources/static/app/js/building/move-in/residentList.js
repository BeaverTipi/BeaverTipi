document.addEventListener('DOMContentLoaded', () => {
	
	console.log("✅ residentList.js 실행됨");
	
	//  건물 선택 셀렉터
	const selector = document.querySelector('select[name="bldgIdParam"]');
	const savedBldgId = localStorage.getItem("selectedBuildingId");
	
	//  첫 진입 시 unitIdParam 없으면 자동 리다이렉트
	if (!urlParams.get("unitIdParam") && savedBldgId) {
	  axios.get("/ajax/resident/api/units", {
	    params: { bldgId: savedBldgId }
	  })
	  .then(res => {
	    const unitList = res.data;
	    if (unitList.length > 0) {
	      const firstUnitId = unitList[0].unitId;
	      const newUrl = new URL(window.location.origin + "/resident/payment");
	      newUrl.searchParams.set("bldgIdParam", savedBldgId);
	      newUrl.searchParams.set("unitIdParam", firstUnitId);
	      newUrl.searchParams.set("autoredirect", "true");
	      window.location.href = newUrl.toString();
	    }
	  })
	  .catch(err => {
	    console.error("초기 유닛 자동 조회 실패:", err);
	  });
	}
	
	// 납부 버튼 이벤트 등록
	const payButton = document.querySelector('.pay-button');
	if (payButton) {
		payButton.addEventListener('click', requestPayment);
	}

	// 초기 로드 시 로컬에 저장된 건물 아이디가 있으면 select에 반영
	if (selector && savedBldgId) {
		selector.value = savedBldgId;
	}
	if (selector) {
	  selector.addEventListener("change", () => {
	    const selectedBldgId = selector.value;
	    localStorage.setItem("selectedBuildingId", selectedBldgId);
	    console.log("🔧 건물 선택 변경됨:", selectedBldgId);
	
	    //  새 건물에 대한 unitId 조회
	    axios.get("/ajax/resident/api/units", {
	      params: { bldgId: selectedBldgId }
	    })
	    .then(res => {
	      const unitList = res.data;
	      if (unitList.length > 0) {
	        const newUnitId = unitList[0].unitId;
	
	        //  URL 수동 구성 후 이동
	        const newUrl = new URL(window.location.origin + "/resident/payment");
	        newUrl.searchParams.set("bldgIdParam", selectedBldgId);
	        newUrl.searchParams.set("unitIdParam", newUnitId);
	        newUrl.searchParams.set("autoredirect", "true");
	
	        window.location.href = newUrl.toString();
	      } else {
	        Swal.fire({
	          icon: 'warning',
	          title: '호수가 없습니다',
	          text: '선택한 건물에 등록된 호수가 없습니다.'
	        });
	      }
	    })
	    .catch(err => {
	      console.error("건물 변경 시 유닛 조회 실패:", err);
	      Swal.fire({
	        icon: 'error',
	        title: '호수 조회 실패',
	        text: '호수 데이터를 가져오는 중 문제가 발생했습니다.'
	      });
	    });
	  });
	}
function requestPayment() {
	const tossPayments = TossPayments("test_ck_DLJOpm5QrlxRJLBQ0xqLrPNdxbWn"); // 실제 clientKey로 교체
	const payButton = document.querySelector('.pay-button');
	const payMethodRadio = document.querySelector('input[name="payment_method"]:checked');

	const payMethod = payMethodRadio?.value;
	const orderName = payButton?.dataset.name?.trim();
	const amountStr = payButton?.dataset.pay?.trim();
	const amount = parseInt(amountStr, 10);
	const unitId = document.querySelector("#unitSelect")?.value;
	const bldgId = localStorage.getItem("selectedBuildingId");
	console.log("payMethod : ",payMethod);
	console.log("amount : " ,amount);
	console.log("unitId : ",unitId);
	console.log("bldgId : ",bldgId);
	if (!payMethod || isNaN(amount) || !unitId || !bldgId) {
		Swal.fire({
			icon: 'warning',
			title: '결제 불가',
			text: '결제 정보가 누락되었습니다.',
			confirmButtonText: '확인'
		});
		return;
	}

	// 1단계: Toss 결제 요청
	fetch("/ajax/resident/payment/ready", {
		method: "POST",
		headers: { "Content-Type": "application/json" },
		body: JSON.stringify({
			orderName: orderName,
			amount: amount
		})
	})
		.then(res => res.json())
		.then(data => {

			const current = window.location.href;
			const chargeMonth = payButton?.dataset.chargeMonth;
			let successUrl = data.successUrl;
			if (successUrl.includes("?")) {
				successUrl += `&success=true&unitId=${unitId}&chgbillChargeMonth=${chargeMonth}&current=${current}`;
			} else {
				successUrl += `?success=true&unitId=${unitId}&chgbillChargeMonth=${chargeMonth}&current=${current}`;
			}
			// Toss 결제 요청
			return tossPayments.requestPayment(payMethod, {
				amount: data.amount,
				orderId: data.orderId,
				orderName: data.orderName,
				customerName: data.customerName,
				successUrl: successUrl,
				failUrl: window.location.href + "?fail=true"
			});
		})
		.catch(err => {
			Swal.fire({
				icon: 'error',
				title: '에러',
				text: err.message || "결제 또는 납부 처리 중 오류가 발생했습니다.",
				confirmButtonText: '확인'
			});
		});
}
});