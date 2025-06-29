<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>주택 소개</title>
  <style>
    body {
      font-family: 'Arial', sans-serif;
      background-color: #f4f4f4;
      margin: 0;
      padding: 20px;
    }

    .container {
      max-width: 800px;
      margin: auto;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
      overflow: hidden;
    }

    .main-image img {
      width: 100%;
      height: auto;
      display: block;
    }

    .description-board {
      padding: 20px;
      border-top: 1px solid #ccc;
    }

    .description-board h2 {
      margin-top: 0;
      color: #333;
    }

    .description-board p {
      line-height: 1.6;
      color: #555;
    }
  </style>
</head>
<body>
  <div class="container">
    <div class="main-image">
      <img src="" alt="대표 주택 이미지">
    </div>

    <div class="description-board">
      <h2>아늑한 단독주택 소개</h2>
      <p>
        이 집은 햇살이 잘 드는 거실과 넓은 마당이 매력적인 단독주택입니다.<br>
        3개의 침실과 2개의 욕실, 주방, 주차 공간이 마련되어 있어 가족 단위에 딱 맞는 공간이에요.<br>
        조용한 주택가에 위치해 있으며, 근처에 공원과 편의시설도 가까워 생활이 편리합니다.
      </p>
      <table class="board">
  <thead>
    <tr>
      <th>제목</th>
      <th>작성자</th>
      <th>날짜</th>
    </tr>
  </thead>
  <tbody>
    <tr>
      <td>조용하고 아늑한 신축 단독주택</td>
      <td>관리자</td>
      <td>2025-06-29</td>
    </tr>
  </tbody>
</table>
    </div>
  </div>
</body>
</html>