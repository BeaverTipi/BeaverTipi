<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주 상세정보</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  
  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      font-size: 14px;
      background: #f4f6f9;
      padding: 2rem;
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
    .icon {
      font-size: 16px;
    }

    /* 모달 */
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

<h3>입주 상세정보</h3>
<div class="filter-bar" style="margin-bottom: 1rem;">
  <label for="buildingFilter">건물 선택:</label>
  <select id="buildingFilter">
    <option value="">전체</option>
    <option value="A">A 건물</option>
    <option value="B">B 건물</option>
    <option value="C">C 건물</option>
  </select>
  <button>수정</button>
  <button>삭제</button>
</div>

<table>
  <thead>
    <tr>
      <th><input type="checkbox" id="selectAll"></th>
      <th>NO</th>
      <th>입주민</th>
      <th>ID</th>
      <th>입주일<br>확인</th>
      <th>입주민<br>페이지 접근</th>
      <th>공실 여부</th>
      <th>조작</th>
    </tr>
  </thead>
  <tbody>
    <!-- 입주 중 -->
    <tr>
      <td><input type="checkbox" class="rowCheckbox"></td>
      <td>1</td>
      <td>이주민</td>
      <td>A001</td>
      <td>확인</td>
      <td>허가</td>
      <td><input type="checkbox" disabled checked title="입주 중"></td>
      <td>
        <div class="action-buttons">
          <button>수정</button>
          <button>삭제</button>
        </div>
      </td>
    </tr>

    <!-- 공실 -->
    <tr>
      <td><input type="checkbox" class="rowCheckbox"></td>
      <td>2</td>
      <td>-</td>
      <td>-</td>
      <td>-</td>
      <td>-</td>
      <td><input type="checkbox" disabled title="공실"></td>
      <td>
        <div class="action-buttons">
          <button class="add-btn" data-room="102호">추가</button>
        </div>
      </td>
    </tr>
  </tbody>
</table>

<!-- ID 검색 모달 -->
<div id="idSearchModal">
  <div class="modal-inner">
    <h4>입주자 ID 검색</h4>
    <input type="text" placeholder="ID 또는 이름 검색">
    <div class="modal-actions">
      <button onclick="closeModal()" style="background: #ccc;">닫기</button>
      <button style="background: #3a5dfb; color: white;">선택</button>
    </div>
  </div>
</div>
<!-- ✅ ID 검색 모달 -->
<div id="idSearchModal" style="display:none; position: fixed; top: 0; left: 0;
  width: 100%; height: 100%; background: rgba(0,0,0,0.4); z-index: 1000;">
  <div style="background: #fff; width: 400px; margin: 100px auto; padding: 1.5rem; border-radius: 12px; position: relative;">
    <h3 style="margin-bottom: 1rem;">입주자 ID 검색</h3>
    <input type="text" placeholder="ID 또는 이름 검색" style="width: 100%; padding: 8px; margin-bottom: 1rem;">
    <div style="text-align: right;">
      <button onclick="closeModal()" style="background: #ccc; color: #000;">닫기</button>
      <button style="background: #3a5dfb; color: white;">선택</button>
    </div>
  </div>
</div>

<!-- ✅ 자바스크립트 파일 연결 (항상 맨 마지막에) -->
<script src="/app/js/building/move-in/moveInDashBoard.js"></script>

</body>
</html>
