<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
 <title>건물 등록</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedAdd.css">
</head>
<body>

<h2>신규 건물 등록</h2>

  <!-- 내 매물 불러오기 -->
  <div class="form-group">
    <label>
      <input type="checkbox" id="toggleProperty"> 내 매물 정보 불러오기
    </label>
    <select id="propertySelect">
      <option value="">내 매물을 선택하세요</option>
      <option value="a101">[A101] 서울시 강남구 테헤란로 123, 10층 오피스텔</option>
      <option value="b202">[B202] 서울시 마포구 양화로 23, 5층 상가</option>
      <option value="c303">[C303] 인천시 부평구 부평대로 88, 아파트 101동</option>
    </select>
  </div>

  <div class="form-container">
    <!-- 왼쪽 폼 -->
    <div class="form-box">
      <div class="form-row">
        <label for="name">건물 이름</label>
        <input type="text" id="name" placeholder="입력해주세요">
      </div>

      <div class="form-row">
        <label for="zipcode">우편번호</label>
        <input type="text" id="zipcode" placeholder="우편번호">
      </div>

      <div class="form-row">
        <label for="addr1">기본주소</label>
        <input type="text" id="addr1" placeholder="기본 주소">
      </div>

      <div class="form-row">
        <label for="addr2">상세주소</label>
        <input type="text" id="addr2" placeholder="상세 주소">
      </div>

      <div class="form-row">
        <label for="type">건물 유형</label>
        <select id="type">
          <option value="">선택하세요</option>
          <option value="APT">아파트</option>
          <option value="OFFICE">오피스텔</option>
          <option value="SHOP">상가</option>
          <option value="ETC">기타</option>
        </select>
      </div>

      <div class="form-row">
        <label for="completion">준공일</label>
        <input type="date" id="completion">
      </div>

      <div class="form-row">
        <label for="floors">층 수</label>
        <input type="number" id="floors" placeholder="입력해주세요">
      </div>

      <div class="form-row">
        <label for="area">연 면적</label>
        <input type="text" id="area" placeholder="㎡ 단위">
      </div>

      <div class="form-row">
        <label for="units">호실 수</label>
        <input type="number" id="units" placeholder="입력해주세요">
      </div>
    </div>

    <!-- 오른쪽 이미지 -->
    <div class="image-box">
      <img src="/images/sample-building.jpg" alt="건물 이미지 미리보기">
      <button type="button">이미지 등록</button>
    </div>
  </div>

  <div style="width: 840px; margin-top: 30px;">
    <button class="submit-btn">건물 등록</button>
  </div>
  <script src="/app/js/building/managed/managedAdd.js"></script>
</body>
</html>