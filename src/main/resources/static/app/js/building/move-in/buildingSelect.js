/**
 * 
 */
document.addEventListener("DOMContentLoaded", () => {
  const selector = document.querySelector("select[name='bldgIdParam']");
  const savedBldgId = localStorage.getItem("selectedBuildingId");

  // 저장된 건물 ID가 있으면 select에 반영
  if (selector && savedBldgId) {
    Array.from(selector.options).forEach(opt => {
      opt.selected = opt.value === savedBldgId;
    });
  }

  // 건물 선택 변경 시 localStorage에 저장 + 폼 submit
  if (selector) {
    selector.addEventListener("change", () => {
      localStorage.setItem("selectedBuildingId", selector.value);
      const form = selector.closest("form");
      if (form) form.submit();
    });
  }
});
