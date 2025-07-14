document.addEventListener("DOMContentLoaded", () => {
	  const hostname = window.location.hostname;
	  const reactPort = 81;
	  const brokerOfficeUrl = `http://${hostname}:${reactPort}/broker/myoffice`;

    // Bootstrap dropdown은 button이 아닌 부모 .dropdown element에 이벤트가 바인딩됩니다
    const dropdownToggle = document.querySelector('[data-bs-toggle="dropdown"][id="userDropdown"]');

    if (dropdownToggle) {
      // Bootstrap 5의 드롭다운 인스턴스를 수동으로 가져옴
      const dropdownEl = dropdownToggle.closest(".dropdown");
      if (dropdownEl) {
        dropdownEl.addEventListener("show.bs.dropdown", () => {
          const brokerLinkEl = document.querySelector("#brokerOfficeLink");
          if (brokerLinkEl) {
            brokerLinkEl.setAttribute("href", brokerOfficeUrl);
            console.log("✅ brokerOfficeLink href set:", brokerOfficeUrl);
          } else {
            console.warn("❌ #brokerOfficeLink not found");
          }
        });
      }
    } else {
      console.warn("⚠️ userDropdown toggle button not found");
    }
  });