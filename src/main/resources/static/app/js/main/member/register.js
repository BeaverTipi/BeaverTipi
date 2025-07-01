/**
 * 
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
const input = document.querySelector("#mbrTelno");
  window.intlTelInput(input, {
    initialCountry: "kr",
    preferredCountries: ["kr", "us", "jp"],
    utilsScript: "https://cdn.jsdelivr.net/npm/intl-tel-input@17.0.19/build/js/utils.js"
  });
    
let verified = false;

    const authBtn = document.querySelector(".btn-auth-code");
    const authCodeWrapper = document.querySelector(".authCodeWrapper");
    const authCodeInput = document.querySelector("#authCode");
    const phoneInput = document.querySelector("#memTel");
    const submitBtn = document.querySelector("#submitBtn");
    const resetBtn = document.querySelector("#resetBtn");
    const signupForm = document.querySelector("#signup-form");

	resetBtn.addEventListener("click",()=>{
		location
	})
	

    // 인증요청
    authBtn.addEventListener("click", () => {
	   e.preventDefault();
      const phone = phoneInput.value.trim();
      if (!phone) {
        alert("전화번호를 입력하세요.");
        return;
      }
     authCodeWrapper.classList.remove("hidden");
     
      fetch("/auth/sendCode", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ phone })
      })
      .then(res => res.json())
      .then(data => {
        if (data.success) {
          alert("인증번호가 전송되었습니다.");
          authCodeInput.disabled = false;
          verified = false;
        } else {
          alert(data.message || "전송 실패");
        }
      });
    });

    // 최종 제출 전 인증번호 확인
    submitBtn.addEventListener("click", async (e) => {
      const inputCode = authCodeInput.value.trim();
      if (authCodeInput.disabled || !inputCode) {
        alert("인증번호를 입력하세요.");
        e.preventDefault();
        return;
      }

      // 서버에 인증번호 검증 요청
      const res = await fetch("/auth/verifyCode", {
        method: "POST",
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
        body: new URLSearchParams({ inputCode })
      });

      const data = await res.json();
      if (!data.success) {
        alert(data.message);
        e.preventDefault();
      }
    });
  });
  
function toggleOptionalInfo() {
  const section = document.querySelector("#optionalInfo");
  const isVisible = section.style.display === "block";
  section.style.display = isVisible ? "none" : "block";
}




function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      // 선택된 주소 정보를 input에 반영
      document.querySelector("#postcode").value = data.zonecode;
      document.querySelector("#address").value = data.address;
      document.querySelector("#detailAddress").focus();
    }
  }).open();
}
function updateFileName(input) {
  const fileName = input.files.length > 0 ? input.files[0].name : '선택된 파일 없음';
  document.getElementById('file-name').textContent = fileName;
}



