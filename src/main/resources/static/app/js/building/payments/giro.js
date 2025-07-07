/**
 * 
 */   
document.addEventListener("DOMContentLoaded",()=>{
 const subItems = document.getElementById('subItems');
    const addBtn = document.getElementById('addItem');
    const removeBtn = document.getElementById('removeItem');



    removeBtn.addEventListener('click', () => {
      const items = subItems.querySelectorAll('.form-item');
      if (items.length > 0) {
        subItems.removeChild(items[items.length - 1]);
      }
    });
    
      // 건물별 전체 선택 기능
  document.querySelectorAll('.building-all').forEach(masterCheckbox => {
    masterCheckbox.addEventListener('change', function () {
      const targetClass = this.dataset.target;
      const checkboxes = document.querySelectorAll('.' + targetClass);
      checkboxes.forEach(cb => cb.checked = this.checked);
    });
  });

  // 기존: 항목 추가/삭제 & 건물별 전체 선택 유지

  // 건물별 전체 선택
  document.querySelectorAll('.building-all').forEach(masterCheckbox => {
    masterCheckbox.addEventListener('change', function () {
      const targetClass = this.dataset.target;
      const checkboxes = document.querySelectorAll('.' + targetClass);
      checkboxes.forEach(cb => cb.checked = this.checked);
    });
  });

  // 전체 전체 선택 (상단)
  document.getElementById('totalAll').addEventListener('change', function () {
    const allCheckboxes = document.querySelectorAll('.building1, .building2');
    allCheckboxes.forEach(cb => cb.checked = this.checked);
  });

addBtn.addEventListener('click', () => {
  const div = document.createElement('div');
  div.className = 'form-item sub-item';
  div.innerHTML = `
    <select class="label-select">
      <option value="전기">전기</option>
      <option value="수도">수도</option>
      <option value="가스">가스</option>
      <option value="난방비">난방비</option>
      <option value="청소비">청소비</option>
    </select>
    <input type="text" placeholder="입력해주세요" class="value-input">
  `;
  subItems.appendChild(div);
});

})