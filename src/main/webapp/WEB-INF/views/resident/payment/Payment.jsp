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
    }
    .header-bar {
      display: flex;
      justify-content: space-between;
      align-items: center;
      border-bottom: 2px dashed #bbb;
      padding-bottom: 10px;
    }
    .page-title h1 {
      font-size: 22px;
      margin: 0;
      color: #007bff;
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
    }
    .notice-grid {
      flex: 2;
    }
    .giro-notice-card {
      background: #fff;
      border: 2px dashed #999;
      border-radius: 6px;
      padding: 20px;
    }
    .giro-notice-card h4 {
      color: #007bff;
      margin-top: 0;
    }
    .energy-summary {
      margin-top: 10px;
      padding: 12px;
      font-size: 13px;
      background: #fdfdfd;
      border: 1px dashed #ccc;
      border-radius: 6px;
    }
    .diff.up { color: #e74c3c; font-weight: bold; }
    .diff.down { color: #2ecc71; font-weight: bold; }
    .diff.same { color: #999; }

    .ad-sidebar {
      flex: 1;
      display: flex;
      flex-direction: column;
      gap: 16px;
    }

    .ad-sidebar-row {
      display: flex;
      flex-direction: row;
      gap: 16px;
    }

    .ad-box, .total-charge-box, .combined-paybox {
      background: #fff;
      border: 1px solid #ccc;
      border-radius: 6px;
      padding: 15px;
      font-size: 14px;
      line-height: 1.4;
      flex: 1;
    }

    .pay-button {
      background: linear-gradient(to right, #007bff, #00c2ff);
      color: #fff;
      padding: 10px 20px;
      font-size: 14px;
      border: none;
      border-radius: 6px;
      cursor: pointer;
      margin-top: 10px;
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
      border-right: 1px dashed #bbb;
      padding-right: 15px;
    }
    .total-charge-amount {
      font-size: 24px;
      font-weight: bold;
      color: #007bff;
      margin-top: 10px;
    }
  </style>
</head>
<body>
  <fmt:parseDate var="prevDate" value="${previousMonth}" pattern="yyyyMM" />
  <div class="container">
    <div class="header-bar">
      <div class="page-title">
        <h1>📄 관리비 납부 비교</h1>
        <p>${previousMonth} 기준 항목을 전전월과 비교한 요약 카드입니다</p>
      </div>
      <form id="noticeSearchForm" method="get" action="/resident/payment">
        <select name="bldgIdParam">
          <c:forEach var="unit" items="${unitList}">
            <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
              ${unit.building.bldgNm}
            </option>
          </c:forEach>
        </select>
      </form>
    </div>
    <div class="main-grid">
     <div class="notice-grid">
	  <c:set var="totalIntegratedAmount" value="0" />
	  <c:set var="totalSavedAmount" value="0" />
	  <c:forEach var="item" items="${chargeBillComparisonList}">
	    <c:set var="totalIntegratedAmount" value="${totalIntegratedAmount + item.currentAmount}" />
	    <c:if test="${item.diffAmount < 0}">
	      <c:set var="totalSavedAmount" value="${totalSavedAmount + (-item.diffAmount)}" />
	    </c:if>
	  </c:forEach>
        <div class="giro-notice-card">
          <h4><fmt:formatDate value="${prevDate}" pattern="yyyy년 MM월" /> 관리비 요약</h4>
          <table style="width:100%; border-collapse:collapse; margin-top:12px;">
            <thead>
              <tr><th>항목</th><th style="text-align:right">금액</th><th style="text-align:right">변동</th></tr>
            </thead>
            <tbody>
              <c:forEach var="item" items="${chargeBillComparisonList}">
                <tr>
                  <td>${item.feeName}</td>
                  <td style="text-align:right"><fmt:formatNumber value="${item.previousAmount}" type="number" groupingUsed="true" />원</td>
                  <td style="text-align:right">
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
            <c:forEach var="type" items="${energySummary[currentMonth]}">
              <c:set var="energyType" value="${type.key}" />
              <c:if test="${energyType eq '전기' || energyType eq '가스' || energyType eq '수도'}">
                <c:set var="currRow" value="${type.value}" />
                <c:set var="prevRow" value="${energySummary[previousMonth][energyType]}" />
                <c:set var="diffUsage" value="${currRow.usageQty - prevRow.usageQty}" />
                <c:set var="diffCharge" value="${currRow.chargeAmt - prevRow.chargeAmt}" />
                <p>
                    🔹 ${energyType} 사용량:  
                  						<fmt:formatNumber value="${prevRow.usageQty}" type="number" groupingUsed="true" /> ${prevRow.unitName} →
        								<fmt:formatNumber value="${currRow.usageQty}" type="number" groupingUsed="true" /> ${currRow.unitName}
                  <c:choose>
                    <c:when test="${diffUsage > 0}"><span class="diff up">▲ <fmt:formatNumber value="${diffUsage}" type="number" groupingUsed="true" /></span></c:when>
                    <c:when test="${diffUsage < 0}"><span class="diff down">▼ <fmt:formatNumber value="${-diffUsage}" type="number" groupingUsed="true" /></span></c:when>
                    <c:otherwise><span class="diff same">변화 없음</span></c:otherwise>
                  </c:choose>
                  , 요금: <fmt:formatNumber value="${prevRow.chargeAmt}" type="currency" /> → <fmt:formatNumber value="${currRow.chargeAmt}" type="currency" />
                  <c:choose>
                    <c:when test="${diffCharge > 0}"><span class="diff up">▲ <fmt:formatNumber value="${diffCharge}" type="currency" /></span></c:when>
                    <c:when test="${diffCharge < 0}"><span class="diff down">▼ <fmt:formatNumber value="${-diffCharge}" type="currency" /></span></c:when>
                    <c:otherwise><span class="diff same">변화 없음</span></c:otherwise>
                  </c:choose>
                </p>
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
		  <h3>📦 총 관리비 내역</h3>
		  <p class="total-charge-amount">
		    <fmt:formatNumber value="${totalIntegratedAmount}" type="currency" />
		  </p>
		  <c:if test="${totalSavedAmount > 0}">
		    <p style="font-size:13px; color:#2ecc71; margin-top:6px;">
		      💚 전월 대비 <strong><fmt:formatNumber value="${totalSavedAmount}" type="currency" /></strong> 절감되었습니다
		    </p>
		  </c:if>
		</div>
        <div class="combined-paybox">
          <h3>💳 결제 방식</h3>
          <c:forEach items="${payment}" var="pay">
          <label><input type="radio" name="payment_method" value="${pay.codeName}" checked>${pay.codeName}</label>
          </c:forEach>
          <button class="pay-button"  data-name="<fmt:formatDate value='${prevDate}' pattern='MM' />월 총 관리비 금액" data-pay="${totalIntegratedAmount}">💸 납부하기</button>
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
<script src="${pageContext.request.contextPath}/app/js/building/move-in/residentList.js"></script>
</body>
</html>
