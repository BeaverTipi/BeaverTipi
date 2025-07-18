// DOM이 완전히 로드된 후 실행
document.addEventListener("DOMContentLoaded", function () {

  // 카카오 주소 검색
  window.execDaumPostcode = function () {
    new daum.Postcode({
      oncomplete: function (data) {
        document.querySelector("#bldgZipNo").value = data.zonecode;
        document.querySelector("#bldgAddr").value = data.address;
        document.querySelector("#bldgDtlAddr").focus();
      }
    }).open();
  };

  // 매물 정보 불러오기
  window.fillListingInfo = function () {
    const selectedId = document.getElementById("listingSelectBox").value;
    if (!selectedId) {
      console.warn("어서 선택해.");
      return;
    }

    fetch(`/building/managed/listing/detail?lstgId=${selectedId}`)
      .then(res => res.json())
      .then(data => {
        console.log("매물 데이터:", data);

        if (data && data.lstgId) {
          requestAnimationFrame(() => {
            // 매물 기본 정보 → 건물 등록 폼에 채우기
            document.querySelector("input[name='bldgNm']").value = data.lstgNm || "";
            document.querySelector("input[name='bldgZipNo']").value = data.lstgPostal || "";
            document.querySelector("input[name='bldgAddr']").value = data.lstgAdd || "";
            document.querySelector("input[name='bldgDtlAddr']").value = data.lstgAdd2 || "";
            document.querySelector("input[name='bldgGrossArea']").value = data.lstgGrArea || "";

            // 매물 유형 코드 → 건물 유형 셀렉트에 반영
            document.querySelector("#bldgTypeCode").value = data.lstgTypeCode1 || "";

            // 임대 금액 정보 바인딩
            const leaseInput = document.querySelector("input[name='lstgLease']");
            const leaseMInput = document.querySelector("input[name='lstgLeaseM']");
            const leaseAmtInput = document.querySelector("input[name='lstgLeaseAmt']");

            if (leaseInput) leaseInput.value = data.lstgLease || "";
            if (leaseMInput) leaseMInput.value = data.lstgLeaseM || "";
            if (leaseAmtInput) leaseAmtInput.value = data.lstgLeaseAmt || "";

            console.info("매물 정보 가져옴");
          });
        } else {
          console.warn("매물 정보가 없거나 lstgId 없음");
        }
      })
      .catch(err => {
        console.error("fetch 에 문제가 있는겨:", err);
      });
  };
});
