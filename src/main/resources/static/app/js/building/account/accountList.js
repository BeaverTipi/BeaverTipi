window.openModal = function () {
  const modal = document.getElementById("accountModal");
  if (!modal) return;

  modal.style.display = "flex"; // 중심 정렬
  modal.setAttribute("aria-hidden", "false");
  modal.removeAttribute("inert");
  loadBuildingList();
};

window.closeModal = function () {
  const modal = document.getElementById("accountModal");
  if (!modal) return;

  modal.style.display = "none";
  modal.setAttribute("aria-hidden", "true");
  modal.setAttribute("inert", "");
  document.getElementById("accountForm")?.reset();
};

// 건물 목록 불러오기
function loadBuildingList() {
  fetch("/building/account/buildingList")
    .then(res => res.json())
    .then(data => {
      const select = document.getElementById("buildingSelect");
      if (!select) return;

      select.innerHTML = '';

      const defaultOpt = document.createElement("option");
      defaultOpt.text = "건물 선택";
      defaultOpt.disabled = true;
      defaultOpt.selected = true;
      select.appendChild(defaultOpt);

      if (!data || data.length === 0) {
        const emptyOpt = document.createElement("option");
        emptyOpt.text = "등록된 건물이 없습니다";
        emptyOpt.disabled = true;
        select.appendChild(emptyOpt);
        return;
      }

      data.forEach(b => {
        const opt = document.createElement("option");
        opt.value = b.bldgId;
        opt.textContent = b.bldgNm;
        select.appendChild(opt);
      });
    })
    .catch(err => {
      console.error("건물 목록 불러오기 실패", err);
    });
}

// 비정상적인 모달 상태 초기화
function fixBlockedPage() {
  const visibleModal = document.querySelector(".modal.show");
  const hasBackdrop = document.querySelector(".modal-backdrop");

  if (!visibleModal && hasBackdrop) {
    console.warn("🧹 잘못 남은 modal backdrop 제거됨");
    hasBackdrop.parentNode?.removeChild(hasBackdrop);
    document.body.classList.remove("modal-open");
  }

  document.querySelectorAll('.modal[aria-hidden="true"]').forEach(modal => {
    if (modal.id === "accountModal") return; // accountModal은 제외
    modal.style.display = "none";
    modal.setAttribute("inert", "");
    modal.classList.remove("show");
  });
}

// ✅ DOM 로드 시 초기화
document.addEventListener('DOMContentLoaded', () => {
  const active = document.activeElement;
  if (
    active &&
    (
      active.closest(".modal[aria-hidden='true']") ||
      (active.matches(".modal") && active.getAttribute("aria-hidden") === "true")
    )
  ) {
    console.warn("🛑 잘못된 포커스 제거됨:", active);
    active.blur();
  }

  const notiModal = document.getElementById("notificationModal");
  if (notiModal && notiModal.getAttribute("aria-hidden") === "true") {
    notiModal.setAttribute("inert", "");
    notiModal.style.display = "none";
    notiModal.classList.remove("show");
    console.log("✅ notificationModal 초기 inert 처리 완료");
  }

  fixBlockedPage();
});

// 모달 열릴 때 포커스 및 접근성 복원
document.addEventListener("shown.bs.modal", function (e) {
  const modal = e.target;
  modal.removeAttribute("inert");
  modal.removeAttribute("aria-hidden");
  modal.style.pointerEvents = "auto";
  console.log("✅ inert 제거 완료:", modal.id);
});

// 모달 닫힐 때 backdrop 제거
document.addEventListener("hidden.bs.modal", () => {
  console.log("✅ Bootstrap 모달 완전히 닫힘 → 클래스 정리");
  document.body.classList.remove("modal-open");
  document.querySelectorAll(".modal-backdrop").forEach(b => {
    b.parentNode?.removeChild(b); // ✅ Illegal invocation 방지
  });
});
