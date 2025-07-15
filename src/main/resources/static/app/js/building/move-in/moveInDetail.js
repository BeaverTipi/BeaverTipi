/**
 * 
 */
document.addEventListener("DOMContentLoaded",()=>{
    const ctx = document.querySelector('#vacancyChart').getContext('2d');

    const vacancyChart = new Chart(ctx, {
      type: 'doughnut',
      data: {
        labels: ['공실', '입주'],
        datasets: [{
          data: [30, 70],  // 예시: 공실 30%, 입주 70%
          backgroundColor: ['#e74c3c', '#2ecc71']
        }]
      },
      options: {
        plugins: {
          legend: {
            position: 'bottom'
          }
        }
      }
    });
})


// 탭 전환
const tabBtns = document.querySelectorAll(".tab-btn");
const tabContents = document.querySelectorAll(".tab-content");

tabBtns.forEach(btn => {
  btn.addEventListener("click", () => {
    tabBtns.forEach(b => b.classList.remove("active"));
    tabContents.forEach(tc => tc.classList.remove("active"));
    btn.classList.add("active");
    document.getElementById("tab-" + btn.dataset.tab).classList.add("active");
  });
});

// 추가 모달 열기
document.addEventListener("click", function(e) {
  if (e.target.classList.contains("add-btn")) {
    document.getElementById("idSearchModal").style.display = "block";
  }

if (e.target.classList.contains("edit-btn")) {
  const row = e.target.closest("tr");

  editTarget = {
    unitId: row.dataset.unitId,
    bldgId: row.dataset.bldgId,
    rentalPtyId: row.dataset.rentalPtyId,
    mbrCd: row.dataset.mbrCd
  };

  document.getElementById("editMoveIn").value = row.querySelector("td:nth-child(5)").innerText || "";
  document.getElementById("editMoveOut").value = ""; // 퇴거일 비움

  document.getElementById("editModal").style.display = "block";
}
});

// ID 검색 모달 꺼
function closeModal() {
  document.getElementById("idSearchModal").style.display = "none";
}


function closeEditModal() {
  document.getElementById("editModal").style.display = "none";
}

let editTarget = {}; // 수정 대상 row의 식별자 정보 저장

document.getElementById("editSaveBtn").addEventListener("click", function() {
  const moveInDt = document.getElementById("editMoveIn").value;
  const moveOutDt = document.getElementById("editMoveOut").value;

  const payload = Object.assign({}, editTarget, { moveInDt, moveOutDt });

  fetch("/building/move-in/update", {
    method: "POST",
    headers: {
      "Content-Type": "application/json"
    },
    body: JSON.stringify(payload)
  })
    .then(res => res.text())
    .then(msg => {
      if (msg === "SUCCESS") {
        alert("수정 완료!");
        location.reload();
      } else {
        alert("수정 실패");
      }
    });
});


