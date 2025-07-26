<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>납부데이터 통합관리</title>
  <link rel="stylesheet" href="/app/css/building/chargeBill/paymentsReceiptList.css">
</head>
<body>

<%
  java.time.LocalDate now = java.time.LocalDate.now();
%>

<h2>납부데이터 통합관리</h2>
<div class="month-header" id="monthHeader">
  📅 <%= now.getYear() %>년 <%= now.getMonthValue() %>월 납부현황
</div>

<!-- 🔍 필터 및 요약 카드 -->
<div class="filter-summary-row">
  <!-- 좌측 필터 -->
  <div class="filter-left">
    <div class="filter-group">
      <label for="bldgId">건물:</label>
      <select id="bldgId"><option value>전체</option></select>
    </div>
    <div class="filter-group">
      <label for="unitRoom">호수:</label>
      <select id="unitRoom"><option value>전체</option></select>
    </div>
  </div>

  <!-- 중앙 요약 카드 -->
	<div class="summary-card orange" data-status="">청구서 현황<br>
	  <span id="summaryTotalCount">-</span><br>
	  <span id="summaryTotalAmount">-</span>
	</div>
	<div class="summary-card red" data-status="001">미납 현황<br>
	  <span id="summaryUnpaidCount">-</span><br>
	  <span id="summaryUnpaidAmount">-</span>
	</div>
	<div class="summary-card blue" data-status="002">수납 현황<br>
	  <span id="summaryPaidCount">-</span><br>
	  <span id="summaryPaidAmount">-</span>
	</div>
	<div class="summary-card purple" data-status="004">연체 현황<br>
	  <span id="summaryLateCount">-</span><br>
	  <span id="summaryLateAmount">-</span>
	</div>

  <!-- 우측 날짜 필터 -->
  <div class="filter-right">
    <div class="filter-group">
      <label for="chgbillChargeMonth">청구월:</label>
      <input type="month" id="chgbillChargeMonth" value="<%= now.getYear() %>-<%= String.format("%02d", now.getMonthValue()) %>" />
    </div>
    <div class="filter-group">
      <label>납기일 기간:</label>
      <input type="date" id="chgbillDueStartDate" /> ~ <input type="date" id="chgbillDueEndDate" />
    </div>
  </div>
</div>
<!-- 📊 납부 테이블 -->
<table id="paymentTable">
  <thead>
    <tr>
      <th>No</th><th>건물명</th><th>층</th><th>호수</th><th>임차인명</th>
      <th class="sortable">청구금액</th><th class="sortable">납부금액</th>
      <th class="sortable">납부상태</th><th class="sortable">청구일</th>
      <th class="sortable">납기일</th><th class="sortable">납부일</th><th>청구계좌</th>
    </tr>
  </thead>
  <tbody>
    <!-- 동적 데이터 삽입 예정 -->
  </tbody>
</table>
<!-- 📎 페이지네이션 버튼 영역 -->
<div class="pagination" id="paginationContainer"></div>


<div class="modal">
  <h2>청구 상세 정보</h2>

  <!-- 기본 정보 -->
  <div class="info-block">
    <p><strong>건물명:</strong> ${item.bldgNm}</p>
    <p><strong>호수:</strong> ${item.unitRoom}</p>
    <p><strong>청구일:</strong> ${item.billDate}</p>
    <p><strong>납기일:</strong> ${item.dueDate}</p>
    <p><strong>완납일:</strong> ${item.paidDate || '-'}</p>
    <p><strong>청구상태:</strong> ${item.status}</p>
  </div>

  <!-- 공용관리비 항목 -->
  <div class="charge-section">
    <h3>공용 관리비</h3>
    <ul>
      <li>청소비: ${cleanFee} 원</li>
      <li>승강기 유지비: ${elevatorFee} 원</li>
      <li>공용 전기료: ${publicElectricFee} 원</li>
      <li>공용 수도료: ${publicWaterFee} 원</li>
      <li>일반 운영비: ${operationFee} 원</li>
      <li>경비 인건비: ${guardFee} 원</li>
      <li>방역 소독비: ${disinfectionFee} 원</li>
      <li>소모품비: ${supplyFee} 원</li>
      <li>소방 설비 유지비: ${fireSafetyFee} 원</li>
      <li>보안 시스템 유지비: ${securityFee} 원</li>
    </ul>
  </div>

  <!-- 개인 에너지 사용량 -->
  <div class="energy-section">
    <h3>에너지 사용 내역</h3>
    <table>
      <thead>
        <tr>
          <th>항목</th><th>사용량</th><th>비용</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td>가스</td><td>${gasUsage} ㎥</td><td>${gasFee} 원</td>
        </tr>
        <tr>
          <td>수도</td><td>${waterUsage} ㎥</td><td>${waterFee} 원</td>
        </tr>
        <tr>
          <td>전기</td><td>${electricUsage} kWh</td><td>${electricFee} 원</td>
        </tr>
      </tbody>
    </table>
  </div>

  <!-- 청구액 및 납부액 -->
  <div class="amount-section">
    <p><strong>청구액:</strong> ${item.totalCharge} 원</p>

    <div class="editable-field">
      <label for="paidAmount"><strong>납부액:</strong></label>
      <input type="number" id="paidAmount" value="${item.paidAmount}" readonly>
      <button onclick="toggleEdit(this)">수정</button>
    </div>
  </div>
</div>

<script>
  window.rentalPtyId = "${rentalPtyId}";
</script>
<script src="/app/js/building/chargeBill/paymentsReceiptList.js"></script>
</body>
</html>