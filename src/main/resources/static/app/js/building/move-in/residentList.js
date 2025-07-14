document.addEventListener('DOMContentLoaded', () => {
  console.log("✅ residentList.js 실행됨");

  // 🏢 건물 선택 셀렉터
  const selector = document.querySelector('select[name="bldgIdParam"]');
  const savedBldgId = localStorage.getItem("selectedBuildingId");

  // 초기 로드 시 로컬에 저장된 건물 아이디가 있으면 select에 반영
  if (selector && savedBldgId) {
    selector.value = savedBldgId;
  }

  // 건물 선택 변경 시 로컬 저장 후 폼 제출
  if (selector) {
    selector.addEventListener("change", () => {
      console.log("🔧 건물 선택 변경됨:", selector.value);
      localStorage.setItem("selectedBuildingId", selector.value);
      const form = selector.closest("form");
      if (form) form.submit();
    });
  }
});
