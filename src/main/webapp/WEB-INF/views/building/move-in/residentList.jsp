<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>입주민 설정</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <link rel="stylesheet" href="/app/css/building/move-in/residentList.css">
  <style>

  </style>
</head>
<body>

  <div class="container">
  <!-- 건물 선택 필터 -->
    <div class="filter-bar">
      <label for="buildingFilter">건물 선택:</label>
      <select id="buildingFilter">
        <option value="">전체</option>
        <option value="A">A 건물</option>
        <option value="B">B 건물</option>
        <option value="C">C 건물</option>
      </select>
    </div>
    <!-- 상단 제목 및 버튼 -->
    <div class="header">
      <h3>입주민 설정</h3>
      <div class="button-group">
        <button class="btn edit-button">수정</button>
        <button class="btn delete-button">삭제</button>
      </div>
    </div>

    <!-- 테이블 -->
    <table>
      <thead>
        <tr>
          <th><input type="checkbox" id="selectAll"></th> <!-- 전체 선택 -->
          <th>NO</th>
          <th>입주민</th>
          <th>ID</th>
          <th>입주일 확인</th>
          <th>입주민 페이지 접근</th>
        </tr>
      </thead>
      <tbody>
        <tr>
          <td><input type="checkbox" class="rowCheckbox"></td>
          <td>1</td><td>이주민</td><td>A001</td><td>확인</td><td>허가</td>
        </tr>
        <tr>
          <td><input type="checkbox" class="rowCheckbox"></td>
          <td>2</td><td>이주민</td><td>A002</td><td>확인</td><td>허가</td>
        </tr>
        <tr>
          <td><input type="checkbox" class="rowCheckbox"></td>
          <td>3</td><td>이주민</td><td>A003</td><td>확인</td><td>허가</td>
        </tr>
        <tr>
          <td><input type="checkbox" class="rowCheckbox"></td>
          <td>4</td><td>이주민</td><td>A004</td><td>확인</td><td>허가</td>
        </tr>
        <tr>
          <td><input type="checkbox" class="rowCheckbox"></td>
          <td>5</td><td>이주민</td><td>A005</td><td>확인</td><td>허가</td>
        </tr>
      </tbody>
    </table>

  </div>
  
  </body>
</html>