/**
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 25.     	     김재윤           최초작성
 * 2025. 7. 28.				 김재윤 			상세정보 추가
 * </pre>
 */

const rentalPtyId = window.rentalPtyId;
let currentPage = 1;
const pageSize = 15;

// 페이지 변경
function changePage(page) {
	currentPage = page;
	loadPaymentData();
}

// 페이지네이션 출력
function renderPagination(totalPages) {
	const container = document.getElementById("paginationContainer");
	container.innerHTML = "";

	const maxVisible = 5;
	const half = Math.floor(maxVisible / 2);
	let startPage = Math.max(1, currentPage - half);
	let endPage = Math.min(totalPages, startPage + maxVisible - 1);
	if (endPage - startPage + 1 < maxVisible) {
		startPage = Math.max(1, endPage - maxVisible + 1);
	}

	const createBtn = (label, target, isActive = false, isNav = false) => {
		const btn = document.createElement("button");
		btn.type = "button";
		btn.innerText = label;
		btn.className = "page-btn";
		if (isNav) btn.classList.add("nav-btn");
		if (isActive) btn.classList.add("active");
		btn.onclick = () => changePage(target);
		return btn;
	};

	if (startPage > 1) container.appendChild(createBtn("◀ 이전", startPage - 1, false, true));
	for (let i = startPage; i <= endPage; i++) {
		container.appendChild(createBtn(i, i, i === currentPage));
	}
	if (endPage < totalPages) container.appendChild(createBtn("다음 ▶", endPage + 1, false, true));
}

// 요약 데이터 불러오기
async function loadSummaryData(chgbillChargeMonth) {
	if (!chgbillChargeMonth) return;

	const res = await fetch(`/building/payments/receipt/list/history/summary?chgbillChargeMonth=${chgbillChargeMonth}`);
	const summaryList = await res.json();

	const summaryMap = {};
	summaryList.forEach(item => {
		const status = item.CHGBILL_STATUS || item.chgbill_status;
		const count = item.COUNT ?? item.count ?? 0;
		const amount = item.AMOUNT ?? item.amount ?? 0;
		if (status) {
			summaryMap[status] = { count, amount };
		}
	});

	const unpaid = summaryMap["001"] || { count: 0, amount: 0 };
	const paid = summaryMap["002"] || { count: 0, amount: 0 };
	const late = summaryMap["004"] || { count: 0, amount: 0 };

	const totalCount = unpaid.count + paid.count + late.count;
	const totalAmount = unpaid.amount + paid.amount + late.amount;

	document.getElementById("summaryTotalCount").innerText = `${totalCount}건`;
	document.getElementById("summaryTotalAmount").innerText = `${totalAmount.toLocaleString()}원`;
	document.getElementById("summaryPaidCount").innerText = `${paid.count}건`;
	document.getElementById("summaryPaidAmount").innerText = `${paid.amount.toLocaleString()}원`;
	document.getElementById("summaryUnpaidCount").innerText = `${unpaid.count}건`;
	document.getElementById("summaryUnpaidAmount").innerText = `${unpaid.amount.toLocaleString()}원`;
	document.getElementById("summaryLateCount").innerText = `${late.count}건`;
	document.getElementById("summaryLateAmount").innerText = `${late.amount.toLocaleString()}원`;
}

// 페이지 번호 기준 결제내역 리스트 불러오기
async function loadPaymentData(chgbillStatus = "") {
	const bldgId = document.getElementById("bldgId").value;
	const unitRoom = document.getElementById("unitRoom").value;
	const rawMonth = document.getElementById("chgbillChargeMonth").value;
	const chgbillChargeMonth = rawMonth.replace("-", "");
	const chgbillDueStartDate = document.getElementById("chgbillDueStartDate").value;
	const chgbillDueEndDate = document.getElementById("chgbillDueEndDate").value;

	const params = new URLSearchParams({
		rentalPtyId, bldgId, unitRoom, chgbillStatus,
		chgbillChargeMonth, chgbillDueStartDate, chgbillDueEndDate,
		page: currentPage, pageSize
	});

	const statusMap = { "001": "미납", "002": "완납", "004": "연체" };
	const res = await fetch("/building/payments/receipt/list/history?" + params);
	const { billList, pagination } = await res.json();

	const tbody = document.querySelector("#paymentTable tbody");
	tbody.innerHTML = "";
	billList.forEach((item, idx) => {
		const row = document.createElement("tr");
		const cells = [
			(idx + 1) + ((currentPage - 1) * pageSize),
			item.bldgNm ?? '-', item.unitRoom ?? '-',
			item.mbrNm ?? '-', item.chgbillAmount?.toLocaleString() ?? '-',
			item.chgbillPayAmount?.toLocaleString() ?? '-',
			statusMap[item.chgbillStatus] ?? item.chgbillStatus,
			item.chgbillDate ?? '-', item.chgbillDueDate ?? '-',
			item.chgbillPaidDate && item.chgbillPaidDate !== "0" ? item.chgbillPaidDate : '-',
			item.chgbillAccNum ?? '-'
		];
		cells.forEach(txt => {
			const td = document.createElement("td");
			td.innerText = txt;
			row.appendChild(td);
		});
		row.addEventListener("click", () => {
			openDetailModal(item);
		});
		tbody.appendChild(row);
	});

	renderPagination(pagination.totalPageCount);
}

// 현재 페이지 변경 시 재조회
function changePage(pageNo, status = "") {
	currentPage = pageNo;
	loadPaymentData(status);
}

// 건물 리스트 불러오기
function loadBuildings() {
	const buildingSelect = document.getElementById("bldgId");
	if (!buildingSelect) return;

	fetch("/building/payments/receipt/list/history/buildings?rentalPtyId=" + rentalPtyId)
		.then(res => res.json())
		.then(data => {
			buildingSelect.innerHTML = `<option value>전체</option>`;
			data.forEach(({ bldgId, bldgNm }) => {
				const option = document.createElement("option");
				option.value = bldgId;
				option.textContent = bldgNm;
				buildingSelect.appendChild(option);
			});

			const selected = buildingSelect.value;
			if (selected) loadUnits(selected);
			changePage(1);
		})
		.catch(err => console.error("건물 리스트 호출 실패", err));
}

// 유닛 리스트 불러오기
function loadUnits(bldgId) {
	const unitSelect = document.getElementById("unitRoom");
	if (!unitSelect) return;

	fetch(`/building/payments/receipt/list/history/units?bldgId=${bldgId}`)
		.then(res => res.json())
		.then(data => {
			const sorted = data.sort((a, b) => a.unitRoom - b.unitRoom);
			unitSelect.innerHTML = `<option value>전체</option>`;
			sorted.forEach(({ unitRoom }) => {
				const option = document.createElement("option");
				option.value = unitRoom;
				option.textContent = unitRoom;
				unitSelect.appendChild(option);
			});
		})
		.catch(err => console.error("유닛 리스트 호출 실패", err));
}

// 필터 이벤트 연결
document.getElementById("bldgId")?.addEventListener("change", (e) => {
	const buildingCode = e.target.value;
	document.getElementById("unitRoom").value = "";
	loadUnits(buildingCode);
	changePage(1);
});

document.getElementById("unitRoom")?.addEventListener("change", () => {
	changePage(1);
});

document.getElementById("chgbillChargeMonth")?.addEventListener("change", function() {
	const rawMonth = this.value;
	if (!rawMonth) return;
	const [year, monthStr] = rawMonth.split("-");
	document.getElementById("monthHeader").innerText = `📅 ${year}년 ${monthStr}월 납부현황`;

	const chgbillChargeMonth = rawMonth.replace("-", "");
	loadSummaryData(chgbillChargeMonth);
	changePage(1);
});

document.querySelectorAll("#chgbillDueStartDate, #chgbillDueEndDate")
	.forEach(el => el.addEventListener("change", () => changePage(1)));

// 카드 클릭 이벤트: 상태 필터로 동작
document.querySelectorAll(".summary-card").forEach(card => {
	card.addEventListener("click", () => {
		const status = card.dataset.status;

		// 전체 카드에서 active 제거
		document.querySelectorAll(".summary-card").forEach(c => c.classList.remove("active"));

		// 현재 선택된 카드만 active 추가
		card.classList.add("active");

		changePage(1, status);
	});
});

// 테이블 정렬 기능
document.querySelectorAll("th.sortable").forEach((header, i) => {
	header.addEventListener("click", () => {
		const table = document.getElementById("paymentTable");
		const rows = Array.from(table.tBodies[0].rows);
		const asc = header.dataset.order !== "asc";

		rows.sort((a, b) => {
			let v1 = a.cells[i].innerText;
			let v2 = b.cells[i].innerText;
			const isNumber = !isNaN(parseFloat(v1)) && !isNaN(parseFloat(v2));
			if (isNumber) {
				v1 = parseFloat(v1);
				v2 = parseFloat(v2);
			}
			return (v1 > v2 ? 1 : v1 < v2 ? -1 : 0) * (asc ? 1 : -1);
		});

		rows.forEach(r => table.tBodies[0].appendChild(r));
		document.querySelectorAll("th.sortable").forEach(th => delete th.dataset.order);
		header.dataset.order = asc ? "asc" : "desc";
	});
});

// 초기 로딩 처리
document.addEventListener("DOMContentLoaded", () => {
	loadBuildings(); // 건물 및 유닛 리스트 초기화

	const rawMonth = document.getElementById("chgbillChargeMonth")?.value;
	if (rawMonth) {
		const chgbillChargeMonth = rawMonth.replace("-", "");
		loadSummaryData(chgbillChargeMonth); // 요약 초기 호출
	}

	changePage(1); // 리스트 초기 호출
});
function openDetailModal(item) {
  const params = new URLSearchParams({
    chgbillChargeMonth: item.chgbillChargeMonth,
    unitId: item.unitId
  });

  fetch(`/building/payments/receipt/list/history/details?${params.toString()}`)
    .then(res => res.text())
    .then(html => {
      const wrapper = document.getElementById("billDetailModalWrapper");
      wrapper.innerHTML = html;
      wrapper.style.display = "block";
    })
    .catch(err => console.error("상세 모달 로딩 실패", err));
}

function closeModal() {
  document.getElementById('billDetailModalWrapper').style.display = 'none';
}


