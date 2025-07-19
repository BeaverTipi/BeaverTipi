<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
  <title>관리비 청구 등록</title>
<link rel="stylesheet" href="/app/css/building/chargeBill/giro.css">
<script src="/app/js/building/chargeBill/giro.js"></script>
</head>
<body>
  <div class="charge-wrap">
    <h2>관리비 청구 등록</h2>

<form action="/charge/register" method="post">

  <!-- 셀렉트 + 버튼 4종 한 줄 배치 -->
<!-- 🔹 1열: 건물 + 세대 버튼 + 사용량 불러오기 버튼 -->
<div class="charge-section">
  <div class="control-row">
    <label>건물</label>
    <select name="bldgId">
      <option value="">선택</option>
      <c:forEach var="b" items="${bldgList}">
        <option value="${b.id}">${b.name}</option>
      </c:forEach>
    </select>

    <button type="button" onclick="openUnitPopup()">세대</button>
    <button type="button" onclick="loadAllUsage()">사용량 불러오기</button>
  </div>
</div>

<!-- 🔹 2열: 계좌 + 납기일 -->
<div class="charge-section">
  <div class="control-row">
    <label>계좌 선택</label>
    <select name="depositAccount">
      <option value="">선택</option>
      <c:forEach var="acc" items="${accountList}">
        <option value="${acc.accNum}">${acc.accBank} ${acc.accNum}</option>
      </c:forEach>
    </select>

    <label>납기일</label>
    <input type="date" name="dueDate">
  </div>
</div>
  <!-- 공용 관리비 항목 -->
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

  <!-- 입주민 청구 정보 영역 -->
  <div class="charge-section" id="residentContainer"></div>

  <!-- 하단 버튼 -->
  <div class="charge-buttons">
    <button type="submit" class="btn-submit">청구</button>
    <button type="reset" class="btn-reset">초기화</button>
  </div>

</form>
  </div>
</body>
</html>