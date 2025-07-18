/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
	function toggleDetail(value) {
		const noticeBox = document.getElementById('noticeDetailBox');
		const faqBox = document.getElementById('faqDetailBox');
		const qnaBox = document.getElementById("qnaDetailBox");
		if (!noticeBox || !faqBox || !qnaBox) return;

		if (value.startsWith('N')) {
			noticeBox.style.display = 'block';
			faqBox.style.display = 'none';
			qnaBox.style.display = 'none';
		} else if (value.startsWith('F')) {
			noticeBox.style.display = 'none';
			faqBox.style.display = 'block';
			qnaBox.style.display = 'none';
		} else if (value.startsWith('Q')) {
			noticeBox.style.display = 'none';
			faqBox.style.display = 'none';
			qnaBox.style.display = 'block';
		} else {
			noticeBox.style.display = 'none';
			faqBox.style.display = 'none';
			qnaBox.style.display = 'none';
		}
	}

	const radios = document.querySelectorAll('input[name="brdCode"]');
	radios.forEach(radio => {
		radio.addEventListener('change', function() {
			toggleDetail(this.value);
		});
	});


	const selected = document.querySelector('input[name="brdCode"]:checked');
	if (selected) {
		toggleDetail(selected.value);
	}

	const titleLinks = document.querySelectorAll(".toggle-detail");
	titleLinks.forEach(link => {
		link.addEventListener("click", function(e) {
			e.preventDefault();

			const currentRow = this.closest("tr");
			const currentDetailRow = currentRow.nextElementSibling;

			// 1️⃣ 먼저 모든 detailRow 닫기
			document.querySelectorAll(".toggle-detail").forEach(otherLink => {
				const otherRow = otherLink.closest("tr");
				const otherDetailRow = otherRow.nextElementSibling;
				if (otherDetailRow && otherDetailRow !== currentDetailRow) {
					otherDetailRow.style.display = "none";
				}
			});

			// 2️⃣ 현재 클릭한 detailRow만 토글
			if (currentDetailRow && currentDetailRow.style) {
				currentDetailRow.style.display =
					currentDetailRow.style.display === "none" ? "table-row" : "none";
			}
		});
	});

	const selectAll = document.getElementById("selectAllCheckbox");
	const checkboxes = document.querySelectorAll(".rowCheckbox");

	if (selectAll) {
		selectAll.addEventListener("change", function() {
			checkboxes.forEach(cb => cb.checked = selectAll.checked);
		});
	}


});