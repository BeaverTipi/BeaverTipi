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
  

 });