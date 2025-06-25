<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 설정</title>
  <style>
    body {
      font-family: sans-serif;
      padding: 40px;
      background-color: #f9f9f9;
    }

    h2 {
      margin-bottom: 20px;
    }

    select {
      padding: 6px;
      font-size: 14px;
      margin-bottom: 15px;
    }

    .button-group {
      text-align: right;
      margin-bottom: 10px;
    }

    .action-button {
      border: none;
      padding: 10px 20px;
      font-size: 14px;
      border-radius: 4px;
      cursor: pointer;
      margin-left: 10px;
    }

    .delete-button {
      background-color: #ff4d4d;
      color: white;
    }

    .add-button {
      background-color: #4CAF50;
      color: white;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
      background-color: #fff;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 10px;
      text-align: center;
    }

    th {
      background-color: #f2f2f2;
    }

    .checkbox-col {
      width: 50px;
    }
  </style>
</head>
<body>

  <h2>입주민 설정</h2>

  <!-- 건물 선택 -->
  <label for="buildingSelect">건물 선택</label>
  <select id="buildingSelect">
    <option value="A동">A동</option>
    <option value="B동">B동</option>
    <option value="C동">C동</option>
  </select>

  <!-- 추가/삭제 버튼 -->
  <div class="button-group">
    <button class="action-button add-button">추가</button>
    <button class="action-button delete-button">삭제</button>
  </div>

  <!-- 테이블 -->
  <table>
    <thead>
      <tr>
        <th>No</th>
        <th>입주민</th>
        <th>건물명</th>
        <th>입주민 호수</th>
        <th>입주민 연락처</th>
        <th class="checkbox-col">선택</th>
      </tr>
    </thead>
    <tbody>
      <tr><td>1</td><td>유찬영</td><td>A동</td><td>101호</td><td>010-1111-2222</td><td><input type="checkbox"></td></tr>
      <tr><td>2</td><td>이태수</td><td>A동</td><td>102호</td><td>010-2222-3333</td><td><input type="checkbox"></td></tr>
      <tr><td>3</td><td></td><td></td><td>103호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>4</td><td></td><td></td><td>104호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>5</td><td>박은별</td><td>B동</td><td>201호</td><td>010-3333-4444</td><td><input type="checkbox"></td></tr>
      <tr><td>6</td><td></td><td></td><td>202호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>7</td><td></td><td></td><td>203호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>8</td><td></td><td></td><td>204호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>9</td><td>김하린</td><td>C동</td><td>301호</td><td>010-5555-6666</td><td><input type="checkbox"></td></tr>
      <tr><td>10</td><td></td><td></td><td>302호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>11</td><td></td><td></td><td>303호</td><td></td><td><input type="checkbox"></td></tr>
      <tr><td>12</td><td></td><td></td><td>304호</td><td></td><td><input type="checkbox"></td></tr>
    </tbody>
  </table>

</body>
</html>
