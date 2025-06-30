<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>납부데이터 통합관리</title>
<link rel="stylesheet"
	href="/app/css/building/managed/paymentsReceiptList.css">
</head>
<body>

	<h2>납부데이터 통합관리</h2>

	<div class="filter-box">
		<div class="filter-grid">
			<select>
				<option value="">납부상태</option>
				<option>완납</option>
				<option>미납</option>
				<option>부분납부</option>
			</select> <select>
				<option value="">납부방법</option>
				<option>계좌이체</option>
				<option>카드</option>
				<option>현금</option>
			</select> <select>
				<option value="">청구서 유형</option>
				<option>전기요금</option>
				<option>임대료</option>
				<option>관리비</option>
			</select> <select>
				<option value="">조정여부</option>
				<option>조정</option>
				<option>미조정</option>
			</select> <select>
				<option value="">할인여부</option>
				<option>할인 적용</option>
				<option>할인 없음</option>
			</select> <select>
				<option value="">증빙종류</option>
				<option>세금계산서</option>
				<option>계산서</option>
				<option>현금영수증</option>
			</select> <select>
				<option value="">증빙발행여부</option>
				<option>발행</option>
				<option>미발행</option>
			</select> <select>
				<option value="">건물명</option>
				<option>현대오피스텔</option>
				<option>마포상가</option>
			</select> <select>
				<option value="">양식</option>
				<option>표준양식</option>
				<option>커스텀양식</option>
			</select> <select>
				<option value="">청구서태그</option>
				<option>전기</option>
				<option>임대</option>
				<option>관리비</option>
			</select> <input type="text" placeholder="납부고유번호" class="full">
			<div class="filter-buttons">
				<button class="gray">초기화</button>
				<button class="blue">검색</button>
			</div>
		</div>
	</div>

	<!-- 요약 상태바 -->
	<div class="summary-bar">
		<div class="orange">
			청구서 현황<br> 2건<br> ₩340,000
		</div>
		<div class="blue-sum">
			수납현황<br> 1건<br> ₩200,000
		</div>
		<div class="purple">
			조정 청구서<br> 1건
		</div>
	</div>

	<!-- 테이블 -->
	<table>
		<thead>
			<tr>
				<th>No</th>
				<th>건물명</th>
				<th>층</th>
				<th>호수</th>
				<th>입차인명</th>
				<th>청구서명</th>
				<th>청구금액(단위)</th>
				<th>납부상태</th>
				<th>납기일</th>
				<th>납부일</th>
				<th>납부방법</th>
				<th>증빙발행</th>
				<th>태그</th>
			</tr>
		</thead>
		<tbody>
			<tr>
				<td>1</td>
				<td>현대오피스텔</td>
				<td>3</td>
				<td>301</td>
				<td>홍길동</td>
				<td>전기요금</td>
				<td>200,000</td>
				<td>완납</td>
				<td>2025-06-20</td>
				<td>2025-06-18</td>
				<td>계좌이체</td>
				<td>세금계산서</td>
				<td>전기</td>
			</tr>
			<tr>
				<td>2</td>
				<td>마포상가</td>
				<td>5</td>
				<td>503</td>
				<td>이순신</td>
				<td>임대료</td>
				<td>140,000</td>
				<td>미납</td>
				<td>2025-06-25</td>
				<td>-</td>
				<td>카드</td>
				<td>현금영수증</td>
				<td>임대</td>
			</tr>
		</tbody>
	</table>

</body>
</html>
