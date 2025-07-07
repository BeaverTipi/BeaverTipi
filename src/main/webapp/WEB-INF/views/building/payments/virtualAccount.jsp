<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>가상계좌</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <style>
    body {
      font-family: Arial, sans-serif;
      padding: 30px;
    }

    h2 {
      margin-bottom: 10px;
    }

    .notice {
      background-color: #f9f9f9;
      border-left: 4px solid #ccc;
      padding: 15px;
      font-size: 14px;
      margin-bottom: 20px;
      line-height: 1.6;
    }

    .btn-group {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-bottom: 15px;
    }

    .btn {
      padding: 8px 16px;
      font-size: 14px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }

    .btn-blue {
      background-color: #00aaff;
      color: white;
    }

    .btn-outline {
      border: 1px solid #ccc;
      background-color: white;
      color: #333;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      border: 1px solid #ddd;
      padding: 10px;
      text-align: center;
    }

    thead {
      background-color: #f2f2f2;
    }

    .empty-message {
      text-align: center;
      color: #888;
      padding: 30px 0;
    }

    .count-text {
      margin-bottom: 10px;
      font-size: 14px;
    }
  </style>
</head>
<body>

  <h2>가상계좌</h2>

  <div class="notice">
    <p>① 등록 절차 : 출금계좌 등록 → 가상계좌 신청 → 결제정보 심사 → 가상계좌 사용 승인 → 건물 연동</p>
    <p>② 동일한 가상계좌는 결제기관(웰컴페이먼츠)에서 위탁발급됩니다.</p>
    <p>③ 승인 후 가상계좌 관리자페이지(<a href="https://with.welcomefn.com/with" target="_blank">https://with.welcomefn.com/with</a>)에 로그인 가능합니다. (사전 비밀번호는 ID와 동일)</p>
    <p>④ 가상계좌 삭제 시 유의 : 가상계좌 문서체크 실패, 테스트 이외로 마감으로 인한 자동해제, 건물 연동 해제, 임의마감 등으로 종료</p>
  </div>

  <div class="count-text">출금계좌 등록: <strong>0</strong>개 출금계좌 승인 완료</div>

  <div class="btn-group">
    <button class="btn btn-blue">출금계좌 추가</button>
    <button class="btn btn-outline">가상계좌 삭제 자세히 보기</button>
  </div>

  <table>
    <thead>
      <tr>
        <th>금융사명<br>(계좌구분)</th>
        <th>출금계좌번호</th>
        <th>예금주명</th>
        <th>사업자번호</th>
        <th>승인여부</th>
        <th>관리자 ID</th>
        <th>가상계좌 정보발송</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td colspan="7" class="empty-message">가상계좌 목록이 없습니다.</td>
      </tr>
    </tbody>
  </table>

</body>
</html>
