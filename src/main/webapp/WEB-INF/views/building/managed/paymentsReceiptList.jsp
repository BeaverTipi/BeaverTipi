<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>납부데이터 통합관리</title>
<link rel="stylesheet"
	href="/app/css/building/chargeBill/paymentsReceiptList.css">
</head>
<body>

	<%
	java.time.LocalDate now = java.time.LocalDate.now();
	%>

	<h2>납부데이터 통합관리</h2>
	<div class="month-header" id="monthHeader">
		📅
		<%=now.getYear()%>년
		<%=now.getMonthValue()%>월 납부현황
	</div>

	<!-- 🔍 필터 및 요약 카드 -->
	<div class="filter-summary-row">
		<!-- 좌측 필터 -->
		<div class="filter-left">
			<div class="filter-group">
				<label for="bldgId">건물:</label> <select id="bldgId"><option
						value>전체</option></select>
			</div>
			<div class="filter-group">
				<label for="unitRoom">호수:</label> <select id="unitRoom"><option
						value>전체</option></select>
			</div>
		</div>

		<!-- 중앙 요약 카드 -->
		<div class="summary-card orange" data-status="">
			청구서 현황<br> <span id="summaryTotalCount">-</span><br> <span
				id="summaryTotalAmount">-</span>
		</div>
		<div class="summary-card red" data-status="001">
			미납 현황<br> <span id="summaryUnpaidCount">-</span><br> <span
				id="summaryUnpaidAmount">-</span>
		</div>
		<div class="summary-card blue" data-status="002">
			수납 현황<br> <span id="summaryPaidCount">-</span><br> <span
				id="summaryPaidAmount">-</span>
		</div>
		<div class="summary-card purple" data-status="004">
			연체 현황<br> <span id="summaryLateCount">-</span><br> <span
				id="summaryLateAmount">-</span>
		</div>

		<!-- 우측 날짜 필터 -->
		<div class="filter-right">
			<div class="filter-group">
				<label for="chgbillChargeMonth">청구월:</label> <input type="month"
					id="chgbillChargeMonth"
					value="<%=now.getYear()%>-<%=String.format("%02d", now.getMonthValue())%>" />
			</div>
			<div class="filter-group">
				<label>납기일자:</label> <input type="date" id="chgbillDueStartDate" />
				~ <input type="date" id="chgbillDueEndDate" />
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
				<th class="sortable">청구금액</th>
				<th class="sortable">납부금액</th>
				<th class="sortable">납부상태</th>
				<th class="sortable">청구일자</th>
				<th class="sortable">납기일자</th>
				<th class="sortable">납부일자</th>
				<th>청구계좌</th>
			</tr>
		</thead>
		<tbody>
			<!-- 동적 데이터 삽입 예정 -->
		</tbody>
	</table>
	<!-- 📎 페이지네이션 버튼 영역 -->
	<div class="pagination" id="paginationContainer"></div>



<div id="billDetailModalWrapper" class="modal-overlay" style="display: none;"></div>
	<script>
		window.rentalPtyId = "${rentalPtyId}";
	</script>
	<script src="/app/js/building/chargeBill/paymentsReceiptList.js"></script>
</body>
</html>