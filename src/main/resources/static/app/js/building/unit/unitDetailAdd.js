document.addEventListener('DOMContentLoaded', function () {
  const unitStates = {};
  const areaUnitStates = {};
  const unitOrder = ["억", "만원", "원"];
  const areaUnitOrder = ["㎡", "평"];

  function convertToWon(value, unit) {
    const raw = value.toString().replace(/,/g, "");
    const num = Number(raw);
    if (isNaN(num)) return "";
    switch (unit) {
      case "억": return num * 100000000;
      case "만원": return num * 10000;
      case "원": return num;
      default: return num;
    }
  }

  function convertFromWon(value, unit) {
    const num = Number(value);
    if (isNaN(num)) return "";
    switch (unit) {
      case "억": return (num / 100000000).toFixed(2);
      case "만원": return (num / 10000).toFixed(0);
      case "원": return num.toFixed(0);
      default: return num;
    }
  }

  function convertToM2(value, unit) {
    const raw = value.toString().replace(/,/g, "");
    const num = parseFloat(raw);
    if (isNaN(num)) return "";
    switch (unit) {
      case "㎡": return num;
      case "평": return num * 3.3058;
      default: return num;
    }
  }

  function convertFromM2(value, unit) {
    const num = parseFloat(value);
    if (isNaN(num)) return "";
    switch (unit) {
      case "㎡": return num.toFixed(2);
      case "평": return (num / 3.3058).toFixed(2);
      default: return num;
    }
  }

  function formatWithComma(value) {
    const num = Number(value);
    if (isNaN(num)) return "";
    return num.toLocaleString();
  }

  function parseCommaNumber(value) {
    return value.replace(/,/g, "");
  }

  function toggleUnitForField(fieldKey) {
    const viewInput = document.querySelector(`#${fieldKey}View`);
    const hiddenInput = document.querySelector(`#${fieldKey}`);
    const label = document.querySelector(`#label_${fieldKey}`);
    const btn = document.querySelector(`#${fieldKey}UnitBtn`);
    if (!viewInput || !hiddenInput || !btn) return;

    const currentUnit = unitStates[fieldKey];
    const currentIdx = unitOrder.indexOf(currentUnit);
    const nextUnit = unitOrder[(currentIdx + 1) % unitOrder.length];
    const nextNextUnit = unitOrder[(currentIdx + 2) % unitOrder.length];

    unitStates[fieldKey] = nextUnit;

    const wonValue = hiddenInput.value;
    const converted = convertFromWon(wonValue, nextUnit);
    viewInput.value = formatWithComma(converted);
    btn.textContent = `${nextNextUnit} ▼`;

    if (label) {
      label.textContent = label.dataset.labelBase + ` (${nextUnit})`;
    }
  }

  function toggleAreaUnit(fieldKey) {
    const viewInput = document.querySelector(`#${fieldKey}View`);
    const hiddenInput = document.querySelector(`#${fieldKey}`);
    const label = document.querySelector(`#label_${fieldKey}`);
    const btn = document.querySelector(`#${fieldKey}UnitBtn`);
    if (!viewInput || !hiddenInput || !btn) return;

    const currentUnit = areaUnitStates[fieldKey];
    const currentIdx = areaUnitOrder.indexOf(currentUnit);
    const nextUnit = areaUnitOrder[(currentIdx + 1) % areaUnitOrder.length];
    const nextBtnUnit = areaUnitOrder[(currentIdx + 2) % areaUnitOrder.length];

    areaUnitStates[fieldKey] = nextUnit;

    const m2Value = hiddenInput.value;
    const converted = convertFromM2(m2Value, nextUnit);
    viewInput.value = formatWithComma(converted);
    btn.textContent = `${nextBtnUnit} ▼`;

    if (label) {
      label.textContent = label.dataset.labelBase + ` (${nextUnit})`;
    }
  }

  function bindUnitInputSyncForField(fieldKey) {
    const viewInput = document.querySelector(`#${fieldKey}View`);
    const hiddenInput = document.querySelector(`#${fieldKey}`);
    if (!viewInput || !hiddenInput) return;

    viewInput.addEventListener("input", () => {
      const raw = parseCommaNumber(viewInput.value);
      const unit = unitStates[fieldKey];
      const won = convertToWon(raw, unit);
      hiddenInput.value = won;
      viewInput.value = formatWithComma(raw);
    });
  }

  function bindAreaInputSync(fieldKey) {
    const viewInput = document.querySelector(`#${fieldKey}View`);
    const hiddenInput = document.querySelector(`#${fieldKey}`);
    if (!viewInput || !hiddenInput) return;

    viewInput.addEventListener("input", () => {
      const raw = parseCommaNumber(viewInput.value);
      const unit = areaUnitStates[fieldKey];
      const m2 = convertToM2(raw, unit);
      hiddenInput.value = m2;
      viewInput.value = formatWithComma(raw);
    });
  }

  function initUnitInput(fieldKey, defaultUnit = "만원") {
    unitStates[fieldKey] = defaultUnit;

    bindUnitInputSyncForField(fieldKey);

    const btn = document.querySelector(`#${fieldKey}UnitBtn`);
    if (btn) {
      btn.addEventListener("click", () => toggleUnitForField(fieldKey));
    }

    const hiddenInput = document.querySelector(`#${fieldKey}`);
    const viewInput = document.querySelector(`#${fieldKey}View`);
    if (hiddenInput && viewInput) {
      const converted = convertFromWon(hiddenInput.value || 0, defaultUnit);
      viewInput.value = formatWithComma(converted);
    }
  }

  function initAreaUnitInput(fieldKey, defaultUnit = "㎡") {
    areaUnitStates[fieldKey] = defaultUnit;

    bindAreaInputSync(fieldKey);

    const btn = document.querySelector(`#${fieldKey}UnitBtn`);
    if (btn) {
      btn.addEventListener("click", () => toggleAreaUnit(fieldKey));
    }

    const hiddenInput = document.querySelector(`#${fieldKey}`);
    const viewInput = document.querySelector(`#${fieldKey}View`);
    if (hiddenInput && viewInput) {
      const converted = convertFromM2(hiddenInput.value || 0, defaultUnit);
      viewInput.value = formatWithComma(converted);
    }
  }

  function generateUnitInputs() {
    const container = document.querySelector("#unitInputContainer");
    const count = parseInt(document.querySelector("#unitCount").value, 10);
    if (isNaN(count) || count < 1) {
      Swal.fire({
        icon: 'warning',
        title: '입력 오류',
        text: '세대 수를 1 이상 입력해주세요.',
        confirmButtonText: '확인'
      });
      return;
    }

	// 기존 안내문 제거
	container.innerHTML = "";
	
	// 클래스 교체
	container.classList.remove("unit-placeholder-box");
	container.classList.add("unit-list-wrapper");
    for (let i = 0; i < count; i++) {
      const wrapper = document.createElement("div");
      wrapper.className = "card mb-3 p-3";

      wrapper.innerHTML = `
        <h5 class="card-title mb-3 w-100">호실 ${i + 1}</h5>
        <div class="row g-3">
        <div class="col-md-4">
            <label class="form-label">호실</label>
            <input type="text" name="unitList[${i}].unitRoom" class="form-control" />
          </div>
          
          <div class="col-md-4">
            <label class="form-label" id="label_unitCmar_${i}" data-label-base="공급면적">공급면적 (평) <span class="text-danger">*</span></label>
            <div class="unit-input-group">
              <input type="text" id="unitCmar_${i}View" class="form-control" inputmode="numeric" />
              <input type="hidden" id="unitCmar_${i}" name="unitList[${i}].unitCmar" />
              <button type="button" class="btn btn-unit-toggle" id="unitCmar_${i}UnitBtn">㎡ ▼</button>
            </div>
          </div>
          <div class="col-md-4">
            <label class="form-label" id="label_unitXuar_${i}" data-label-base="전용면적">전용면적 (평)</label>
            <div class="unit-input-group">
              <input type="text" id="unitXuar_${i}View" class="form-control" inputmode="numeric" />
              <input type="hidden" id="unitXuar_${i}" name="unitList[${i}].unitXuar" />
              <button type="button" class="btn btn-unit-toggle" id="unitXuar_${i}UnitBtn">㎡ ▼</button>
            </div>
          </div>
          <div class="col-md-4">
            <label class="form-label" id="label_unitDpstAmt_${i}" data-label-base="보증금 예상금액">보증금 예상금액 (만원)</label>
            <div class="unit-input-group">
              <input type="text" id="unitDpstAmt_${i}View" name="unitList[${i}].unitDpstAmtDisplay" class="form-control" inputmode="numeric" />
              <input type="hidden" id="unitDpstAmt_${i}" name="unitList[${i}].unitDpstAmt" />
              <button type="button" class="btn btn-unit-toggle" id="unitDpstAmt_${i}UnitBtn">원 ▼</button>
            </div>
          </div>
          <div class="col-md-4">
            <label class="form-label" id="label_unitDsrMnthRentAmt_${i}" data-label-base="월세 예상금액">월세 예상금액 (만원)</label>
            <div class="unit-input-group">
              <input type="text" id="unitDsrMnthRentAmt_${i}View" name="unitList[${i}].unitDsrMnthRentAmtDisplay" class="form-control" inputmode="numeric" />
              <input type="hidden" id="unitDsrMnthRentAmt_${i}" name="unitList[${i}].unitDsrMnthRentAmt" />
              <button type="button" class="btn btn-unit-toggle" id="unitDsrMnthRentAmt_${i}UnitBtn">원 ▼</button>
            </div>
          </div>
          <div class="col-md-4">
            <label class="form-label" id="label_unitDsrSaleAmt_${i}" data-label-base="전세 예상금액">전세 예상금액 (만원)</label>
            <div class="unit-input-group">
              <input type="text" id="unitDsrSaleAmt_${i}View" name="unitList[${i}].unitDsrSaleAmtDisplay" class="form-control" inputmode="numeric" />
              <input type="hidden" id="unitDsrSaleAmt_${i}" name="unitList[${i}].unitDsrSaleAmt" />
              <button type="button" class="btn btn-unit-toggle" id="unitDsrSaleAmt_${i}UnitBtn">원 ▼</button>
            </div>
          </div>
          <div class="col-md-12">
            <label class="form-label">상세설명</label>
            <textarea name="unitList[${i}].unitDtlDescCn" class="form-control" rows="2"></textarea>
          </div>
          <div class="col-md-4">
            <label class="form-label">층수 <span class="text-danger">*</span></label>
            <input type="number" name="unitList[${i}].unitFlrNo" class="form-control" required />
          </div>
        </div>
        <input type="hidden" name="unitList[${i}].unitStatCd" value="REGISTERED" />
      `;

      container.appendChild(wrapper);

      // 금액 단위
      initUnitInput(`unitDpstAmt_${i}`, "만원");
      initUnitInput(`unitDsrMnthRentAmt_${i}`, "만원");
      initUnitInput(`unitDsrSaleAmt_${i}`, "만원");

      // 면적 단위
      initAreaUnitInput(`unitCmar_${i}`,  "평");
      initAreaUnitInput(`unitXuar_${i}`,  "평");
    }
  }

  document.querySelector('#generateBtn')?.addEventListener('click', generateUnitInputs);
});
