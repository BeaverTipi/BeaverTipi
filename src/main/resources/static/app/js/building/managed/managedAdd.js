/**
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
	

const checkbox = document.querySelector('#toggleProperty');
    const selectBox = document.querySelector('#propertySelect');

    const nameInput = document.querySelector('#name');
    const addr1Input = document.querySelector('#addr1');
    const typeSelect = document.querySelector('#type');
    const areaInput = document.querySelector('#area');
    const zipcodeInput = document.querySelector('#zipcode');
    const addr2Input = document.querySelector('#addr2');

    const dummyData = {
      a101: {
        name: '현대오피스텔',
        addr1: '서울시 강남구 테헤란로 123',
        zipcode: '06130',
        type: 'OFFICE',
        area: '3200㎡',
        addr2: '10층 1001호'
      },
      b202: {
        name: '마포상가타워',
        addr1: '서울시 마포구 양화로 23',
        zipcode: '04060',
        type: 'SHOP',
        area: '1500㎡',
        addr2: '5층 201호'
      },
      c303: {
        name: '부평아파트 101동',
        addr1: '인천시 부평구 부평대로 88',
        zipcode: '21404',
        type: 'APT',
        area: '4500㎡',
        addr2: '101동 303호'
      }
    };

    checkbox.addEventListener('change', () => {
      selectBox.style.display = checkbox.checked ? 'inline-block' : 'none';
      if (!checkbox.checked) {
        selectBox.selectedIndex = 0;
        clearForm();
      }
    });

    selectBox.addEventListener('change', () => {
      const selected = selectBox.value;
      if (dummyData[selected]) {
        const data = dummyData[selected];
        nameInput.value = data.name;
        addr1Input.value = data.addr1;
        zipcodeInput.value = data.zipcode;
        addr2Input.value = data.addr2;
        typeSelect.value = data.type;
        areaInput.value = data.area;
      } else {
        clearForm();
      }
    });

    function clearForm() {
      nameInput.value = '';
      addr1Input.value = '';
      zipcodeInput.value = '';
      addr2Input.value = '';
      areaInput.value = '';
      typeSelect.value = '';
    }
    
})