document.addEventListener("DOMContentLoaded", () => {
  const selectButtons = document.querySelectorAll(".select-btn");

  selectButtons.forEach(button => {
    button.addEventListener("click", () => {
      const data = {
        lstgNm: button.dataset.lstgNm,
        lstgAdd: button.dataset.lstgAddr,
        lstgAdd2: button.dataset.lstgAddr2,
        lstgGrArea: button.dataset.lstgArea,
        lstgRoomCnt: button.dataset.lstgRoom,
        lstgFloor: button.dataset.lstgFloor
      };

      if (window.opener && typeof window.opener.receiveListing === "function") {
        window.opener.receiveListing(data);
        window.close();
      } else {
        console.warn("부모창 연결 실패: receiveListing 함수 없음");
      }
    });
  });
});
