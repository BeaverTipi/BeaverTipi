<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주 관리 대시보드</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
  
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background: #f6faff;
      padding: 32px;
    }
    .tab-wrap {
      margin: 0 auto 20px auto;
      max-width: 950px;
      background: #fff;
      border-radius: 16px;
      box-shadow: 0 2px 12px rgba(58,93,251,0.08);
      padding-bottom: 32px;
    }
    h2 {
      padding: 32px 40px 4px 40px;
      font-size: 1.9rem;
      letter-spacing: -0.5px;
      font-weight: 700;
    }
    .filter-bar {
      margin-bottom: 1.5rem;
      padding: 0 40px;
      display: flex;
      align-items: center;
      gap: 8px;
    }
    .filter-bar label { font-weight: 500; }
    #buildingFilter {
      border: 1px solid #d1d8e8;
      border-radius: 8px;
      height: 36px;
      min-width: 140px;
      font-size: 15px;
      padding: 0 12px;
      background: #f8fbfe;
      margin-left: 6px;
    }

    .tab-buttons {
      display: flex; gap: 12px;
      margin-bottom: 1.3rem; padding: 0 40px;
    }
    .tab-btn {
      padding: 10px 24px; border: none;
      border-radius: 8px; font-weight: 600; background: #e5eaf9; color: #344565; font-size: 1rem;
      transition: background 0.2s, color 0.2s;
    }
    .tab-btn.active {
      background: #3a5dfb;
      color: #fff;
      box-shadow: 0 2px 8px rgba(58,93,251,0.10);
    }
    .tab-content { display: none; padding: 0 40px; }
    .tab-content.active { display: block; }
    table {
      width: 100%; border-collapse: separate;
      border-spacing: 0;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 3px 12px rgba(58,93,251,0.07);
      margin-top: 1rem;
      overflow: hidden;
    }
    th, td {
      white-space: nowrap;
      text-align: center;
      padding: 0.7rem 0.5rem;
      border-bottom: 1px solid #e5e5e5;
      font-size: 15px;
    }
    th { background-color: #f3f6fa; font-weight: 600; }
    tr:last-child td { border-bottom: none; }
    button {
      padding: 6px 14px; font-size: 14px;
      cursor: pointer; border: none;
      border-radius: 8px; background: #ecf0ff;
      color: #334785; font-weight: 600;
      transition: background .15s, color .15s;
      outline: none;
    }
    button:hover, .tab-btn:hover {
      background: #3a5dfb; color: #fff;
    }
    /* 모달 스타일 공통 */
    .modal-overlay { display: none; position: fixed; left:0; top:0; width:100vw; height:100vh; z-index: 9999; }
    .modal-overlay.active { display: block; }
    .modal-inner {
      background: #fff;
      width: 360px; max-width: 95vw;
      margin: 100px auto;
      padding: 2.1rem 1.2rem 1.1rem 1.2rem;
      border-radius: 14px;
      position: relative; box-shadow: 0 4px 20px rgba(44,78,255,0.09);
    }
    .modal-bg {position: absolute; left:0; top:0; width:100vw; height:100vh; background:rgba(0,0,0,0.22);}
    .modal-content { position: relative; z-index: 1; }
    .modal-inner label { display:block; font-size:15px; font-weight:500; margin:10px 0 2px 1px;}
    .modal-inner input, .modal-inner select {
      width:100%; border:1px solid #d8e0f0; border-radius:8px;
      padding:8px 12px; margin-bottom:8px; font-size:15px; background:#f8fbfe;
    }
    .modal-inner button { margin-top:8px; }
    #manualMemberResult, #idSearchResult {font-size:14px; margin:7px 0; min-height:30px;}
    @media (max-width: 600px){
      .tab-wrap, .tab-content { padding: 0 6px !important;}
      h2, .tab-buttons, .filter-bar { padding:0 8px !important; }
      .modal-inner { padding: 1.2rem .5rem .8rem .5rem; }
    }
  </style>
</head>
<body>
  <div class="tab-wrap">
    <h2>입주 관리 대시보드</h2>
    <input type="hidden" id="hiddenRentalPtyId">

    <div class="filter-bar">
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
        <h4 style="font-weight:700;font-size:1.2rem;margin-bottom:10px;">입주자 ID 검색</h4>
        <input type="hidden" id="hiddenRentalPtyId">
        <input type="text" id="idSearchInput" placeholder="ID, 이름, 전화번호" />
        <button type="button" id="idSearchBtn">검색</button>
        <div id="idSearchResult"></div>
        <input type="hidden" id="selectedVacantMbrCd">
        <div style="text-align:right;margin-top:10px;">
          <button type="button" onclick="closeIdSearchModal()">취소</button>
          <button type="button" id="confirmSearchBtn" style="background:#3a5dfb;color:#fff;">등록</button>
        </div>
      </div>
    </div>

    <!-- (2) 입주민 직접등록 모달 -->
    <div id="addResidentModal" class="modal-overlay">
      <div class="modal-bg" onclick="closeAddResidentModal()"></div>
      <div class="modal-inner modal-content">
        <h4 style="font-weight:700;font-size:1.2rem;margin-bottom:10px;">입주민 직접 등록</h4>
        <input type="hidden" id="hiddenRentalPtyId">
        <label>건물</label>
        <select id="addModalBldgSel"><option value="">건물 선택</option></select>
        <label>호실</label>
        <select id="addModalUnitSel"><option value="">호실 선택</option></select>
        <label>회원 검색</label>
        <input type="text" id="manualMemberKeyword" placeholder="ID, 이름" />
        <button type="button" id="manualSearchBtn">검색</button>
        <div id="manualMemberResult"></div>
        <input type="hidden" id="manualMbrCd">
        <label>입주일</label>
        <input type="date" id="manualMoveInDt" />
        <div style="text-align:right;margin-top:10px;">
          <button type="button" onclick="closeAddResidentModal()">취소</button>
          <button type="button" id="manualConfirmBtn" style="background:#3a5dfb;color:#fff;">등록</button>
        </div>
      </div>
    </div>
  </div>
  <script src="https://cdn.jsdelivr.net/npm/chart.js"></script>
  <script src="/app/js/building/move-in/moveInDetail.js"></script>
</body>
</html>
