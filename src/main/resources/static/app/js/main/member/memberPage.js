// ✅ 탭 전환
function openTab(tabId) {
	console.log("🔁 openTab 호출됨:", tabId);

	const buttons = document.querySelectorAll('.tab-button');
	const contents = document.querySelectorAll('.tab-content');

	buttons.forEach(btn => btn.classList.remove('active'));
	contents.forEach(cont => cont.classList.remove('active'));

	const button = document.querySelector(`[onclick="openTab('${tabId}')"]`);
	const content = document.getElementById(tabId);

	if (button) button.classList.add('active');
	if (content) content.classList.add('active');
}

// ✅ 구독 선택 후 이동
function storeSubscriptionInfoAndGo(userType, solId, subsId) {
	console.log("🧭 storeSubscriptionInfoAndGo 실행됨", { userType, solId, subsId });

	const subInfo = { solId, subsId };
	sessionStorage.setItem("selectedSubscription", JSON.stringify(subInfo));
	window.location.href = `/payment/business/${userType}`;
}

// ✅ 비정상적으로 남아있는 backdrop + 모달 숨김 처리
function fixBlockedPage() {
	const visibleModal = document.querySelector(".modal.show");
	const hasBackdrop = document.querySelector(".modal-backdrop");

	// ✅ 1. 모달이 보이지 않는데 backdrop만 남아있으면 제거
	if (!visibleModal && hasBackdrop) {
		console.warn("🧹 잘못 남은 modal backdrop 제거됨");
		hasBackdrop.remove();
		document.body.classList.remove("modal-open");
	}

	// ✅ 2. aria-hidden인데 포커스 가능한 모달들 inert 처리
	document.querySelectorAll('.modal[aria-hidden="true"]').forEach(modal => {
		modal.style.display = "none";
		modal.setAttribute("inert", ""); // focus 차단
		modal.classList.remove("show");
	});
}
document.addEventListener("hidden.bs.modal", () => {
	console.log("✅ Bootstrap 모달 완전히 닫힘 → 클래스 정리");
	document.body.classList.remove("modal-open");
	document.querySelectorAll(".modal-backdrop").forEach(b => b.remove());
});

// ✅ DOM 로드 시 실행
document.addEventListener("DOMContentLoaded", () => {
	console.log("✅ memberPage.js 시작됨");

	fixBlockedPage(); // 👉 백드롭, 포커스 문제 제거

	// 👉 탭 초기화
	const urlParams = new URLSearchParams(window.location.search);
	if (urlParams.get("success") === "true") {
		sessionStorage.removeItem("attemptedSolId");
	}

	const wrapper = document.querySelector(".register-wrapper");
	const defaultTabId = wrapper?.dataset?.defaultTab;
	if (defaultTabId) {
		openTab(defaultTabId);
	}

	// 👉 솔루션 클릭 시 solId 저장
	document.querySelectorAll("form[action^='/payment/business']").forEach((form, idx) => {
		const button = form.querySelector("button[type='submit']");
		const solIdInput = form.querySelector("input[name='solId']");

		if (button && solIdInput) {
			button.addEventListener("click", () => {
				const solId = solIdInput.value;
				if (solId) {
					sessionStorage.setItem("attemptedSolId", solId);
				}
			});
		}
	});

	/*
	// 👉 모달 이벤트 연결 (필요 시 복원)
	const passwordModalElement = document.querySelector('#passwordModal');
	if (passwordModalElement) {
		passwordModalElement.addEventListener('shown.bs.modal', onModalShown);
	}
	*/
	
		document.querySelector('#confirmPasswordBtn').addEventListener('click', () => {
		const input = document.querySelector('#passwordCheckInput');
		const error = document.querySelector('#passwordCheckError');
		const password = input.value.trim();

		if (!password) {
			error.textContent = "비밀번호를 입력하세요.";
			error.classList.remove("d-none");
			return;
		}

		axios.post("/ajax/member/check-password", { password })
			.then((res) => {
				if (res.data.success) {
					sessionStorage.setItem("passwordVerified",true);
					window.location.href = "/account/update"; // ← 성공 시 이동
				} else {
					error.textContent = "비밀번호가 일치하지 않습니다.";
					error.classList.remove("d-none");
				}
			})
			.catch((err) => {
				console.error("비밀번호 확인 실패:", err);
				error.textContent = "서버 오류가 발생했습니다.";
				error.classList.remove("d-none");
			});
	});
});

// ✅ 모달 열기 함수 추가 (수정 버튼에서 호출)
function showPasswordModal() {
	const passwordModal = new bootstrap.Modal(document.querySelector('#passwordModal'));
	passwordModal.show();
}
document.addEventListener("shown.bs.modal", function (e) {
  const modal = e.target;
  modal.removeAttribute("inert");
  modal.removeAttribute("aria-hidden"); // 같이 제거
  modal.style.pointerEvents = "auto";
  console.log("✅ inert 제거 완료:", modal.id);
});
