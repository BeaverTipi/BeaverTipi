
//거래유형: 전세/월세/매매 필드 토글

function toggleLeaseFields() {
  const type = document.getElementById('lstgTypeSale')?.value;
  const jeonse = document.getElementById('jeonseField');
  const wolse = document.getElementById('wolseField');
  const sale = document.getElementById('salePriceField');

  if (!jeonse || !wolse || !sale) return;

  jeonse.style.display = 'none';
  wolse.style.display = 'none';
  sale.style.display = 'none';

  if (type === '1') {
    jeonse.style.display = 'block';
  } else if (type === '2') {
    wolse.style.display = 'block';
  } else if (type === '3') {
    sale.style.display = 'block';
  }
}


// 거래유형 코드별 보증금/가격/관리비 토글

function toggleTradeFields() {
  const tradeType = document.querySelector("select[name='lstgTrdTypeCode']")?.value;
  const deposit = document.getElementById("depositField")?.closest(".col-md-4");
  const price = document.getElementById("priceField")?.closest(".col-md-4");
  const mngFee = document.getElementById("mngFeeField")?.closest(".col-md-4");

  if (!deposit || !price || !mngFee) return;

  deposit.style.display = "block";
  price.style.display = "block";
  mngFee.style.display = "block";

  if (tradeType === "RENT") {
    // 월세
    deposit.style.display = "block";
    price.style.display = "block";
    mngFee.style.display = "block";
  } else if (tradeType === "LEASE") {
    // 전세
    deposit.style.display = "block";
    price.style.display = "none";
    mngFee.style.display = "block";
  } else if (tradeType === "SALE") {
    // 매매
    deposit.style.display = "none";
    price.style.display = "block";
    mngFee.style.display = "none";
  }
}


// 시설 전체선택 바인딩

function bindSelectAll(sectionId) {
  const section = document.getElementById(sectionId);
  if (!section) return;

  const selectAll = section.querySelector(".select-all");
  const checkboxes = section.querySelectorAll(".form-check-input.option");

  if (!selectAll) return;

  selectAll.addEventListener("change", function () {
    checkboxes.forEach(cb => cb.checked = this.checked);
  });
}


//다음 주소 API

function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      document.querySelector("#postcode").value = data.zonecode;
      document.querySelector("#address").value = data.address;
      document.querySelector("#detailAddress").focus();
    }
  }).open();
}


// 매물유형 

axios.get('/building/product/selectLstg1List')
.then(response => {
  const result = response.data;
  const area = document.querySelector("#lstgTypeListArea");
  let html = ``;

  if (result.status === 'OK') {
    result.data.forEach((v, i) => {
      html += `
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode1" id="typeCode1_${i}" value="${v.codeValue}">
          <label class="form-check-label" for="typeCode1_${i}">${v.codeName}</label>
        </div>
      `;
    });
    area.innerHTML = html;
  } else {
    alert('매물유형을 불러오는데 실패하였습니다.');
  }
}).catch(err => console.error('매물유형1 AJAX 오류:', err));

axios.get('/building/product/selectLstg2List')
.then(response => {
  const result = response.data;
  const area = document.querySelector("#lstgType2ListArea");
  let html = ``;

  if (result.status === 'OK') {
    result.data.forEach((v, i) => {
      html += `
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode2" id="typeCode2_${i}" value="${v.codeValue}">
          <label class="form-check-label" for="typeCode2_${i}">${v.codeName}</label>
        </div>
      `;
    });
    area.innerHTML = html;
  } else {
    alert('매물유형을 불러오는데 실패하였습니다.');
  }
}).catch(err => console.error('매물유형2 AJAX 오류:', err));


// 초기 실행 및 이벤트 바인딩

window.addEventListener("DOMContentLoaded", () => {
  toggleLeaseFields();
  toggleTradeFields();

  document.getElementById("lstgTypeSale")?.addEventListener("change", toggleLeaseFields);
  document.querySelector("select[name='lstgTrdTypeCode']")?.addEventListener("change", toggleTradeFields);

  bindSelectAll("life-section");
  bindSelectAll("security-section");
  bindSelectAll("etc-section");
  
    const selectAllCheckbox = document.getElementById("selectAllBrokers");
  const brokerCheckboxes = document.querySelectorAll(".broker-check");
  const confirmBtn = document.getElementById("confirmBrokerSelection");
  const selectedBrokersDiv = document.getElementById("selectedBrokers");
  const selectedBrokerInputs = document.getElementById("selectedBrokerInputs");

  // 전체 선택 제어
  selectAllCheckbox.addEventListener("change", function () {
    brokerCheckboxes.forEach(cb => cb.checked = this.checked);
  });

  // 선택 완료 버튼
  confirmBtn.addEventListener("click", function () {
    const selected = Array.from(brokerCheckboxes)
      .filter(cb => cb.checked)
      .map(cb => ({
        id: cb.value,
        label: document.querySelector(`label[for=${cb.id}]`).textContent.trim()
      }));

    // 선택 내용 표시
    selectedBrokersDiv.innerHTML = selected.length === 0
      ? "선택된 중개인 없음"
      : selected.map(s => `✅ ${s.label}`).join("<br>");

    // hidden input 정리 후 새로 추가
    selectedBrokerInputs.innerHTML = "";
    selected.forEach(s => {
      const input = document.createElement("input");
      input.type = "hidden";
      input.name = "brokerIds";
      input.value = s.id;
      selectedBrokerInputs.appendChild(input);
    });

    // 모달 닫기
    const modalEl = bootstrap.Modal.getInstance(document.getElementById("brokerModal"));
    modalEl.hide();
  });
});
