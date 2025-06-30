<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
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
        <div class="ad-left-box">
          <img src="/volt/assets/img/images/sample-logo2.png" alt="로고2" class="ad-logo">
          <div class="ad-title">커피빈</div>
          <div class="ad-desc">스페셜티 커피 전문점<br>조용한 분위기에서 공부해요</div>
        </div>
        <div class="ad-right-box">
          <img src="/volt/assets/img/images/sample-bg2.png" class="ad-image" alt="커피빈 배경">
        </div>
      </div>
    </div>

    <!-- 슬라이드 3 -->
    <div class="carousel-item">
      <div class="d-flex justify-content-center align-items-center ad-slide-box">
        <div class="ad-left-box">
          <img src="/volt/assets/img/images/sample-logo3.png" alt="로고3" class="ad-logo">
          <div class="ad-title">미미네떡볶이</div>
          <div class="ad-desc">매콤달콤 추억의 맛<br>학교 앞 분식집의 정석</div>
        </div>
        <div class="ad-right-box">
          <img src="/volt/assets/img/images/sample-bg3.png" class="ad-image" alt="미미네 배경">
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
<!-- 🔐 로그인 모달 -->
<div class="modal fade" id="loginModal" tabindex="-1" aria-labelledby="loginModalLabel" aria-hidden="true">
  <div class="modal-dialog">
    <div class="modal-content">
      <form method="post" action="${pageContext.request.contextPath}/login">
        <div class="modal-header">
          <h5 class="modal-title" id="loginModalLabel">로그인</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
        </div>
        <div class="modal-body">
          <div class="mb-3">
            <label for="username" class="form-label">아이디</label>
            <input type="text" class="form-control" name="username" id="username">
          </div>
          <div class="mb-3">
            <label for="password" class="form-label">비밀번호</label>
            <input type="password" class="form-control" name="password" id="password">
          </div>
          <button type="submit" class="btn btn-primary w-100">로그인</button>

          <!-- 🔗 아이디/비번/회원가입 링크 추가 -->
          <div class="text-center my-3 small-link-group">
            <a href="#" class="small-link">아이디 찾기</a> |
            <a href="#" class="small-link">비밀번호 찾기</a> |
            <a href="#" class="small-link">회원가입</a>
          </div>

          <!-- 소셜 로그인 -->
          <!-- 소셜 로그인 버튼들 -->
<div class="d-grid gap-2">
  <a href="${pageContext.request.contextPath}/oauth2/authorization/google" class="btn social-btn google-btn">
    <i class="bi bi-google me-2"></i> Google 로그인
  </a>
  <a href="${pageContext.request.contextPath}/oauth2/authorization/kakao" class="btn social-btn kakao-btn">
    <i class="bi bi-chat-right-dots-fill me-2"></i> Kakao 로그인
  </a>
</div>

        </div>
      </form>
    </div>
  </div>
</div>



</body>
</html>
