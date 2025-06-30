<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>지로 발행</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <link rel="stylesheet" href="/app/css/building/payments/giro.css">

</head>
<body>

  <h2 style="text-align:center;">지로 발행</h2>

  <div class="container">
    <!-- 신청 금액 입력 -->
    <div class="form-box">
      <h4>신청 금액</h4>

      <div class="form-item">
        <label style="width: 50px;">월세</label>
        <input type="text" class="value-input" placeholder="입력해주세요">
      </div>

      <div class="form-item">
        <label style="width: 50px;">관리비</label>
        <input type="text" class="value-input" placeholder="입력해주세요">
      </div>

      <!-- 하위 항목 (전기/수도/가스 + 동적 추가용) -->
<!-- 기존 하위 항목 영역 -->
<div id="subItems">
  <div class="form-item sub-item">
    <select class="label-select">
      <option value="전기">전기</option>
      <option value="수도">수도</option>
      <option value="가스">가스</option>
      <option value="난방비">난방비</option>
      <option value="청소비">청소비</option>
      <option value="기타">기타</option>
    </select>
    <input type="text" placeholder="입력해주세요" class="value-input">
  </div>
</div>


      <!-- 추가/삭제 버튼 -->
      <div class="plus-minus">
        <button id="removeItem">-</button>
        <span>하위</span>
        <button id="addItem">+</button>
      </div>
    </div>

    <!-- 세대 체크박스 -->
    <div class="checkbox-box">
<div class="top-checkbox">
  <h4>세대 적용</h4>
  <label><input type="checkbox" id="totalAll"> 전체</label>
</div>


<!-- 건물1 -->
<div class="checkbox-group">
  <strong>
    건물1
    <label style="font-weight: normal; float: right;">
      <input type="checkbox" class="building-all" data-target="building1"> 전체 선택
    </label>
  </strong>
  <label><input type="checkbox" class="building1"> 401호</label><br>
  <label><input type="checkbox" class="building1"> 402호</label><br>
  <label><input type="checkbox" class="building1"> 403호</label><br>
  <label><input type="checkbox" class="building1"> 404호</label>
</div>

<hr>

<!-- 건물2 -->
<div class="checkbox-group">
  <strong>
    건물2
    <label style="font-weight: normal; float: right;">
      <input type="checkbox" class="building-all" data-target="building2"> 전체 선택
    </label>
  </strong>
  <label><input type="checkbox" class="building2"> 301호</label><br>
  <label><input type="checkbox" class="building2"> 302호</label><br>
  <label><input type="checkbox" class="building2"> 303호</label>
</div>
  <!-- 하단 발행 버튼 -->
  <div class="issue-button">
    <button>발행</button>
  </div>

  <!-- JS: 항목 추가/삭제 -->
<script src="/app/js/building/payments/giro.js"></script>

</body>
</html>
