<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>거래 내역</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 30px;
    }

    h2 {
      margin-bottom: 20px;
    }

    .add-button {
      float: right;
      margin-bottom: 10px;
      background-color: #00aaff;
      color: white;
      padding: 8px 16px;
      border: none;
      border-radius: 5px;
      cursor: pointer;
    }

    table {
      width: 100%;
      border-collapse: collapse;
      margin-top: 10px;
      margin-bottom: 30px;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 10px;
      text-align: center;
    }

    thead {
      background-color: #f2f2f2;
    }

    .delete-btn {
      background-color: #e74c3c;
      color: white;
      border: none;
      padding: 6px 12px;
      border-radius: 4px;
      cursor: pointer;
    }

    .empty-message {
      text-align: center;
      padding: 30px;
      color: #888;
    }
  </style>
</head>
<body>

  <h2>거래 내역</h2>

  <!-- 수납계좌 테이블 -->
  <button class="add-button">수납계좌 추가</button>

  <table>
    <thead>
      <tr>
        <th>금융기관<br>(계좌구분)</th>
        <th>계좌번호</th>
        <th>계좌상태</th>
        <th>건물 연동 현황</th>
        <th>정상계좌 확인일시</th>
        <th>사용변경 일시</th>
        <th>관리</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>〇〇은행</td>
        <td>123-45-678910</td>
        <td>사용</td>
        <td>사용</td>
        <td>####년.##.##</td>
        <td>-</td>
        <td><button class="delete-btn">삭제</button></td>
      </tr>
    </tbody>
  </table>

  <!-- 삭제 내역 -->
  <h3>수납계좌 삭제 내역</h3>
  <table>
    <thead>
      <tr>
        <th>금융기관<br>(계좌구분)</th>
        <th>계좌번호</th>
        <th>계좌상태</th>
        <th>정상계좌 확인일시</th>
        <th>사용변경 일시</th>
        <th>삭제일시</th>
        <th>삭제사유</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td colspan="7" class="empty-message">수납계좌 삭제 내역이 없습니다.</td>
      </tr>
    </tbody>
  </table>

</body>
</html>
