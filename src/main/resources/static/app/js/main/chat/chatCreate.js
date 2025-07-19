/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 15.     	     김재윤            최초작성
 *
 * </pre>
 */

// 문의 버튼 클릭 시 채팅방 생성 후 팝업으로 채팅방 열기
document.addEventListener("click", async (e) => {
	const target = e.target.closest("#inquiryBtn");
	if (!target) return;

	if (!window.loggedInUserId || window.loggedInUserId === "null" || window.loggedInUserId === "") {
		Swal.fire({
			icon: 'warning',
			title: '로그인이 필요합니다',
			text: '로그인 후 문의가 가능합니다.',
			confirmButtonText: '확인'
		});
		e.preventDefault();
		return;
	}

	const lstgId = target.dataset.lstgId;
	if (!lstgId) {
		Swal.fire({
			icon: 'error',
			title: '매물 정보 오류',
			text: '❗ 매물 정보가 누락되었습니다.',
			confirmButtonText: '확인'
		});
		return;
	}

	const popup = window.open("", "brokerChatPopup", "width=500,height=600,scrollbars=yes,resizable=yes");

	try {
		const res = await fetch("/broker/chat/create", {
			method: "POST",
			headers: { "Content-Type": "application/x-www-form-urlencoded" },
			body: new URLSearchParams({ lstgId })
		});

		const raw = await res.text();
		const url = raw.replace(/^redirect:/, ""); // 'redirect:' 제거
		popup.location.href = url; // ➕ popup=true 포함된 URL로 이동
	} catch (err) {
		console.error("❌ 팝업 이동 중 오류 발생:", err);
		popup.document.write("<p>⚠️ 채팅방 생성에 실패했습니다.</p>");
	}
});