document.addEventListener("DOMContentLoaded", function () {
  // ① 카카오 주소 검색
  window.execDaumPostcode = function () {
    new daum.Postcode({
      oncomplete: function (data) {
        document.querySelector("#bldgZipNo").value = data.zonecode;
        document.querySelector("#bldgAddr").value = data.address;
        document.querySelector("#bldgDtlAddr").focus();
      }
    }).open();
  };

  // ② 매물 정보 불러오기
  window.fillListingInfo = function () {
    const selectedId = document.getElementById("listingSelectBox").value;
    if (!selectedId) {
      console.warn("어서 선택해.");
      return;
    }

    fetch(`/building/managed/listing/detail?lstgId=${selectedId}`)
      .then(res => res.json())
      .then(data => {
        if (data && data.lstgId) {
          requestAnimationFrame(() => {
            document.querySelector("input[name='bldgNm']").value = data.lstgNm || "";
            document.querySelector("input[name='bldgZipNo']").value = data.lstgPostal || "";
            document.querySelector("input[name='bldgAddr']").value = data.lstgAdd || "";
            document.querySelector("input[name='bldgDtlAddr']").value = data.lstgAdd2 || "";
            document.querySelector("input[name='bldgGrossArea']").value = data.lstgGrArea || "";
            document.querySelector("#bldgTypeCode").value = data.lstgTypeCode1 || "";

            
            const leaseInput = document.querySelector("input[name='lstgLease']");
            const leaseMInput = document.querySelector("input[name='lstgLeaseM']");
            const leaseAmtInput = document.querySelector("input[name='lstgLeaseAmt']");
            if (leaseInput) leaseInput.value = data.lstgLease || "";
            if (leaseMInput) leaseMInput.value = data.lstgLeaseM || "";
            if (leaseAmtInput) leaseAmtInput.value = data.lstgLeaseAmt || "";
          });
        } else {
          console.warn("매물 정보가 없거나 lstgId 없음");
        }
      })
      .catch(err => {
        console.error("fetch 에 문제가 있는겨:", err);
      });
  };

  // ③ 이미지 미리보기
  const imgInput = document.getElementById('bldgImgFile');
  const previewImg = document.getElementById('previewImg');

  if (imgInput && previewImg) {
    imgInput.addEventListener('change', function(e) {
      const file = e.target.files[0];
      if (file) {
        const reader = new FileReader();
        reader.onload = function(ev) {
          previewImg.src = ev.target.result;
        };
        reader.readAsDataURL(file);
      } else {
        previewImg.src = '/images/sample-building.jpg';
      }
    });
  }
});
