// ✅ 토스 SDK 로딩 필요
// <script src="https://js.tosspayments.com/v1/payment"></script> 는 HTML에서 로딩되어야 함

document.addEventListener("DOMContentLoaded", () => {
	initSolutionSelection();
	initMethodSelection();
	updateSolutionSummary();
});

function initSolutionSelection() {
	const selected = document.querySelector("input[name='solutionCode']:checked");
	if (selected) {
		document.querySelectorAll(".solution-card").forEach(label => label.classList.remove("selected"));
		selected.closest("label").classList.add("selected");
	}
	document.querySelectorAll("input[name='solutionCode']").forEach(radio => {
		radio.addEventListener("change", () => {
			document.querySelectorAll(".solution-card").forEach(label => label.classList.remove("selected"));
			radio.closest("label").classList.add("selected");
			updateSolutionSummary();
		});
	});
}

function initMethodSelection() {
	const groups = ["billingMethod", "normalMethod"];
	groups.forEach(name => {
		const selected = document.querySelector(`input[name='${name}']:checked`);
		if (selected) {
			document.querySelectorAll(`input[name='${name}']`).forEach(input => {
				input.closest("label").classList.remove("selected");
			});
			selected.closest("label").classList.add("selected");
		}

		document.querySelectorAll(`input[name='${name}']`).forEach(radio => {
			radio.addEventListener("change", e => {
				const selected = e.target;
				document.querySelectorAll(`input[name='${name}']`).forEach(input => {
					input.closest("label").classList.remove("selected");
				});
				selected.closest("label").classList.add("selected");

				// 반대 그룹 초기화
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

function requestPayment(selectedPayment) {
	const selectedSolution = document.querySelector('input[name="solutionCode"]:checked');

	if (!selectedSolution || !selectedPayment) {
		Swal.fire("요금제와 결제 수단을 선택해주세요.", "", "warning");
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
		// 정기결제는 등록만 하고 끝
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

function openBillingModal() {
	const selectedSol = document.querySelector('input[name="solutionCode"]:checked');
	if (!selectedSol) {
		alert("요금제를 선택해주세요.");
		return;
	}

	const solId = selectedSol.value;

	axios.post("/ajax/toss/billing-ready", { solId })
		.then(res => {
			console.log(res.data);
			const { clientKey, customerKey, successUrl, customerName, customerEmail } = res.data;
			
			const tossPayments = TossPayments(clientKey);
			tossPayments.requestBillingAuth({
				customerKey,
				successUrl,
				failUrl: window.location.href + "?fail=true",
				method: "CARD",
				customerName,
				customerEmail
			});
		})
		.catch(err => {
			console.error(err);
			Swal.fire("실패", "정기결제 준비 중 오류가 발생했습니다.", "error");
		});
}
