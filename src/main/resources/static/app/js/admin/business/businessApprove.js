document.addEventListener("DOMContentLoaded", () => {
  const searchForm = document.querySelector("#searchForm");
  const searchBtn = searchForm.querySelector("#searchBtn");
  const resetBtn = document.querySelector("#resetBtn");
  const selectAll = document.querySelector("#selectAll");
  const checkboxes = document.querySelectorAll(".row-check");

  // 전체 선택
  selectAll.addEventListener("change", () => {
    checkboxes.forEach(cb => {
      if (!cb.disabled) cb.checked = selectAll.checked;
    });
  });

  // 검색 시 페이지 초기화
  searchBtn.addEventListener("click", () => {
    searchForm.querySelector('input[name="page"]').value = 1;
  });

  // 검색 초기화 버튼
  resetBtn.addEventListener("click", () => {
    searchForm.reset();
    searchForm.querySelectorAll('input[type="text"], input[type="hidden"]').forEach(i => i.value = '');
    searchForm.querySelectorAll('select').forEach(s => s.value = '');
    searchForm.querySelector('input[name="page"]').value = 1;
    searchForm.requestSubmit();
  });

  // 페이지 이동 함수 (JSP 페이징에서 호출)
  window.fnPaging = function(page) {
    searchForm.querySelector('input[name="page"]').value = page;
    searchForm.requestSubmit();
  };

  // 상세 팝업
  window.openDetailPopup = function(userId) {
    window.open(`/admin/business/detail/${userId}`, "_blank", "width=600,height=600");
  };

  // 단일 승인
  window.submitApproval = function(mbrCd, userType) {
    showConfirm('정말 승인하시겠습니까?', 'question', '#28a745', () => {
      postAction(`/admin/business/approve/${userType}/${mbrCd}`);
    });
  };

  // 단일 거절
  window.submitRejection = function(mbrCd, userType) {
    showConfirm('정말 거절하시겠습니까?', 'warning', '#dc3545', () => {
      postAction(`/admin/business/reject/${userType}/${mbrCd}`);
    });
  };

  // 일괄 승인/거절 처리
  const bulkForm = document.querySelector("#bulkForm");
   // 일괄 승인/거절 처리
  bulkForm.addEventListener("submit", e => {
    e.preventDefault();

    // 어떤 버튼 눌렀는지 확인 (approve/reject)
    const actionType = document.activeElement.value;
    const checkedBoxes = document.querySelectorAll(".row-check:checked");
    const count = checkedBoxes.length;

    if (count === 0) {
      Swal.fire("알림", "선택된 항목이 없습니다.", "info");
      return;
    }

    // 선택된 회원 및 유형 수집
    const selectedUsers = Array.from(checkedBoxes).map(cb => ({
      mbrCd: cb.value,
      userType: cb.dataset.usertype || ''
    }));

    Swal.fire({
      title: `선택한 ${count}개 계정을 ${actionType === 'approve' ? '승인' : '거절'}하시겠습니까?`,
      icon: actionType === 'approve' ? 'question' : 'warning',
      showCancelButton: true,
      confirmButtonText: '확인',
      cancelButtonText: '취소',
      confirmButtonColor: actionType === 'approve' ? '#28a745' : '#dc3545',
      cancelButtonColor: '#6c757d'
    }).then(result => {
      if (result.isConfirmed) {
        // CSRF 토큰 가져오기
        const csrfToken = document.querySelector("input[name='_csrf']").value;

        axios.post('/admin/business/bulkAction', {
          action: actionType,
          users: selectedUsers
        }, {
          headers: {
            'Content-Type': 'application/json',
            ...(csrfToken && { 'X-CSRF-TOKEN': csrfToken })
          }
        })
        .then(response => {
          Swal.fire('완료', '처리가 완료되었습니다.', 'success').then(() => {
            location.reload();
          });
        })
        .catch(error => {
          Swal.fire('오류', '처리 중 오류가 발생했습니다.', 'error');
          console.error(error);
        });
      }
    });
  });

  // 파일 팝업 열기
  window.openFilePopup = function(mbrCd, userType) {
    window.open(`/admin/business/filePopup/${userType}/${mbrCd}`, "_blank", "width=900,height=700");
  };

  // 공통 Confirm 팝업
  function showConfirm(title, icon, btnColor, callback) {
    Swal.fire({
      title,
      icon,
      showCancelButton: true,
      confirmButtonText: "확인",
      cancelButtonText: "취소",
      confirmButtonColor: btnColor,
      cancelButtonColor: "#6c757d"
    }).then(result => {
      if (result.isConfirmed) callback();
    });
  }

  // POST 방식 동적 폼 전송
  function postAction(url) {
    const existingForm = document.querySelector("#dynamicPostForm");
    if (existingForm) existingForm.remove();

    const form = document.createElement("form");
    form.method = "POST";
    form.action = url;
    form.id = "dynamicPostForm";
    form.style.display = "none";

    const csrf = document.querySelector("input[name='_csrf']");
    if (csrf) {
      const csrfInput = document.createElement("input");
      csrfInput.type = "hidden";
      csrfInput.name = "_csrf";
      csrfInput.value = csrf.value;
      form.appendChild(csrfInput);
    }

    document.body.appendChild(form);
    form.submit();
  }
});
