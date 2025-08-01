function toggleLeaseFields() {
	const type = document.querySelector('#lstgTypeSale')?.value;
	const jeonse = document.querySelector('#jeonseField');
	const wolse = document.querySelector('#wolseField');
	const sale = document.querySelector('#salePriceField');
	const guide = document.querySelector('#tradeTypeGuide');

	if (!jeonse || !wolse || !sale) return;

	jeonse.style.display = 'none';
	wolse.style.display = 'none';
	sale.style.display = 'none';
	guide.style.display = 'none';

	// 선택 안 했을 경우 안내 표시
	if (type === '000') {
		guide.style.display = 'block';
		return;
	}

	if (type === '001') jeonse.style.display = 'block';
	else if (type === '002') wolse.style.display = 'block';
	else if (type === '003') sale.style.display = 'block';
}

// 거래유형 코드(RENT/LEASE/SALE)에 따른 금액 필드 토글
function toggleTradeFields() {
	const tradeType = document.querySelector("select[name='lstgTrdTypeCode']")?.value;
	const deposit = document.querySelector("#depositField")?.closest(".col-md-4");
	const price = document.querySelector("#priceField")?.closest(".col-md-4");
	const mngFee = document.querySelector("#mngFeeField")?.closest(".col-md-4");

	if (!deposit || !price || !mngFee) return;

	if (tradeType === "RENT") {
		deposit.style.display = "block";
		price.style.display = "block";
		mngFee.style.display = "block";
	} else if (tradeType === "LEASE") {
		deposit.style.display = "block";
		price.style.display = "none";
		mngFee.style.display = "block";
	} else if (tradeType === "SALE") {
		deposit.style.display = "none";
		price.style.display = "block";
		mngFee.style.display = "none";
	}
}

// 중개인 선택 요약 표시
function renderSelectedBrokerSummary(selected) {
	const countEl = document.querySelector("#brokerCount");
	const badgeWrapper = document.querySelector("#brokerCountWrapper");
	const noSelectedText = document.querySelector("#noBrokerSelectedText");

	if (!countEl || !badgeWrapper || !noSelectedText) return;

	if (selected.length > 0) {
		countEl.textContent = selected.length;
		badgeWrapper.hidden = false;
		noSelectedText.style.display = "none";

		const tooltipTitle = selected.map(s => `• ${s.label}`).join("\n");
		badgeWrapper.setAttribute("title", tooltipTitle);
		badgeWrapper.setAttribute("data-bs-original-title", tooltipTitle);
		badgeWrapper.setAttribute("data-bs-placement", "bottom"); // 🔽 아래로 표시

		bootstrap.Tooltip.getInstance(badgeWrapper)?.dispose();
		new bootstrap.Tooltip(badgeWrapper);
	} else {
		badgeWrapper.hidden = true;
		noSelectedText.style.display = "inline";
	}
}

function bindAreaUnitToggle() {
	const toggleSupplyBtn = document.querySelector("#toggleSupplyUnit");
	const toggleExclusiveBtn = document.querySelector("#toggleExclusiveUnit");

	const supplyInput = document.querySelector("#supplyAreaField");
	const exclusiveInput = document.querySelector("#exclusiveAreaField");

	const supplyLabel = document.querySelector("#supplyAreaLabel");
	const exclusiveLabel = document.querySelector("#exclusiveAreaLabel");

	const supplyHidden = document.querySelector("input[name='lstgGrArea']");
	const exclusiveHidden = document.querySelector("input[name='lstgExArea']");

	let isSupplyPyeong = true;
	let isExclusivePyeong = true;

	const toM2 = (p) => (p * 3.3058).toFixed(2);
	const toPyeong = (m2) => (m2 / 3.3058).toFixed(2);

	function updateSupply() {
		if (isSupplyPyeong) {
			supplyInput.value = toM2(parseFloat(supplyInput.value || 0));
			supplyLabel.textContent = "공급면적 (m²)";
			toggleSupplyBtn.textContent = "평 ▼ ";
			isSupplyPyeong = false;
		} else {
			supplyInput.value = toPyeong(parseFloat(supplyInput.value || 0));
			supplyLabel.textContent = "공급면적 (평)";
			toggleSupplyBtn.textContent = "㎡ ▼";
			isSupplyPyeong = true;
		}
		updateSupplyHidden();
	}

	function updateExclusive() {
		if (isExclusivePyeong) {
			exclusiveInput.value = toM2(parseFloat(exclusiveInput.value || 0));
			exclusiveLabel.textContent = "전용면적 (m²)";
			toggleExclusiveBtn.textContent = "평 ▼";
			isExclusivePyeong = false;
		} else {
			exclusiveInput.value = toPyeong(parseFloat(exclusiveInput.value || 0));
			exclusiveLabel.textContent = "전용면적 (평)";
			toggleExclusiveBtn.textContent = "㎡ ▼";
			isExclusivePyeong = true;
		}
		updateExclusiveHidden();
	}

	function updateSupplyHidden() {
		const m2Value = isSupplyPyeong ? toM2(parseFloat(supplyInput.value || 0)) : supplyInput.value;
		supplyHidden.value = m2Value;
	}
	function updateExclusiveHidden() {
		const m2Value = isExclusivePyeong ? toM2(parseFloat(exclusiveInput.value || 0)) : exclusiveInput.value;
		exclusiveHidden.value = m2Value;
	}

	toggleSupplyBtn?.addEventListener("click", updateSupply);
	toggleExclusiveBtn?.addEventListener("click", updateExclusive);

	supplyInput?.addEventListener("input", updateSupplyHidden);
	exclusiveInput?.addEventListener("input", updateExclusiveHidden);

	// 초기값 반영
	updateSupplyHidden();
	updateExclusiveHidden();
}

// 중개인 선택 완료 버튼 클릭
function handleBrokerConfirm() {
	const brokerCheckboxes = document.querySelectorAll(".broker-check");
	const selected = Array.from(brokerCheckboxes)
		.filter(cb => cb.checked)
		.map(cb => ({
			id: cb.value,
			label: document.querySelector(`label[for='${cb.id}']`)?.textContent.trim()
		}));

	if (selected.length === 0) {
		Swal.fire({
			icon: "warning",
			title: "중개인 선택 필수",
			text: "1명 이상의 중개인을 선택해주세요.",
			confirmButtonText: "확인"
		});
		return;
	}

	const selectedBrokersDiv = document.querySelector("#selectedBrokers");
	const selectedBrokerInputs = document.querySelector("#selectedBrokerInputs");

	selectedBrokersDiv.innerHTML = ""; // 개별 라벨은 숨김
	selectedBrokerInputs.innerHTML = "";

	selected.forEach(s => {
		const input = document.createElement("input");
		input.type = "hidden";
		input.name = "brokerIds";
		input.value = s.id;
		selectedBrokerInputs.appendChild(input);
	});

	renderSelectedBrokerSummary(selected);

	const modal = bootstrap.Modal.getInstance(document.querySelector("#brokerModal"));
	if (modal) modal.hide();
}

// 전체선택 체크박스 상태 반영
function updateSelectAllCheckbox() {
	const checkboxes = document.querySelectorAll(".broker-check");
	const allChecked = Array.from(checkboxes).every(cb => cb.checked);
	const selectAll = document.querySelector("#selectAllBrokers");
	if (selectAll) selectAll.checked = allChecked;
}

// 중개인 카드 클릭 시 체크 토글
function bindBrokerCardToggle() {
	document.querySelectorAll(".broker-card").forEach(card => {
		card.addEventListener("click", (e) => {
			if (e.target.tagName === "INPUT") return;
			const checkbox = card.querySelector("input[type='checkbox']");
			if (checkbox) {
				checkbox.checked = !checkbox.checked;
				updateSelectAllCheckbox();
			}
		});
	});
}

// 전체선택/개별선택 바인딩
function bindSelectAllCheckbox() {
	const selectAll = document.querySelector("#selectAllBrokers");
	if (!selectAll) return;

	selectAll.addEventListener("change", function() {
		document.querySelectorAll(".broker-check").forEach(cb => cb.checked = this.checked);
	});

	document.querySelectorAll(".broker-check").forEach(cb => {
		cb.addEventListener("change", updateSelectAllCheckbox);
	});
}

// 모달 열기 버튼
document.querySelector("#openBrokerModalBtn")?.addEventListener("click", function(e) {
	const addressInput = document.querySelector("#address");
	const address = addressInput?.value?.trim();

	if (!address || address.length === 0) {
		e.preventDefault();
		Swal.fire("주소 필요", "매물 주소를 먼저 입력해주세요.", "info");
		return;
	}


	loadNearbyBrokers(address).then(() => {
		const modal = new bootstrap.Modal(document.querySelector("#brokerModal"));
		modal.show();
	}).catch(err => {
		Swal.fire("오류", err, "error");
	});
});

// 주소로 중개인 목록을 불러와 렌더링
function loadNearbyBrokers(address) {
	return new Promise((resolve, reject) => {
		kakao.maps.load(() => {
			const geocoder = new kakao.maps.services.Geocoder();

			geocoder.addressSearch(address, function(result, status) {
				if (status !== kakao.maps.services.Status.OK || !result.length) {
					return reject("주소의 위치를 찾을 수 없습니다.");
				}

				const lat = result[0].y;
				const lng = result[0].x;

				// 위경도 hidden 필드 삽입
				const hiddenFields = document.querySelector("#hiddenLocationFields");
				if (hiddenFields) {
					hiddenFields.innerHTML = `
            <input type="hidden" name="lstgLat" value="${lat}">
            <input type="hidden" name="lstgLng" value="${lng}">
          `;
				}

				// 중개인 검색 요청
				axios.post("/ajax/building/broker/list", { lat, lng, radiusKm: 5.0 })
					.then(res => {
						const brokers = res.data;
						const listArea = document.querySelector("#brokerListArea");
						let html = "";

						if (!Array.isArray(brokers) || brokers.length === 0) {
							html = "<p class='text-muted'>반경 5km 내 중개인이 없습니다.</p>";
						} else {
							brokers.forEach(broker => {
								const brokerId = `broker_${broker.mbrCd}`;
								const name = broker.brokNm || "중개사무소";
								const repr = broker.reprNm || "-";
								const tel = broker.reprTelNo || "-";
								const addr = `${broker.brokAddr1 || ""} ${broker.brokAddr2 || ""}`;
								const card = broker.businessCardUrl?.trim();

								html += `
<div class="card p-3 mb-3 broker-item broker-card d-flex align-items-center justify-content-between flex-row" style="cursor:pointer;">
  <div class="form-check me-3 mt-1">
    <input class="form-check-input broker-check" type="checkbox" id="${brokerId}" value="${broker.mbrCd}">
  </div>
  <div class="ml-3 flex-grow-1 pe-3 ms-5">
    <label class="fw-semibold d-block mb-2 ms-4" for="${brokerId}" style="color:#343a40;">
      ${name} <span class="text-muted">(${repr})</span>
    </label>
    <div class="text-body-secondary mb-1">
      <i class="bi bi-telephone-fill text-danger me-1"></i> ${tel}
    </div>
    <div class="text-body-secondary">
      <i class="bi bi-geo-alt-fill text-danger me-1"></i> ${addr}
    </div>
  </div>
  <div style="flex-shrink:0;">
    ${card
										? `<img src="${card}" alt="명함" style="width:200px; height:auto; max-height:120px;" class="border rounded">`
										: `<div class="border rounded p-3 text-center d-flex align-items-center justify-content-center" style="width:200px; height:100px;">
           <small class="text-muted">명함 이미지 없음</small>
         </div>`}
  </div>
</div>`;
							});
						}

						listArea.innerHTML = html;

						// 카드 클릭 시 체크박스 토글
						bindBrokerCardToggle();

						// 전체선택 체크박스 바인딩
						bindSelectAllCheckbox();

						resolve();
					})
					.catch(() => reject("중개인 정보를 불러오는 중 오류가 발생했습니다."));
			});
		});
	});
}

// 선택완료 버튼 이벤트 바인딩
document.querySelector("#confirmBrokerSelection")?.addEventListener("click", handleBrokerConfirm);
document.querySelector("#myBuildingModal").addEventListener("show.bs.modal", function() {
	axios.get("/ajax/building/myList") // API 엔드포인트는 실제 URI에 맞게
		.then(res => {
			const data = res.data;
			const listEl = document.querySelector("#myBuildingListArea");

			if (!Array.isArray(data) || data.length === 0) {
				listEl.innerHTML = `<p class="text-muted">등록된 건물이 없습니다.</p>`;
				return;
			}

			const html = data.map(bldg => `
        <div class="card mb-2 p-3 bldg-item" style="cursor:pointer;" data-id="${bldg.bldgId}">
          <strong>${bldg.bldgNm}</strong><br>
          <small class="text-muted">${bldg.bldgAddr} ${bldg.bldgDtlAddr}</small>
        </div>
      `).join("");

			listEl.innerHTML = html;

			// 카드 클릭 시 해당 건물 정보 채우기
			document.querySelectorAll(".bldg-item").forEach(card => {
				card.addEventListener("click", () => {
					const id = card.getAttribute("data-id");
					showUnitStep(id);
				});
			});
		})
		.catch(() => {
			document.querySelector("#myBuildingListArea").innerHTML = `<p class="text-danger">불러오기 실패</p>`;
		});
});
// 내 건물 모달 제어 스크립트

function loadMyBuildings() {
	const listArea = document.querySelector("#myBuildingListArea");
	listArea.innerHTML = `<p class="text-muted">건물 목록을 불러오는 중입니다...</p>`;

	axios.get("/ajax/building/myList")
		.then(res => {
			const buildings = res.data.data;
			if (!Array.isArray(buildings) || buildings.length === 0) {
				listArea.innerHTML = `<p class="text-muted">보유한 건물이 없습니다.</p>`;
				return;
			}

			let html = '';
			buildings.forEach(b => {
				html += `
          <button type="button" class="list-group-item" data-bldg-id="${b.bldgId}">
            <strong>${b.bldgName ?? '(이름없음)'}</strong>
            <small>${(b.addr1 ?? '')} ${(b.addr2 ?? '')}</small>
          </button>`;
			});
			listArea.innerHTML = html;

			// 바인딩은 이 시점에서 반드시 필요함
			document.querySelectorAll("#myBuildingListArea .list-group-item").forEach(btn => {
				btn.addEventListener("click", () => {
					const bldgId = btn.dataset.bldgId;
					if (!bldgId) return alert("건물 ID가 없습니다.");
					showUnitStep(bldgId);
				});
			});
		})
		.catch(() => {
			listArea.innerHTML = `<p class="text-danger">건물 정보를 불러오는 중 오류가 발생했습니다.</p>`;
		});
}



// 전역 캐시 선언
let unitCache = [];

function showUnitStep(bldgId) {
	document.querySelector("#buildingStep").style.display = "none";
	document.querySelector("#unitStep").style.display = "block";

	const unitArea = document.querySelector("#myUnitListArea");
	unitArea.innerHTML = `<p class="text-muted">유닛 정보를 불러오는 중입니다...</p>`;

	axios.get(`/ajax/building/${bldgId}/units`).then(res => {
		const buildings = res.data;
		const unitArea = document.querySelector("#myUnitListArea");
		console.log("빌딩이둥 :  ", buildings);
		// UnitCache에 flatten된 유닛 리스트 저장하면서 건물 주소 정보 포함
		unitCache = buildings.flatMap(b =>
			(b.unitList || []).map(u => ({
				...u,
				bldgAddr: b.bldgAddr,
				bldgDtlAddr: b.bldgDtlAddr,
				bldgNm: b.bldgNm,
				bldgTypeCode: b.bldgTypeCode,
				bldgZipNo: b.bldgZipNo,

			}))
		);
		if (unitCache.length === 0) {
			unitArea.innerHTML = `<p class="text-muted">등록된 유닛이 없습니다.</p>`;
			return;
		}
		unitCache.sort((a, b) => {
			const aEmpty = !a.mbrCd;
			const bEmpty = !b.mbrCd;
			return aEmpty === bEmpty ? 0 : aEmpty ? -1 : 1;
		});

		// 유닛 버튼 렌더링
		let html = '<div class="list-group">';
		unitCache.forEach(u => {
			html += `
      <button class="list-group-item list-group-item-action text-start" data-unit-id="${u.unitId}">
        <div class="fw-bold">[${u.unitFlrNo ?? '-'}층] ${u.unitRoom ?? '-'}룸</div>
        <div class="text-muted small">
          보증금: ${u.unitDpstAmt?.toLocaleString() ?? '-'} / 월세: ${u.unitDsrMnthRentAmt?.toLocaleString() ?? '-'} / 전세 : ${u.unitDsrSaleAmt?.toLocaleString() ?? '-'}
        </div>
      </button>`;
		});
		html += '</div>';
		unitArea.innerHTML = html;

		// 🔥 버튼 이벤트 바인딩
		document.querySelectorAll("[data-unit-id]").forEach(btn => {
			btn.addEventListener("click", () => {
				const unitId = btn.dataset.unitId;
				applySelectedUnit(unitId);
			});
		});
	}).catch(() => {
		document.querySelector("#myUnitListArea").innerHTML = `<p class="text-danger">유닛 정보를 불러오는 중 오류가 발생했습니다.</p>`;
	});

}

// 뒤로가기 버튼 처리
const backBtn = document.querySelector("#backToBuildings");
if (backBtn) {
	backBtn.addEventListener("click", () => {
		document.querySelector("#unitStep").style.display = "none";
		document.querySelector("#buildingStep").style.display = "block";
	});
}
function applySelectedUnit(unitId) {
	const bldgToLstgMap = {
		"001": { lstg1: "003", lstg2: "008" }, // 아파트 → 공동주택-아파트
		"002": { lstg1: "001", lstg2: "006" }, // 빌라 → 다세대-빌라/연립
		"003": { lstg1: "001", lstg2: "003" }, // 오피스텔
		"004": { lstg1: "001", lstg2: "004" }, // 단독주택
		"005": { lstg1: "004", lstg2: "009" }, // 상가
		"006": { lstg1: "002", lstg2: "007" }, // 오피스빌딩 → 사무실
		"007": { lstg1: "004", lstg2: "009" }, // 상점 → 상가
		"008": null,                            // 기타 → 선택 안함
		"009": { lstg1: "001", lstg2: "001" }   // 다세대주택
	};

	const unit = unitCache.find(u => u.unitId === unitId);
	if (!unit) {
		Swal.fire("오류", "해당 유닛 정보를 찾을 수 없습니다.", "error");
		return;
	}
	console.log("시밤바들아 unit이다 ", unit);
	// 주소 필드
	const addrInput = document.querySelector("input[name='lstgAdd']");
	if (addrInput) addrInput.value = unit.bldgAddr || "";

	const detailAddrInput = document.querySelector("input[name='lstgAdd2']");
	if (detailAddrInput) detailAddrInput.value = unit.bldgDtlAddr || "";

	const postalInput = document.querySelector("input[name='lstgPostal']");
	if (postalInput) postalInput.value = unit.bldgZipNo || "";
	console.log(unit.bldgZipCode);
	// 층/호수 → 상세주소2 (lstgRoomNum)
	const roomNumInput = document.querySelector("input[name='lstgRoomNum']");
	if (roomNumInput) roomNumInput.value = unit.unitRoom || "";

	// 공급면적
	const grInput = document.querySelector("input[name='lstgGrArea']");
	if (grInput) grInput.value = unit.unitCmar || "";

	const grView = document.querySelector("#supplyAreaField");
	if (grView) {
		const py = unit.unitCmar ? (parseFloat(unit.unitCmar) / 3.3058).toFixed(2) : "";
		grView.value = py;
	}

	// 전용면적
	const exInput = document.querySelector("input[name='lstgExArea']");
	if (exInput) exInput.value = unit.unitXuar || "";

	const exView = document.querySelector("#exclusiveAreaField");
	if (exView) {
		const py = unit.unitXuar ? (parseFloat(unit.unitXuar) / 3.3058).toFixed(2) : "";
		exView.value = py;
	}

	// 해당 층수
	const floorInput = document.querySelector("input[name='lstgFloor']");
	if (floorInput) floorInput.value = unit.unitFlrNo || "";

	// 보증금
	const dpstInput = document.querySelector("input[name='lstgLeaseAmt']");
	const depositAmtView = document.querySelector("#depositAmtView");
	if (dpstInput && depositAmtView) {
		const raw = unit.unitDpstAmt || "";
		dpstInput.value = raw;

		const displayUnit = unitState.deposit; // 현재 단위 상태: 억/만원/원
		depositAmtView.value = formatWithComma(convertFromWon(raw, displayUnit));
	}

	// 월세
	const rentInput = document.querySelector("input[name='lstgLeaseM']");
	const mnthRentAmtView = document.querySelector("#mnthRentAmtView");

	if (rentInput && mnthRentAmtView) {
		const raw = unit.unitDsrMnthRentAmt || "";
		rentInput.value = raw;

		const displayUnit = unitState.mnthRent;
		mnthRentAmtView.value = formatWithComma(convertFromWon(raw, displayUnit));
	}

	// 전세가
	const saleInput = document.querySelector("input[name='lstgLease']");
	const jeonseAmtView = document.querySelector("#jeonseAmtView");
	if (saleInput && jeonseAmtView) {
		const raw = unit.unitDsrSaleAmt || "";
		saleInput.value = raw;

		const displayUnit = unitState.jeonse;
		jeonseAmtView.value = formatWithComma(convertFromWon(raw, displayUnit));
	}
	const lstgTitle = document.querySelector("input[name='lstgNm']");
	if (lstgTitle) lstgTitle.value = unit.bldgNm || "";

	const lstgDecs = document.querySelector("textarea[name='lstgDtlDst']");
	if (lstgDecs) lstgDecs.value = unit.unitDtlDescCn || "";


	const mapping = bldgToLstgMap[unit.bldgTypeCode]; // 예: { lstg1: "001", lstg2: "004" }

	if (mapping) {
		// 📌 1차 유형 라디오 선택
		const type1Radio = document.querySelector(`input[name='lstgTypeCode1'][value='${mapping.lstg1}']`);
		if (type1Radio) {
			type1Radio.checked = true;
			type1Radio.dispatchEvent(new Event("change")); // → AJAX로 lstgTypeCode2 라디오 생성됨
		}

		const trySelectLstg2 = setInterval(() => {
			const lstg2Radio = document.querySelector(`input[name="lstgTypeCode2"][value="${mapping.lstg2}"]`);
			if (lstg2Radio) {
				lstg2Radio.checked = true;
				clearInterval(trySelectLstg2);
			}
		}, 100);
	}


	// 모달 닫기
	const modalEl = document.querySelector("#myBuildingModal");
	if (modalEl) {
		const modal = bootstrap.Modal.getInstance(modalEl);
		if (modal) modal.hide();
	}
}
// 현재 단위 상태 저장
const unitState = {
	jeonse: "억",
	deposit: "만원",
	mnthRent: "만원",
	sale: "억"
};

// 단위별 원단위 변환 함수
function convertToWon(value, unit) {
	if (!value) return "";
	const raw = value.toString().replace(/,/g, ""); // 콤마 제거
	const num = Number(raw);
	if (isNaN(num)) return "";
	switch (unit) {
		case "억": return num * 100000000;
		case "만원": return num * 10000;
		case "원": return num;
		default: return num;
	}
}

// 원단위 값을 해당 단위로 환산 (보기용)
function convertFromWon(value, unit) {
	const num = Number(value);
	if (isNaN(num)) return "";
	switch (unit) {
		case "억": return (num / 100000000).toFixed(2);
		case "만원": return (num / 10000).toFixed(0);
		case "원": return num.toFixed(0);
		default: return num;
	}
}

// 콤마 처리
function formatWithComma(value) {
	const num = Number(value);
	if (isNaN(num)) return "";
	return num.toLocaleString();
}

// 입력값 콤마 제거
function parseCommaNumber(value) {
	return value.replace(/,/g, "");
}

// 라벨 업데이트
function updateUnitLabel(type, unit) {
	const label = document.querySelector(`#${type}Label`);
	if (label) {
		const baseText = label.dataset.labelBase || label.textContent;
		label.textContent = `${baseText} (${unit})`;
	}
}

// 단위 토글 버튼 핸들러
function toggleUnit(type) {
	const units = ["억", "만원", "원"];
	const current = unitState[type];
	const nextIndex = (units.indexOf(current) + 1) % units.length;
	const nextUnit = units[nextIndex];
	unitState[type] = nextUnit;

	// 다음 단위 기준으로 버튼 텍스트 설정
	const nextNextUnit = units[(nextIndex + 1) % units.length];
	const btn = document.querySelector(`#${type}UnitBtn`);
	if (btn) btn.textContent = `${nextNextUnit} ▼`;

	const visibleInput = document.querySelector(`#${type}AmtView`);
	const hiddenInput = document.querySelector(`#${type}Amt`);
	if (!visibleInput || !hiddenInput) return;

	const rawWon = hiddenInput.value;
	const converted = convertFromWon(rawWon, nextUnit);
	visibleInput.value = formatWithComma(converted);

	updateUnitLabel(type, nextUnit);
}

// 입력 시 원단위로 변환 후 hidden input에 저장
function handleVisibleInputChange(type) {
	const visibleInput = document.querySelector(`#${type}AmtView`);
	const hiddenInput = document.querySelector(`#${type}Amt`);
	if (!visibleInput || !hiddenInput) return;

	const currentUnit = unitState[type];
	const raw = parseCommaNumber(visibleInput.value);
	const won = convertToWon(raw, currentUnit);

	hiddenInput.value = won;
	visibleInput.value = formatWithComma(raw); // 콤마 다시 붙이기
}

// 필드와 이벤트 바인딩 연결
function bindUnitInputSync(type) {
	const visibleInput = document.querySelector(`#${type}AmtView`);
	if (visibleInput) {
		visibleInput.addEventListener("input", () => handleVisibleInputChange(type));
	}
}
function updateUnitButtonText(type) {
	const units = ["억", "만원", "원"];
	const current = unitState[type];
	const currentIndex = units.indexOf(current);
	const nextIndex = (currentIndex + 1) % units.length;
	const btn = document.querySelector(`#${type}UnitBtn`);
	if (btn) {
		btn.textContent = `${units[nextIndex]} ▼`;
	}
}
function bindOptionSelectAllCheckbox() {
	document.querySelectorAll(".select-all").forEach(selectAllCheckbox => {
		const container = selectAllCheckbox.closest(".form-group");
		if (!container) return;

		const optionCheckboxes = container.querySelectorAll("input.option[type='checkbox']");

		// 전체선택 → 옵션 전체 체크
		selectAllCheckbox.addEventListener("change", function() {
			optionCheckboxes.forEach(cb => {
				cb.checked = this.checked;
			});
		});

		// 개별 체크 시 → 전체선택 체크 여부 업데이트
		optionCheckboxes.forEach(cb => {
			cb.addEventListener("change", () => {
				const total = optionCheckboxes.length;
				const checkedCount = container.querySelectorAll("input.option[type='checkbox']:checked").length;
				selectAllCheckbox.checked = total === checkedCount;
			});
		});

		// 최초 로딩 시 전체선택 체크 여부 설정
		const initiallyChecked = container.querySelectorAll("input.option[type='checkbox']:checked").length;
		selectAllCheckbox.checked = optionCheckboxes.length > 0 && initiallyChecked === optionCheckboxes.length;
	});
}


// 페이지 로드 시 초기 바인딩
document.addEventListener("DOMContentLoaded", () => {
	toggleLeaseFields?.(); // 거래유형 필드 토글 함수 있으면 호출
	toggleTradeFields?.(); // 거래구분 필드 토글 함수 있으면 호출

	document.querySelector("#lstgTypeSale")?.addEventListener("change", toggleLeaseFields);
	document.querySelector("select[name='lstgTrdTypeCode']")?.addEventListener("change", toggleTradeFields);

	// 콤마 및 단위 처리 바인딩
	["jeonse", "deposit", "mnthRent", "sale"].forEach(type => {
		bindUnitInputSync(type);
		updateUnitButtonText(type);
	});

	// 초기화 시 필드 토글 + 이벤트 바인딩
	toggleLeaseFields();
	toggleTradeFields();

	document.querySelector("#lstgTypeSale")?.addEventListener("change", toggleLeaseFields);
	document.querySelector("select[name='lstgTrdTypeCode']")?.addEventListener("change", toggleTradeFields);

	bindBrokerCardToggle();
	bindOptionSelectAllCheckbox();
	bindAreaUnitToggle();
	const type1Radios = document.querySelectorAll("input[name='lstgTypeCode1']");
	const type2Group = document.querySelector("#lstgTypeCode2Group");

	type1Radios.forEach(radio => {
		radio.addEventListener("change", function() {
			const parentCodeValue = this.value;

			axios.get("/ajax/building/product/selectLstg2List", {
				params: {
					lstg1: parentCodeValue
				}
			})
				.then(res => {
					const data = res.data.filter(c => c.codeValue != '000');

					if (!Array.isArray(data) || data.length === 0) {
						type2Group.innerHTML = '<p class="text-muted">해당 유형에 대한 상세 유형이 없습니다.</p>';
						type2Group.hidden = false;
						return;
					}

					// 라디오 버튼 HTML 동적 생성
					let html = '';
					data.forEach(code => {
						html += `
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="radio" name="lstgTypeCode2" id="lstg2_${code.codeValue}" value="${code.codeValue}">
            <label class="form-check-label" for="lstg2_${code.codeValue}">${code.codeName}</label>
          </div>
        `;
					});

					type2Group.innerHTML = html;
					type2Group.hidden = false;
				})
				.catch(() => {
					type2Group.innerHTML = '<p class="text-danger">상세 유형 정보를 불러오지 못했습니다.</p>';
					type2Group.hidden = false;
				});
		});
	});


	const form = document.querySelector("#product-form");

	form.addEventListener("submit", function(e) {
		e.preventDefault(); // 항상 막고 시작

		const lstgRoomNum = document.querySelector("input[name='lstgRoomNum']")?.value?.trim();
		const lstgTypeSale = document.querySelector("#lstgTypeSale")?.value;
		const lstgNm = document.querySelector("input[name='lstgNm']")?.value?.trim();
		const lstgPostal = document.querySelector("input[name='lstgPostal']")?.value?.trim();

		const selectedBrokerInputs = document.querySelectorAll("input[name='brokerIds']");
		const imageInput = document.querySelector("#imageUpload");
		const files = imageInput?.files || [];

		// 필수: 호실 번호
		if (!lstgRoomNum) {
			return Swal.fire({ icon: "warning", title: "호실 번호 입력", text: "호실 번호는 필수입니다." });
		}

		// 필수: 거래 유형
		if (!lstgTypeSale || lstgTypeSale === "000") {
			return Swal.fire({ icon: "warning", title: "거래 유형 선택", text: "거래 유형을 선택해주세요." });
		}

		// 필수: 매물명
		if (!lstgNm) {
			return Swal.fire({ icon: "warning", title: "매물명 입력", text: "매물명을 입력해주세요." });
		}

		// 필수: 주소 (우편번호)
		if (!lstgPostal) {
			return Swal.fire({ icon: "warning", title: "주소 입력", text: "주소 검색을 통해 우편번호를 입력해주세요." });
		}

		// 필수: 중개인 1명 이상
		if (selectedBrokerInputs.length === 0) {
			return Swal.fire({ icon: "warning", title: "중개인 선택 필수", text: "최소 1명의 중개인을 선택해야 합니다." });
		}

		// 필수: 사진 최소 5장, 최대 10장
		if (files.length < 5) {
			return Swal.fire({ icon: "warning", title: "사진 부족", text: "사진은 최소 5장 이상 등록해야 합니다." });
		}
		if (files.length > 10) {
			return Swal.fire({ icon: "warning", title: "사진 초과", text: "사진은 최대 10장까지만 등록할 수 있습니다." });
		}

		// ✅ 전부 통과했을 때 수동 제출
		form.submit();
	});

});
