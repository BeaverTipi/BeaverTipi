/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
	function toggleDetail(value) {
		const noticeBox = document.getElementById('noticeDetailBox');
		const faqBox = document.getElementById('faqDetailBox');
		const qnaBox = document.getElementById("qnaDetailBox");

		if (!noticeBox || !faqBox || !qnaBox) return;

		// 모두 숨김
		noticeBox.style.display = 'none';
		faqBox.style.display = 'none';
		qnaBox.style.display = 'none';

		// 해당 영역만 표시
		if (value === '007') {
			noticeBox.style.display = 'block';
			initSummernote("#summernote-notice");
		} else if (value === '009') {
			faqBox.style.display = 'block';
			initSummernote("#summernote-faq");
		} else if (value === '008') {
			qnaBox.style.display = 'block';
			initSummernote("#summernote-qna");
		}
	}

	// Summernote 초기화 함수
	function initSummernote(selector) {
		// 이미 초기화되었으면 다시 안 함
		if ($(selector).next('.note-editor').length > 0) return;

		$(selector).summernote({
			height: 300,
			placeholder: '내용을 입력하세요...',
			lang: 'ko-KR'
		});
	}

	// 공지유형 라디오 버튼 이벤트
	const radios = document.querySelectorAll('input[name="brdCode"]');
	radios.forEach(radio => {
		radio.addEventListener('change', function () {
			toggleDetail(this.value);
		});
	});

	// 최초 체크된 항목 반영
	const selected = document.querySelector('input[name="brdCode"]:checked');
	if (selected) {
		toggleDetail(selected.value);
	}

	// 아래는 기존 리스트용 toggle 등 (필요 시 유지)
	const titleLinks = document.querySelectorAll(".toggle-detail");
	titleLinks.forEach(link => {
		link.addEventListener("click", function (e) {
			e.preventDefault();
			const currentRow = this.closest("tr");
			const currentDetailRow = currentRow.nextElementSibling;

			// 모든 row 닫기
			document.querySelectorAll(".toggle-detail").forEach(otherLink => {
				const otherRow = otherLink.closest("tr");
				const otherDetailRow = otherRow.nextElementSibling;
				if (otherDetailRow && otherDetailRow !== currentDetailRow) {
					otherDetailRow.style.display = "none";
				}
			});

			// 현재 row만 토글
			if (currentDetailRow && currentDetailRow.style) {
				currentDetailRow.style.display =
					currentDetailRow.style.display === "none" ? "table-row" : "none";
			}
		});
	});

	const selectAll = document.getElementById("selectAllCheckbox");
	const checkboxes = document.querySelectorAll(".rowCheckbox");
	if (selectAll) {
		selectAll.addEventListener("change", function () {
			checkboxes.forEach(cb => cb.checked = selectAll.checked);
		});
	}
});
