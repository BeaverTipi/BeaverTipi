<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
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

    table {
      width: 100%;
      border-collapse: collapse;
      background: white;
      border-radius: 8px;
      overflow: hidden;
      box-shadow: 0 3px 12px rgba(0,0,0,0.03);
      margin-top: 1rem;
    }
    th, td {
      white-space: nowrap;
      text-align: center;
      padding: 0.6rem 0.5rem;
      border-bottom: 1px solid #e5e5e5;
    }
    thead {
      background-color: #f1f3f9;
    }
    .action-buttons {
      display: flex;
      gap: 4px;
      justify-content: center;
    }
    button {
      padding: 4px 8px;
      font-size: 12px;
      cursor: pointer;
      border: 1px solid #ccc;
      border-radius: 4px;
      background: #fff;
    }
    button:hover {
      background: #f0f0f0;
    }

    #idSearchModal {
      display: none;
      position: fixed; top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0,0,0,0.4);
      z-index: 1000;
    }
    #idSearchModal .modal-inner {
      background: #fff;
      width: 400px;
      margin: 100px auto;
      padding: 1.5rem;
      border-radius: 12px;
      position: relative;
    }
    #idSearchModal input {
      width: 100%;
      padding: 8px;
      margin-bottom: 1rem;
    }
    #idSearchModal .modal-actions {
      text-align: right;
    }
    #idSearchModal .modal-actions button {
      margin-left: 6px;
    }
  </style>
</head>
<body>

<h2>입주 관리 대시보드</h2>

<div class="filter-bar" style="margin-bottom: 1rem;">
  <label for="buildingFilter">건물 선택:</label>
 <select id="buildingFilter">
  
  <option value="">전체</option>
</select>
</div>

<!-- 탭 버튼 -->
<div class="tab-buttons">
  <button class="tab-btn active" data-tab="detail">입주 상세정보</button>
  <button class="tab-btn" data-tab="graph">그래프 보기</button>
  <button class="tab-btn" data-tab="chart">차트 보기</button>
</div>

<!-- 탭1: 입주 상세정보 -->
<div id="tab-detail" class="tab-content active">
  <table>
    <thead>
      <tr>
        <th><input type="checkbox" id="selectAll"></th>
        <th>NO</th>
        <th>입주민</th>
        <th>ID</th>
        <th>입주일</th>
        <th>호실정보</th>
        <th>공실 여부</th>
        <th>조작</th>
      </tr>
    </thead>
    <tbody>
      <!-- 데이터 비동기로 채워짐 -->
    </tbody>
  </table>
</div>

<!-- 탭2: 그래프 보기 -->
<div id="tab-graph" class="tab-content">
  <h3>그래프 보기</h3>
  <canvas id="vacancyChart" width="300" height="300"></canvas>
</div>

<!-- 탭3: 차트 보기 -->
<div id="tab-chart" class="tab-content">
  <h3>차트 보기</h3>
</div>

<!-- ID 검색 모달 -->
<div id="idSearchModal">
  <div class="modal-inner">
    <h4>입주자 ID 검색</h4>
    <input type="text" placeholder="ID 또는 이름 검색">
    <div class="modal-actions">
      <button onclick="closeModal()">닫기</button>
      <button style="background: #3a5dfb; color: white;">선택</button>
    </div>
  </div>
</div>
<!-- 입주 수정 모달 -->
<div id="editModal" style="display: none; position: fixed; top: 0; left: 0;
  width: 100%; height: 100%; background: rgba(0,0,0,0.4); z-index: 1001;">
  <div class="modal-inner" style="background: #fff; width: 400px; margin: 100px auto; padding: 1.5rem; border-radius: 12px; position: relative;">
    <h4 style="margin-bottom: 1rem;">입주 정보 수정</h4>

    <label for="editMoveIn" style="display:block; margin-bottom:4px;">입주일</label>
    <input type="date" id="editMoveIn" style="width:100%; padding:8px; margin-bottom:1rem;">

    <label for="editMoveOut" style="display:block; margin-bottom:4px;">퇴거일</label>
    <input type="date" id="editMoveOut" style="width:100%; padding:8px; margin-bottom:1rem;">

    <div class="modal-actions" style="text-align:right;">
      <button type="button" onclick="closeEditModal()">취소</button>
      <button type="button" id="editSaveBtn" style="background:#3a5dfb; color:white;">저장</button>
    </div>
  </div>
</div>

<!-- JS -->
<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/app/js/building/move-in/moveInDetail.js"></script>

</body>
</html>
