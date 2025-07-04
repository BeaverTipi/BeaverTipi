<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>main</title>
</head>
<body>
<div class="container py-4">

<div class="search-section">
  <!-- 검색창 -->
  <div class="search-box">
    <input type="text" class="search-input" placeholder="지역, 지하철, 단지명 또는 매물번호를 입력해주세요...">
  </div>

  <!-- 카테고리 + 배너 -->
  <div class="category-banner">
    <div class="categories">
      <button class="category-btn">원룸,투룸<br><i class="bi bi-house-door"></i></button>
      <button class="category-btn">주택/빌라<br><i class="bi bi-house"></i></button>
      <button class="category-btn">오피스텔<br><i class="bi bi-buildings"></i></button>
      <button class="category-btn">아파트/신축<br><i class="bi bi-building"></i></button>
      <button class="category-btn">신축분양<br><i class="bi bi-building-check"></i></button>
    </div>
    <div class="banner">
      <img src="/volt/assets/img/images/ourads.png" alt="광고 배너">
    </div>
  </div>
</div>

  <!-- 태그 -->
  <div class="mb-5 text-center tag-box">
    <button class="tag-btn">#원룸</button>
    <button class="tag-btn">#인기매물</button>
    <button class="tag-btn">#안심</button>
  </div>

  <!-- 매물 슬라이더 -->
  <div id="roomCarousel" class="carousel slide" data-bs-ride="carousel">
    <div class="carousel-inner">

      <div class="carousel-item active">
        <div class="row justify-content-center">
          <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
            <div class="card room-card">
              <img src="${pageContext.request.contextPath}/volt/assets/img/images/room1.png" class="card-img-top room-image" alt="room1">
              <div class="card-body">
                <p class="room-title">원룸</p>
                <p>월세 1000/80 | 관리비 5만</p>
              </div>
            </div>
          </div>
          <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
            <div class="card room-card">
              <img src="${pageContext.request.contextPath}/volt/assets/img/images/room2.png" class="card-img-top room-image" alt="room2">
              <div class="card-body">
                <p class="room-title">원룸</p>
                <p>월세 500/60 | 관리비 8만</p>
              </div>
            </div>
          </div>
          <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
            <div class="card room-card">
              <img src="${pageContext.request.contextPath}/volt/assets/img/images/room3.png" class="card-img-top room-image" alt="room3">
              <div class="card-body">
                <p class="room-title">원룸</p>
                <p>월세 1000/40 | 관리비 3만</p>
              </div>
            </div>
          </div>
        </div>
      </div>

      <div class="carousel-item">
        <div class="row justify-content-center">
          <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
            <div class="card room-card">
              <img src="${pageContext.request.contextPath}/volt/assets/img/images/room4.png" class="card-img-top room-image" alt="room4">
              <div class="card-body">
                <p class="room-title">원룸</p>
                <p>월세 700/70 | 관리비 4만</p>
              </div>
            </div>
          </div>
          <div class="col-sm-6 col-md-4 col-lg-3 mb-4">
            <div class="card room-card">
              <img src="${pageContext.request.contextPath}/volt/assets/img/images/room5.png" class="card-img-top room-image" alt="room5">
              <div class="card-body">
                <p class="room-title">투룸</p>
                <p>월세 1300/90 | 관리비 6만</p>
              </div>
            </div>
          </div>
        </div>
      </div>

    </div>
    <button class="carousel-control-prev" type="button" data-bs-target="#roomCarousel" data-bs-slide="prev">
      <span class="carousel-control-prev-icon"></span>
    </button>
    <button class="carousel-control-next" type="button" data-bs-target="#roomCarousel" data-bs-slide="next">
      <span class="carousel-control-next-icon"></span>
    </button>
  </div>

<h5 class="ad-section-title"><i class="bi bi-megaphone"></i> 광고</h5>

<div id="adSlider" class="carousel slide" data-bs-ride="carousel">
  <div class="carousel-inner">

    <!-- 슬라이드 1 -->
    <div class="carousel-item active">
      <div class="d-flex justify-content-center align-items-center ad-slide-box">
        <div class="ad-left-box">
          <img src="/volt/assets/img/images/여운선.png" alt="여운선 로고" class="ad-logo">
          <div class="ad-title">여운선</div>
          <div class="ad-desc">나베가 맛있고 술이 맛있는<br>언덕 위의 작은 요리주점</div>
        </div>
        <div class="ad-right-box">
          <img src="/volt/assets/img/images/여운선배경.png" class="ad-image" alt="여운선 배경">
        </div>
      </div>
    </div>

    <!-- 슬라이드 2 -->
    <div class="carousel-item">
      <div class="d-flex justify-content-center align-items-center ad-slide-box">
     
        <div class="ad-right-box">
          <img src="/volt/assets/img/images/여운선2.png" class="ad-image" alt="여운선 배경">
        </div>
      </div>
    </div>

    <!-- 슬라이드 3 -->
    <div class="carousel-item">
      <div class="d-flex justify-content-center align-items-center ad-slide-box">
   
        <div class="ad-right-box">
          <img src="/volt/assets/img/images/여운선3.png" class="ad-image" alt="여운선 배경">
        </div>
      </div>
    </div>
    <div class="carousel-item">
      <div class="d-flex justify-content-center align-items-center ad-slide-box">
   
        <div class="ad-right-box">
          <img src="/volt/assets/img/images/여운선4.png" class="ad-image" alt="여운선 배경">
        </div>
      </div>
    </div>

  </div>

  <!-- 컨트롤 버튼 -->
  <button class="carousel-control-prev" type="button" data-bs-target="#adSlider" data-bs-slide="prev">
    <span class="carousel-control-prev-icon"></span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#adSlider" data-bs-slide="next">
    <span class="carousel-control-next-icon"></span>
  </button>
</div>




</body>
</html>
