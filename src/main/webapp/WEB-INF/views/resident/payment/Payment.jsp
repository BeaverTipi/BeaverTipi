<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>납부 화면</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/modal.css'/>" />
  <script src="<c:url value='/app/js/building/move-in/residentList.js'/>?v=${now.time}"></script>
  <style>
    /* 컨테이너 */
    body { font-family: Arial, sans-serif; background: #f8f9fa; color: #212529; margin:0; }
    .container { max-width:960px; margin:50px auto; background:#fff; padding:20px; border-radius:8px;
                 box-shadow:0 4px 8px rgba(0,0,0,0.1); }

    /* 탭 / 빌딩 선택 */
    .tabs { display:flex; margin-bottom:20px; border-bottom:2px solid #ddd; }
    .tab-button { padding:15px 30px; cursor:pointer; border:1px solid transparent;
                  background:#f0f0f0; font-size:18px; font-weight:bold; color:#555;
                  transition: background-color .3s; }
    .tab-button.active { background:#fff; border-color:#ddd; border-bottom:2px solid #007bff;
                         color:#007bff; }
	.tabs form {
		  margin-left: 20px;
		  align-self: center;
		}
    /* 결제 방식 */
    .payment-method { margin-bottom:30px; padding:20px; border:1px solid #ddd;
                      border-radius:8px; background:#f9f9f9; }
    .payment-method h3 { margin:0 0 10px; color:#007bff; }
    .payment-options label { margin-right:20px; cursor:pointer; font-size:18px; }

    /* 좌우 비교 레이아웃 */
    .payment-comparison { display:flex; gap:20px; margin-bottom:30px; }
    .payment-panel { flex:1; background:#fff; padding:15px; border:1px solid #ddd;
                     border-radius:6px; box-shadow:0 2px 4px rgba(0,0,0,0.05); }

    /* 세로형 테이블 */
    	.vertical-table {
		  width: 100%;
		  border-collapse: collapse;    /* 셀 경계선 합치기 */
		}
		
		/* th, td 모두 테두리와 패딩 적용 */
		.vertical-table th,
		.vertical-table td {
		  border: 1px solid #ddd;
		  padding: 8px;
		}
		
		/* 왼쪽 첫 번째 컬럼: 헤더(항목) */
		.vertical-table th {
		  width: 40%;
		  text-align: left;   /* 헤더는 왼쪽 정렬 */
		  background: #fafafa;
		}
		
		/* 오른쪽 두 번째 컬럼: 데이터 */
		.vertical-table td {
		  width: 60%;
		  text-align: right;  /* 데이터는 오른쪽 정렬 */
		}

    /* 하단 버튼 */
    .footer-buttons { text-align:center; margin-top:20px; }
    .footer-buttons button { padding:15px 30px; border:none; border-radius:8px;
                             font-size:18px; font-weight:bold; cursor:pointer; margin:0 10px; }
    .pay-button { background:#28a745; color:#fff; }
    .pay-button:hover { background:#218838; }
    .cancel-button { background:#6c757d; color:#fff; }
    .cancel-button:hover { background:#5a6268; }
    
     /* 상세보기 모달 버튼 */
    .modal {
	  display: none;
	  position: fixed;
	  z-index: 999;
	  padding-top: 80px;
	  left: 0; top: 0;
	  width: 100%; height: 100%;
	  overflow: auto;
	  background-color: rgba(0,0,0,0.4);
	}
	.modal-content {
	  background-color: #fff;
	  margin: auto;
	  padding: 30px;
	  border: 1px solid #888;
	  width: 80%;
	  max-width: 700px;
	  border-radius: 8px;
	}
	.modal .close {
	  float: right;
	  font-size: 28px;
	  font-weight: bold;
	  cursor: pointer;
	}
	.modal .close:hover {
	  color: red;
	}
    
  </style>
</head>
<body>
  <div class="container">
    <!-- 상단 탭 & 빌딩 선택 -->
    <div class="tabs">
	  <button class="tab-button active">납부</button>
	  <form id="noticeSearchForm" method="get" action="/resident/payment" style="margin-left: 20px;">
	    <select name="bldgIdParam" onchange="noticeSearchForm.submit()">
	      <c:forEach var="unit" items="${unitList}">
	        <option value="${unit.bldgId}"
	          <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
	          ${unit.building.bldgNm}
	        </option>
	      </c:forEach>
	    </select>
	  </form>
	</div>

    <!-- 결제 방식 선택 -->
    <div class="payment-method">
      <h3>결제 방식 선택</h3>
      <div class="payment-options">
        <input type="radio" id="card_pay" name="payment_method" value="card" checked/>
        <label for="card_pay">카드 납부</label>
        <input type="radio" id="virtual_account_pay" name="payment_method" value="virtual_account"/>
        <label for="virtual_account_pay">가상계좌 입금</label>
      </div>
    </div>

    <!-- 전전월·전월 좌우 비교 -->
    <div class="payment-comparison">
      <!-- 전전월 -->
      <div class="payment-panel">
		  <h3>전전월 (${beforeLastMonth})
		  <button type="button" class="detail-btn" data-month="${lastMonth}">상세보기</button>
		  </h3>
		  <table class="vertical-table">
		    <tbody>
		      <c:forEach var="bill" items="${chargeBillListBeforeLastMonth}">
		        <tr><th>지로번호</th><td>${bill.chgbillId}</td></tr>
		        <tr><th>세대명</th><td>${bill.unitId}</td></tr>
		        <tr><th>총 금액</th><td><fmt:formatNumber value="${bill.chgbillAmount}" type="number"/>원</td></tr>
		        <tr><th>납부상태</th><td>${bill.chgbillStatus}</td></tr>
		        <tr><th>납부마감일자</th><td>${bill.formattedDueDate}</td></tr>
		      </c:forEach>
		    </tbody>
		  </table>
		</div>
      <!-- 전월 -->
      <div class="payment-panel">
        <h3>
          전월 (${previousMonth})
          <button type="button" class="detail-btn" data-month="${previousMonth}">상세보기</button>
        </h3>
        <table class="vertical-table">
           <tbody>
			  <c:if test="${not empty chargeBillListLastMonth}">
				  <c:set var="bill" value="${chargeBillListLastMonth[0]}" />
				  <tr><th>지로번호</th>   <td>${bill.chgbillId}</td></tr>
				  <tr><th>세대명</th>     <td>${bill.unitId}</td></tr>
				  <tr><th>총 금액</th>     <td><fmt:formatNumber value="${bill.chgbillAmount}" type="number"/>원</td></tr>
				  <tr><th>납부상태</th>   <td>${bill.chgbillStatus}</td></tr>
				  <tr><th>납부마감일자</th><td>${bill.formattedDueDate}</td></tr>
				</c:if>
			</tbody>
        </table>
      </div>
    </div>

    <!-- 결제/취소 버튼 -->
    <div class="footer-buttons">
      <button id="payBtn" class="pay-button">납부하기</button>
      <button type="button" class="cancel-button" onclick="location.reload()">취소</button>
    </div>
  </div>

  <!-- 상세보기 모달 (동일) -->
  <div id="detailModal" class="modal">
    <div class="modal-content">
      <span class="close">&times;</span>
      <div id="modalBody">
      	<h3>202507 청구서 상세 내역</h3>
			  <h4>📌 사용자 정보</h4>
			  <ul>
			    <li>지로번호: 2505000007</li>
			    <li>건물명: 현대아파트</li>
			    <li>임대인명: 홍길동</li>
			    <li>입주민명: 김찬영</li>
			  </ul>
			
			  <h4>💡 에너지 사용량</h4>
			  <ul>
			    <li>전기 사용량: 123 kWh</li>
			    <li>전기요금: 25,000원</li>
			    <li>가스 사용량: 30㎥</li>
			    <li>가스요금: 15,000원</li>
			    <li>수도 사용량: 20㎥</li>
			    <li>수도요금: 10,000원</li>
			  </ul>
			
			  <h4>🧾 공동 관리비 내역</h4>
			  <ul>
			    <li>총 관리비: 18,000원</li>
			  </ul>
			
			  <h4>📍 청구내역</h4>
			  <ul>
			    <li>청구금액: 68,000원</li>
			    <li>납부상태: 미납</li>
			    <li>납부마감일자: 2025-07-25</li>
			  </ul>
      </div>
    </div>
  </div>
</body>
</html>
