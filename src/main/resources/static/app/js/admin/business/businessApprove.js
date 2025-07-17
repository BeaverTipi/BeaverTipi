document.addEventListener("DOMContentLoaded", () => {
	const searchForm = document.querySelector("#searchForm");
	const searchBtn = searchForm.querySelector("#searchBtn");
	const resetBtn = document.querySelector("#resetBtn");
	const selectAll = document.querySelector("#selectAll");
	const checkboxes = document.querySelectorAll(".row-check");

	// 전체 선택
	selectAll.addEventListener("change", () => {
		checkboxes.forEach(cb => {
			const mbrCd = cb.value;
			const userType = cb.dataset.usertype;
			const popupKey = `popupOpened_${userType}_${mbrCd}`;
			const fileBtn = document.querySelector(`#fileBtn_${userType}_${mbrCd}`);

			// 첨부파일이 있는 경우 & 확인하지 않은 경우는 선택 안 됨
			if (fileBtn && !sessionStorage.getItem(popupKey)) {
				cb.checked = false;
			} else if (!cb.disabled) {
				cb.checked = selectAll.checked;
			}
		});
	});


	// 검색 시 페이지 초기화
	searchBtn.addEventListener("click", () => {
		searchForm.querySelector('input[name="page"]').value = 1;
	});

	// 검색 초기화 버튼
	resetBtn.addEventListener("click", () => {
		const radios = searchForm.querySelectorAll('input[name="hasFile"]');
		searchForm.reset();
		radios.forEach(radio => {
			radio.checked = false;
		});
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
	checkboxes.forEach(cb => {
		cb.addEventListener("click", (e) => {
			const mbrCd = cb.value;
			const userType = cb.dataset.usertype;
			const popupKey = `popupOpened_${userType}_${mbrCd}`;

			const fileBtn = document.querySelector(`#fileBtn_${userType}_${mbrCd}`);
			if (fileBtn && !sessionStorage.getItem(popupKey)) {
				e.preventDefault(); // 체크 방지

				Swal.fire({
					title: "안내",
					text: "첨부파일을 먼저 확인해주세요.",
					icon: "info",
					confirmButtonText: "확인"
				});

				return;
			}
		});
	});


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
				const csrfInput = document.querySelector("input[name='_csrf']");
				const csrfToken = csrfInput ? csrfInput.value : null;
				const headers = {
					'Content-Type': 'application/json'
				};

				if (csrfToken) {
					headers['X-CSRF-TOKEN'] = csrfToken;
				}

				axios.post('/ajax/admin/business/bulkAction', {
					action: actionType,
					users: selectedUsers
				}, {
					headers: headers
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
	// 파일 팝업 열기 + 체크박스 활성화 + 다시 열 수 없게
	window.openFilePopup = function(mbrCd, userType) {
		const popupUrl = `/admin/business/filePopup/${userType}/${mbrCd}`;
		const popupKey = `popupOpened_${userType}_${mbrCd}`;

		// 이미 열람한 경우: 다시 못 엶
		if (sessionStorage.getItem(popupKey)) {
			Swal.fire("안내", "이미 첨부파일을 확인하셨습니다.", "info");
			return;
		}

		// 팝업 열기
		const popup = window.open(popupUrl, "_blank", "width=900,height=700");

		// 대상 체크박스 및 버튼 요소 찾기
		const checkbox = document.querySelector(`.row-check[data-usertype="${userType}"][value="${mbrCd}"]`);
		const fileBtn = document.querySelector(`#fileBtn_${userType}_${mbrCd}`);

		const checkInterval = setInterval(() => {
			if (popup.closed) {
				clearInterval(checkInterval);

				// 체크박스 활성화
				if (checkbox) checkbox.disabled = false;

				// 버튼 다시 못 누르게 비활성화
				if (fileBtn) fileBtn.disabled = true;

				// 열람 여부 기록
				sessionStorage.setItem(popupKey, "true");
			}
		}, 500);
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
