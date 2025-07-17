<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>관리비 청구 등록</title>
  <style>
body {
  font-family: 'Pretendard', sans-serif;
  font-size: 14px;
  background-color: #f2f4f6;
  margin: 0;
  padding: 0;
  color: #333;
}

.charge-wrap {
  max-width: 880px;
  margin: 0 auto;
  background-color: #fff;
  padding: 2rem;
  border-radius: 10px;
  box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
}

h2 {
  font-size: 1.5rem;
  margin-bottom: 1.5rem;
}

.charge-section {
  margin-bottom: 1.2rem;
}

.charge-section h3 {
  font-size: 1.2rem;
  font-weight: 600;
  margin-bottom: 1.0rem;
  border-left: 4px solid #007bff;
  padding-left: 0.5rem;
}

/* 셀렉트 + 버튼 한 줄 */
.control-row {
  display: flex;
  align-items: center;
  gap: 2.0rem;
  margin: 0.4rem 0;
}

.control-row label {
  width: 80px;
  font-weight: 500;
  margin-right: 0.4rem;
}

.control-row select {
  width: 200px;
  height: 32px;
  padding: 0 0.6rem;
  font-size: 0.9rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #fff;
}

.control-row button {
  height: 32px;
  padding: 0 0.8rem;
  font-size: 0.85rem;
  border-radius: 6px;
  border: none;
  background-color: #00aaff;
  color: white;
  cursor: pointer;
}

/* 공용 관리비 항목 */
.charge-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 1rem 2rem;
  margin-bottom: 0.8rem;
}

.charge-item {
  display: flex;
  align-items: center;
}

.charge-item label {
  width: 140px;
  font-weight: 600;
  font-size: 1rem;
  margin-right: 0.6rem;
}

.charge-item input {
  flex: 1;
  height: 32px;
  padding: 0 0.6rem;
  font-size: 0.9rem;
  border-radius: 6px;
  border: 1px solid #ccc;
  background-color: #fff;
}

/*  단위 표기 ("원") */
.charge-item span {
  margin-left: 0.4rem;
  font-size: 1rem;
  color: #555;
}

/* 입주민 카드 */
.resident-block {
  padding: 1rem;
  margin-bottom: 1rem;
  background-color: #f7f9fc;
  border-radius: 8px;
  border: 1px solid #e0e4ea;
}

.resident-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 0.6rem;
}

.resident-header h4 {
  font-size: 1rem;
  font-weight: 600;
}

.resident-header button {
  margin-left: 0.3rem;
  padding: 0.3rem 0.6rem;
  font-size: 0.85rem;
  border: none;
  border-radius: 6px;
  cursor: pointer;
}

.btn-fetch {
  background-color: #28a745;
  color: white;
}
.btn-delete {
  background-color: #dc3545;
  color: white;
}

/* 하단 버튼 */
.charge-buttons {
  display: flex;
  justify-content: center;
  gap: 0.8rem;
  margin-top: 1.6rem;
  flex-wrap: wrap;
}

.charge-buttons button {
  padding: 0.6rem 1.2rem;
  font-size: 0.9rem;
  border-radius: 6px;
  border: none;
  cursor: pointer;
  min-width: 100px;
  text-align: center;
}

.btn-submit {
  background-color: #00aaff;
  color: white;
}
.btn-reset {
  background-color: #6c757d;
  color: white;
}
  </style>

	<script>
	function openResidentPopup() {
	  alert("입주민 팝업 호출");
	}
	
	function addResidentBlock(id, bldgNm, residentNm) {
	  const html = `
	    <div class="resident-block" id="resident_${id}">
	      <div class="resident-header">
	        <h4>${bldgNm} / ${residentNm}</h4>
	        <div>
	          <button class="btn-fetch" onclick="loadUsage('${id}')">사용량 불러오기</button>
	          <button class="btn-delete" onclick="removeResident('${id}')">삭제</button>
	        </div>
	      </div>
	      <div class="charge-row">
	        <div class="charge-item"><label>가스 사용량</label><input name="gasUsage_${id}"></div>
	        <div class="charge-item"><label>가스 요금</label><input name="gasFee_${id}"></div>
	      </div>
	      <div class="charge-row">
	        <div class="charge-item"><label>수도 사용량</label><input name="waterUsage_${id}"></div>
	        <div class="charge-item"><label>수도 요금</label><input name="waterFee_${id}"></div>
	      </div>
	      <div class="charge-row">
	        <div class="charge-item"><label>전기 사용량</label><input name="electricUsage_${id}"></div>
	        <div class="charge-item"><label>전기 요금</label><input name="electricFee_${id}"></div>
	      </div>
	    </div>
	  `;
	  document.getElementById("residentContainer").insertAdjacentHTML("beforeend", html);
	}
	
	function removeResident(id) {
	  document.getElementById(`resident_${id}`)?.remove();
	}
	
	function loadUsage(id) {
	  alert(`${id} 사용량 불러오기`);
	}
	
	function loadCommonCharges() {
	  alert("공용 관리비 불러오기");
	}
	
	function navigateToCommonInput() {
	  alert("공용 관리비 작성 화면 이동");
	}
	</script>
</head>
<body>
  <div class="charge-wrap">
    <h2>관리비 청구 등록</h2>

    <form action="/charge/register" method="post">

      <!-- 건물 선택 + 기능 버튼 -->
      <div class="charge-section">
        <div class="control-row">
          <label>건물 선택</label>
          <select name="bldgId">
            <option value="">건물 선택</option>
            <c:forEach var="b" items="${bldgList}">
              <option value="${b.id}">${b.name}</option>
            </c:forEach>
          </select>

          <button type="button" onclick="openResidentPopup()">입주민 선택</button>
          <button type="button" onclick="navigateToCommonInput()">공용 관리비 작성</button>
          <button type="button" onclick="loadCommonCharges()">이번달 관리비 불러오기</button>
        </div>
      </div>

      <!-- 공용 관리비 -->
	<div class="charge-section">
	  <h3>공용 관리비 항목</h3>
	
	  <div class="charge-row">
	    <div class="charge-item"><label>청소비</label><input name="cleanFee"><span>원</span></div>
	    <div class="charge-item"><label>승강기 유지비</label><input name="elevatorFee"><span>원</span></div>
	  </div>
	  <div class="charge-row">
	    <div class="charge-item"><label>공용 전기료</label><input name="publicElectricFee"><span>원</span></div>
	    <div class="charge-item"><label>공용 수도료</label><input name="publicWaterFee"><span>원</span></div>
	  </div>
	  <div class="charge-row">
	    <div class="charge-item"><label>일반 운영비</label><input name="operationFee"><span>원</span></div>
	    <div class="charge-item"><label>경비 인건비</label><input name="guardFee"><span>원</span></div>
	  </div>
	  <div class="charge-row">
	    <div class="charge-item"><label>방역 소독비</label><input name="disinfectionFee"><span>원</span></div>
	    <div class="charge-item"><label>소모품비</label><input name="supplyFee"><span>원</span></div>
	  </div>
	  <div class="charge-row">
	    <div class="charge-item"><label>소방 설비 유지비</label><input name="fireSafetyFee"><span>원</span></div>
	    <div class="charge-item"><label>보안 시스템 유지비</label><input name="securityFee"><span>원</span></div>
	  </div>
	</div>

      <!-- 입주민 입력 -->
      <div class="charge-section" id="residentContainer"></div>

      <!-- 버튼 -->
      <div class="charge-buttons">
        <button type="submit" class="btn-submit">청구</button>
        <button type="reset" class="btn-reset">초기화</button
      </div>

    </form>
  </div>
</body>
</html>