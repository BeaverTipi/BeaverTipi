<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>관리비 납부 안내</title>
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
  margin: 40px auto;
  padding: 0 20px;
}
.header-bar {
  display: flex;
  justify-content: space-between;
  align-items: center;
  border-bottom: 2px dashed #bbb;
  padding: 20px 0;
}
.page-title h1 {
  margin: 0;
  font-size: 28px;
  color: #007bff;
}
.page-title p {
  margin-top: 6px;
  font-size: 15px;
  color: #555;
}
.building-selector {
  display: flex;
  align-items: center;
  gap: 10px;
}
.building-icon {
  font-size: 20px;
}
.building-selector select {
  padding: 6px 10px;
  font-size: 15px;
  border-radius: 4px;
  border: 1px solid #ccc;
}
.giro-paymethod {
  padding: 20px;
  background: #f4f6fa;
  border: 1px solid #ccc;
  border-radius: 6px;
  margin-top: 20px;
}
.giro-paymethod h3 {
  margin-bottom: 10px;
  color: #007bff;
}
.giro-paymethod label {
  margin-right: 20px;
  font-size: 15px;
}
.notice-grid {
  margin: 30px 0;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 20px;
}
.giro-notice-card {
  background: #fff;
  border: 2px dashed #999;
  border-radius: 6px;
  padding: 20px;
  position: relative;
  transition: box-shadow .2s;
}
.giro-notice-card:hover {
  box-shadow: 0 4px 12px rgba(0,0,0,0.1);
}
.giro-notice-card h4 {
  margin: 0 0 10px;
  color: #007bff;
  font-size: 16px;
}
.giro-notice-card p {
  margin: 6px 0;
  font-size: 14px;
}
.status {
  position: absolute;
  top: 20px;
  right: 20px;
  padding: 4px 8px;
  border-radius: 4px;
  font-size: 12px;
}
.status.unpaid {
  background: #ffc107;
  color: #fff;
}
.status.paid {
  background: #28a745;
  color: #fff;
}
.giro-receipt {
  margin-top: 15px;
  padding-top: 15px;
  border-top: 1px solid #ccc;
  font-size: 14px;
  line-height: 1.6;
}

.section-divider {
  border: none;
  border-top: 2px dashed #bbb;
  margin: 40px 0;
}

.double-info-box {
  display: flex;
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 20px;
  gap: 30px;
  font-size: 14px;
  line-height: 1.6;
}

.info-column {
  flex: 1;
  padding-right: 20px;
}

.info-column:first-child {
  border-right: 1px dashed #bbb;
}

.info-column h3 {
  margin-top: 0;
  color: #007bff;
  font-size: 16px;
}

.info-column ul {
  padding-left: 18px;
  margin: 8px 0;
}

.info-column li {
  margin-bottom: 6px;
}

.footer-buttons {
  text-align: center;
  margin: 40px 0;
}

.pay-button {
  background: #28a745;
  color: #fff;
  padding: 15px 40px;
  font-size: 18px;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: background .2s;
}

.pay-button:hover {
  background: #218838;
}
.main-grid {
  display: flex;
  gap: 30px;
  margin: 40px 0;
}

.notice-grid {
  flex: 2;
}

.double-info-box {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}
.ad-sidebar {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.ad-box {
  background: #fff;
  border: 1px solid #ccc;
  border-radius: 6px;
  padding: 20px;
  font-size: 14px;
  line-height: 1.6;
  box-shadow: 0 2px 6px rgba(0,0,0,0.05);
}

.combined-paybox {
    display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 15px 20px;
  background: #f4f6fa;
  border: 1px solid #ccc;
  border-radius: 6px;
  gap: 20px;
  flex-wrap: wrap; /* 화면이 좁을 땐 줄바꿈 */
}

.pay-button-wrap {
   display: flex;
  align-items: center;
  justify-content: flex-end;
  flex-shrink: 0;
}

.pay-button {
 background: linear-gradient(to right, #007bff, #00c2ff);
  color: #fff;
  padding: 12px 24px;
  font-size: 16px;
  border: none;
  border-radius: 6px;
  cursor: pointer;
  box-shadow: 0 2px 6px rgba(0,0,0,0.1);
  transition: transform 0.2s ease;
}

.pay-button:hover {
  transform: scale(1.05);
}
.giro-paymethod.combined-paybox {
  display: flex;
  flex-direction: column;
  align-items: flex-start; /* 좌측 정렬 */
  gap: 10px;
  margin-top: 10px;
}

.giro-paymethod.combined-paybox h3 {
  margin: 0;
}

.giro-paymethod.combined-paybox label {
  font-size: 14px;
}

.giro-paymethod .pay-button-wrap {
  align-self: flex-end; /* 버튼은 오른쪽 정렬 */
}
.payment-options {
  display: flex;
  flex-direction: column;
  flex: 1;
}
.payment-options h3 {
  margin: 0 0 10px 0;
  color: #007bff;
  font-size: 16px;
}
.payment-options label {
  margin-bottom: 6px;
  font-size: 15px;
  white-space: nowrap;
}
.giro-notice-card.highlight {
  border: 3px solid #007bff;
  background: #e9f3ff;
  box-shadow: 0 0 12px rgba(0, 123, 255, 0.2);
  transform: scale(1.02);
  transition: all 0.3s ease;
}
.badge-pay {
  position: absolute;
  top: -10px;
  left: -10px;
  background: #007bff;
  color: white;
  padding: 4px 10px;
  font-size: 12px;
  border-radius: 12px;
  font-weight: bold;
  box-shadow: 0 2px 4px rgba(0,0,0,0.2);
}


  </style>
</head>
<body>
  <div class="container">
    <!-- 상단 헤더 -->
    <div class="header-bar">
      <div class="page-title">
        <h1>📄 관리비 납부 페이지</h1>
        <p>청구 내역을 확인하고 납부를 진행하세요</p>
      </div>
      <div class="building-selector">
        <span class="building-icon">🏢</span>
        <form id="noticeSearchForm" method="get" action="/resident/payment">
          <select name="bldgIdParam" onchange="noticeSearchForm.submit()">
            <c:forEach var="unit" items="${unitList}">
              <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
                ${unit.building.bldgNm}
              </option>
            </c:forEach>
          </select>
        </form>
      </div>
    </div>
    
    <!-- 청구 카드 영역 -->
    <div class="main-grid">
  <!-- 왼쪽: 청구 카드 -->
  <div class="notice-grid">
    <c:forEach var="bill" items="${chargeBillList}" varStatus="loop">
	 <div class="giro-notice-card <c:if test='${loop.index == 0}'>highlight</c:if>'" 
       data-unit-id="${bill.unitId}" data-month="${bill.chgbillChargeMonth}">
        <c:if test="${loop.index == 1}">
	      <div class="badge-pay">납부 대상</div>
	    </c:if>
        <h4>${bill.chgbillChargeMonth} 청구 안내</h4>
        <p>금액: <b><fmt:formatNumber value="${bill.chgbillAmount}" type="number"/>원</b></p>
        <p>납부기한: ${bill.formattedDueDate}</p>
        <c:choose>
        <c:when test="${bill.chgbillStatusName eq '미납'}">
          <span class="status unpaid">${bill.chgbillStatusName}</span>
        </c:when>
        <c:otherwise>
          <span class="status paid">${bill.chgbillStatusName}</span>
        </c:otherwise>
      </c:choose>
        <div class="giro-receipt"></div>
      </div>
    </c:forEach>
  </div>
  <!-- 오른쪽 광고 -->
  <div class="ad-sidebar">
    <div class="ad-box">
      <h3>📢 이벤트 안내</h3>
      <p>관리비 자동이체 시 스타벅스 기프티콘 증정!</p>
      <img src="/images/ad-banner.jpg" alt="광고 배너" style="width:100%; border-radius:6px; margin-top:10px;" />
    </div>
	   <div class="giro-paymethod combined-paybox">
	  <div class="payment-options">
	    <h3>💳 결제 방식</h3>
	    <label><input type="radio" name="payment_method" value="card" checked> 카드 납부</label>
	    <label><input type="radio" name="payment_method" value="account"> 가상계좌 입금</label>
	  </div>
	  <div class="pay-button-wrap">
	    <button class="pay-button">💸 납부하기</button>
	  </div>
	</div>
  </div>
</div>


    <!-- 안내 영역 분리 -->
    <hr class="section-divider" />

    <!-- 안내 박스 -->
    <div class="double-info-box">
    <div class="info-column">
      <h3>💡 입금 안내</h3>
      <p>국민은행 123-456-7890<br/>예금주: 동산아파트 관리사무소</p>
      <p>입금자명에 “세대ID(예: 101동201호)”를 꼭 기재해주세요.</p>
    </div>
    <div class="info-column">
      <h3>⚠️ 입주자 유의 사항</h3>
      <ul>
        <li>납부기한 경과 시 연체료가 부과됩니다.</li>
        <li>가상계좌 입금 선택 시, 이체 후 1~2일 이내 반영됩니다.</li>
        <li>분할 납부는 불가하며, 전액 입금만 인정합니다.</li>
      </ul>
    </div>
  </div>



<script src="<c:url value='/app/js/building/move-in/residentList.js' />"></script>

</body>
</html>
