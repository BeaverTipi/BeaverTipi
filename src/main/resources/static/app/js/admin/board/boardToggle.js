/**
 *
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
	function toggleDetail(code) {
		const mapping = {
			'S0001': '#noticeDetailBox',
			'S0002': '#qnaDetailBox',
			'S0003': '#faqDetailBox'
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

	function initSummernote(selector) {
		const $el = $(selector);
		if ($el.length === 0 || $el.next('.note-editor').length > 0) return;

		$el.summernote({
			height: 300,
			placeholder: '내용을 입력하세요...',
			lang: 'ko-KR'
		});
	}

	
	const brdCodeHidden = document.getElementById("brdCodeHidden");
	const radios = document.querySelectorAll('input[name="brdCtgryValue"]');
	radios.forEach(radio => {
		radio.addEventListener("change", function () {
			const selectedCode = this.dataset.syncCode || this.value;
			if (brdCodeHidden) {
				brdCodeHidden.value = selectedCode;
			}
			toggleDetail(selectedCode);
		});
	});

	
	const selected = document.querySelector('input[name="brdCtgryValue"]:checked');
	if (selected && brdCodeHidden) {
		const selectedCode = selected.dataset.syncCode || selected.value;
		brdCodeHidden.value = selectedCode;
		toggleDetail(selectedCode);
	}

	
	const faqTitles = document.querySelectorAll(".faq-title");
	faqTitles.forEach(title => {
		title.addEventListener("click", function () {
			const index = this.closest("tr")?.dataset?.index;
			const contentRow = document.querySelector(`.faq-content[data-index='${index}']`);

			document.querySelectorAll(".faq-content").forEach(row => {
				if (row !== contentRow) row.style.display = "none";
			});

			if (contentRow) {
				contentRow.style.display = contentRow.style.display === "none" ? "table-row" : "none";
			}
		});
	});

	
	const titleLinks = document.querySelectorAll(".toggle-detail");
	titleLinks.forEach(link => {
		link.addEventListener("click", function (e) {
			e.preventDefault();
			const currentRow = this.closest("tr");
			const currentDetailRow = currentRow.nextElementSibling;

			document.querySelectorAll(".toggle-detail").forEach(otherLink => {
				const otherRow = otherLink.closest("tr");
				const otherDetailRow = otherRow.nextElementSibling;
				if (otherDetailRow && otherDetailRow !== currentDetailRow) {
					otherDetailRow.style.display = "none";
				}
			});

			if (currentDetailRow && currentDetailRow.style) {
				currentDetailRow.style.display =
					currentDetailRow.style.display === "none" ? "table-row" : "none";
			}
		});
	});
});
