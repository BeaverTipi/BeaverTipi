/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 19.     	     김재윤            최초작성
 * </pre>
 */

// 🔹 세대 선택 팝업 열기
function openUnitPopup() {
  const bldgId = document.querySelector('select[name="bldgId"]').value;
  if (!bldgId) {
    alert("건물을 먼저 선택해주세요.");
    return;
  }

  const url = `/building/accountBill/unitPopup?bldgId=${encodeURIComponent(bldgId)}&popup=true`;
  window.open(url, "unitPopup", "width=600,height=400");
}

// 🔹 입주민 입력 블록 추가
function addResidentBlock(id, bldgNm) {
  const html = `
    <div class="resident-block" id="resident_${id}">
      <div class="resident-header">
        <h4>${bldgNm} </h4>
        <div>
          <button class="btn-fetch" onclick="loadUsage('${id}')">사용량 불러오기</button>
          <button class="btn-delete" onclick="removeResident('${id}')">삭제</button>
        </div>
      </div>
      <div class="charge-row">
        <div class="charge-item"><label>가스 사용량</label><input name="gasUsage_${id}"></div>
        <div class="charge-item"><label>가스 요금</label><input name="gasFee_${id}"></div>
      </div>
      <div class="charge-row">
        <div class="charge-item"><label>수도 사용량</label><input name="waterUsage_${id}"></div>
        <div class="charge-item"><label>수도 요금</label><input name="waterFee_${id}"></div>
      </div>
      <div class="charge-row">
        <div class="charge-item"><label>전기 사용량</label><input name="electricUsage_${id}"></div>
        <div class="charge-item"><label>전기 요금</label><input name="electricFee_${id}"></div>
      </div>
    </div>
  `;
  document.getElementById("residentContainer").insertAdjacentHTML("beforeend", html);
}

// 🔹 입주민 블록 제거
function removeResident(id) {
  document.getElementById(`resident_${id}`)?.remove();
}

// 🔹 단일 사용량 불러오기 (컨트롤러 연동)
function loadUsage(id) {
  fetch(`/building/accountBill/usage?unitId=${id}`)
    .then(res => res.json())
    .then(list => {
      if (!list || list.length === 0) return;

      const block = document.getElementById(`resident_${id}`);
      const usage = list[0]; // 단일 DTO로 가정

      block.querySelector(`input[name="gasUsage_${id}"]`).value = usage.gasUsage || "";
      block.querySelector(`input[name="gasFee_${id}"]`).value = usage.gasFee || "";
      block.querySelector(`input[name="waterUsage_${id}"]`).value = usage.waterUsage || "";
      block.querySelector(`input[name="waterFee_${id}"]`).value = usage.waterFee || "";
      block.querySelector(`input[name="electricUsage_${id}"]`).value = usage.electricUsage || "";
      block.querySelector(`input[name="electricFee_${id}"]`).value = usage.electricFee || "";
    })
    .catch(err => {
      console.error("사용량 조회 실패", err);
      alert("사용량을 불러오는 데 실패했습니다.");
    });
}

// 🔹 전체 사용량 불러오기 (단순 Alert → 연동 가능성 있음)
function loadAllUsage() {
  alert("전체 세대 사용량 불러오기 실행");

}

// 🔹 건물 / 계좌 목록 초기 로딩
function fetchBuildings() {
  fetch("/building/accountBill/buildings")
    .then(res => res.json())
    .then(list => {
      const sel = document.querySelector('select[name="bldgId"]');
      sel.innerHTML = '<option value="">선택</option>';
      list.forEach(b => {
        const opt = document.createElement("option");
        opt.value = b.bldgId;
        opt.textContent = b.bldgNm;
        sel.appendChild(opt);
      });

      sel.addEventListener("change", () => {
        const container = document.getElementById("residentContainer");
        if (container) container.innerHTML = "";
      });
    });
}

function fetchAccounts() {
  fetch("/building/accountBill/accounts")
    .then(res => res.json())
    .then(list => {
      const sel = document.querySelector('select[name="depositAccount"]');
      sel.innerHTML = '<option value="">선택</option>';
      list.forEach(acc => {
        const opt = document.createElement("option");
        opt.value = acc.accNum; // 🔥 실제 필드 이름
        opt.textContent = `${acc.accBank} ${acc.accNum}`; // 🔥 accBank + accNum
        sel.appendChild(opt);
      });
    });
}

// 🔹 페이지 로딩 시 자동 실행
document.addEventListener("DOMContentLoaded", () => {
  fetchBuildings();
  fetchAccounts();
});