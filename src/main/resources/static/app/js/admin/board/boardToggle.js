/**
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
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
		} else if (value.startsWith('Q')){
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
});