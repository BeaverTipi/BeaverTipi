document.addEventListener("DOMContentLoaded", () => {
	const searchForm = document.querySelector("#searchForm");
	const searchBtn = searchForm.querySelector("#searchBtn");
	const resetBtn = document.querySelector("#resetBtn");
	const selectAll = document.getElementById("selectAll");
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

	// 페이지 이동
	window.fnPaging = function (page) {
		searchForm.querySelector('input[name="page"]').value = page;
		searchForm.requestSubmit();
	};

	// 상세 팝업
	window.openDetailPopup = function (userId) {
		window.open(`/admin/business/detail/${userId}`, "_blank", "width=600,height=600");
	};

	// 승인
	window.submitApproval = function (mbrCd) {
		showConfirm('정말 승인하시겠습니까?', 'question', '#28a745', () => {
			postAction(`/admin/business/approve/${mbrCd}`);
		});
	};

	// 거절
	window.submitRejection = function (mbrCd) {
		showConfirm('정말 거절하시겠습니까?', 'warning', '#dc3545', () => {
			postAction(`/admin/business/reject/${mbrCd}`);
		});
	};

	// 일괄 승인/거절
	const bulkForm = document.querySelector("#bulkForm");
	bulkForm.addEventListener("submit", e => {
		e.preventDefault();
		const actionType = document.activeElement.value;
		const checked = document.querySelectorAll(".row-check:checked");
		const count = checked.length;

		if (count === 0) {
			Swal.fire("알림", "선택된 항목이 없습니다.", "info");
			return;
		}

		const messages = {
			approve: { title: `선택한 ${count}개 계정을 승인하시겠습니까?`, icon: "question", btn: "#28a745" },
			reject: { title: `선택한 ${count}개 계정을 거절하시겠습니까?`, icon: "warning", btn: "#dc3545" }
		};

		const opt = messages[actionType];
		showConfirm(opt.title, opt.icon, opt.btn, () => bulkForm.submit());
	});

	// 파일 팝업 열기
window.openFilePopup = function (mbrCd, userType) {
	window.open(`/admin/business/filePopup/${mbrCd}/${userType}`, "_blank", "width=900,height=700");
};


	// 공통 Confirm
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

	// POST 방식 처리
	function postAction(url) {
		const existingForm = document.getElementById("dynamicPostForm");
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
		// 파일 정보 localStorage 저장 (fileMapJson -> data-json 속성에서 가져오기)
	const fileMapEl = document.querySelector("#jsonFileMap");
	if (fileMapEl) {
		try {
			const fileMapJson = fileMapEl.dataset.json;
			const fileMap = JSON.parse(fileMapJson);
			Object.entries(fileMap).forEach(([key, fileList]) => {
				localStorage.setItem(key, JSON.stringify(fileList));
			});
		} catch (err) {
			console.error("파일 정보 저장 중 오류 발생:", err);
		}
	}
});
