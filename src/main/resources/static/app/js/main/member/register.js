document.addEventListener("DOMContentLoaded", () => {
	const input = document.querySelector("#mbrTelno");
	const iti = window.intlTelInput(input, {
		initialCountry: "kr",
		preferredCountries: ["kr", "us", "jp"],
		utilsScript: "https://cdn.jsdelivr.net/npm/intl-tel-input@17.0.19/build/js/utils.js"
	});

	let verified = false;
	let failCount = 0;
	let timer = null;
	let remainingTime = 300;
	let resendReady = false;

	const authBtn = document.querySelector(".btn-auth-code");
	const authCodeWrapper = document.querySelector("#authCodeWrapper");
	const authCodeInput = document.querySelector("#authCode");
	const resetBtn = document.querySelector("#resetBtn");
	const signupForm = document.querySelector("#signup-form");
	const timerText = document.querySelector("#timerText");
	const phoneInput = document.querySelector("#phoneInput");
	const checkAuthCodeBtn = document.querySelector("#checkAuthCodeBtn");
	const fillDummyBtn = document.querySelector("#fillDummyBtn");

	resetBtn?.addEventListener("click", () => {
		history.back();
	});

	fillDummyBtn?.addEventListener("click", () => {
		document.querySelector("#mbrId").value = "dummyUser01";
		document.querySelector("#mbrPw").value = "qwer";
		document.querySelector("input[name='mbrNm']").value = "누구로하냐";

		const phoneInput = document.querySelector("#mbrTelno");
		const itiInstance = window.intlTelInputGlobals.getInstance(phoneInput);
		itiInstance.setNumber("+821089526419");

		toggleOptionalInfo();

		document.querySelector("input[name='mbrNnm']").value = "집구해야징";
		document.querySelector("input[name='mbrEmlAddr']").value = "kkk90327@naver.com";
		document.querySelector("input[name='mbrZip']").value = "34823";
		document.querySelector("input[name='mbrBasicAddr']").value = "대전 중구 계룡로765번길 16";
		document.querySelector("input[name='mbrDetailAddr']").value = "302호 (프라임빌)";
	});

	authBtn.addEventListener("click", (e) => {
		e.preventDefault();

		if (authBtn.disabled) return;

		if (!iti.isValidNumber()) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: "유효한 전화번호를 입력해주세요.",
				confirmButtonText: '확인'
			});
			phoneInput.focus();
			return;
		}

		let rawPhone = iti.getNumber();
		let phone = rawPhone.replace(/^\+82/, "0").replace(/-/g, "");

		if (!phone) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: "전화번호를 입력하세요.",
				confirmButtonText: '확인'
			});
			return;
		}

		authCodeWrapper?.classList.remove("hidden");

		authBtn.disabled = true;
		authBtn.textContent = "인증요청";

		fetch("/ajax/member/verification", {
			method: "POST",
			headers: { "Content-Type": "application/x-www-form-urlencoded" },
			body: new URLSearchParams({ phone })
		})
			.then(res => res.json())
			.then(data => {
				if (data.success) {
					Swal.fire({
						icon: 'info',
						title: '알림',
						text: "인증번호가 전송되었습니다.",
						confirmButtonText: '확인'
					});
					authCodeInput.disabled = false;
					authCodeInput.value = "";
					authCodeWrapper.classList.remove("hidden");
					verified = false;
					failCount = 0;
					startTimer();
				} else {
					alert(data.message || "전송 실패");
					authBtn.disabled = false;
				}
			})
			.catch(err => {
				console.error(err);
				authBtn.disabled = false;
			});
	});

	checkAuthCodeBtn?.addEventListener("click", () => {
		const inputCode = authCodeInput.value.trim();

		if (authCodeInput.disabled || !inputCode) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: "인증번호를 입력하세요.",
				confirmButtonText: '확인'
			});
			return;
		}

		fetch("/ajax/member/verification-check", {
			method: "POST",
			headers: { 'Content-Type': 'application/x-www-form-urlencoded' },
			body: new URLSearchParams({ code: inputCode })
		})
			.then(res => res.text())
			.then(result => {
				if (result === "valid") {
					Swal.fire({
						icon: 'success',
						title: '인증 성공',
						text: '회원가입을 계속 진행합니다.',
						confirmButtonText: '확인'
					}).then(() => {
						verified = true;
						clearInterval(timer);
						authCodeInput.disabled = true;
						checkAuthCodeBtn.disabled = true;
					});
				} else {
					failCount++;
					if (failCount >= 3) {
						authCodeInput.disabled = true;
						Swal.fire({
							icon: 'error',
							title: '인증 실패',
							text: '3회 실패했습니다. 인증번호를 다시 요청하세요.',
							confirmButtonText: '확인'
						});
					} else if (result === "invalid") {
						Swal.fire({
							icon: 'warning',
							title: '알림',
							text: `인증번호가 일치하지 않습니다. (${failCount}/3)`,
							confirmButtonText: '확인'
						});
					} else if (result === "expired") {
						Swal.fire({
							icon: 'warning',
							title: '알림',
							text: "인증번호가 만료되었습니다",
							confirmButtonText: '확인'
						});
					}
				}
			})
			.catch(err => {
				console.error("인증 확인 중 오류:", err);
				Swal.fire({
					icon: 'error',
					title: '오류',
					text: '인증 확인 중 문제가 발생했습니다.',
					confirmButtonText: '확인'
				});
			});
	});

signupForm?.addEventListener("submit", async (e) => {
	e.preventDefault();

	const mbrId = document.querySelector("#mbrId").value.trim();
	const mbrPw = document.querySelector("#mbrPw").value.trim();
	const mbrNm = document.querySelector("#mbrNm").value.trim();
	const mbrTelnoInput = document.querySelector("#mbrTelno");
	const authCode = document.querySelector("#authCode").value.trim();

	if (!verified) {
		Swal.fire({ icon: 'warning', title: '전화번호 인증을 먼저 완료해주세요.', confirmButtonText: '확인' });
		return;
	}
	if (!mbrId) {
		Swal.fire({ icon: "warning", title: "회원 ID를 입력해주세요", confirmButtonText: "확인" });
		return;
	}
	if (!mbrPw) {
		Swal.fire({ icon: "warning", title: "비밀번호를 입력해주세요", confirmButtonText: "확인" });
		return;
	}
	if (!mbrNm) {
		Swal.fire({ icon: "warning", title: "이름을 입력해주세요", confirmButtonText: "확인" });
		return;
	}
	if (!mbrTelnoInput.value.trim()) {
		Swal.fire({ icon: "warning", title: "전화번호를 입력해주세요", confirmButtonText: "확인" });
		return;
	}
	if (!authCode) {
		Swal.fire({ icon: "warning", title: "인증번호를 입력해주세요", confirmButtonText: "확인" });
		return;
	}

	// ✅ 전송 직전에 정제된 전화번호로 세팅
	const itiInstance = window.intlTelInputGlobals.getInstance(mbrTelnoInput);
	const rawTel = itiInstance.getNumber(); // +821012345678
	const cleanedTel = rawTel.replace(/^\+82/, "0").replace(/-/g, "");
	mbrTelnoInput.value = cleanedTel;

	// ✅ 최종 제출
	signupForm.submit();
});


	function updateTimerText() {
		const m = String(Math.floor(remainingTime / 60)).padStart(2, "0");
		const s = String(remainingTime % 60).padStart(2, "0");
		timerText.textContent = `${m}:${s}`;
	}

	function startTimer() {
		clearInterval(timer);
		remainingTime = 300;
		updateTimerText();

		resendReady = false;
		authBtn.disabled = true;
		authBtn.textContent = "인증요청";

		timer = setInterval(() => {
			remainingTime--;
			updateTimerText();

			if (!resendReady && remainingTime <= 240) {
				authBtn.textContent = "재전송";
				authBtn.disabled = false;
				resendReady = true;
			}

			if (remainingTime <= 0) {
				clearInterval(timer);
				authCodeInput.disabled = true;
				authBtn.disabled = false;
				timerText.textContent = "인증번호가 만료되었습니다.";
			}
		}, 1000);
	}
});

function toggleOptionalInfo() {
	const section = document.querySelector("#optionalInfo");
	if (section) {
		const isVisible = section.style.display === "block";
		section.style.display = isVisible ? "none" : "block";
	}
}

function updateFileName(input) {
	const fileName = input.files.length > 0 ? input.files[0].name : '선택된 파일 없음';
	document.querySelector('#file-name').textContent = fileName;
}
