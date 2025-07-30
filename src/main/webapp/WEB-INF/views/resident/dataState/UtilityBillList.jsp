<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>공과금 내역 조회</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <style>
    :root {
      --primary: #E17100;
      --primary-hover: #973C00;
      --gray: #6c757d;
      --bg: #f8f9fa;
      --font: 'Noto Sans KR', sans-serif;
    }
    body {
      font-family: var(--font);
      background: var(--bg);
      margin: 0;
      color: #212529;
      line-height: 1.6;
    }
    .container {
      max-width: 1200px;
      margin: 2rem auto;
      padding: 0 1rem;
    }
    .top-bar {
      display: flex;
      flex-direction: column;
      gap: 0.5rem;
      margin-bottom: 1.5rem;
    }
    .top-title-row {
      display: flex;
      justify-content: flex-start;
      align-items: center;
    }
    .top-select-row {
      display: flex;
      justify-content: flex-end;
      align-items: center;
      margin-top: 0.3rem;
    }
    .top-select-row form {
      display: flex;
      gap: 0.5rem;
    }
    .top-select-row select {
      padding: 0.5rem;
      font-size: 0.9rem;
      border: 1px solid #ccc;
      border-radius: 4px;
    }
    h2 {
      font-size: 1.3rem;
      font-weight: 600;
      color: var(--primary);
      margin-bottom: 0.8rem;
    }
    .main-section {
      display: flex;
      gap: 1.5rem;
    }
    .bill-box, .chart-box {
      background: #fff;
      border-radius: 8px;
      padding: 1.2rem;
      box-shadow: 0 2px 6px rgba(0,0,0,0.06);
    }
	.bill-box {
	  flex: 0.6;
	  border: 2px solid var(--primary); /* → 주황색 강조 */
	  border-radius: 8px;
	  background: #fff;
	  padding: 1.2rem;
	  box-shadow: 0 2px 6px rgba(0,0,0,0.06);
	}
    .chart-box {
      flex: 1.4;
      border: 2px solid var(--primary);
    }
    .bill-box h3, .chart-box h3 {
      margin-top: 0;
      font-size: 1.1rem;
      font-weight: 600;
      color: var(--primary);
      border-bottom: 1px solid #eee;
      padding-bottom: 0.4rem;
    }
    .summary-entry {
      margin: 0.6rem 0;
      font-size: 0.92rem;
      display: flex;
      justify-content: space-between;
    }
    .summary-entry span:first-child {
      font-weight: 500;
    }
    .price-item {
      display: flex;
      justify-content: flex-end;
      align-items: center;
    }
    .won-symbol {
      padding-right: 2px;
      font-weight: bold;
      color: #28a745;
    }
    .amount {
      min-width: 52px;
      text-align: right;
      font-family: monospace;
    }
    .unit-label {
      min-width: 40px;
      display: inline-block;
      text-align: left;
    }
    .bottom-section {
      margin-top: 2rem;
      background: #fff;
      border: 1px solid #ccc;
      border-radius: 6px;
      padding: 1rem;
      font-size: 0.9rem;
    }
    .bottom-section h4 {
      margin-top: 0;
      color: #333;
      font-size: 1rem;
      border-bottom: 1px solid #ddd;
      padding-bottom: 0.4rem;
    }
    ul {
      padding-left: 1.2rem;
    }
    .search-button {
      height: 40px;
      padding: 0 16px;
      border: none;
      border-radius: 4px;
      font-size: 14px;
      font-weight: bold;
      cursor: pointer;
      white-space: nowrap;
      background-color: #E17100;
      color: white;
}
.search-button:hover {
	background-color: #973C00;
}
  </style>
</head>
<body>
  <div class="container">
    <div class="top-bar">
      <div class="top-title-row">
        <h2>📄 ${chargeMonth} 공과금 및 관리비 상세내역</h2>
      </div>
      <div class="top-banner-filter-row" style="display: flex; justify-content: space-between; align-items: center; margin-top: 0.5rem;">
        <div class="ad-banner" style="flex-grow: 1;">
          <div style="background: #fff; border: 2px dashed var(--primary); border-radius: 8px; padding: 0.6rem 1rem; display: flex; align-items: center; gap: 10px; width: 100%;">
            <span style="color: var(--primary); font-weight: 500;">
              🎁 자동납부 시 기프티콘 100% 증정 이벤트!
            </span>
          </div>
        </div>
        <div class="top-select-row" style="flex-shrink: 0; margin-left: 1rem;">
          <form id="billSearchForm" method="get" action="/resident/dataState/bill" style="display: flex; gap: 0.5rem;">
            <select name="bldgIdParam">
              <c:forEach var="unit" items="${unitList}">
                <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>${unit.building.bldgNm}</option>
              </c:forEach>
            </select>
            <select name="unitIdParam" id="unitSelect">
              <c:forEach var="unit" items="${unitsInBuilding}">
                <option value="${unit.unitId}" <c:if test="${selectedUnitId eq unit.unitId}">selected</c:if>>${unit.unit.unitRoom}호</option>
              </c:forEach>
            </select>
            <select name="chargeMonth">
              <c:forEach var="m" items="${availableMonths}">
                <option value="${m}" <c:if test="${m eq chargeMonth}">selected</c:if>>${m}</option>
              </c:forEach>
            </select>
            <button type="submit" class="search-button" style="margin-left: 10px;">검색</button>
          </form>
        </div>
      </div>

    <div class="main-section">
        <c:if test="${empty chargeComparison and empty energyComparison[chargeMonth]}">
          <div style="margin-top: 1rem; padding: 1rem; background: #fff3cd; border: 1px solid #ffeeba; border-radius: 6px; color: #856404;">
            선택하신 월에는 조회 가능한 공과금 및 에너지 내역이 없습니다.
          </div>
        </c:if>
      <div class="bill-box">
        <h3>관리비 내역</h3>
			<c:forEach var="entry" items="${energyComparison[chargeMonth]}">
			  <c:set var="energyType" value="${entry.key}" />
			  <c:set var="data" value="${entry.value}" />
			  <c:if test="${not empty data}">
			    <div class="summary-entry">
			      <span>${energyType}</span>
			      <span>
			        사용량 :
			        <fmt:formatNumber value="${data['usageQty']}" type="number" groupingUsed="true" />
			        <span class="unit-label">
			          <c:choose>
			            <c:when test="${energyType eq '전기'}">kWh</c:when>
			            <c:when test="${energyType eq '가스'}">㎥</c:when>
			            <c:when test="${energyType eq '수도'}">㎥</c:when>
			          </c:choose>
			        </span>
			        <span class="price-item">
			          <span class="won-symbol">₩</span>
			          <span class="amount">
			            <fmt:formatNumber value="${data['chargeAmt']}" type="number" groupingUsed="true" />
			          </span> 원
			        </span>
			      </span>
			    </div>
			  </c:if>
			</c:forEach>
        <c:forEach var="item" items="${currentCharges}">
          <div class="summary-entry">
            <span>${item.feeName}</span>
            <span class="price-item">
              <span class="won-symbol">₩</span>
              <span class="amount">
                <fmt:formatNumber value="${item.chargeAmount}" type="number" groupingUsed="true" />
              </span> 원
            </span>
          </div>
        </c:forEach>
      </div>
      <div class="chart-box">
        <h3>💡 공과금 + 관리비 전월 비교 그래프</h3>
        <canvas id="chargeChart" style="width:100%"></canvas>
      </div>
    </div>
    <div class="bottom-section">
      <h4>입주민 유의 사항</h4>
      <ul>
        <li>납부기한 이후에는 연체료가 발생할 수 있습니다.</li>
        <li>가상계좌 입금은 반영까지 최대 2일 소요될 수 있습니다.</li>
        <li>문의사항은 관리사무소로 연락 바랍니다.</li>
        <li>이번 달에는 전기요금 인상분이 반영되었습니다.</li>
        <li>납부 내역은 로그인 후 확인 가능합니다.</li>
      </ul>
    </div>
  </div>
  <script>
	  document.addEventListener("DOMContentLoaded", () => {
		  const savedBldgId = localStorage.getItem("selectedBuildingId");
	
		  const urlParams = new URLSearchParams(window.location.search);
		  const hasBldgId = urlParams.has("bldgIdParam");
		  const hasUnitId = urlParams.has("unitIdParam");
		  const hasMonth  = urlParams.has("chargeMonth");
	
		  if (savedBldgId && !hasBldgId && !hasUnitId && !hasMonth) {
		    const selector = document.querySelector("select[name='bldgIdParam']");
		    if (selector) {
		      selector.value = savedBldgId;
		      document.querySelector("#billSearchForm")?.submit(); // ✅ 정확한 폼 ID
		    }
		  }
		});
	</script>
  <script>
	document.addEventListener('DOMContentLoaded', () => {
	  const unitSelect  = document.querySelector('select[name="unitIdParam"]');
	  const yearSelect  = document.querySelector('select[name="yearSelect"]');
	  const monthSelect = document.querySelector('select[name="monthSelect"]');
	  const bldgId      = document.querySelector('select[name="bldgIdParam"]')?.value || "";
	
	  const handleChange = () => {
	    const unitId = unitSelect?.value;
	    const year   = yearSelect?.value;
	    const month  = monthSelect?.value;
	    if (!unitId || !year || !month || year === '년도 선택' || month === '월 선택') return;
	
	    axios.get('/ajax/resident/api/payment/charge-info', {
	      params: { unitId, bldgId, year, month }
	    }).then(res => {
	      renderChargeInfo(res.data); // ✅ 기존 함수 그대로 활용
	    }).catch(err => {
	      console.error(err);
	      Swal.fire("에러", "데이터를 불러오는 중 문제가 발생했습니다.", "error");
	    });
	  };
	
	  unitSelect?.addEventListener('change', handleChange);
	  yearSelect?.addEventListener('change', handleChange);
	  monthSelect?.addEventListener('change', handleChange);
	});
	</script>
  
	<script>
	// ✅ 관리비 + 에너지 항목 라벨 구성
	const combinedLabels = [
	  <c:forEach var="item" items="${currentCharges}">
	    <c:if test="${item.feeName != '전기' and item.feeName != '가스' and item.feeName != '수도'}">
	      "${item.feeName}",
	    </c:if>
	  </c:forEach>
	  <c:forEach var="energyType" items="${['전기', '가스', '수도']}">
	    <c:if test="${not empty energyComparison[chargeMonth][energyType]}">
	      "${energyType}",
	    </c:if>
	  </c:forEach>
	];
	
	const prevCombinedValues = [
		  <c:forEach var="item" items="${currentCharges}">
		    <c:if test="${item.feeName != '전기' and item.feeName != '가스' and item.feeName != '수도'}">
		      ${item.previousAmount != null ? item.previousAmount : 0},
		    </c:if>
		  </c:forEach>
		  <c:forEach var="energyType" items="${['전기', '가스', '수도']}">
		    <c:if test="${not empty energyComparison[previousMonth][energyType]}">
		      ${energyComparison[previousMonth][energyType].chargeAmt},
		    </c:if>
		  </c:forEach>
		];
		
	const currCombinedValues = [
	  <c:forEach var="item" items="${currentCharges}">
	    <c:if test="${item.feeName != '전기' and item.feeName != '가스' and item.feeName != '수도'}">
	      ${item.chargeAmount != null ? item.chargeAmount : 0},
	    </c:if>
	  </c:forEach>
	  <c:forEach var="energyType" items="${['전기', '가스', '수도']}">
	    <c:if test="${not empty energyComparison[chargeMonth][energyType]}">
	      ${energyComparison[chargeMonth][energyType].chargeAmt},
	    </c:if>
	  </c:forEach>
	];
	
	// ✅ 디버깅용 로그 (브라우저 콘솔 확인)
	console.log("chargeMonth: ${chargeMonth}");
	console.log("previousMonth: ${previousMonth}");
	console.log("twoMonthsAgo : ${twoMonthsAgo}");
	console.log("전월 값", prevCombinedValues);
	console.log("당월 값", currCombinedValues);
	console.log("라벨", combinedLabels);
	
	// ✅ Chart.js 생성
	new Chart(document.getElementById('chargeChart'), {
	  type: 'bar',
	  data: {
	    labels: combinedLabels,
	    datasets: [
	      {
	        label: '전월',
	        data: prevCombinedValues,
	        backgroundColor: '#6c757d',
	        order: 1
	      },
	      {
	        label: '당월',
	        data: currCombinedValues,
	        backgroundColor: '#007bff',
	        order: 2
	      },
	      {
	        label: '당월 추세선',
	        type: 'line',
	        data: currCombinedValues,
	        borderColor: '#ffc107',
	        backgroundColor: 'transparent',
	        tension: 0,
	        pointRadius: 5,
	        pointStyle: 'circle',
	        pointBackgroundColor: '#ffc107',
	        pointBorderColor: '#fff',
	        pointBorderWidth: 1,
	        fill: false,
	        yAxisID: 'y',
	        order: 3,
	        clip: false,
	        segment: {
	          borderWidth: 2
	        }
	      }
	    ]
	  },
	  options: {
	    responsive: true,
	    clip: false,
	    layout: { padding: { top: 8 } },
	    elements: {
	      point: { radius: 5, backgroundColor: '#ffc107', borderColor: '#fff', borderWidth: 1 },
	      line: { borderWidth: 2 },
	      bar: { borderRadius: 2 }
	    },
	    plugins: {
	      tooltip: {
	        callbacks: {
	          label: ctx => `${ctx.dataset.label}: ₩${ctx.formattedValue}`
	        }
	      }
	    },
	    scales: {
	      y: {
	        beginAtZero: true,
	        ticks: {
	          callback: val => '₩' + val.toLocaleString()
	        }
	      }
	    }
	  }
	});
	</script>

	<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
