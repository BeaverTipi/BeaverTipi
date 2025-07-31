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
				if (code === 'S0001') initSummernote('#summernote-notice');
				else if (code === 'S0002') initSummernote('#summernote-qna');
				else if (code === 'S0003') initSummernote('#summernote-faq');
			}
		}
	}

	function initSummernote(selector) {
		const $el = $(selector);
		if ($el.length === 0 || $el.next('.note-editor').length > 0) return;
	
		const initialContent = $el.val(); 
	
		$el.summernote({
			height: 300,
			placeholder: '내용을 입력하세요...',
			lang: 'ko-KR'
		});
	
		if (initialContent && initialContent.trim() !== '') {
			$el.summernote('code', initialContent);
		}
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
		} else {
			const defaultRadio = document.querySelector('input[name="brdCtgryValue"][value="007"]');
			if (defaultRadio) {
				defaultRadio.checked = true;
				const defaultCode = defaultRadio.dataset.syncCode || defaultRadio.value;
				brdCodeHidden.value = defaultCode;
				toggleDetail(defaultCode);
			}
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
		
		const formEl = document.querySelector("form");
		if (formEl && formEl.id === "writeForm") { 
			formEl.addEventListener("submit", function (e) {
				e.preventDefault();
		
				const code = document.getElementById("brdCodeHidden")?.value;
				let content = '';
		
				if (code === 'S0001') {
					content = $('#summernote-notice').summernote('code');
				} else if (code === 'S0002') {
					content = $('#summernote-qna').summernote('code');
				} else if (code === 'S0003') {
					content = $('#summernote-faq').summernote('code');
				}
		
				if (!content || content === '<p><br></p>') {
					alert("내용을 입력하세요.");
					return;
				}
		
				const hiddenField = document.getElementById("finalBrdCont");
				if (hiddenField) {
					hiddenField.value = content;
				}
		
				this.submit();
			});
		}

	
			const deleteColumns = document.querySelectorAll(".delete-column");
			const toggleDeleteModeBtn = document.getElementById("toggleDeleteModeBtn");
			const selectAllCheckbox = document.getElementById("selectAll");
			const deleteCheckboxes = document.querySelectorAll(".delete-checkbox");
			const deleteModeState = document.getElementById("deleteMode");
			const deleteSelectedBtn = document.getElementById("deleteSelectedBtn");
			
			toggleDeleteModeBtn?.addEventListener("click", function () {
				const isDeleteMode = deleteModeState.value === "true";
				const newMode = !isDeleteMode;
			
				deleteModeState.value = newMode ? "true" : "false";
			
				deleteColumns.forEach(el => {
					el.style.display = newMode ? "table-cell" : "none";
				});
			
				deleteCheckboxes.forEach(cb => {
					cb.checked = false;
				});
			
				if (selectAllCheckbox) {
					selectAllCheckbox.checked = false;
				}
			
				if (deleteSelectedBtn) {
					deleteSelectedBtn.style.display = newMode ? "inline-block" : "none";
				}
			
				toggleDeleteModeBtn.textContent = newMode ? "삭제 취소" : "삭제 모드";
			});
		
			if (selectAllCheckbox) {
				selectAllCheckbox.addEventListener("change", function () {
					const isChecked = this.checked;
					deleteCheckboxes.forEach(cb => {
						if (cb.style.display !== "none") {
							cb.checked = isChecked;
						}
					});
				});
			}
		
			if (deleteSelectedBtn) {
				deleteSelectedBtn.addEventListener("click", function () {
					const checkedItems = document.querySelectorAll('input[name="brdNoList"]:checked');
					if (checkedItems.length === 0) {
						alert("삭제할 게시글을 선택하세요.");
						return;
					}
					if (!confirm("정말 삭제하시겠습니까?")) {
						return;
					}
					const deleteForm = document.getElementById("deleteForm");
					if (deleteForm) {
						deleteForm.submit();
					}
				});
			}
			
			const singleDeleteForm = document.getElementById("singleDeleteForm");
			const singleDeleteBtn = document.getElementById("singleDeleteBtn");
			
			if (singleDeleteForm && singleDeleteBtn) {
				singleDeleteForm.addEventListener("submit", function (e) {
					const confirmed = confirm("정말 삭제하시겠습니까?");
					if (!confirmed) {
						e.preventDefault();
					}
				});
			}

});
