/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 4.     	김재윤				최초 작성
 *
 * </pre>
 */
document.addEventListener("DOMContentLoaded", () => {
  const chatLink = document.getElementById("chatSidebarLink");

  if (chatLink) {
    chatLink.addEventListener("click", (e) => {
      e.preventDefault(); // 페이지 이동 막기

      const popupUrl = chatLink.getAttribute("href") + "?popup=true"; // 원래 링크 그대로 사용

      window.open(
        popupUrl,
        "chatPopup",
        "width=400,height=600,resizable=yes,scrollbars=yes"
      );
    });
  }
});

 
 