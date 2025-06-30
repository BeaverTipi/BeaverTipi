<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>사업자등록증 리스트</title>
  <link rel="stylesheet" href="/app/css/building/main.css">
  <link rel="stylesheet" href="/app/css/building/payments/certificateList.css">
  
</head>
<body>

  <h2>사업자등록증 리스트</h2>

  <div class="register-btn-wrapper">
    <a href="/register-business">
      <button class="btn btn-blue">등록</button>
    </a>
  </div>

  <table>
    <thead>
      <tr>
        <th>NO</th>
        <th>사업자명</th>
        <th>사업자등록번호</th>
        <th>대표자명</th>
        <th>업태 / 종목</th>
        <th>사업장 주소</th>
        <th>등록일자</th>
        <th>상태</th>
        <th>관리</th>
      </tr>
    </thead>
    <tbody>
      <tr>
        <td>1</td>
        <td>㈜예제</td>
        <td>123-45-67890</td>
        <td>홍길동</td>
        <td>도소매 / 전자상거래</td>
        <td>서울특별시 중구 세종대로 110</td>
        <td>2024-12-01</td>
        <td class="status-approved">승인</td>
        <td><button class="btn btn-red">삭제</button></td>
      </tr>
      <tr>
        <td>2</td>
        <td>ABC상사</td>
        <td>987-65-43210</td>
        <td>김철수</td>
        <td>서비스 / 마케팅</td>
        <td>부산광역시 해운대구 센텀중로 45</td>
        <td>2025-01-15</td>
        <td class="status-pending">검토중</td>
        <td><button class="btn btn-red">삭제</button></td>
      </tr>
      <!-- 빈 데이터 예시 -->
      <!--
      <tr>
        <td colspan="9" class="empty-message">등록된 사업자등록증이 없습니다.</td>
      </tr>
      -->
    </tbody>
  </table>
<script src="/app/js/building/payments/certificateList.js"></script>
</body>
</html>
    