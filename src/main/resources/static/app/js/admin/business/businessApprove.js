document.addEventListener("DOMContentLoaded", () => {
	const searchForm = document.querySelector("#searchForm");
	const searchBtn = searchForm.querySelector("#searchBtn");
	const resetBtn = document.querySelector("#resetBtn");

	// 전체 선택
	document.querySelector("#selectAll").addEventListener("change", function() {
		document.querySelectorAll(".row-check").forEach(cb => cb.checked = this.checked);
	});

	// 상세 팝업
	window.openDetailPopup = function(userId) {
		window.open(`/admin/business/detail/${userId}`, "_blank", "width=600,height=600");
	};

	// 검색 시 페이지 초기화
	searchBtn.addEventListener("click", function() {
		searchForm.querySelector('input[name="page"]').value = 1;
	});

	// 초기화 버튼 클릭 시
	resetBtn.addEventListener("click", function () {
		searchForm.reset(); // 기본 리셋 시도
	
		// form 내부 input 필드 수동 초기화
		searchForm.querySelectorAll('input[type="text"], input[type="hidden"]').forEach(input => {
			input.value = '';
		});
	
		// select 필드 초기화 (이미 하고 있긴 하지만 명시적으로 유지)
		searchForm.querySelector('select[name="authApprYn"]').value = '';
		searchForm.querySelector('select[name="role"]').value = '';
		searchForm.querySelector('select[name="hasFile"]').value = '';
		searchForm.querySelector('input[name="page"]').value = 1;
	
		// form 제출
		searchForm.requestSubmit();
	});


	// 페이지 이동 함수
	window.fnPaging = function(page) {
		searchForm.querySelector('input[name="page"]').value = page;
		searchForm.requestSubmit();
	};

	// 개별 승인
	window.submitApproval = function(mbrCd) {
		Swal.fire({
			title: '정말 승인하시겠습니까?',
			icon: 'question',
			showCancelButton: true,
			confirmButtonText: '승인',
			cancelButtonText: '취소',
			confirmButtonColor: '#28a745',
			cancelButtonColor: '#6c757d'
		}).then((result) => {
			if (result.isConfirmed) {
				const form = document.createElement('form');
				form.method = 'POST';
				form.action = `/admin/business/approve/${mbrCd}`;
				form.style.display = 'none';

				const csrf = document.querySelector('input[name="_csrf"]');
				if (csrf) {
					const csrfInput = document.createElement('input');
					csrfInput.type = 'hidden';
					csrfInput.name = '_csrf';
					csrfInput.value = csrf.value;
					form.appendChild(csrfInput);
				}

				document.body.appendChild(form);
				form.submit();
			}
		});
	};

	// 개별 거절
	window.submitRejection = function(mbrCd) {
		Swal.fire({
			title: '정말 거절하시겠습니까?',
			icon: 'warning',
			showCancelButton: true,
			confirmButtonText: '거절',
			cancelButtonText: '취소',
			confirmButtonColor: '#dc3545',
			cancelButtonColor: '#6c757d'
		}).then((result) => {
			if (result.isConfirmed) {
				const form = document.createElement('form');
				form.method = 'POST';
				form.action = `/admin/business/reject/${mbrCd}`;
				form.style.display = 'none';

				const csrf = document.querySelector('input[name="_csrf"]');
				if (csrf) {
					const csrfInput = document.createElement('input');
					csrfInput.type = 'hidden';
					csrfInput.name = '_csrf';
					csrfInput.value = csrf.value;
					form.appendChild(csrfInput);
				}

				document.body.appendChild(form);
				form.submit();
			}
		});
	};

	bulkForm.addEventListener("submit", function(e) {
		e.preventDefault(); // 기본 전송 막기
		const actionType = document.activeElement.value;
		const checked = document.querySelectorAll(".row-check:checked");
		const count = checked.length;

		if (count === 0) {
			e.preventDefault();
			Swal.fire("알림", "선택된 항목이 없습니다.", "info");
			return;
		}

		let title = "";
		let icon = "";
		let btnColor = "";
		let actionLabel = "";

		if (actionType === "approve") {
			title = `선택한 ${count}개 계정을 승인하시겠습니까?`;
			icon = "question";
			btnColor = "#28a745";
			actionLabel = "승인";
		} else if (actionType === "reject") {
			title = `선택한 ${count}개 계정을 거절하시겠습니까?`;
			icon = "warning";
			btnColor = "#dc3545";
			actionLabel = "거절";
		}

		Swal.fire({
			title: title,
			text: `확인 시 ${count}개의 계정이 ${actionLabel} 처리됩니다.`,
			icon: icon,
			showCancelButton: true,
			confirmButtonText: "확인",
			cancelButtonText: "취소",
			confirmButtonColor: btnColor,
			cancelButtonColor: "#6c757d"
		}).then((result) => {
			if (result.isConfirmed) {
				bulkForm.submit(); // 실제 전송
			}
		});
	});
	function openFilePopup(mbrCd) {
			window.open("/admin/business/files/" + mbrCd, "_blank", "width=800,height=600");
		}
});
