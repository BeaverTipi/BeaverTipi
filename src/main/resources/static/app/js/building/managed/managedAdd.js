document.addEventListener("DOMContentLoaded", function () {
  // ① 주소 검색
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
  window.fillListingInfo = function (selectedId) {
    if (!selectedId) return;

    fetch(`/building/managed/listing/detail?lstgId=${selectedId}`)
      .then(res => res.json())
      .then(data => {
        if (data && data.lstgId) {
          requestAnimationFrame(() => {
            document.querySelector("input[name='bldgNm']").value = data.lstgNm || "";
            document.querySelector("input[name='bldgZipNo']").value = data.lstgPostal || "";
            document.querySelector("input[name='bldgAddr']").value = data.lstgAdd || "";
            document.querySelector("input[name='bldgDtlAddr']").value = data.lstgAdd2 || "";

            // 공급면적 표시
            const sqm = parseFloat(data.lstgGrArea);
            const display = document.querySelector("#supplyAreaDisplay");
            const hidden = document.querySelector("#supplyAreaHidden");
            if (!isNaN(sqm)) {
              const pyeong = (sqm / 3.305785).toFixed(2);
              display.value = pyeong;
              hidden.value = sqm.toFixed(2);
              document.querySelector("#areaUnitLabel").textContent = "평";
              document.querySelector("#toggleUnitBtn").textContent = "㎡ ▼";
              currentUnit = "평";
            } else {
              display.value = "";
              hidden.value = "";
            }

            // 유형 코드 매핑
            const lstg2ToBldgCodeMap = {
              "001": "009", // 다세대 주택
              "003": "003",
              "004": "004",
              "005": "008",
              "006": "002",
              "007": "006",
              "008": "001",
              "009": "005"
            };
            const bldgSelect = document.querySelector("#bldgTypeCode");
            const mapped = lstg2ToBldgCodeMap[data.lstgTypeCode2];
            if (mapped) {
              const opt = [...bldgSelect.options].find(o => o.value === mapped);
              if (opt) bldgSelect.value = mapped;
              
            } else {
              bldgSelect.value = "";
            }

          });

          // 모달 닫기
          const modalEl = document.querySelector("#myListingsModal");
          const modal = bootstrap.Modal.getInstance(modalEl);
          if (modal) modal.hide();
        }
    })
    .catch(() => {
      // 네트워크 오류 등 처리
      Swal.fire({
        icon: "error",
        title: "서버 오류",
        text: "매물 정보를 불러오는 중 문제가 발생했습니다.",
        confirmButtonText: "확인"
      });
    });
}
  // ③ 이미지 미리보기
  const fileInput = document.querySelector('#bldgImgFile');
  const previewImg = document.querySelector('#previewImg');
  const placeholder = document.querySelector('.image-placeholder-text');
  const triggerBtn = document.querySelector('#triggerImgUpload');

  triggerBtn.addEventListener('click', () => {
    fileInput.value = '';
    fileInput.click();
  });

  fileInput.addEventListener('change', function () {
    const file = this.files[0];
    if (!file) return;
    const reader = new FileReader();
    reader.onload = function (e) {
      previewImg.src = e.target.result;
      previewImg.style.display = 'block';
      placeholder.style.display = 'none';
    };
    reader.readAsDataURL(file);
  });

  // ④ 단위 변환
  let currentUnit = "평";
  const displayInput = document.querySelector("#supplyAreaDisplay");
  const hiddenInput = document.querySelector("#supplyAreaHidden");
  const label = document.querySelector("#areaUnitLabel");
  const toggleBtn = document.querySelector("#toggleUnitBtn");

  toggleBtn.addEventListener("click", () => {
    let val = parseFloat(displayInput.value);
    if (isNaN(val)) val = 0;

    if (currentUnit === "평") {
      const sqm = (val * 3.305785).toFixed(2);
      displayInput.value = sqm;
      hiddenInput.value = sqm;
      label.textContent = "㎡";
      toggleBtn.textContent = "평 ▼";
      currentUnit = "㎡";
    } else {
      const pyeong = (val / 3.305785).toFixed(2);
      displayInput.value = pyeong;
      hiddenInput.value = val.toFixed(2);
      label.textContent = "평";
      toggleBtn.textContent = "㎡ ▼";
      currentUnit = "평";
    }
  });

  // ⑤ 제출 전 값 보정
  document.querySelector("form").addEventListener("submit", () => {
    const val = parseFloat(displayInput.value);
    if (!isNaN(val)) {
      hiddenInput.value = currentUnit === "평"
        ? (val * 3.305785).toFixed(2)
        : val.toFixed(2);
    }
  });

  // ⑥ 유효성 검사 + 애니메이션 제출
  const submitBtn = document.getElementById("submitBtn");
  if (!submitBtn) return;

  submitBtn.addEventListener("click", async function (e) {
    e.preventDefault();

    const requiredFields = [
      { selector: "select[name='bldgTypeCode']", label: "건물 유형" },
      { selector: "input[name='bldgNm']", label: "건물 이름" },
      { selector: "#supplyAreaDisplay", label: "공급면적" },
      { selector: "input[name='bldgCmpltnDt']", label: "준공일" },
      { selector: "input[name='bldgZipNo']", label: "우편번호" },
      { selector: "input[name='bldgDtlAddr']", label: "상세주소" }
    ];

    for (const field of requiredFields) {
      const input = document.querySelector(field.selector);
      if (!input || input.value.trim() === "") {
        await Swal.fire({
          icon: "warning",
          title: "입력 누락",
          text: `"${field.label}" 항목은 필수입니다.`,
          confirmButtonText: "확인"
        });
        input?.focus();
        return;
      }
    }

    const hiddenArea = document.querySelector("#supplyAreaHidden");
    if (!hiddenArea.value || parseFloat(hiddenArea.value) <= 0) {
      await Swal.fire({
        icon: "warning",
        title: "공급면적 오류",
        text: "공급면적을 올바르게 입력하고 단위를 확인해주세요.",
        confirmButtonText: "확인"
      });
      document.querySelector("#supplyAreaDisplay").focus();
      return;
    }

    const container = document.querySelector(".container-wrap");
    if (!container) {
      document.forms[0].submit();
      return;
    }

  });
});
