document.addEventListener('DOMContentLoaded', () => {
  function openModal() {
    document.getElementById("accountModal").style.display = "flex";
    loadBuildingList();
  }
  function closeModal() {
    document.getElementById("accountModal").style.display = "none";
    document.getElementById("accountForm").reset();
  }
  function loadBuildingList() {
    fetch("/building/account/buildingList")
      .then(res => res.json())
      .then(data => {
        const select = document.getElementById("buildingSelect");
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

  const addBtn = document.querySelector('.add-button');
  if (addBtn) {
    addBtn.addEventListener('click', openModal);
  }

  const closeBtn = document.querySelector('.close');
  if (closeBtn) {
    closeBtn.addEventListener('click', closeModal);
  }
});
