<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>납부데이터 통합관리</title>
  <style>
    body {
      font-family: sans-serif;
      font-size: 13px;
      padding: 20px;
    }

    h2 {
      text-align: center;
      font-size: 22px;
      margin-bottom: 10px;
      margin-right: 10px;
    }

    .month-header {
      text-align: center;
      font-size: 18px;
      font-weight: bold;
      margin-bottom: 25px;
      margin-right: 10px;
    }

    .filter-summary-row {
      display: flex;
      justify-content: center;
      align-items: center;
      gap: 60px;
      max-width: 1000px;
      margin: 0 auto;
      padding: 0 10px;
    }

    .filter-left {
      display: flex;
      gap: 10px;
      align-items: center;
      flex-wrap: nowrap;
      margin-right: auto;
    }

    .filter-center {
      display: flex;
      justify-content: center;
      align-items: stretch;
      gap: 15px;
      margin-right: 0px; 
    }

    .filter-right {
      display: flex;
      gap: 10px;
      align-items: center;
      flex-wrap: nowrap;
      margin-left: auto;
    }

    .filter-group {
      display: flex;
      align-items: center;
      gap: 5px;
      white-space: nowrap;
    }

    .filter-group label {
      font-weight: bold;
    }

    .filter-group select,
    .filter-group input[type="month"],
    .filter-group input[type="date"] {
      padding: 5px;
      font-size: 13px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    #buildingFilter {
      width: 200px; /* ✅ 건물 선택 너비 늘림 */
    }

    .summary-card {
      padding: 15px 20px;
      border-radius: 8px;
      color: white;
      font-weight: bold;
      text-align: center;
      font-size: 14px;
      min-width: 140px;
      flex: 1;
      background-color: #f79646;
    }

    .summary-card.blue {
      background-color: #4aacc5;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 13px;
      margin-top: 15px;
    }

    th, td {
      border: 1px solid #ccc;
      padding: 8px;
      text-align: center;
      cursor: pointer;
    }

    th.sortable:hover {
      background-color: #f2f2f2;
    }

    @media screen and (max-width: 768px) {
      .filter-summary-row {
        flex-direction: column;
        align-items: stretch;
      }

      .filter-left,
      .filter-center,
      .filter-right {
        justify-content: center;
        flex-wrap: wrap;
        margin-top: 10px;
      }
    }
  </style>
</head>
<body>

<%
  java.time.LocalDate now = java.time.LocalDate.now();
  String currentYearMonth = now.getYear() + "-" + String.format("%02d", now.getMonthValue());
%>

<h2>납부데이터 통합관리</h2>
<div class="month-header" id="monthHeader">📅 <%= now.getYear() %>년 <%= now.getMonthValue() %>월 납부현황</div>

<div class="filter-summary-row">
  <!-- 좌측 필터 -->
  <div class="filter-left">
    <div class="filter-group">
      <label for="buildingFilter">건물:</label>
      <select id="buildingFilter">
        <option value="all">전체</option>
        <option value="A동">A동</option>
        <option value="B동">B동</option>
      </select>
    </div>
    <div class="filter-group">
      <label for="roomFilter">호수:</label>
      <select id="roomFilter">
        <option value="all">전체</option>
        <option value="101">101호</option>
        <option value="201">201호</option>
        <option value="301">301호</option>
      </select>
    </div>
    <div class="filter-group">
      <label for="statusFilter">납부상태:</label>
      <select id="statusFilter">
        <option value="all">전체</option>
        <option value="paid">납부완료</option>
        <option value="unpaid">미납</option>
      </select>
    </div>
  </div>

  <!-- 중앙 현황 카드 -->
  <div class="filter-center">
    <div class="summary-card">
      청구서 현황<br>
      <span id="billCount">-</span><br>
      <span id="billAmount">-</span>
    </div>
    <div class="summary-card blue">
      수납현황<br>
      <span id="payCount">-</span><br>
      <span id="payAmount">-</span>
    </div>
  </div>

  <!-- 우측 날짜 필터 -->
  <div class="filter-right">
    <div class="filter-group">
      <label for="monthPicker">청구월:</label>
      <input type="month" id="monthPicker" value="<%= currentYearMonth %>" />
    </div>
    <div class="filter-group">
      <label>납기일 기간:</label>
      <input type="date" id="dueStart" /> ~ <input type="date" id="dueEnd" />
    </div>
  </div>
</div>

<table id="paymentTable">
  <thead>
    <tr>
      <th>No</th>
      <th>건물명</th>
      <th>층</th>
      <th>호수</th>
      <th>임차인명</th>
      <th class="sortable">청구금액</th>
      <th class="sortable">납부금액</th>
      <th class="sortable">납부상태</th>
      <th class="sortable">청구일</th>
      <th class="sortable">납기일</th>
      <th class="sortable">납부일</th>
      <th>청구계좌</th>
    </tr>
  </thead>
  <tbody>
    <!-- 동적 데이터 삽입 예정 -->
  </tbody>
</table>

<script>
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

  document.getElementById("monthPicker").addEventListener("change", function () {
    const [year, monthStr] = this.value.split("-");
    const yearNum = parseInt(year, 10);
    const monthNum = parseInt(monthStr, 10);
    document.getElementById("monthHeader").innerText = "📅 " + yearNum + "년 " + monthNum + "월 납부현황";
  });
</script>

</body>
</html>