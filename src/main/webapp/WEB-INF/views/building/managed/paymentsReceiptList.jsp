<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%
  String year = request.getParameter("year");
  String month = request.getParameter("month");

  if (year == null || month == null) {
      java.util.Calendar cal = java.util.Calendar.getInstance();
      year = String.valueOf(cal.get(java.util.Calendar.YEAR));
      month = String.valueOf(cal.get(java.util.Calendar.MONTH) + 1);
  }
%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>납부데이터 통합관리</title>
  <style>
    body { font-family: sans-serif; font-size: 13px; padding: 1rem; }
    h2 { text-align: center; }
    .month-header {
      font-size: 24px;
      font-weight: bold;
      margin: 20px 0 10px;
      text-align: center;
      cursor: pointer;
    }
    .month-header:hover {
      text-decoration: underline;
      color: #4aacc5;
    }
    .filter-box {
      display: flex;
      justify-content: space-between;
      margin-bottom: 15px;
      align-items: center;
    }
    .filter-box select {
      padding: 4px;
      font-size: 13px;
      margin-right: 10px;
    }
    .summary-bar {
      display: flex;
      justify-content: center;
      gap: 30px;
      margin: 20px 0;
    }
    .summary-bar > div {
      padding: 15px 25px;
      border-radius: 8px;
      color: white;
      font-weight: bold;
      text-align: center;
      font-size: 14px;
      min-width: 140px;
    }
    .orange { background-color: #f79646; }
    .blue-sum { background-color: #4aacc5; }
    .purple { background-color: #8064a2; }

    table {
      width: 100%;
      border-collapse: collapse;
      font-size: 13px;
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

    /* 모달 스타일 */
   #monthModal {
	  display: none;
	  position: fixed;
	  top: 50%;
	  left: 50%;
	  transform: translate(-50%, -50%);
	  background: #fff;
	  border-radius: 12px;
	  padding: 30px 20px;
	  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.25);
	  z-index: 999;
	  width: 300px;
	  text-align: center;
	}
	
	#monthModal h3 {
	  margin-bottom: 15px;
	  font-size: 18px;
	  color: #333;
	}
	
	#monthModal input[type="month"] {
	  padding: 8px 12px;
	  font-size: 14px;
	  width: 100%;
	  border: 1px solid #ccc;
	  border-radius: 6px;
	}
	
	#monthModal button {
	  margin: 15px 5px 0;
	  padding: 8px 16px;
	  font-size: 14px;
	  border: none;
	  border-radius: 6px;
	  cursor: pointer;
	  transition: background 0.2s ease-in-out;
	}
	
	#monthModal button:first-of-type {
	  background-color: #4aacc5;
	  color: #fff;
	}
	#monthModal button:first-of-type:hover {
	  background-color: #3a90a5;
	}
	
	#monthModal button:last-of-type {
	  background-color: #ccc;
	}
	#monthModal button:last-of-type:hover {
	  background-color: #aaa;
	}
  </style>
</head>
<body>

<h2>납부데이터 통합관리</h2>

<div class="month-header" id="monthHeader">📅 <%=year%>년 <%=month%>월 납부현황</div>

<!-- 모달 및 오버레이 -->
<div id="overlay" onclick="closeModal()"></div>
<div id="monthModal">
  <h3>월 선택</h3>
  <input type="month" id="modalMonth" />
  <br><br>
  <button onclick="submitMonth()">확인</button>
  <button onclick="closeModal()">취소</button>
</div>

<div class="filter-box">
  <div>
    <label>건물:</label>
    <select id="buildingFilter">
      <option value="all">전체 보기</option>
      <option value="현대오피스텔">현대오피스텔</option>
      <option value="마포상가">마포상가</option>
    </select>

    <label>납부상태:</label>
    <select id="statusFilter">
      <option value="all">전체</option>
      <option value="완납">완납</option>
      <option value="미납">미납</option>
      <option value="부분납부">부분납부</option>
    </select>

    <label>납부방법:</label>
    <select id="methodFilter">
      <option value="all">전체</option>
      <option>계좌이체</option>
      <option>카드</option>
      <option>현금</option>
    </select>
  </div>
</div>

<div class="summary-bar">
  <div class="orange">청구서 현황<br>2건<br>₩340,000</div>
  <div class="blue-sum">수납현황<br>1건<br>₩200,000</div>
  <div class="purple">조정 청구서<br>1건</div>
</div>

<table id="paymentTable">
  <thead>
    <tr>
      <th>No</th>
      <th>건물명</th>
      <th>층</th>
      <th>호수</th>
      <th>입차인명</th>
      <th>청구서명</th>
      <th class="sortable">청구금액(단위)</th>
      <th class="sortable">납부상태</th>
      <th class="sortable">납기일</th>
      <th class="sortable">납부일</th>
      <th>납부방법</th>
      <th>증빙발행</th>
      <th>태그</th>
    </tr>
  </thead>
  <tbody>
    <tr data-building="현대오피스텔" data-status="완납" data-method="계좌이체">
      <td>1</td><td>현대오피스텔</td><td>3</td><td>301</td><td>홍길동</td>
      <td>전기요금</td><td>200000</td><td>완납</td><td>2025-06-20</td>
      <td>2025-06-18</td><td>계좌이체</td><td>세금계산서</td><td>전기</td>
    </tr>
    <tr data-building="마포상가" data-status="미납" data-method="카드">
      <td>2</td><td>마포상가</td><td>5</td><td>503</td><td>이순신</td>
      <td>임대료</td><td>140000</td><td>미납</td><td>2025-06-25</td>
      <td>-</td><td>카드</td><td>현금영수증</td><td>임대</td>
    </tr>
  </tbody>
</table>

<script>
  // 모달 로직
  const monthHeader = document.getElementById("monthHeader");
  const monthModal = document.getElementById("monthModal");
  const overlay = document.getElementById("overlay");
  const modalMonth = document.getElementById("modalMonth");

  monthHeader.addEventListener("click", () => {
    monthModal.style.display = "block";
    overlay.style.display = "block";
  });

  function closeModal() {
    monthModal.style.display = "none";
    overlay.style.display = "none";
  }

  function submitMonth() {
    const val = modalMonth.value;
    if (!val) return;

    const [year, month] = val.split("-");
    const url = new URL(window.location.href);
    url.searchParams.set("year", year);
    url.searchParams.set("month", month);
    window.location.href = url.toString();
  }

  // 필터링
  document.getElementById('buildingFilter').addEventListener('change', filterRows);
  document.getElementById('statusFilter').addEventListener('change', filterRows);
  document.getElementById('methodFilter').addEventListener('change', filterRows);

  function filterRows() {
    const building = document.getElementById('buildingFilter').value;
    const status = document.getElementById('statusFilter').value;
    const method = document.getElementById('methodFilter').value;
    const rows = document.querySelectorAll('#paymentTable tbody tr');

    rows.forEach(row => {
      const b = row.dataset.building;
      const s = row.dataset.status;
      const m = row.dataset.method;
      const visible =
        (building === 'all' || b === building) &&
        (status === 'all' || s === status) &&
        (method === 'all' || m === method);
      row.style.display = visible ? '' : 'none';
    });
  }

  // 정렬
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
          v1 = parseFloat(v1); v2 = parseFloat(v2);
        }
        return (v1 > v2 ? 1 : v1 < v2 ? -1 : 0) * (asc ? 1 : -1);
      });
      rows.forEach(r => table.tBodies[0].appendChild(r));
      document.querySelectorAll("th.sortable").forEach(th => delete th.dataset.order);
      header.dataset.order = asc ? "asc" : "desc";
    });
  });
</script>

</body>
</html>
