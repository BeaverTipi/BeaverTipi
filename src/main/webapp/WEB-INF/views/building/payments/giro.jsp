<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리비 발행</title>
  <style>
    body {
      padding: 1rem;
      font-family: sans-serif;
      font-size: 13px;
    }
    .top-controls {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
    }
    .left-buttons button {
      font-size: 12px;
      padding: 4px 8px;
      margin-right: 6px;
    }
    .building-select {
      font-size: 12px;
      padding: 4px;
    }
    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 12px;
    }
    th, td {
      border: 1px solid #ccc;
      padding: 6px;
      text-align: center;
    }
    th:nth-child(1), td:nth-child(1) { width: 60px; font-size: 11px; }  /* 건물 */
	th:nth-child(2), td:nth-child(2) { width: 60px; font-size: 11px; }  /* 호실 */
	th:nth-child(3), td:nth-child(3) { width: 40px; font-size: 11px; }  /* 공실 */
	input[type="checkbox"] {
	  transform: scale(0.85); /* 체크박스 살짝 축소 */
	}
    
    input[type="text"] {
      width: 60px;
      font-size: 12px;
      padding: 2px;
      text-align: right;
    }
    .issue-button {
      text-align: center;
      margin-top: 15px;
    }
  </style>
</head>
<body>

<h3 style="text-align:center;">관리비 발행</h3>

<div class="top-controls">
  <div class="left-buttons">
    <button type="button" id="addColumnBtn">+ 항목추가</button>
    <button type="button" id="removeColumnBtn">- 항목삭제</button>
  </div>
  <div>
    <label for="buildingSelect">건물 선택: </label>
    <select id="buildingSelect" class="building-select">
      <option value="all">전체 보기</option>
      <option value="건물1">건물1</option>
      <option value="건물2">건물2</option>
      <option value="건물3">건물3</option>
    </select>
  </div>
</div>

<form method="post" action="/building/payments/issue">
  <table id="feeTable">
    <thead>
      <tr id="headerRow">
        <th>건물</th>
        <th>호실</th>
        <th>공실</th>
        <th>월세</th>
        <th>관리비</th>
      </tr>
    </thead>
    <tbody>
      <tr data-building="건물1">
        <td>건물1</td>
        <td>401호</td>
        <td><input type="checkbox" class="vacancy-check"></td>
        <td><input type="text" name="rent_401" value="500000"></td>
        <td><input type="text" name="mgmt_401" value="80000"></td>
      </tr>
      <tr data-building="건물1">
        <td>건물1</td>
        <td>402호</td>
        <td><input type="checkbox" class="vacancy-check"></td>
        <td><input type="text" name="rent_402" value="530000"></td>
        <td><input type="text" name="mgmt_402" value="85000"></td>
      </tr>
      <tr data-building="건물2">
        <td>건물2</td>
        <td>301호</td>
        <td><input type="checkbox" class="vacancy-check"></td>
        <td><input type="text" name="rent_301" value="450000"></td>
        <td><input type="text" name="mgmt_301" value="75000"></td>
      </tr>
      <tr data-building="건물3">
        <td>건물3</td>
        <td>201호</td>
        <td><input type="checkbox" class="vacancy-check"></td>
        <td><input type="text" name="rent_201" value="400000"></td>
        <td><input type="text" name="mgmt_201" value="70000"></td>
      </tr>
    </tbody>
  </table>

  <div class="issue-button">
    <button type="submit">관리비 발행</button>
  </div>
</form>

<script>
  const feeTable = document.getElementById("feeTable");
  const headerRow = document.getElementById("headerRow");
  const addBtn = document.getElementById("addColumnBtn");
  const removeBtn = document.getElementById("removeColumnBtn");

  const chargeOptions = [
    { label: "전기", value: "elec" },
    { label: "수도", value: "water" },
    { label: "가스", value: "gas" },
    { label: "청소비", value: "clean" },
    { label: "인터넷", value: "wifi" },
    { label: "경비비", value: "security" }
  ];

  addBtn.addEventListener("click", () => {
    const th = document.createElement("th");
    th.innerHTML = `<select class="charge-label">` +
      chargeOptions.map(o => `<option value="` + o.value + `">` + o.label + `</option>`).join("") +
      `</select>`;
    headerRow.appendChild(th);

    const rows = feeTable.querySelectorAll("tbody tr");
    rows.forEach((row) => {
      const td = document.createElement("td");
      const room = row.children[1].innerText.replace("호", "");
      td.innerHTML = `<input type="text" name="dynamic${headerRow.children.length}_${room}" value="">`;
      row.appendChild(td);
    });
  });

  removeBtn.addEventListener("click", () => {
    const columnCount = headerRow.children.length;
    if (columnCount <= 5) return; // 기본 열: 건물, 호실, 공실, 월세, 관리비
    headerRow.removeChild(headerRow.lastElementChild);
    const rows = feeTable.querySelectorAll("tbody tr");
    rows.forEach(row => row.removeChild(row.lastElementChild));
  });

  document.getElementById('buildingSelect').addEventListener('change', function () {
    const selected = this.value;
    document.querySelectorAll('tbody tr').forEach(row => {
      const bldg = row.dataset.building;
      row.style.display = (selected === 'all' || selected === bldg) ? '' : 'none';
    });
  });

  // 공실 체크박스에 따른 입력 필드 비활성화
  document.querySelectorAll(".vacancy-check").forEach((checkbox) => {
    checkbox.addEventListener("change", function () {
      const row = this.closest("tr");
      const inputs = row.querySelectorAll("input[type='text']");
      inputs.forEach((input) => {
        input.disabled = this.checked;
      });
    });
  });
</script>

</body>
</html>
