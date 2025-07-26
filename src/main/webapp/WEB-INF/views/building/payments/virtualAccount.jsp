<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>가상계좌 관리</title>
  <style>
    body {
      font-family: Arial, sans-serif;
      background-color: #f8fbfe;
      padding: 30px;
    }

    h2 {
      margin-bottom: 20px;
      font-size: 24px;
      color: #333;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      background-color: white;
      margin-top: 10px;
      box-shadow: 0 2px 4px rgba(0,0,0,0.1);
    }

    th, td {
      border: 1px solid #ddd;
      padding: 12px;
      text-align: center;
      font-size: 14px;
    }

    thead {
      background-color: #f2f2f2;
    }

    .delete-btn {
      float: right;
      margin-top: 15px;
      background-color: #e74c3c;
      color: white;
      padding: 8px 16px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }

    .add-button {
      float: right;
      margin-top: 15px;
      background-color: #00aaff;
      color: white;
      padding: 8px 16px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }

    .empty-message {
      text-align: center;
      padding: 30px;
      color: #888;
      background-color: #fff;
      border: 1px solid #ddd;
      border-radius: 5px;
    }

    .modal {
      display: none;
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0, 0, 0, 0.6);
      justify-content: center;
      align-items: center;
    }

    .modal-content {
      background: white;
      padding: 20px;
      border-radius: 8px;
      width: 400px;
    }

    .modal-content input, .modal-content select {
      width: 100%;
      margin-bottom: 12px;
      padding: 8px;
      font-size: 14px;
    }

    .modal-buttons {
      display: flex;
      justify-content: space-between;
    }

    .close {
      float: right;
      font-size: 20px;
      cursor: pointer;
    }

    #detailModal {
      display: none;
      position: fixed;
      top: 0; left: 0;
      width: 100%; height: 100%;
      background: rgba(0, 0, 0, 0.6);
      justify-content: center;
      align-items: center;
    }

    #detailContent {
      background: white;
      padding: 20px;
      border-radius: 8px;
      width: 400px;
    }
  </style>
</head>
<body>

<h2>가상계좌 관리</h2>

<button class="add-button" onclick="openModal()">가상계좌 발급</button>

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

<div class="modal" id="accountModal" style="display:none;">
  <div class="modal-content" style="position: relative; padding: 30px; width: 400px;">
    <span class="close" onclick="closeModal()" style="position: absolute; top: 10px; right: 15px; font-size: 20px; cursor: pointer;">&times;</span>
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

      <div style="display: flex; justify-content: space-between; gap: 10px; margin-top: 20px;">
        <button type="button" class="delete-btn" onclick="closeModal()" style="flex:1;">취소</button>
        <button type="submit" class="add-button" style="flex:1; padding: 10px;">등록</button>
      </div>
    </form>
  </div>
</div>

<!-- 상세보기할거임 -->
<div id="detailModal">
  <div id="detailContent"></div>
</div>

<script src="/app/js/building/virtualAccount/virtualAccount.js"></script>

</body>
</html>
