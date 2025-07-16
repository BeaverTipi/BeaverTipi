function requestPayment(selectedPayment) {
	const selectedSolution = document.querySelector('input[name="solutionCode"]:checked');

	if (!selectedSolution || !selectedPayment) {
		Swal.fire({
			icon: 'warning',
			title: '요금제와 결제 수단을 선택해주세요.',
			confirmButtonText: '확인'
		});
		return;
	}

	const group = selectedPayment.dataset.group;
	const solName = selectedSolution.dataset.name;
	const solPrice = Number(selectedSolution.dataset.price);
	const currentUrl = window.location.href;

	if (group === "BILLING") {
		const billingKey = selectedPayment.dataset.billingKey;
		const customerKey = selectedPayment.dataset.customerKey;

		if (!billingKey || !customerKey) {
			Swal.fire("안내", "등록된 카드 정보가 없습니다.", "warning");
			return;
		}
		// 정기결제는 billing-key 등록 시 이미 Toss에서 처리됨
	} else {
		const solId = selectedSolution.value;
		const currentPath = window.location.pathname;
		const role = currentPath.split("/").pop().toUpperCase();
		axios.post("/ajax/toss/ready", { solId })
			.then(res => {
				const data = res.data;
				const tossPayments = TossPayments(data.clientKey);

				return tossPayments.requestPayment(selectedPayment.value, {
					amount: solPrice,
					orderId: data.orderId,
					orderName: solName,
					customerName: data.customerName,
					successUrl: data.successUrl + "?role=" + role,
					failUrl: currentUrl + "?fail=true"
				});
			})
			.catch(err => {
				console.error(err);
				Swal.fire("결제 실패", "일반결제 처리 중 오류가 발생했습니다.", "error");
			});
	}
}

function updateSolutionSummary() {
	const selected = document.querySelector("input[name='solutionCode']:checked");
	if (selected) {
		document.querySelector("#productNameText").textContent = selected.dataset.name || "";
		document.querySelector("#priceText").textContent = Number(selected.dataset.price || 0).toLocaleString();
	}
}

function handlePayment() {
	const selectedSolution = document.querySelector('input[name="solutionCode"]:checked');
	const selectedBilling = document.querySelector('input[name="billingMethod"]:checked');
	const selectedNormal = document.querySelector('input[name="normalMethod"]:checked');

	let selectedPayment = null;
	if (selectedBilling && selectedBilling.value.trim() !== '') {
		selectedPayment = selectedBilling;
	} else if (selectedNormal && selectedNormal.value.trim() !== '') {
		selectedPayment = selectedNormal;
	}

	if (!selectedSolution || selectedSolution.value.trim() === '') {
		Swal.fire("안내", "요금제를 선택해주세요.", "warning");
		return;
	}

	if (!selectedPayment || selectedPayment.value.trim() === '') {
		Swal.fire("안내", "결제 수단을 선택해주세요.", "warning");
		return;
	}

	const group = selectedPayment.dataset.group;

	if (group === "BILLING") {
		openBillingModal();
	} else if (group === "NORMAL") {
		requestPayment(selectedPayment);
	}
}

function openBillingModal() {
	const selectedSol = document.querySelector('input[name="solutionCode"]:checked');
	if (!selectedSol) {
		alert("요금제를 선택해주세요.");
		return;
	}

	const name = selectedSol.dataset.name || '';
	const price = selectedSol.dataset.price || '0';
	const cycle = selectedSol.dataset.cycle || '';

	const formattedPrice = Number(price).toLocaleString();

	document.querySelector("#modalSolName").textContent = name;
	document.querySelector("#modalSolPrice").textContent = `${formattedPrice} 원`;
	document.querySelector("#modalSolCycle").textContent = cycle;

	const modal = new bootstrap.Modal(document.querySelector("#billingModal"));
	modal.show();
}

document.addEventListener("DOMContentLoaded", () => {
	const defaultSelectedSolution = document.querySelector("input[name='solutionCode']:checked");
	if (defaultSelectedSolution) {
		document.querySelectorAll(".solution-card").forEach(label => label.classList.remove("selected"));
		defaultSelectedSolution.closest("label").classList.add("selected");
	}

	const groups = ["billingMethod", "normalMethod"];
	groups.forEach(name => {
		const selected = document.querySelector(`input[name='${name}']:checked`);
		if (selected) {
			document.querySelectorAll(`input[name='${name}']`).forEach(input => {
				input.closest("label").classList.remove("selected");
			});
			selected.closest("label").classList.add("selected");
		}
	});

	document.querySelectorAll("input[name='solutionCode']").forEach(radio => {
		radio.addEventListener("change", () => {
			document.querySelectorAll(".solution-card").forEach(label => label.classList.remove("selected"));
			radio.closest("label").classList.add("selected");
			updateSolutionSummary();
		});
	});

	updateSolutionSummary();

	groups.forEach(name => {
		document.querySelectorAll(`input[name='${name}']`).forEach(radio => {
			radio.addEventListener("change", e => {
				const selected = e.target;
				const group = selected.dataset.group;

				document.querySelectorAll(`input[name='${name}']`).forEach(input => {
					input.closest("label").classList.remove("selected");
				});
				selected.closest("label").classList.add("selected");

				const otherName = name === "billingMethod" ? "normalMethod" : "billingMethod";
				document.querySelectorAll(`input[name='${otherName}']`).forEach(input => {
					input.checked = false;
					input.closest("label").classList.remove("selected");
				});

				const noneOption = document.querySelector(`input[name='${otherName}'][value='']`);
				if (noneOption) {
					noneOption.checked = true;
					noneOption.closest("label").classList.add("selected");
				}
			});
		});
	});

	const billingForm = document.querySelector("#billingForm");
if (billingForm) {
  billingForm.addEventListener("submit", async (e) => {
    e.preventDefault();
    const formData = new FormData(billingForm);
    const cardData = Object.fromEntries(formData.entries());

    try {
      // 1. billingKey 발급
      const res = await axios.post("/ajax/toss/billing-key", cardData);
      const responseData = res.data;

      const billingKey = responseData.billingKey;
      const approvedAt = responseData.authenticatedAt;
      const card = responseData.card || {};
      const cardCompany = card.company;
      const cardNumber = card.number;
      const customerKey = responseData.customerKey;

      if (!billingKey) throw new Error("billingKey 발급 실패");
 console.log(responseData);
      // 2. 라디오 버튼에 값 반영
      const billingRadio = document.querySelector("input[name='billingMethod'][data-group='BILLING']");
      if (billingRadio) {
        billingRadio.dataset.billingKey = billingKey;
        billingRadio.dataset.customerKey = customerKey;
      }

      // 3. 모달 닫기
      const modal = bootstrap.Modal.getInstance(document.querySelector("#billingModal"));
      modal.hide();

      // 4. role 추출
      const currentPath = window.location.pathname;
      const role = currentPath.split("/").pop().toUpperCase();  // ex: /payment/business/broker → BROKER

      // 5. 서버에 저장 요청
      const result = await axios.post("/ajax/toss/billing-success", {
        billingKey,
        approvedAt,
        cardCompany,
        cardNumber,
        customerKey,
        role
      });

      // 6. 성공 처리
      let redirectUrl = "/account/read?success=true";
      if (result && result.data && result.data.redirectUrl) {
        redirectUrl = result.data.redirectUrl;
      }

      Swal.fire({
        icon: "success",
        title: "정기결제 완료",
        text: "카드 등록과 결제가 정상 처리되었습니다.",
        confirmButtonText: "확인"
      }).then(() => {
        window.location.href = redirectUrl;
      });

    } catch (err) {
      console.error(err);
      if (err && err.response && (err.response.status === 400 || err.response.status === 500)) {
        Swal.fire({
          icon: "warning",
          title: "결제 실패",
          html: "카드는 정상 등록되었으나<br>정기결제 처리 중 오류가 발생했습니다.",
          confirmButtonText: "확인"
        })
      } else {
        Swal.fire("등록 실패", "카드 등록 처리 중 오류가 발생했습니다.", "error");
      }
    }
  });
}

});
