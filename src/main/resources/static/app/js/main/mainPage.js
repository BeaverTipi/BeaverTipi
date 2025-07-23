document.addEventListener('DOMContentLoaded', () => {
  let hostname = window.location.hostname;
  const reactPort = 81;
  let brokerOfficeUrl = `http://${hostname}:${reactPort}/broker/myoffice`;
  if(hostname =="beavertipi.com"){
	hostname = "react.beavertipi.com";
  	brokerOfficeUrl = `https://${hostname}/broker/myoffice`;
  }

  // ✅ 마이오피스 링크 동적 할당
  const dropdownToggle = document.querySelector('[data-bs-toggle="dropdown"][id="userDropdown"]');
  if (dropdownToggle) {
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

  // ✅ 알림 모달 로드
  const modal = document.querySelector('#notificationModal');
  if (modal) {
	  modal.addEventListener('shown.bs.modal', () => {
	    fetchNotifications(1);
	  });
	
	  document.addEventListener('click', function (e) {
	    if (e.target.matches('.notification-page-link')) {
	      e.preventDefault();
	      const page = e.target.getAttribute('data-page');
	      fetchNotifications(page);
	    }
	  });
	
	  // ✅ 내부 함수로 알림 가져오기 정의
	  function fetchNotifications(page) {
	    axios.get(`/ajax/notification/list?page=${page}`, {
	      headers: {
	        'X-Requested-With': 'XMLHttpRequest'
	      }
	    })
	    .then(response => {
	      const container = document.querySelector('#notificationModalContent');
	      if (container) container.innerHTML = response.data;
	    })
	    .catch(error => {
	      console.error(error);
	      alert('알림을 불러오는 중 오류가 발생했습니다.');
	    });
	  }
  }
  
window.fnPagingModal = function(page) {
  axios.get(`/ajax/notification/list?page=${page}`, {
    headers: {
      'X-Requested-With': 'XMLHttpRequest'
    }
  })
  .then(resp => {
    const container = document.querySelector('#notificationModalContent');
    if (container) container.innerHTML = resp.data;
  })
  .catch(error => {
    console.error(error);
    alert('알림을 불러오는 중 오류가 발생했습니다.');
  });
};


});


