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

		axios.post("/ajax/toss/billing", {
			customerKey: customerKey,
			billingKey: billingKey,
			amount: solPrice,
			orderId: "ORD-" + Date.now(),
			orderName: solName
		})
			.then(() => {
				Swal.fire("정기결제 완료", "결제가 정상 처리되었습니다.", "success");
			})
			.catch(err => {
				console.error(err);
				Swal.fire("결제 실패", "정기결제 처리 중 오류가 발생했습니다.", "error");
			});
	} else {
		const solId = selectedSolution.value;

		axios.post("/ajax/toss/ready", { solId })
			.then(res => {
				const data = res.data;
				const tossPayments = TossPayments(data.clientKey);

				return tossPayments.requestPayment(selectedPayment.value, {
					amount: solPrice,
					orderId: data.orderId,
					orderName: solName,
					customerName: data.customerName,
					successUrl: data.successUrl,
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
		openBillingModal(); // 모달 텍스트 설정 + 모달 오픈
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
	// ✅ 솔루션 초기 selected
	const defaultSelectedSolution = document.querySelector("input[name='solutionCode']:checked");
	if (defaultSelectedSolution) {
		document.querySelectorAll(".solution-card").forEach(label => label.classList.remove("selected"));
		defaultSelectedSolution.closest("label").classList.add("selected");
	}

	// ✅ 결제수단 초기 selected
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

	// ✅ 솔루션 선택
	document.querySelectorAll("input[name='solutionCode']").forEach(radio => {
		radio.addEventListener("change", () => {
			document.querySelectorAll(".solution-card").forEach(label => label.classList.remove("selected"));
			radio.closest("label").classList.add("selected");
			updateSolutionSummary();
		});
	});

	updateSolutionSummary();

	// ✅ 결제 수단 선택
	groups.forEach(name => {
		document.querySelectorAll(`input[name='${name}']`).forEach(radio => {
			radio.addEventListener("change", e => {
				const selected = e.target;
				const group = selected.dataset.group;

				// 현재 선택 라벨 selected 처리
				document.querySelectorAll(`input[name='${name}']`).forEach(input => {
					input.closest("label").classList.remove("selected");
				});
				selected.closest("label").classList.add("selected");

				// ❗ 반대 그룹 비활성화 처리
				const otherName = name === "billingMethod" ? "normalMethod" : "billingMethod";
				document.querySelectorAll(`input[name='${otherName}']`).forEach(input => {
					input.checked = false;
					input.closest("label").classList.remove("selected");
				});

				// ❗ 반대 그룹에서 "선택 안 함" 자동 선택
				const noneOption = document.querySelector(`input[name='${otherName}'][value='']`);
				if (noneOption) {
					noneOption.checked = true;
					noneOption.closest("label").classList.add("selected");
				}
			});
		});
	});

// ✅ 카드 등록
const billingForm = document.querySelector("#billingForm");
if (billingForm) {
	billingForm.addEventListener("submit", async (e) => {
		e.preventDefault();
		const formData = new FormData(billingForm);
		const cardData = Object.fromEntries(formData.entries());

		try {
			const res = await axios.post("/ajax/toss/billing-key", cardData);
			const responseData = res.data;

			const billingKey = responseData.billingKey;
			const customerKey = cardData.customerKey;

			if (!billingKey) throw new Error("billingKey 발급 실패");

			const billingRadio = document.querySelector("input[name='billingMethod'][data-group='BILLING']");
			if (billingRadio) {
				billingRadio.dataset.billingKey = billingKey;
				billingRadio.dataset.customerKey = customerKey;
			}

			const modal = bootstrap.Modal.getInstance(document.querySelector("#billingModal"));
			modal.hide();

			// ✅ 카드 등록 성공 후 즉시 정기결제 요청
			const selectedSol = document.querySelector('input[name="solutionCode"]:checked');
			const solName = selectedSol.dataset.name;
			const solPrice = Number(selectedSol.dataset.price);

			await axios.post("/ajax/toss/billing", {
				customerKey,
				billingKey,
				amount: solPrice,
				orderId: "ORD-" + Date.now(),
				orderName: solName
			});

			// ✅ 결제 성공 안내 및 리다이렉트
			Swal.fire({
				icon: "success",
				title: "정기결제 완료",
				text: "카드 등록과 결제가 정상 처리되었습니다.",
				confirmButtonText: "확인"
			}).then(() => {
				window.location.href = "/account/read?success=true";
			});

		} catch (err) {
			console.error(err);

			// ✅ 카드 등록은 되었지만 결제 실패
			if (err?.response?.status === 400 || err?.response?.status === 500) {
				Swal.fire({
					icon: "warning",
					title: "결제 실패",
					html: "카드는 정상 등록되었으나<br>정기결제 처리 중 오류가 발생했습니다.",
					confirmButtonText: "확인"
				}).then(() => {
					window.location.href = "/account/read?fail=true";
				});
			} else {
				// ✅ 카드 등록 실패
				Swal.fire("등록 실패", "카드 등록 처리 중 오류가 발생했습니다.", "error");
			}
		}
	});
}

});
