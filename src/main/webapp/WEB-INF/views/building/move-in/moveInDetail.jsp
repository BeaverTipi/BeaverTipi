<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주 정보</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <link rel="stylesheet" href="/app/css/building/main.css">
</head>
<body>

  <div class="container">
    
    <!-- 건물 선택 -->
    <div class="building-select">
      <label for="building">건물 선택:</label>
      <select id="building">
        <option value="A">A 건물</option>
        <option value="B">B 건물</option>
        <option value="C">C 건물</option>
      </select>
    </div>

    <!-- 입주 정보 섹션 -->
    <div class="info-section" style="display: flex; justify-content: space-between; align-items: start; margin-top: 20px;">
      
      <!-- 왼쪽: 텍스트 정보 -->
      <div class="summary" style="flex: 1;">
        <h3>입주 정보</h3>
        <p><strong>총 호실</strong></p>
        <p>○○○ 개</p>
      </div>

      <!-- 오른쪽: 공실 비율 차트 -->
      <div class="chart" style="flex: 1; text-align: center;">
        <h3>공실 비율</h3>
        <canvas id="vacancyChart" width="200" height="200"></canvas>
      </div>
    </div>

    <!-- 테이블 -->
    <div class="table-section" style="margin-top: 30px;">
      <table border="1" cellspacing="0" cellpadding="10" style="width: 100%; border-collapse: collapse;">
        <thead>
          <tr>
            <th>NO</th>
            <th>호실</th>
            <th>입주자</th>
            <th>입주 확인</th>
            <th>계약 만기일</th>
          </tr>
        </thead>
        <tbody>
          <tr><td>1</td><td>101호</td><td>A001</td><td>확인</td><td>확인</td></tr>
          <tr><td>2</td><td>102호</td><td>A002</td><td>확인</td><td>확인</td></tr>
          <tr><td>3</td><td>103호</td><td>A003</td><td>미확인</td><td>미확인</td></tr>
          <tr><td>4</td><td>104호</td><td>A004</td><td>확인</td><td>연기</td></tr>
          <tr><td>5</td><td>105호</td><td>A005</td><td>확인</td><td>확인</td></tr>
        </tbody>
      </table>
    </div>
  </div>

  <!-- 차트 스크립트 -->
<script src="/app/js/building/move-in/moveInDetail.js"></script>

</body>
</html>
