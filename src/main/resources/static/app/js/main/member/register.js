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

	const authBtn = document.querySelector(".btn-auth-code");
	const authCodeWrapper = document.querySelector(".authCodeWrapper");
	const authCodeInput = document.querySelector("#authCode");
	const resetBtn = document.querySelector("#resetBtn");
	const signupForm = document.querySelector("#signup-form");
	const timerText = document.querySelector("#timerText");

	resetBtn.addEventListener("click", () => {
		history.back();
	});

	function startTimer() {
		clearInterval(timer);
		remainingTime = 300;
		updateTimerText();
		timer = setInterval(() => {
			remainingTime--;
			updateTimerText();
			if (remainingTime <= 0) {
				clearInterval(timer);
				authCodeInput.disabled = true;
				timerText.textContent = "⏰ 인증번호가 만료되었습니다.";
			}
		}, 1000);
	}

	function updateTimerText() {
		const m = String(Math.floor(remainingTime / 60)).padStart(2, "0");
		const s = String(remainingTime % 60).padStart(2, "0");
		timerText.textContent = `${m}:${s}`;
	}

	authBtn.addEventListener("click", (e) => {
		e.preventDefault();

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
		let phone = rawPhone.replace(/^\+82/, "0");

		if (!phone) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: "전화번호를 입력하세요.",
				confirmButtonText: '확인'
			});
			return;
		}

		const authCodeWrapper = document.querySelector("#authCodeWrapper");
		if (authCodeWrapper) {
			authCodeWrapper.classList.remove("hidden");
		} else {
			console.warn("authCodeWrapper 요소가 없습니다.");
		}

		fetch("/member/verification", {
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
					verified = false;
					failCount = 0;
					startTimer();
				} else {
					alert(data.message || "전송 실패");
				}
			});
	});
/* 
	signupForm.addEventListener("submit", async (e) => {
		e.preventDefault();

		if (verified) {
			Swal.fire({
				icon: 'warning',
				title: '알림',
				text: "전화번호 인증을 먼저 완료해주세요.",
				confirmButtonText: '확인'
			});
			return;
		}

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

		fetch("/member/verification-check", {
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
						signupForm.submit();
					});
				} else {
					failCount++;
					if (failCount >= 3) {
						authCodeInput.disabled = true;
						Swal.fire({
							icon: 'error',
							title: '인증 실패',
							text: '❌ 3회 실패했습니다. 인증번호를 다시 요청하세요.',
							confirmButtonText: '확인'
						});
					} else if (result === "invalid") {
						Swal.fire({
							icon: 'warning',
							title: '알림',
							text: `❌ 인증번호가 일치하지 않습니다. (${failCount}/3)`,
							confirmButtonText: '확인'
						});
					} else if (result === "expired") {
						Swal.fire({
							icon: 'warning',
							title: '알림',
							text: "⏰ 인증번호가 만료되었습니다",
							confirmButtonText: '확인'
						});
					}
				}
			});
	});
*/
});

function toggleOptionalInfo() {
	const section = document.querySelector("#optionalInfo");
	const isVisible = section.style.display === "block";
	section.style.display = isVisible ? "none" : "block";
}

function updateFileName(input) {
	const fileName = input.files.length > 0 ? input.files[0].name : '선택된 파일 없음';
	document.querySelector('#file-name').textContent = fileName;
}
