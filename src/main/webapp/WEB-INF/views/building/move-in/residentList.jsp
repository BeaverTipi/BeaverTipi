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

  <!-- A 건물 -->
  <div class="building-section">
    <div class="building-title">A 건물</div>
    <div class="building-info">
      <img src="/images/buildingA.jpg" alt="A 건물" class="building-img">

      <div class="info-box">
        <table class="info-table">
          <tr><td><strong>건물 이름</strong></td><td>현대오피스텔</td></tr>
          <tr><td><strong>우편번호 / 상세 주소</strong></td><td>04523 / 서울 중구 세종대로 110</td></tr>
          <tr><td><strong>유형 코드</strong></td><td>OFFC</td></tr>
          <tr><td><strong>층수</strong></td><td>5층</td></tr>
          <tr><td><strong>호실 수</strong></td><td>40호</td></tr>
          <tr><td><strong>연면적</strong></td><td>3,200㎡</td></tr>
          <tr><td><strong>준공일</strong></td><td>2015-03-15</td></tr>
        </table>

        <div class="button-box">
          <button class="btn btn-edit">수정</button>
          <button class="btn btn-delete">삭제</button>
        </div>
      </div>
    </div>
  </div>

  <hr>

  <!-- B 건물 -->
  <div class="building-section">
    <div class="building-title">B 건물</div>
    <div class="building-info">
      <img src="/images/buildingB.jpg" alt="B 건물" class="building-img">

      <div class="info-box">
        <table class="info-table">
          <tr><td><strong>건물 이름</strong></td><td>신세계빌딩</td></tr>
          <tr><td><strong>우편번호 / 상세 주소</strong></td><td>06234 / 서울 강남구 테헤란로 231</td></tr>
          <tr><td><strong>유형 코드</strong></td><td>SHOP</td></tr>
          <tr><td><strong>층수</strong></td><td>10층</td></tr>
          <tr><td><strong>호실 수</strong></td><td>80호</td></tr>
          <tr><td><strong>연면적</strong></td><td>7,800㎡</td></tr>
          <tr><td><strong>준공일</strong></td><td>2020-07-01</td></tr>
        </table>

        <div class="button-box">
          <button class="btn btn-edit">수정</button>
          <button class="btn btn-delete">삭제</button>
        </div>
      </div>
    </div>
  </div>

<script src="/app/js/building/move-in/residentList.js"></script>
</body>
</html>
