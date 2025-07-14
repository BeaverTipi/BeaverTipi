<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주 관리 대시보드</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background: #f4f6f9;
      padding: 2rem;
    }
    .tab-buttons {
      display: flex;
      gap: 8px;
      margin-bottom: 1rem;
    }
    .tab-buttons button {
      padding: 8px 16px;
      border: 1px solid #ccc;
      border-radius: 4px;
      background: #fff;
      cursor: pointer;
    }
    .tab-buttons button.active {
      background: #3a5dfb;
      color: #fff;
      border-color: #3a5dfb;
    }
    .tab-content {
      display: none;
    }
    .tab-content.active {
      display: block;
    }
  </style>
</head>
<body>

<h2>입주 관리 대시보드</h2>

<!-- 탭 버튼 -->
<div class="tab-buttons">
  <button class="tab-btn active" data-tab="detail">입주 상세정보</button>
  <button class="tab-btn" data-tab="graph">그래프 보기</button>
  <button class="tab-btn" data-tab="chart">차트 보기</button>
</div>

<!-- 탭1: 입주 상세정보 -->
<div id="tab-detail" class="tab-content active">
  <%-- 방금 만든 입주 상세 테이블 코드를 여기에 붙여넣으면 돼! --%>
  <jsp:include page="/WEB-INF/views/building/move-in/moveInDetailContent.jsp" />
</div>

<!-- 탭2: 그래프 보기 -->
<div id="tab-graph" class="tab-content">
  <h3>그래프 보기 (추후 구현)</h3>
  <canvas id="vacancyChart" width="300" height="300"></canvas>
</div>

<!-- 탭3: 차트 보기 -->
<div id="tab-chart" class="tab-content">
  <h3>차트 보기 (추후 구현)</h3>
  <!-- 자유로운 시각화 차트 공간 -->
</div>

<script src="/app/js/building/move-in/moveInDashBoard.js"></script>
<script>
  const tabBtns = document.querySelectorAll(".tab-btn");
  const tabContents = document.querySelectorAll(".tab-content");

  tabBtns.forEach(btn => {
    btn.addEventListener("click", () => {
      tabBtns.forEach(b => b.classList.remove("active"));
      tabContents.forEach(tc => tc.classList.remove("active"));
      btn.classList.add("active");
      document.getElementById("tab-" + btn.dataset.tab).classList.add("active");
    });
  });
</script>

</body>
</html>
