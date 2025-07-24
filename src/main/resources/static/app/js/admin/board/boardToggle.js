/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
	function toggleDetail(code) {
		const mapping = {
			'007': '#noticeDetailBox',
			'008': '#qnaDetailBox',
			'009': '#faqDetailBox'
		};

		Object.values(mapping).forEach(selector => {
			const el = document.querySelector(selector);
			if (el) el.style.display = 'none';
		});

		const activeSelector = mapping[code];
		if (activeSelector) {
			const box = document.querySelector(activeSelector);
			if (box) {
				box.style.display = 'block';
				initSummernote(`${activeSelector} textarea`);
			}
		}
	}

	// Summernote 초기화 함수
	function initSummernote(selector) {
		const $el = $(selector);
		if ($el.length === 0 || $el.next('.note-editor').length > 0) return;

		$el.summernote({
			height: 300,
			placeholder: '내용을 입력하세요...',
			lang: 'ko-KR'
		});
	}

	// 게시판 유형 라디오 버튼 변경 시 상세영역 토글
	const radios = document.querySelectorAll('input[name="brdCode"]');
	radios.forEach(radio => {
		radio.addEventListener('change', function() {
			toggleDetail(this.value);
		});
	});

	// 최초 체크된 유형 반영
	const selected = document.querySelector('input[name="brdCode"]:checked');
	if (selected) {
		toggleDetail(selected.value);
	}

	// ✅ FAQ 게시판만 아코디언 토글
	const faqTitles = document.querySelectorAll(".faq-title");
	faqTitles.forEach(title => {
		title.addEventListener("click", function() {
			const index = this.closest("tr")?.dataset?.index;
			const contentRow = document.querySelector(`.faq-content[data-index='${index}']`);

			// 열려있는 다른 row 닫기
			document.querySelectorAll(".faq-content").forEach(row => {
				if (row !== contentRow) row.style.display = "none";
			});

			if (contentRow) {
				contentRow.style.display = contentRow.style.display === "none" ? "table-row" : "none";
			}
		});
	});

	// ✅ 기존 리스트 toggle 유지 (공지사항/QnA용)
	const titleLinks = document.querySelectorAll(".toggle-detail");
	titleLinks.forEach(link => {
		link.addEventListener("click", function(e) {
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
});
