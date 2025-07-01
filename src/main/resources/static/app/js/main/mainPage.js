/**
 * 
 */
document.addEventListener("DOMContentLoaded", function () {
  const params = new URLSearchParams(window.location.search);
  const isError = params.get("error");

  if (isError === "true") {
    const loginModal = new bootstrap.Modal(document.querySelector("#loginModal"));
    loginModal.show();

    const errorBox = document.querySelector("#login-error-msg");
    if (errorBox) {
      errorBox.style.display = "block";
    }
    
    // 🔻 URL에서 ?error=true 제거 (히스토리만 조작)
    const url = new URL(window.location);
    url.searchParams.delete("error");
    window.history.replaceState({}, document.title, url.pathname);
  }  
  
  Kakao.init('f6ac04f1e14d24a9da646848581a9a89'); // 🔑 자바스크립트 키

  document.getElementById('kakao-login-btn').addEventListener('click', function () {
    Kakao.Auth.authorize({
      redirectUri: 'http://localhost/login/oauth2/code/kakao'  // ✅ 서버 콜백 URL
    });
  });

  });A