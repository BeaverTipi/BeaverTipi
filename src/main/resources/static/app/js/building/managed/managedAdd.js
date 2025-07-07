// ✅ 카카오 주소 검색
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      document.querySelector('#zipcode').value = data.zonecode;
      document.querySelector('#addr1').value = data.address;
      document.querySelector('#addr2').focus();
    }
  }).open();
}

// ✅ DOM 로드 후 이벤트 바인딩
document.addEventListener("DOMContentLoaded", () => {
  const checkbox = document.querySelector('#toggleProperty');
  const selectBox = document.querySelector('#propertySelect');

  const nameInput = document.querySelector('#name');
  const addr1Input = document.querySelector('#addr1');
  const addr2Input = document.querySelector('#addr2');
  const zipcodeInput = document.querySelector('#zipcode');
  const typeSelect = document.querySelector('#type');
  const areaInput = document.querySelector('#area');
  const floorsInput = document.querySelector('#floors');
  const unitsInput = document.querySelector('#units');

  // ✅ 더미 데이터
  const dummyData = {
    a101: {
      name: '현대오피스텔',
      addr1: '서울시 강남구 테헤란로 123',
      addr2: '10층 1001호',
      zipcode: '06130',
      type: 'OFFICE',
      area: '3200',
      floors: 10,
      units: 20
    },
    b202: {
      name: '마포상가타워',
      addr1: '서울시 마포구 양화로 23',
      addr2: '5층 201호',
      zipcode: '04060',
      type: 'SHOP',
      area: '1500',
      floors: 5,
      units: 8
    },
    c303: {
      name: '부평아파트 101동',
      addr1: '인천시 부평구 부평대로 88',
      addr2: '101동 303호',
      zipcode: '21404',
      type: 'APT',
      area: '4500',
      floors: 15,
      units: 60
    }
  };

  // ✅ 체크박스 토글
const selectWrapper = document.querySelector('#propertySelectBox');

checkbox.addEventListener('change', () => {
  selectWrapper.style.display = checkbox.checked ? 'block' : 'none';
  if (!checkbox.checked) {
    document.querySelector('#propertySelect').selectedIndex = 0;
    clearForm();
  }
});


  // ✅ 매물 선택 → 자동입력
  selectBox.addEventListener('change', () => {
    const selected = selectBox.value;
    if (dummyData[selected]) {
      const data = dummyData[selected];
      nameInput.value = data.name;
      addr1Input.value = data.addr1;
      addr2Input.value = data.addr2;
      zipcodeInput.value = data.zipcode;
      typeSelect.value = data.type;
      areaInput.value = data.area;
      floorsInput.value = data.floors;
      unitsInput.value = data.units;
    } else {
      clearForm();
    }
  });

  // ✅ 폼 초기화 함수
  function clearForm() {
    nameInput.value = '';
    addr1Input.value = '';
    addr2Input.value = '';
    zipcodeInput.value = '';
    typeSelect.value = '';
    areaInput.value = '';
    floorsInput.value = '';
    unitsInput.value = '';
  }
});
