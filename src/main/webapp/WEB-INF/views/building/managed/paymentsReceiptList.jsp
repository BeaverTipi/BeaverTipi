<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>납부데이터 통합관리</title>
  <link rel="stylesheet" href="/app/css/building/chargeBill/paymentsReceiptList.css" />
  <style>

  </style>
</head>
<body>
<%
  java.time.LocalDate now = java.time.LocalDate.now();
%>

<h2 class="board-title">납부데이터 통합관리</h2>
<div class="page-card">
<div id="monthFilterRow" class="d-flex justify-content-between align-items-center flex-wrap mb-3">
<div id="emptyDiv"></div>
  <!-- ⬇ 중간 정렬을 위한 wrapper 추가 -->
  <div id="monthHeaderWrapper" class="flex-grow-1 d-flex justify-content-center">
    <div id="monthHeader" class="fw-semibold">
      <%=now.getYear()%>년 <%=String.format("%02d", now.getMonthValue())%>월 납부현황
    </div>
  </div>

  <div id="chargeMonthFilter" class="d-flex align-items-center ms-md-3 mt-2 mt-md-0">
    <label for="chgbillChargeMonth" class="form-label mb-0 me-2 fw-semibold">청구월</label>
    <input type="month" id="chgbillChargeMonth" class="form-control"
      value="<%=now.getYear()%>-<%=String.format("%02d", now.getMonthValue())%>" />
  </div>
</div>

<div class="card mb-4">
  <div class="card-body p-4">

    <!-- 🔍 필터 조건: 한 줄로 -->
    <div class="search-grid-row">
      <div class="search-item">
        <label for="bldgId">건물</label>
        <select id="bldgId" class="form-control">
          <option value="">전체</option>
        </select>
      </div>
      <div class="search-item">
        <label for="unitRoom">호수</label>
        <select id="unitRoom" class="form-control">
          <option value="">전체</option>
        </select>
      </div>
      <div class="search-item full-width">
        <label for="chgbillDueStartDate">납기일자</label>
        <div class="date-range-group">
          <input type="date" id="chgbillDueStartDate" class="form-control" />
          <span class="range-separator">~</span>
          <input type="date" id="chgbillDueEndDate" class="form-control" />
        </div>
      </div>
    </div>

    <!-- 📊 요약 카드 하단 1줄 -->
    <div class="d-flex flex-wrap justify-content-between gap-3 mt-4">
      <div class="summary-card orange">
        청구서 현황
        <span id="summaryTotalCount">-</span>
        <span id="summaryTotalAmount">-</span>
      </div>
      <div class="summary-card red">
        미납 현황
        <span id="summaryUnpaidCount">-</span>
        <span id="summaryUnpaidAmount">-</span>
      </div>
      <div class="summary-card blue">
        수납 현황
        <span id="summaryPaidCount">-</span>
        <span id="summaryPaidAmount">-</span>
      </div>
      <div class="summary-card purple">
        연체 현황
        <span id="summaryLateCount">-</span>
        <span id="summaryLateAmount">-</span>
      </div>
    </div>
  </div>
</div>


<!-- 📊 납부 테이블 -->
<table id="paymentTable">
  <thead>
    <tr>
      <th>No</th>
      <th>건물명</th>
      <th>호수</th>
      <th>임차인명</th>
      <th>청구금액</th>
      <th>납부금액</th>
      <th>납부상태</th>
      <th>청구일자</th>
      <th>납기일자</th>
      <th>납부일자</th>
      <th>청구계좌</th>
    </tr>
  </thead>
  <tbody>
    <!-- 동적 데이터 삽입 예정 -->
  </tbody>
</table>

<!-- 📎 페이지네이션 -->
<div class="pagination-wrapper mt-4" id="paginationContainer"></div>
</div>
<div id="billDetailModalWrapper" class="modal-overlay" style="display: none;"></div>
<script>
  window.rentalPtyId = "${rentalPtyId}";
</script>
<script src="/app/js/building/chargeBill/paymentsReceiptList.js"></script>

<!-- 💬 상세 모달 -->
</body>
</html>
