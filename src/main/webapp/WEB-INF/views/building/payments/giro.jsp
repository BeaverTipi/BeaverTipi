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
      background-color: #f4f6f9;
    }

    .container {
      display: flex;
      gap: 20px;
      align-items: flex-start;
    }

    .left-preview {
      flex: 1;
      border: 1px solid #ccc;
      background: white;
      padding: 1rem;
      max-height: 700px;
      overflow-y: auto;
      font-family: 'Courier New', monospace;
      font-size: 12px;
    }

    .left-preview h4 {
      margin-top: 0;
      border-bottom: 1px solid #ccc;
      padding-bottom: 4px;
    }

    .right-form {
      flex: 2;
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
      background-color: #fff;
    }

    th, td {
      border: 1px solid #ccc;
      padding: 6px;
      text-align: center;
    }

    th:nth-child(1), td:nth-child(1) { width: 60px; font-size: 11px; }
    th:nth-child(2), td:nth-child(2) { width: 60px; font-size: 11px; }
    th:nth-child(3), td:nth-child(3) { width: 40px; font-size: 11px; }

    input[type="checkbox"] {
      transform: scale(0.85);
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

    .filter-row {
      display: flex;
      justify-content: space-between;
      align-items: center;
      margin-bottom: 10px;
    }

    .preview-block {
      margin-bottom: 14px;
      border-bottom: 1px dashed #aaa;
      padding-bottom: 6px;
    }

    .preview-indent {
      margin-left: 16px;
    }
  </style>
</head>
<body>

<h3 style="text-align:center;">관리비 발행</h3>

<div class="container">
  <!-- 좌측 미리보기 -->
  <div class="left-preview">
    <div class="filter-row">
      <h4>지로 미리보기</h4>
      <select id="previewFilter">
        <option value="all">전체 보기</option>
        <option value="rent">월세만</option>
        <option value="mgmt">관리비만</option>
        <option value="dynamic">추가 항목만</option>
      </select>
    </div>
    <div class="bill-list" id="previewArea"></div>
  </div>

  <!-- 우측 입력폼 -->
  <div class="right-form">
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
          <tr data-building="건물2">
            <td>건물2</td>
            <td>301호</td>
            <td><input type="checkbox" class="vacancy-check"></td>
            <td><input type="text" name="rent_301" value="450000"></td>
            <td><input type="text" name="mgmt_301" value="75000"></td>
          </tr>
        </tbody>
      </table>

      <div class="issue-button">
        <button type="submit">관리비 발행</button>
      </div>
    </form>
  </div>
</div>

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
      chargeOptions.map(o => `<option value="${o.value}">${o.label}</option>`).join("") +
      `</select>`;
    headerRow.appendChild(th);

    const rows = feeTable.querySelectorAll("tbody tr");
    rows.forEach((row) => {
      const td = document.createElement("td");
      const room = row.children[1].innerText.replace("호", "");
      td.innerHTML = `<input type="text" name="dynamic${headerRow.children.length}_${room}" value="">`;
      row.appendChild(td);
    });
    updatePreview();
  });

  removeBtn.addEventListener("click", () => {
    if (headerRow.children.length <= 5) return;
    headerRow.removeChild(headerRow.lastElementChild);
    feeTable.querySelectorAll("tbody tr").forEach(row => {
      row.removeChild(row.lastElementChild);
    });
    updatePreview();
  });

  document.getElementById('buildingSelect').addEventListener('change', () => {
    const selected = document.getElementById('buildingSelect').value;
    document.querySelectorAll('tbody tr').forEach(row => {
      row.style.display = (selected === 'all' || row.dataset.building === selected) ? '' : 'none';
    });
    updatePreview();
  });

  document.getElementById('previewFilter').addEventListener('change', updatePreview);

  document.querySelectorAll("input[type='text'], .vacancy-check").forEach(el => {
    el.addEventListener("input", updatePreview);
    el.addEventListener("change", updatePreview);
  });

  function updatePreview() {
    const preview = document.getElementById("previewArea");
    const rows = document.querySelectorAll("#feeTable tbody tr");
    const selectedFilter = document.getElementById("previewFilter").value;
    const selectedBldg = document.getElementById("buildingSelect").value;

    let buildings = new Set();
    let html = "";

    rows.forEach(row => {
      const bldg = row.children[0].innerText;
      const room = row.children[1].innerText;
      const vacant = row.querySelector(".vacancy-check").checked;
      const isVisible = row.style.display !== "none";

      if (vacant || !isVisible) return;

      buildings.add(bldg);

      const inputs = row.querySelectorAll("input[type='text']");
      const details = Array.from(inputs)
        .filter(input => {
          if (selectedFilter === 'all') return true;
          if (selectedFilter === 'rent') return input.name.startsWith("rent");
          if (selectedFilter === 'mgmt') return input.name.startsWith("mgmt");
          if (selectedFilter === 'dynamic') return input.name.startsWith("dynamic");
          return true;
        })
        .map(input => {
          const label = getLabelFromInput(input);
          return `<div class="preview-indent">· ${label}: ${input.value || '0'}원</div>`;
        });

      html += `<div class="preview-block">
        <strong>${bldg} - ${room}</strong>
        ${details.join("")}
      </div>`;
    });

    if (buildings.size > 0) {
      const target = Array.from(buildings).join(", ");
      html = `<div style="margin-bottom: 12px;">📦 <strong>부과 대상:</strong> ${target}</div>` + html;
    }

    preview.innerHTML = html;
  }

  function getLabelFromInput(input) {
    const name = input.name;
    if (name.startsWith("rent")) return "월세";
    if (name.startsWith("mgmt")) return "관리비";
    const match = name.match(/^dynamic(\d+)_/);
    if (match) {
      const idx = parseInt(match[1]);
      const th = document.querySelector(`#headerRow th:nth-child(${idx}) select`);
      return th ? th.options[th.selectedIndex].text : "기타";
    }
    return "기타";
  }

  updatePreview();
</script>

</body>
</html>
