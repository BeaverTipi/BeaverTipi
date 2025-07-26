<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c"   uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리비 납부 비교</title>
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background: #f8f9fa;
      margin: 0;
      color: #212529;
    }
    .container {
      max-width: 1200px;
      margin: 20px auto;
      padding: 10px;
      box-sizing: border-box;
    }
    .header-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 2px dashed #E17100;
      padding-bottom: 10px;
    }
    .page-title h1 {
      font-size: 22px;
      margin: 0;
      color: #E17100;
    }
    .page-title p {
      font-size: 13px;
      color: #555;
      margin: 5px 0 0 0;
    }
    .building-selector select {
      padding: 5px 8px;
      font-size: 14px;
    }
   .main-grid {
     display: flex;
     gap: 20px;
     margin-top: 20px;
     flex-wrap: wrap;
     overflow-x: hidden;
     align-items: stretch; /* ✅ 높이 동일하게 맞춤 */
   }
    .notice-grid {
  flex: 1 1 66%;
  min-width: 0;
  height: 100%; /* ✅ 추가 */
}
    .giro-notice-card {
  background: #fff;
  border: 2px dashed #E17100;
  border-radius: 6px;
  padding: 20px;
  height: 100%; /* ✅ 추가 */
  box-sizing: border-box;
  display: flex;
  flex-direction: column;
  justify-content: space-between; /* 필요시 콘텐츠 배치 보정 */
}
    .giro-notice-card h4 {
      color: #E17100;
      margin-top: 0;
    }
    .energy-summary {
      margin-top: 10px;
      padding: 12px;
      font-size: 13px;
      background: #fdfdfd;
      border: 1px dashed #E17100;
      border-radius: 6px;
    }
    .energy-summary .energy-row {
  display: flex;
  flex-direction: column;
  margin-bottom: 10px;
  padding: 8px 10px;
  border: 1px dashed #E17100;
  border-radius: 6px;
  background-color: #fcfcfc;
}

.energy-summary .energy-label {
  font-weight: bold;
  color: #E17100;
  margin-bottom: 4px;
}

.energy-summary .energy-values {
  display: flex;
  flex-wrap: wrap;
  gap: 6px 12px;
  font-size: 13px;
  justify-content: space-between;
  align-items: center;
}

.energy-summary .value-item {
  flex: 1;
  min-width: 140px;
}

.energy-summary .diff {
  font-weight: bold;
}
    .diff.up { color: #e74c3c; font-weight: bold; }
    .diff.down { color: #2ecc71; font-weight: bold; }
    .diff.same { color: #999; }

    .ad-sidebar {
      flex: 0 0 320px;
      display: flex;
      flex-direction: column;
      gap: 16px;
      box-sizing: border-box;
       min-width: 0;
       max-width: 320px;
    }

    .ad-sidebar-row {
      display: flex;
      flex-direction: row;
      gap: 16px;
    }

    .ad-box,
   .total-charge-box,
   .combined-paybox {
     background: #fff;
     border: 1px solid #dee2e6;
     border-radius: 10px;
     padding: 20px;
     font-size: 14px;
     line-height: 1.6;
     box-shadow: 0 2px 8px rgba(0,0,0,0.05);
     transition: all 0.2s ease;
   }
   .combined-paybox label {
     margin-right: 12px;
     font-size: 13px;
   }
   .ad-box h3,
   .total-charge-box h3,
   .combined-paybox h3 {
     margin-top: 0;
     font-size: 16px;
     color: #343a40;
     display: flex;
     align-items: center;
     gap: 6px;
   }
   
.pay-button {
  background: #E17100;
  color: #fff;
  padding: 10px 24px;
  font-size: 14px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  display: block;
  margin-top: 14px;
  transition: background 0.3s ease;
}
   .pay-button:hover {
     background: #973C00;
   }
    .double-info-box {
      display: flex;
      background: #fff;
      border: 1px solid #ccc;
      border-radius: 6px;
      padding: 15px;
      gap: 20px;
      font-size: 13px;
      line-height: 1.5;
      margin-top: 20px;
    }
    .info-column:first-child {
      border-right: 1px dashed #E17100;
      padding-right: 15px;
    }
    .total-charge-amount {
     font-size: 26px;
     font-weight: bold;
     color: #E17100;
     margin: 12px 0 6px;
   }
   .total-charge-box p:last-child {
     font-size: 13px;
     color: #2ecc71;
   }
    .charge-table {
  width: 100%;
  border-collapse: collapse;
  margin-top: 12px;
  font-size: 13px;
}
.charge-table thead {
  background-color: #fff6ed;
}
.charge-table th {
  padding: 10px 8px;
  border-bottom: 2px solid #E17100;
  color: #E17100;
  text-align: left;
}
.charge-table td {
  padding: 10px 8px;
  border-bottom: 1px dashed #E17100;
  vertical-align: middle;
}
.charge-table tr.even {
  background-color: #fdfdfd;
}
.charge-table tr.odd {
  background-color: #ffffff;
}
.charge-table td.amount{
  text-align: center;
  white-space: nowrap;
  font-variant-numeric: tabular-nums;
}
.charge-table td.diff, 
.charge-table th.diff {
  text-align: right !important;  /* ✅ 우측 정렬 고정 */
  width: 100px;                   /* ✅ 셀 넓이 통일 */
  white-space: nowrap;
  padding-right: 8px;
}
.charge-table .label {
  font-weight: bold;
}
.money-compare {
  display: inline-block;
  min-width: 140px;
  text-align: center;
  letter-spacing: 0.5px;
}
.info-box,
.double-info-box {
  box-sizing: border-box;
  width: 100%;
  overflow-wrap: break-word;
}
.info-box {
  background: #fff;
  border: 1px solid #dee2e6;
  border-radius: 10px;
  padding: 20px;
  font-size: 14px;
  line-height: 1.6;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
  box-sizing: border-box;
  width: 100%; /* ✅ 부모 너비에 맞춰 제한 */
  overflow-wrap: break-word;
  word-wrap: break-word;
  word-break: keep-all;
}
.ad-sidebar .info-box {
  max-height: 255px;   /* 필요시 조정 가능 (예: 180px ~ 240px) */
  overflow-y: auto;
  padding-right: 10px; /* 스크롤 공간 확보 */
}
.info-box h3 {
  margin-top: 0;
  font-size: 16px;
  color: #343a40;
  display: flex;
  align-items: center;
  gap: 6px;
  border-bottom: 1px solid #ddd;
  padding-bottom: 6px;
  margin-bottom: 10px;
}

.info-box ul {
  margin: 0;
  padding-left: 20px;
  font-size: 13px;
  color: #333;
}

.info-box li {
  margin-bottom: 6px;
  line-height: 1.5;
}
select[name="bldgIdParam"] {
  font-size: 14px;
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
  background-color: #fff;
  color: #212529;
  appearance: none; /* 크롬/사파리 기본 화살표 제거 */
  background-image: url("data:image/svg+xml;charset=UTF-8,%3Csvg fill='black' height='20' viewBox='0 0 20 20' width='20' xmlns='http://www.w3.org/2000/svg'%3E%3Cpath d='M7 10l5 5 5-5z'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 10px center;
  background-size: 14px;
  cursor: pointer;
  min-width: 180px;
}


.diff.up { color: #e74c3c; font-weight: bold; }
.diff.down { color: #2ecc71; font-weight: bold; }
.diff.same { color: #999; font-weight: bold; }
  </style>
</head>
<body>
  <fmt:parseDate var="prevDate" value="${currentMonth}" pattern="yyyyMM" />
  <div class="container">
    <div class="header-bar">
      <div class="page-title">
        <h1>📄 관리비 납부 비교</h1>
        <p>${currentMonth} 기준 항목을 전전월과 비교한 요약 카드입니다</p>
      </div>
			<form id="noticeSearchForm" method="get" action="/resident/payment">
			  <select name="bldgIdParam">
			    <c:forEach var="unit" items="${unitList}">
			      <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
			        ${unit.building.bldgNm}
			      </option>
			    </c:forEach>
			  </select>
				<select name="unitIdParam" id="unitSelect" style="margin-left: 10px;">
				  <c:forEach var="unit" items="${unitsInBuilding}">
				    <option value="${unit.unitId}" <c:if test="${unit.unitId eq selectedUnitId}">selected</c:if>>
				      ${unit.unit.unitRoom}호
				    </option>
				  </c:forEach>
				</select>
			  <!-- ✅ 검색 버튼 추가 -->
			  <button type="submit" class="search-button" style="margin-left: 10px;">검색</button>
			</form>
    </div>
    <div class="main-grid">
     <div class="notice-grid">
     <c:set var="totalIntegratedAmount" value="0" />
     <c:set var="totalSavedAmount" value="0" />
     <c:set var="energyChargeSum" value="0" />
     <c:forEach var="item" items="${chargeBillComparisonList}">
       <c:set var="totalIntegratedAmount" value="${totalIntegratedAmount + item.previousAmount}" />
       <c:if test="${item.diffAmount < 0}">
         <c:set var="totalSavedAmount" value="${totalSavedAmount + (-item.diffAmount)}" />
       </c:if>
     </c:forEach>
     <c:forEach var="type" items="${energySummary[currentMonth]}">
        <c:set var="energyType" value="${type.key}" />
        <c:if test="${energyType eq '전기' || energyType eq '가스' || energyType eq '수도'}">
          <c:set var="row" value="${type.value}" />
          <c:set var="energyChargeSum" value="${energyChargeSum + row.chargeAmt}" />
        </c:if>
      </c:forEach>
      <c:set var="totalIntegratedAmount" value="${totalIntegratedAmount + energyChargeSum}" />
        <div class="giro-notice-card">
          <h4><fmt:formatDate value="${prevDate}" pattern="yyyy년 MM월" /> 관리비 요약</h4>
		<table class="charge-table">
		  <thead>
		    <tr>
		      <th>항목</th>
		      <th style="text-align:center">금액</th>
		      <th class="diff">변동</th>
		    </tr>
		  </thead>
		  <tbody>
		    <c:forEach var="item" items="${chargeBillComparisonList}" varStatus="vs">
		      <tr class="${vs.index % 2 == 0 ? 'even' : 'odd'}">
		        <td class="label">${item.feeName}</td>
		        <td class="amount">
		          <span class="money-compare">
		            <fmt:formatNumber value="${item.previousAmount}" type="currency" />
		            →
		            <fmt:formatNumber value="${item.twoMonthsAgo}" type="currency" />
		          </span>
		        </td>
		        <td class="diff">
		          <c:choose>
		            <c:when test="${item.diffAmount > 0}">
		              <span class="diff up">▲ <fmt:formatNumber value="${item.diffAmount}" />원</span>
		            </c:when>
		            <c:when test="${item.diffAmount < 0}">
		              <span class="diff down">▼ <fmt:formatNumber value="${-item.diffAmount}" />원</span>
		            </c:when>
		            <c:otherwise><span class="diff same">변화 없음</span></c:otherwise>
		          </c:choose>
		        </td>
		      </tr>
		    </c:forEach>
		  </tbody>
		</table>


         <div class="energy-summary">
  <c:set var="electricCode" value="전기" />
  <c:set var="gasCode" value="가스" />
  <c:set var="waterCode" value="수도" />
  <c:forEach var="type" items="${energySummary[currentMonth]}">
    <c:set var="energyType" value="${type.key}" />
    <c:if test="${energyType eq '전기' || energyType eq '가스' || energyType eq '수도'}">
      <c:set var="currRow" value="${type.value}" />
      <c:set var="prevRow" value="${energySummary[previousMonth][energyType]}" />
      <c:set var="diffUsage" value="${currRow.usageQty - prevRow.usageQty}" />
      <c:set var="diffCharge" value="${currRow.chargeAmt - prevRow.chargeAmt}" />
      <c:set var="unitLabel" value="" />
      <c:choose>
        <c:when test="${energyType eq '전기'}"><c:set var="unitLabel" value="kWh"/></c:when>
        <c:when test="${energyType eq '가스'}"><c:set var="unitLabel" value="㎥"/></c:when>
        <c:when test="${energyType eq '수도'}"><c:set var="unitLabel" value="L"/></c:when>
      </c:choose>

      <div class="energy-row">
        <div class="energy-label">🔹 ${energyType}</div>
        <div class="energy-values">
          <div class="value-item">
          <span style="white-space:nowrap;">
            사용량: 
            <fmt:formatNumber value="${prevRow.usageQty}" type="number" groupingUsed="true"/>${unitLabel} → 
            <fmt:formatNumber value="${currRow.usageQty}" type="number" groupingUsed="true"/>${unitLabel}
            </span>
            <c:choose>
              <c:when test="${diffUsage > 0}">
                <span class="diff up">▲ <fmt:formatNumber value="${diffUsage}" type="number" groupingUsed="true"/>${unitLabel}</span>
              </c:when>
              <c:when test="${diffUsage < 0}">
                <span class="diff down">▼ <fmt:formatNumber value="${-diffUsage}" type="number" groupingUsed="true"/>${unitLabel}</span>
              </c:when>
              <c:otherwise><span class="diff same">변화 없음</span></c:otherwise>
            </c:choose>
          </div>

          <div class="value-item">
            요금: 
            <fmt:formatNumber value="${prevRow.chargeAmt}" type="currency" /> → 
            <fmt:formatNumber value="${currRow.chargeAmt}" type="currency" />
            <c:choose>
              <c:when test="${diffCharge > 0}">
                <span class="diff up">▲ <fmt:formatNumber value="${diffCharge}" type="currency" /></span>
              </c:when>
              <c:when test="${diffCharge < 0}">
                <span class="diff down">▼ <fmt:formatNumber value="${-diffCharge}" type="currency" /></span>
              </c:when>
              <c:otherwise><span class="diff same">변화 없음</span></c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>
    </c:if>
  </c:forEach>
</div>
        </div>
      </div>
      <div class="ad-sidebar">
        <div class="ad-box">
          <h3>📢 이벤트 안내</h3>
          <p>관리비 자동이체 시 스타벅스 기프티콘 증정!</p>
          <img src="/images/ad-banner.jpg" alt="광고 배너" style="width:100%; border-radius:6px; margin-top:10px;" />
        </div>
        <div class="total-charge-box">
        <h3>📦 이번 달 청구 금액</h3>
        <p class="total-charge-amount">
          <fmt:formatNumber value="${currentChargeAmount}" type="currency" currencySymbol="₩" />
        </p>
        <c:choose>
		  <c:when test="${totalSavedAmount > 0}">
		    <p style="font-size:13px; color:#2ecc71; margin-top:6px;">
		      💚 전월 대비 <strong><fmt:formatNumber value="${totalSavedAmount}" type="currency" /></strong> 절감되었습니다
		    </p>
		  </c:when>
		  <c:when test="${totalSavedAmount < 0}">
		    <p style="font-size:13px; color:#e74c3c; margin-top:6px;">
		      🔺 전월 대비 <strong><fmt:formatNumber value="${-totalSavedAmount}" type="currency" /></strong> 증가하였습니다
		    </p>
		  </c:when>
		  <c:otherwise>
		    <p style="font-size:13px; color:#999; margin-top:6px;">
		      전월과 동일한 금액이 청구되었습니다
		    </p>
		  </c:otherwise>
		</c:choose>
      </div>
       <div class="combined-paybox">
		  <h3>💳 결제 방식</h3>
		  <c:forEach items="${payment}" var="pay">
		    <label>
		      <input type="radio" name="payment_method" value="${pay.codeName}" checked />
		      ${pay.codeName}
		    </label>
		  </c:forEach>
		  <!-- ✅ 납부 버튼 추가 -->
		  <button
			  type="button"
			  class="pay-button"
			  data-name="<fmt:formatDate value='${prevDate}' pattern='yyyy년 MM월 관리비'/>"
			  data-pay="${currentChargeAmount}"
			>
			  💰 납부하기
			</button>
		</div>
    <div class="info-box">
     <h3>📘 고객 안내사항</h3>
     <ul style="margin:0; padding-left: 18px; font-size: 13px;">
       <li>2025년 6월 관리비는 전월 대비 평균 <strong>5.8% 인상</strong>되었습니다.</li>
       <li>공용 수도요금은 <strong>배관 정비공사</strong>로 인해 일시적으로 증가했습니다.</li>
       <li>엘리베이터 보수공사가 <strong>7월 15일 ~ 18일</strong> 예정되어 있습니다.</li>
       <li>관리사무소 운영시간은 <strong>09:00 ~ 18:00</strong>이며, 점심시간은 12:00 ~ 13:00입니다.</li>
     </ul>
   </div>
      </div>
    </div>
    <div class="double-info-box">
      <div class="info-column">
        <h3>💡 입금 안내</h3>
        <p>국민은행 123-456-7890<br/>예금주: 동산아파트 관리사무소</p>
        <p>입금자명에 "세대ID(예: 101동201호)"를 꼭 기재해주세요.</p>
      </div>
      <div class="info-column">
        <h3>⚠️ 유의 사항</h3>
        <ul>
          <li>납부기한 경과 시 연체료가 부과됩니다.</li>
          <li>가상계좌 입금 시 반영까지 1~2일 소요됩니다.</li>
          <li>분할 납부는 불가하며, 전액 입금만 인정됩니다.</li>
        </ul>
      </div>
    </div>
  </div>
<script src="https://js.tosspayments.com/v1"></script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/residentList.js"></script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/residentPayment.js"></script>
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const unitSelect = document.getElementById("unitSelect");

    unitSelect.addEventListener("change", function () {
      const selectedUnitId = this.value;
      const bldgId = localStorage.getItem("selectedBuildingId"); // 건물 ID가 필요하면

      if (selectedUnitId) {
        // ✅ 여기에 공과금 데이터를 AJAX로 요청
        axios.get(`/ajax/resident/api/payment/charge-info`, {
          params: {
            unitId: selectedUnitId,
            bldgId: bldgId
          }
        })
        .then(response => {
          // 데이터를 받아서 렌더링
          renderChargeInfo(response.data);
        })
        .catch(error => {
          console.error("세대 데이터 불러오기 실패:", error);
        });
      }
    });
  });

  function renderChargeInfo(data) {
    // 여기에 표, 차트 등 렌더링 로직 작성
    console.log("청구 내역:", data);
  }
</script>


</body>
</html>
