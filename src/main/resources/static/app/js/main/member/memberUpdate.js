// ✅ 전화번호 인증 UI 전체 스크립트

document.addEventListener("DOMContentLoaded", () => {
  const telInput = document.querySelector("#mbrTelno");
  const requestBtn = document.querySelector("#requestAuthBtn");
  const group = document.querySelector(".phone-auth-group");
  const authCodeInput = document.querySelector("#authCode");
  const authCodeWrapper = document.querySelector("#authCodeWrapper");
  const timerText = document.querySelector("#timerText");
  const resetBtn = document.querySelector("#resetBtn");

  let verified = false;
  let failCount = 0;
  let timer = null;
  let remainingTime = 300;

  const originalTel = telInput.value;

function toggleButtonVisibility() {
  const currentTel = telInput.value.trim();

  if (currentTel === "" || currentTel === originalTel) {
    // 입력이 없거나 기존 전화번호와 같을 때: 버튼 숨김 + input 꽉 채움
    requestBtn.style.display = "none";
    group.classList.add("full-width");
  } else {
    // 전화번호가 기존과 다를 때: 인증 버튼 표시 + input 줄이기
    requestBtn.style.display = "inline-block";
    group.classList.remove("full-width");
  }
}


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

  requestBtn.addEventListener("click", (e) => {
    e.preventDefault();

    const phone = telInput.value.trim();
    if (!phone || phone.length < 10) {
      Swal.fire({
        icon: 'warning',
        title: '알림',
        text: '유효한 전화번호를 입력해주세요.',
        confirmButtonText: '확인'
      });
      return;
    }

    // 인증번호 입력란 표시
    authCodeWrapper.classList.remove("hidden");

    // 인증요청 API 호출
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

  // 최초 실행 시 버튼 표시 여부 판단
  toggleButtonVisibility();

  // 입력 변경 시 인증 버튼 표시 여부 갱신
  telInput.addEventListener("input", toggleButtonVisibility);
});

function updateFileName(input) {
  const fileName = input.files.length > 0 ? input.files[0].name : '선택된 파일 없음';
  document.querySelector('#file-name').textContent = fileName;

  // 미리보기 이미지 변경
  const preview = document.querySelector("#previewImage");
  const file = input.files[0];
  if (file) {
    const reader = new FileReader();
    reader.onload = function (e) {
      preview.src = e.target.result;
    };
    reader.readAsDataURL(file);
  }
}
