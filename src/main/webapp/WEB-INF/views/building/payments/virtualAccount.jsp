<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>계좌/가상계좌 관리</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8fbfe;
      margin: 0;
      padding: 30px;
    }

    .tab-wrap {
      margin: 0 auto 20px auto;
      max-width: 900px;
      background: #fff;
      border-radius: 12px;
      box-shadow: 0 2px 8px rgba(0,0,0,0.08);
      padding: 0 0 30px 0;
    }
    .tabs {
      display: flex;
      border-bottom: 2px solid #e0e0e0;
      margin-bottom: 24px;
    }
    .tab {
      flex: 1;
      padding: 18px 0 12px 0;
      text-align: center;
      font-size: 17px;
      font-weight: 600;
      color: #bbb;
      background: transparent;
      border: none;
      outline: none;
      cursor: pointer;
      transition: color 0.18s;
      border-bottom: 2px solid transparent;
    }
    .tab.active {
      color: #00aaff;
      border-bottom: 2.5px solid #00aaff;
      background: #f8fbfe;
    }
    /* --- 기존 테이블 스타일 유지 (복사해서 스타일 수정) --- */
    h2 { margin-bottom: 20px; font-size: 24px; color: #333;}
    table {
      width: 100%; border-collapse: collapse; background-color: white; margin-top: 10px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }
    th, td { border: 1px solid #ddd; padding: 12px; text-align: center; font-size: 14px;}
    thead { background-color: #f2f2f2;}
    .delete-btn {
      background-color: #e74c3c;
      color: white;
      border: none;
      padding: 8px 16px;
      border-radius: 5px;
      cursor: pointer;
      margin-left: 5px;
    }
    .add-button {
      background-color: #00aaff;
      color: white;
      padding: 8px 16px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
      float: right;
      margin-bottom: 10px;
    }
    .empty-message {
      text-align: center;
      padding: 30px;
      color: #888;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 5px;
    }
    /* 모달 공통 */
    .modal {
      display: none; position: fixed;
      top: 0; left: 0; width: 100vw; height: 100vh;
      background: rgba(0,0,0,0.6);
      justify-content: center; align-items: center; z-index: 9999;
    }
    .modal-container {
      position: absolute; top: 50%; left: 50%; transform: translate(-50%, -50%);
    }
    .modal-content {
      background: white; padding: 30px; border-radius: 10px; width: 400px;
      box-shadow: 0 10px 25px rgba(0,0,0,0.3); position: relative;
    }
    .modal-content h3 {margin-top: 0; margin-bottom: 20px; font-size: 1.2em;}
    .modal-content label {display: block; margin-top: 10px;}
    .modal-content input, .modal-content select {width: 100%; padding: 6px; margin-top: 5px; margin-bottom: 10px;}
    .modal-buttons {margin-top: 20px; text-align: right;}
    .close {position: absolute; top: 12px; right: 15px; font-size: 20px; cursor: pointer;}
  </style>
</head>
<body>
<div class="tab-wrap">
  <!-- 탭 헤더 -->
  <div class="tabs">
    <button class="tab active" id="tab1Btn" onclick="showTab(1)">계좌내역</button>
    <button class="tab" id="tab2Btn" onclick="showTab(2)">가상계좌</button>
  </div>

  <!-- 탭1: 수납계좌 내역 -->
  <div id="tab1Panel">
    <div class="container" style="padding-top: 0;">
      <div>
      <h2>등록 계좌 내역</h2>
      <button class="add-button" onclick="openModal()">수납계좌 추가</button>
      </div>
        
        <c:if test="${fn:length(accountList) == 0}">
        <div class="empty-message">등록된 계좌가 없습니다.</div>
      </c:if>
      <c:if test="${fn:length(accountList) > 0}">
        <table>
          <thead>
            <tr>
              <th>금융기관<br>(계좌구분)</th>
              <th>계좌번호</th>
              <th>예금주</th>
              <th>연동 건물</th>
              <th>계좌 등록일</th>
              <th>관리</th>
            </tr>
          </thead>
          <tbody>
            <c:forEach var="acc" items="${accountList}">
              <tr>
                <td>${acc.accBank}</td>
                <td>${acc.accNum}</td>
                <td>${acc.accMaster}</td>
                <td>${acc.building.bldgNm}</td>
                <td>${acc.accRegDate}</td>
                <td>
                  <form method="post" action="/building/virtualAccount/delete">
                    <input type="hidden" name="accNum" value="${acc.accNum}">
                    <input type="hidden" name="bldgId" value="${acc.bldgId}">
                    <input type="hidden" name="rentalPtyId" value="${acc.rentalPtyId}">
                    <button class="delete-btn" type="submit">삭제</button>
                  </form>
                </td>
              </tr>
            </c:forEach>
          </tbody>
        </table>
      </c:if>
      
    </div>
    <!-- 수납계좌 등록 모달 -->
    <div id="accountModal" class="modal" style="display: none;">
      <div class="modal-backdrop"></div>
      <div class="modal-container">
        <div class="modal-content">
          <span class="close" onclick="closeModal()">&times;</span>
          <h3>수납계좌 등록</h3>
          <form id="accountForm" method="post" action="${pageContext.request.contextPath}/building/account/add">
            <label>계좌번호</label>
            <input type="text" name="accNum" required maxlength="20" />
            <label>금융기관명</label>
            <input type="text" name="accBank" required maxlength="30" />
            <label>예금주</label>
            <input type="text" name="accMaster" required maxlength="10" />
            <label>연동 건물</label>
            <select name="bldgId" id="buildingSelect" required>
              <option value="" disabled selected>건물 선택</option>
            </select>
            <input type="hidden" name="rentalPtyId" value="${rentalPtyId}" />
            <div class="modal-buttons">
              <button type="submit" class="add-button">등록</button>
              <button type="button" class="delete-btn" onclick="closeModal()">취소</button>
            </div>
          </form>
        </div>
      </div>
    </div>
    <script src="/app/js/building/account/accountList.js"></script>
  </div>

  <!-- 탭2: 가상계좌 관리 -->
  <div id="tab2Panel" style="display:none;">
    <div class="container" style="padding-top: 0;">
      <h2>가상계좌 관리</h2>
      <button class="add-button" onclick="openVaModal()">가상계좌 발급</button>
      <div id="emptyMessage" class="empty-message" style="display: none;">
        등록된 가상계좌가 없습니다.
      </div>
      <table id="accountTable" style="display: none;">
        <thead>
          <tr>
            <th>은행명</th>
            <th>계좌번호</th>
            <th>예금주</th>
            <th>결제금액</th>
            <th>만료일</th>
            <th>요청방식</th>
            <th>정산상태</th>
            <th>관리</th>
          </tr>
        </thead>
        <tbody id="vaTableBody"></tbody>
      </table>
      <!-- 가상계좌 등록 모달 -->
      <div class="modal" id="vaModal" style="display:none;">
        <div class="modal-content" style="position: relative; padding: 30px; width: 400px;">
          <span class="close" onclick="closeVaModal()" style="position: absolute; top: 10px; right: 15px; font-size: 20px; cursor: pointer;">&times;</span>
          <h3 style="margin-top: 0;">가상계좌 등록</h3>
          <form id="vaForm" method="post" action="/virtualAccount/register">
            <label>예금주</label>
            <input type="text" name="customerName" required maxlength="20" style="width:100%; padding: 8px; margin-bottom: 10px;" />
            <label>금액</label>
            <input type="number" name="virtualAccountAmount" required min="1000" style="width:100%; padding: 8px; margin-bottom: 10px;" />
            <label>만료일</label>
            <input type="date" name="dueDate" required style="width:100%; padding: 8px; margin-bottom: 10px;" />
            <label>은행</label>
            <select name="bankCode" required style="width:100%; padding: 8px; margin-bottom: 10px;">
              <option value="" disabled selected>은행 선택</option>
              <option value="KOOKMIN">국민은행</option>
              <option value="SHINHAN">신한은행</option>
              <option value="WOORI">우리은행</option>
              <option value="NONGHYEOP">NH농협</option>
            </select>
            <label>결제항목</label>
            <select name="accountType" required style="width:100%; padding: 8px; margin-bottom: 10px;">
              <option value="" disabled selected>-- 선택 --</option>
              <option value="SUBSCRIPTION">정기구독</option>
              <option value="MONTHLY">월세/관리비</option>
              <option value="ONETIME">단건결제</option>
            </select>
            <input type="hidden" name="mbrCd" value="${loginMember.mbrCd}" />
            <div class="modal-buttons">
              <button type="button" class="delete-btn" onclick="closeVaModal()">취소</button>
              <button type="submit" class="add-button" >등록</button>
            </div>
          </form>
        </div>
      </div>
      <!-- 상세보기 모달 -->
      <div id="detailModal">
        <div id="detailContent"></div>
      </div>
    </div>
    <script src="/app/js/building/virtualAccount/virtualAccount.js"></script>
  </div>
</div>

<script>
  // 탭 전환
  function showTab(tabNum) {
    document.getElementById("tab1Panel").style.display = tabNum === 1 ? "" : "none";
    document.getElementById("tab2Panel").style.display = tabNum === 2 ? "" : "none";
    document.getElementById("tab1Btn").classList.toggle("active", tabNum === 1);
    document.getElementById("tab2Btn").classList.toggle("active", tabNum === 2);
  }

  // 수납계좌 모달 오픈/닫기
  function openModal() {
    document.getElementById("accountModal").style.display = "flex";
    if (typeof loadBuildingList === "function") {
      loadBuildingList();
    }
  }
  function closeModal() {
    document.getElementById("accountModal").style.display = "none";
    document.getElementById("accountForm").reset();
  }

  // 가상계좌 모달 오픈/닫기
  function openVaModal() {
    document.getElementById("vaModal").style.display = "flex";
  }
  function closeVaModal() {
    document.getElementById("vaModal").style.display = "none";
    document.getElementById("vaForm").reset();
  }

  // 시작시 첫탭 노출
  window.onload = function() {
    showTab(1);
  }
</script>

</body>
</html>
