function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      document.querySelector("#bldgZipNo").value = data.zonecode;
      document.querySelector("#bldgAddr").value = data.address;
      document.querySelector("#bldgDtlAddr").focus();
    }
  }).open();
}

document.addEventListener("DOMContentLoaded", () => {
  const selectBox = document.querySelector("#listingSelectBox");

  const nameInput = document.querySelector("input[name='bldgNm']");
  const addr1Input = document.querySelector("input[name='bldgAddr']");
  const addr2Input = document.querySelector("input[name='bldgDtlAddr']");
  const zipcodeInput = document.querySelector("input[name='bldgZipNo']");
  const areaInput = document.querySelector("input[name='bldgGrossArea']");

  const fillListingInfo = () => {
    const selectedId = selectBox.value;
    if (!selectedId) {
      console.warn("매물이 선택되지 않았습니다.");
      return;
    }

    fetch(`/building/managed/listing/detail?lstgId=${selectedId}`)
      .then(res => res.json())
      .then(data => {
        if (data && data.lstgId) {
          nameInput.value = data.lstgNm || "";
          addr1Input.value = data.lstgAdd || "";
          addr2Input.value = data.lstgAdd2 || "";
          zipcodeInput.value = data.lstgPostal || "";
          areaInput.value = data.lstgGrArea || "";

          console.info("매물 정보가 건물 등록 폼에 반영되었습니다.");
        } else {
          console.warn("매물 데이터를 찾을 수 없습니다.");
        }
      })
      .catch(err => {
        console.error("서버 요청 중 오류 발생:", err);
      });
  };

  const btn = document.querySelector("button[onclick='fillListingInfo()']");
  if (btn) {
    btn.addEventListener("click", fillListingInfo);
  } else {
    console.warn("불러오기 버튼을 찾을 수 없습니다.");
  }
});
