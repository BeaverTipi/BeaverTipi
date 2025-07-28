<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 28.     		김재윤           최초 생성
 *
-->

<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>Insert title here</title>
<style>
/* ✅ 전체 박스 스타일 */
.resident-block {
  border: none;
  background: #fff;
  padding: 24px;
  margin: 20px auto;
  max-width: 820px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.06);
  font-family: 'Noto Sans KR', sans-serif;
}

/* ✅ 상단 제목 영역 */
.resident-header {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
  border-bottom: 1px solid #ddd;
  margin-bottom: 16px;
  padding-bottom: 8px;
}

.resident-title {
  font-size: 22px;
  font-weight: 600;
  color: #333;
}

.usage-month {
  font-size: 14px;
  color: #888;
}

/* ✅ 버튼 */
.btn-close-modal {
  background-color: #333;
  color: #fff;
  border: none;
  padding: 6px 12px;
  border-radius: 4px;
  cursor: pointer;
  font-size: 13px;
}

/* ✅ 정보 블록 (문장형 정보) */
.info-block {
  margin-top: 12px;
  font-size: 15px;
  color: #222;
  line-height: 1.8;
}

.info-block p {
  margin: 4px 0;
}

/* ✅ 청구설명 강조 */
.desc {
  margin-top: 10px;
  padding: 10px 12px;
  background-color: #f0f4f8;
  border-left: 4px solid #4a90e2
</style>

</head>
<body>
<div class="resident-block" id="resident_${cbhDTO.unitId}">

  <!-- 🏠 타이틀: 건물 + 호수 + 세대주 -->
  <div class="resident-header">
    <h4 class="resident-title">
      ${cbhDTO.bldgNm} ${cbhDTO.unitRoom}호 ${cbhDTO.mbrNm}
    </h4>
    <small class="usage-month">
      ${cbhDTO.chgbillChargeMonth} 청구
    </small>
    <button class="btn-close-modal" onclick="closeModal()">닫기</button>
  </div>

  <!-- 💬 청구 요약 정보 -->
  <div class="info-block">
    <p>${cbhDTO.chgbillDate} 청구일 / 납부기한 ${cbhDTO.chgbillDueDate}</p>
    <p>납부상태: ${cbhDTO.chgbillStatus} / 납부일자: ${cbhDTO.chgbillPaidDate}</p>
    <p>청구계좌: ${cbhDTO.chgbillAccNum}</p>
    <p>총 청구액: ${cbhDTO.chgbillAmount}원</p>
    <p class="desc">${cbhDTO.chgbillDesc}</p>
  </div>

  <!-- 💡 통합 관리비 -->
  <c:forEach var="fee" items="${managementFee}">
    <div class="info-row">
      <span>${fee.intManFeeCd}</span>  
      <span>${fee.intgFeeAmount}원</span>
    </div>
  </c:forEach>

  <!-- 🔌 에너지 사용량 -->
  <c:forEach var="energy" items="${energyUsage}">
    <div class="info-row">
      <c:choose>
        <c:when test="${energy.dumComp == 'electric'}">
          <span>전기: ${energy.totalEnergyUsageQty}kWh / ${energy.totalEnergyChargeAmt}원</span>
        </c:when>
        <c:when test="${energy.dumComp == 'water'}">
          <span>수도: ${energy.totalEnergyUsageQty}㎥ / ${energy.totalEnergyChargeAmt}원</span>
        </c:when>
        <c:when test="${energy.dumComp == 'gas'}">
          <span>가스: ${energy.totalEnergyUsageQty}㎥ / ${energy.totalEnergyChargeAmt}원</span>
        </c:when>
      </c:choose>
    </div>
  </c:forEach>

  <!-- 청구액 + 설명 -->
<div class="info-block">
  <p>총 청구액: ${cbhDTO.chgbillAmount}원</p>
  <p class="desc">
    ${cbhDTO.chgbillDesc}
  </p>
</div>
</div>
</body>
</html>