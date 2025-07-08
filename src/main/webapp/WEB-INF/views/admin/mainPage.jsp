<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>기간별 통계 조회</title>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <link rel="stylesheet" href="/app/css/building/main.css">
  
  
  </style>
</head>
<body>
  <div class="container">

    <!-- 날짜 선택 -->
    <div class="box">
      <h2 class="section-title">아직 수정 안함(임시로 만듬)</h2>
      <div class="date-range">
        <label for="start-date">시작일:</label>
        <input type="date" id="start-date">
        <label for="end-date">종료일:</label>
        <input type="date" id="end-date">
        <button onclick="alert('조회 기능은 추후 구현됩니다.')">조회</button>
      </div>
    </div>

    <!-- 공시 비율 -->
    <div class="box">
      <h3 class="section-title">공시 비율</h3>
      <div class="charts-row">
        <div class="chart-block">
          <div class="chart-title">A 건물</div>
          <canvas id="gongsiChartA"></canvas>
        </div>
        <div class="chart-block">
          <div class="chart-title">B 건물</div>
          <canvas id="gongsiChartB"></canvas>
        </div>
      </div>
    </div>

    <!-- 납부 금액 -->
    <div class="box">
      <h3 class="section-title">납부 금액</h3>
      <div class="charts-row">
        <div class="chart-block">
          <div class="chart-title">A 건물</div>
          <canvas id="paymentChartA"></canvas>
        </div>
        <div class="chart-block">
          <div class="chart-title">B 건물</div>
          <canvas id="paymentChartB"></canvas>
        </div>
      </div>
    </div>

  </div>

<script src="/app/js/building/main.js"></script>
</body>
</html>


