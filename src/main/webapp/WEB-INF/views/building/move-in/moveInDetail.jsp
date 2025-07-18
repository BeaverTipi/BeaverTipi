<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주 관리 대시보드</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <style>
    body { font-family: 'Noto Sans KR', sans-serif; background: #f4f6f9; padding: 2rem;}
    .tab-buttons { display: flex; gap: 8px; margin-bottom: 1rem; }
    .tab-btn { padding: 8px 16px; border: 1px solid #ccc; border-radius: 4px; background: #fff; cursor: pointer;}
    .tab-btn.active { background: #3a5dfb; color: #fff; border-color: #3a5dfb;}
    .tab-content { display: none; }
    .tab-content.active { display: block; }
    table { width: 100%; border-collapse: collapse; background: white; border-radius: 8px; overflow: hidden; box-shadow: 0 3px 12px rgba(0,0,0,0.03); margin-top: 1rem;}
    th, td { white-space: nowrap; text-align: center; padding: 0.6rem 0.5rem; border-bottom: 1px solid #e5e5e5;}
    thead { background-color: #f1f3f9;}
    button { padding: 4px 8px; font-size: 12px; cursor: pointer; border: 1px solid #ccc; border-radius: 4px; background: #fff;}
    button:hover { background: #f0f0f0;}
    /* 모달 스타일 공통 */
    .modal-overlay { display: none; position: fixed; left:0; top:0; width:100vw; height:100vh; z-index: 1000;}
    .modal-inner {background: #fff;
  width: 320px;         /* ← 여기서 400px → 320px 정도로 */
  margin: 100px auto;
  padding: 1.3rem;
  border-radius: 12px;
  position: relative; }
    .modal-bg {position: absolute; left:0; top:0; width:100vw; height:100vh; background:rgba(0,0,0,0.35);}
    .modal-content { position: relative; z-index: 2;}
    
  </style>
</head>
<body>

<h2>입주 관리 대시보드</h2>
<input type="hidden" id="hiddenRentalPtyId">

<div class="filter-bar" style="margin-bottom: 1rem;">
  <label for="buildingFilter">건물 선택:</label>
  <select id="buildingFilter"><option value="">전체</option></select>
</div>

<!-- 탭 버튼 -->
<div class="tab-buttons">
  <button class="tab-btn active" data-tab="detail">입주 상세정보</button>
  <button class="tab-btn" data-tab="graph">그래프 보기</button>
  <button class="tab-btn" data-tab="chart">차트 보기</button>
</div>

<!-- 탭1: 입주 상세정보 -->
<div id="tab-detail" class="tab-content active">
  <div style="display:flex; justify-content:flex-end; margin-bottom:10px;">
    <button id="openAddResidentBtn" style="background:#3a5dfb;color:white;">입주민 추가</button>
  </div>
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
    <tbody><!-- 비동기 채움 --></tbody>
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

<!-- (1) 공실 “추가” (ID검색) 모달 -->
<div id="idSearchModal" class="modal-overlay">
  <div class="modal-bg" onclick="closeIdSearchModal()"></div>
  <div class="modal-inner modal-content">
    <h4>입주자 ID 검색</h4>
    <input type="hidden" id="hiddenRentalPtyId">
    <input type="text" id="idSearchInput" placeholder="ID, 이름, 전화번호" />
    <button type="button" id="idSearchBtn">검색</button>
    <div id="idSearchResult" style="margin:8px 0;"></div>
    <input type="hidden" id="selectedVacantMbrCd">
    <div style="text-align:right;margin-top:10px;">
      <button type="button" onclick="closeIdSearchModal()">취소</button>
      <button type="button" id="confirmSearchBtn">등록</button>
    </div>
  </div>
</div>

<!-- (2) 입주민 직접등록 모달 -->
<div id="addResidentModal" class="modal-overlay">
  <div class="modal-bg" onclick="closeAddResidentModal()"></div>
  <div class="modal-inner modal-content">
    <h4>입주민 직접 등록</h4>
    <input type="hidden" id="hiddenRentalPtyId">
    <label>건물</label>
    <select id="addModalBldgSel"><option value="">건물 선택</option></select>
    <label>호실</label>
    <select id="addModalUnitSel"><option value="">호실 선택</option></select>
    <label>회원 검색</label>
    <input type="text" id="manualMemberKeyword" placeholder="ID, 이름" />
    <button type="button" id="manualSearchBtn">검색</button>
    <div id="manualMemberResult" style="margin:8px 0;"></div>
    <input type="hidden" id="manualMbrCd">
    <label>입주일</label>
    <input type="date" id="manualMoveInDt" />
    <div style="text-align:right;margin-top:10px;">
      <button type="button" onclick="closeAddResidentModal()">취소</button>
      <button type="button" id="manualConfirmBtn">등록</button>
    </div>
  </div>
</div>

<script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
<script src="/app/js/building/move-in/moveInDetail.js"></script>
</body>
</html>
