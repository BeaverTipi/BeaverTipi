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

// 🔹 입주민 블록 추가
function addResidentBlock(id, ho) {
	if (document.getElementById(`resident_${id}`)) return;

	const html = `
    <div class="resident-block" id="resident_${id}">
      <div class="resident-header">
        <h4>${ho}호</h4>
        <small id="usageMonth_${id}" class="usage-month"></small>
        <div>
          <button type="button" class="btn-fetch" data-id="${id}">사용량 불러오기</button>
          <button type="button" class="btn-delete" data-id="${id}">삭제</button>
        </div>
      </div>

      ${["gas", "water", "electric"].map(type => `
        <div class="charge-row">
          <div class="charge-item">
            <label>${type === "gas" ? "가스" : type === "water" ? "수도" : "전기"} 사용량</label>
            <input name="${type}Usage_${id}">
          </div>
          <div class="charge-item">
            <label>${type === "gas" ? "가스" : type === "water" ? "수도" : "전기"} 요금</label>
            <input name="${type}Fee_${id}">
          </div>
        </div>
      `).join("")}

      <div class="charge-row">
        <label>세대 설명</label>
        <textarea name="desc_${id}" rows="2" placeholder="예: 전기료 7천원, 수도료 5천원 등"></textarea>
      </div>
    </div>
  `;

	const container = document.getElementById("residentContainer");
	container.insertAdjacentHTML("beforeend", html);

	container.querySelector(`.btn-fetch[data-id="${id}"]`)
		.addEventListener("click", () => loadUsage(id));
	container.querySelector(`.btn-delete[data-id="${id}"]`)
		.addEventListener("click", () => removeResident(id));
}

// 🔹 입주민 블록 제거
function removeResident(id) {
	document.getElementById(`resident_${id}`)?.remove();
}

// 🔹 단일 / 다건 통합 조회 함수
async function loadUsageData(unitIds) {
	const query = unitIds.map(id => `unitIds=${encodeURIComponent(id)}`).join("&");
	const res = await fetch(`/building/accountBill/usage?${query}`);
	if (!res.ok) throw new Error(`HTTP ${res.status}`);
	return await res.json();
}

// 🔹 단일 사용량 불러오기
async function loadUsage(id) {
	const block = document.getElementById(`resident_${id}`);
	const btn = block.querySelector(`.btn-fetch[data-id="${id}"]`);

	btn.disabled = true;
	btn.textContent = "불러오는 중...";

	try {
		const list = await loadUsageData([id]);
		if (!list || list.length === 0) {
			alert("사용량 데이터가 없습니다.");
			return;
		}

		bindUsage(id, list);
	} catch (err) {
		console.error("단건 조회 실패:", err);
		alert("사용량을 불러오는 데 실패했습니다.");
	} finally {
		btn.disabled = false;
		btn.textContent = "사용량 불러오기";
	}
}

// 🔹 전체 사용량 불러오기
async function loadAllUsage() {
	const ids = [...document.querySelectorAll(".resident-block")]
		.map(el => el.id.replace("resident_", ""));

	try {
		const list = await loadUsageData(ids);
		const groupMap = {};

		list.forEach(item => {
			if (!groupMap[item.unitId]) groupMap[item.unitId] = [];
			groupMap[item.unitId].push(item);
		});

		Object.entries(groupMap).forEach(([id, data]) => {
			bindUsage(id, data);
		});
	} catch (err) {
		console.error("전체 조회 실패:", err);
		alert("전체 사용량을 불러오는 데 실패했습니다.");
	}
}
// 🔹 DOM 바인딩
function bindUsage(id, dataList) {
	const block = document.getElementById(`resident_${id}`);
	if (!block || !dataList) return;

	const monthElem = document.getElementById(`usageMonth_${id}`);
	if (dataList[0].dumMonth) {
		monthElem.textContent = `${dataList[0].dumMonth} 사용량`;
	}

	["electric", "water", "gas"].forEach(type => {
		block.querySelector(`input[name="${type}Usage_${id}"]`).value = "";
		block.querySelector(`input[name="${type}Fee_${id}"]`).value = "";
	});

	dataList.forEach(({ dumComp, usageValue, unitChargeInfo }) => {
		const map = {
			"001": "electric",
			"002": "water",
			"003": "gas"
		};
		const type = map[dumComp];
		if (!type) return;

		block.querySelector(`input[name="${type}Usage_${id}"]`).value = usageValue;
		block.querySelector(`input[name="${type}Fee_${id}"]`).value = unitChargeInfo;
	});
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
				opt.dataset.pty = b.rentalPtyId;
				sel.appendChild(opt);
			});
			sel.addEventListener("change", () => {
				document.getElementById("residentContainer").innerHTML = "";
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
				opt.value = acc.accNum;
				opt.textContent = `${acc.accBank} ${acc.accNum}`;
				sel.appendChild(opt);
			});
		});
}

// 🔹 청구 저장 기능 추가 
async function saveChargeData() {
	const feeCodeMap = {
		cleanFee: "001",
		elevatorFee: "002",
		publicElectricFee: "003",
		publicWaterFee: "004",
		operationFee: "005",
		guardFee: "006",
		disinfectionFee: "007",
		supplyFee: "008",
		fireSafetyFee: "009",
		securityFee: "010"
	};

	const bldgSelect = document.querySelector('select[name="bldgId"]');
	const bldgId = bldgSelect.value;
	const rentalPtyId = bldgSelect.options[bldgSelect.selectedIndex]?.dataset?.pty || "";

	const rawDueDate = document.querySelector('input[name="dueDate"]')?.value.trim() || "";
	const chgbillAccNum = document.querySelector('select[name="depositAccount"]')?.value || "";
	const chgbillGlobalDesc = document.querySelector('textarea[name="globalDesc"]')?.value.trim() || "";

	const residentBlocks = document.querySelectorAll('.resident-block');

	const chargeBillList = [];
	const energyUsageList = [];
	const intgfeeList = [];

	residentBlocks.forEach(block => {
		const unitId = block.id.replace("resident_", "");
		const chgbillPersonalDesc = block.querySelector(`textarea[name="desc_${unitId}"]`)?.value.trim() || "";
		const chgbillDesc = `${chgbillGlobalDesc}\n${chgbillPersonalDesc}`.trim();

		let chgbillAmount = 0;

		//  개인 에너지 사용 금액 합산
		["gas", "water", "electric"].forEach(type => {
			const usageQty = parseFloat(block.querySelector(`input[name="${type}Usage_${unitId}"]`)?.value || "0");
			const chargeAmt = parseInt(block.querySelector(`input[name="${type}Fee_${unitId}"]`)?.value.replace(/,/g, "") || "0", 10);
			chgbillAmount += chargeAmt;

			energyUsageList.push({
				unitId,
				bldgId,
				rentalPtyId,
				dumComp: type === "gas" ? "003" : type === "water" ? "002" : "001",
				totalEnergyUsageQty: usageQty,
				totalEnergyChargeAmt: chargeAmt
			});
		});

		// 공용 관리비 사용 금액 합산
		Object.entries(feeCodeMap).forEach(([field]) => {
			const value = parseInt(document.querySelector(`input[name="${field}"]`)?.value || "0", 10);
			chgbillAmount += value;
		});
		// 관리비 항목 리스트 (세대별 생성)
		Object.entries(feeCodeMap).forEach(([field, code]) => {
			const value = parseInt(document.querySelector(`input[name="${field}"]`)?.value || "0", 10);
			intgfeeList.push({
				intManFeeCd: code,
				intgFeeAmount: value,
				unitId,
				bldgId,
				rentalPtyId
			});
		});

		const billEntry = {
			rentalPtyId,
			unitId,
			bldgId,
			chgbillAmount,
			chgbillDesc,
			chgbillAccNum
		};

		if (rawDueDate !== "") {
			billEntry.chgbillDueDate = rawDueDate;
		}

		chargeBillList.push(billEntry);
	});

	const payload = {
		chargeBillList,
		energyUsageList,
		intgfeeList
	};

	console.log("payload to send:", JSON.stringify(payload, null, 2));

	try {
		const res = await fetch("/building/accountBill/create", {
			method: "POST",
			headers: { "Content-Type": "application/json" },
			body: JSON.stringify(payload)
		});

		if (!res.ok) throw new Error(`HTTP ${res.status}`);
		alert("청구 저장 완료!");
		location.href = "/building/payments/receipt/list"
	} catch (err) {
		console.error("저장 실패:", err);
		alert("저장 중 오류 발생");
	}
}

// 🔹 페이지 로딩 시 초기 실행
document.addEventListener("DOMContentLoaded", () => {
	fetchBuildings();
	fetchAccounts();
});

function validateForm() {
  const bldgId = document.querySelector('select[name="bldgId"]').value;
  if (!bldgId) {
    alert("건물을 선택해주세요.");
    return false;
  }

  const depositAccount = document.querySelector('select[name="depositAccount"]').value;
  if (!depositAccount) {
    alert("계좌를 선택해주세요.");
    return false;
  }

  const dueDate = document.querySelector('input[name="dueDate"]').value.trim();
  if (!dueDate) {
    alert("납기일을 입력해주세요.");
    return false;
  }

  const residentBlocks = document.querySelectorAll('.resident-block');
  if (residentBlocks.length === 0) {
    alert("세대를 하나 이상 추가해주세요.");
    return false;
  }

  return true;
}