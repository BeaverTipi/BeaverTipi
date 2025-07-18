document.addEventListener("DOMContentLoaded", function() {
  // === [1] 엘리먼트 캐싱 ===
  var buildingFilter = document.getElementById("buildingFilter");
  var tableBody     = document.querySelector("#tab-detail tbody");
  var addBtn        = document.getElementById("openAddResidentBtn");
  var addModal      = document.getElementById("addResidentModal");
  var idModal       = document.getElementById("idSearchModal");



  // === [2] 모달 닫기 ===
  window.closeAddResidentModal = function() {
    addModal.style.display = "none";
    document.getElementById("manualMemberKeyword").value = "";
    document.getElementById("manualMemberResult").innerHTML = "";
    document.getElementById("manualMbrCd").value = "";
    document.getElementById("manualMoveInDt").value = "";
    document.getElementById("addModalUnitSel").innerHTML = '<option value="">호실 선택</option>';
  };
  window.closeIdSearchModal = function() {
    idModal.style.display = "none";
    document.getElementById("idSearchInput").value = "";
    document.getElementById("idSearchResult").innerHTML = "";
    document.getElementById("selectedVacantMbrCd").value = "";
    window.unitTarget = {};
  };

  // === [3] 건물리스트 로드 ===
  function loadBuildingList() {
    fetch("/building/move-in/buildingList")
      .then(res => res.json())
      .then(list => {
        buildingFilter.innerHTML = '<option value="">전체</option>';
        list.forEach(b => {
          var o = document.createElement("option");
          o.value = b.bldgId;
          o.textContent = b.bldgNm;
          buildingFilter.appendChild(o);
        });
      });
  }
  loadBuildingList();

  // === [4] 입주 리스트 테이블 렌더 ===
  function loadResidentTable(bldgId) {
    tableBody.innerHTML = "";
    if(!bldgId) return;
    fetch("/building/move-in/list/"+bldgId)
      .then(res => res.json())
      .then(list => {
        list.forEach((row, i) => {
  var mbrCd = row.mbrCd || row.MBR_CD || "";
  var vacant = !mbrCd || mbrCd.trim() === "";
  var unitId = row.unitId || row.UNIT_ID;
  var bldgId = row.bldgId || row.BLDG_ID;
  var rentalPtyId = row.rentalPtyId || row.RENTAL_PTY_ID;
  
  console.log("unitId:", unitId, "bldgId:", bldgId, "rentalPtyId:", rentalPtyId);
  var tr = document.createElement("tr");
		  tr.innerHTML =
		    `<td><input type="checkbox"></td>
		     <td>${i+1}</td>
		     <td>${row.mbrNm||row.MBR_NM||"-"}</td>
		     <td>${row.mbrCd||row.MBR_CD||"-"}</td>
		     <td>${row.moveInDt||row.MOVE_IN_DT||"-"}</td>
		     <td>${unitId||"-"}</td>
		     <td><input type="checkbox" ${vacant?"checked":""} disabled>${vacant?"공실":""}</td>
		     <td>
		       ${vacant ?
		          `<button class="add-btn" data-unit-id="${unitId}" data-bldg-id="${bldgId}" data-rental-pty-id="${rentalPtyId}">추가</button>`
		          : `<button class="edit-btn">수정</button><button class="del-btn">삭제</button>`
		        }
		     </td>`;
		  tableBody.appendChild(tr);
		});
      });
  }

  // === [5] 건물 셀렉트 변경시 리스트 다시 로드 ===
  buildingFilter.addEventListener("change", function() {
    loadResidentTable(this.value);
  });

  // === [6] 우상단 "입주민 추가" (직접등록) ===
  addBtn.addEventListener("click", function() {
    addModal.style.display = "block";
    var bSel = document.getElementById("addModalBldgSel");
    fetch("/building/move-in/searchbuildingList")
      .then(res => res.json())
      .then(list => {
        bSel.innerHTML = '<option value="">건물 선택</option>';
        list.forEach(b => {
          var o = document.createElement("option");
          o.value = b.bldgId;
          o.textContent = b.bldgNm;
          bSel.appendChild(o);
        });
       if (list.length > 0) {
        document.getElementById("hiddenRentalPtyId").value = list[0].rentalPtyId;
      }
      });
    document.getElementById("addModalUnitSel").innerHTML = '<option value="">호실 선택</option>';
  });

  // === [7] 직접등록 모달: 건물 선택 → 공실호실 ===
  document.getElementById("addModalBldgSel").addEventListener("change", function() {
    var bldgId = this.value;
    var uSel = document.getElementById("addModalUnitSel");
    uSel.innerHTML = '<option value="">호실 선택</option>';
    if(!bldgId) return;
    fetch("/building/move-in/vacantUnits/"+bldgId)
      .then(res => res.json())
      .then(list => {
        list.forEach(unit => {
          var o = document.createElement("option");
          o.value = unit.unitId;
          o.textContent = unit.unitId;
          uSel.appendChild(o);
        });
      });
  });

  // === [8] 직접등록 회원검색 + 엔터 처리 ===
  var manualSearchBtn = document.getElementById("manualSearchBtn");
  manualSearchBtn.onclick = function() { searchManualMember(); };
  document.getElementById("manualMemberKeyword").addEventListener("keydown", function(e) {
    if(e.key === "Enter") searchManualMember();
  });
  function searchManualMember() {
    var kw = document.getElementById("manualMemberKeyword").value.trim();
    var resultDiv = document.getElementById("manualMemberResult");
    if(!kw) return alert("검색어 입력!");
    resultDiv.innerHTML = "검색 중…";
    fetch("/building/move-in/searchMember?keyword="+encodeURIComponent(kw))
      .then(res => res.json())
      .then(res => {
        var list = res.data||[];
        if(!list.length) { resultDiv.innerHTML = "결과 없음"; return; }
        resultDiv.innerHTML = list.map(m =>
          `<div style='cursor:pointer;padding:5px;' onclick="selectManualMember('${m.mbrCd}','${m.mbrNm}')">${m.mbrNm}(${m.mbrCd})</div>`
        ).join("");
      });
  }
  // 직접등록 회원선택 (전역)
  window.selectManualMember = function(mbrCd, mbrNm){
    document.getElementById("manualMemberResult").innerHTML = mbrNm+"("+mbrCd+") 선택됨";
    document.getElementById("manualMbrCd").value = mbrCd;
    console.log("회원정보 나옴??? manualMbrCd value:", mbrCd);
  };

  // === [9] 직접등록 실제 등록 ===
  document.getElementById("manualConfirmBtn").onclick = function() {
    var bldgId = document.getElementById("addModalBldgSel").value;
    var unitId = document.getElementById("addModalUnitSel").value;
    var mbrCd  = document.getElementById("manualMbrCd").value;
    var rentalPtyId = document.getElementById("hiddenRentalPtyId").value;
    var moveInDt = document.getElementById("manualMoveInDt").value.replace(/-/g,"");
     console.log("나오라고 좀 bldgId:", bldgId, "unitId:", unitId, "mbrCd:", mbrCd, "moveInDt:", moveInDt);
 	 console.log("bldgId 나옴?:", bldgId);
  	console.log("unitId 나옴?:", unitId);
  	console.log("mbrCd 나옴?:", mbrCd);
  	console.log("rentalPtyId 나옴?:", rentalPtyId);

  	console.log("moveInDt 나옴?:", moveInDt);
    if(!bldgId || !unitId || !mbrCd || !moveInDt) { alert("모두 입력!"); return; }
    fetch("/building/move-in/register", {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({bldgId: bldgId, unitId: unitId, mbrCd: mbrCd, moveInDt: moveInDt, rentalPtyId: rentalPtyId})
    })
    .then(r => r.text())
    .then(msg => {
      if(msg==="SUCCESS") {
        alert("등록 완료");
        closeAddResidentModal();
        loadResidentTable(buildingFilter.value);
      } else {
        alert("등록 실패");
      }
    });
  };

  // === [10] 테이블 내 공실 "추가" 버튼 클릭 (동적 위임) ===
  tableBody.onclick = function(e) {
    if(e.target.classList.contains("add-btn")) {
      var btn = e.target;
      window.unitTarget = {
        unitId: btn.getAttribute("data-unit-id"),
        bldgId: btn.getAttribute("data-bldg-id"),
        rentalPtyId: btn.getAttribute("data-rental-pty-id")
      };
      idModal.style.display = "block";
      document.getElementById("idSearchInput").value = "";
      document.getElementById("idSearchResult").innerHTML = "";
      document.getElementById("selectedVacantMbrCd").value = "";
      document.getElementById("hiddenRentalPtyId").value = window.unitTarget.rentalPtyId;
    }
  };

  // === [11] 공실모달 회원 검색 + 엔터 처리 ===
  var idSearchBtn = document.getElementById("idSearchBtn");
  idSearchBtn.onclick = function() { searchMemberForVacant(); };
  document.getElementById("idSearchInput").addEventListener("keydown", function(e) {
    if(e.key === "Enter") searchMemberForVacant();
  });
  function searchMemberForVacant() {
    var kw = document.getElementById("idSearchInput").value.trim();
    var resultDiv = document.getElementById("idSearchResult");
    if(!kw) return alert("검색어 입력!");
    resultDiv.innerHTML = "검색 중…";
    fetch("/building/move-in/searchMember?keyword="+encodeURIComponent(kw))
      .then(res => res.json())
      .then(res => {
        var list = res.data||[];
        if(!list.length) { resultDiv.innerHTML = "결과 없음"; return; }
        resultDiv.innerHTML = list.map(m =>
          `<div style='cursor:pointer;padding:5px;' onclick="selectMemberForVacant('${m.mbrCd}','${m.mbrNm}')">${m.mbrNm}(${m.mbrCd})</div>`
        ).join("");
      });
  }
  window.selectMemberForVacant = function(mbrCd, mbrNm){
    document.getElementById("idSearchResult").innerHTML = mbrNm+"("+mbrCd+") 선택됨";
    document.getElementById("selectedVacantMbrCd").value = mbrCd;
  };

  // === [12] 공실모달 등록 ===
  document.getElementById("confirmSearchBtn").onclick = function() {
    var unitId = window.unitTarget.unitId;
    var bldgId = window.unitTarget.bldgId;
    var rentalPtyId = document.getElementById("hiddenRentalPtyId").value;
    var mbrCd  = document.getElementById("selectedVacantMbrCd").value;
    if(!unitId || !bldgId || !mbrCd) { alert("모두 선택!"); return; }
    var moveInDt = prompt("입주일 (YYYYMMDD)");
    if(!/^\d{8}$/.test(moveInDt)) { alert("입주일 8자리로 입력"); return; }
    fetch("/building/move-in/register", {
      method: "POST",
      headers: {"Content-Type":"application/json"},
      body: JSON.stringify({bldgId: bldgId, unitId: unitId, mbrCd: mbrCd, moveInDt: moveInDt, rentalPtyId: rentalPtyId})
    })
    .then(r => r.text())
    .then(msg => {
      if(msg==="SUCCESS") {
        alert("등록 완료");
        closeIdSearchModal();
        loadResidentTable(buildingFilter.value);
      } else {
        alert("등록 실패");
      }
    });
  };

  // === [13] 히든 input 동적 생성 (모달 안에 없으면 추가) ===
  if(!document.getElementById("manualMbrCd")){
    var hidden1 = document.createElement("input");
    hidden1.type = "hidden";
    hidden1.id = "manualMbrCd";
    document.getElementById("addResidentModal").appendChild(hidden1);
  }
  if(!document.getElementById("selectedVacantMbrCd")){
    var hidden2 = document.createElement("input");
    hidden2.type = "hidden";
    hidden2.id = "selectedVacantMbrCd";
    document.getElementById("idSearchModal").appendChild(hidden2);
  }

  // === [14] 최초 로드시 자동 진입 ===
  if(buildingFilter.value) loadResidentTable(buildingFilter.value);
  
    // [모달 건물-호실 셀렉트 연동]
  var bldgSel = document.getElementById("addModalBldgSel");
  var unitSel = document.getElementById("addModalUnitSel");
  if (bldgSel && unitSel) {
    bldgSel.addEventListener("change", function() {
      var bldgId = this.value;
      unitSel.innerHTML = '<option value="">호실 선택</option>';
      if(!bldgId) return;
      fetch("/building/move-in/vacantUnits/" + bldgId)
        .then(res => res.json())
        .then(list => {
          list.forEach(function(unit){
            var uid = unit.unitId || unit.UNIT_ID;
            if (!uid) return;
            var o = document.createElement("option");
            o.value = uid;
            o.textContent = uid;
            unitSel.appendChild(o);
            console.log("[모달] option 추가:", uid);
          });
           console.log("호실목록 가져와라 :", unitSel.innerHTML);
        });
        
    });
  }

});
