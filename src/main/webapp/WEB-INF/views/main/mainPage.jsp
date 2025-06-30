<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <title>beavertipi</title>
  <!-- Bootstrap 5 CSS & Icons -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <link href="https://cdn.jsdelivr.net/npm/bootstrap-icons@1.10.5/font/bootstrap-icons.css" rel="stylesheet">
  <style>
    body {
      font-family: 'Segoe UI', sans-serif;
      background-color: #f9f9f9;
    }
    .property-card img {
      object-fit: cover;
      height: 180px;
    }
    footer {
      background-color: #eee;
      padding: 20px 0;
      text-align: center;
      font-size: 14px;
      margin-top: 40px;
    }
  </style>
</head>
<body>

<!-- 🔷 Header -->
<nav class="navbar navbar-expand-lg navbar-dark bg-dark px-4 py-2">
  <div class="container-fluid d-flex justify-content-between align-items-center">
    <a class="navbar-brand fw-bold" href="${pageContext.request.contextPath}/">beavertipi</a>
    <ul class="navbar-nav flex-row align-items-center gap-3 mb-0">
      <li class="nav-item"><a class="nav-link text-white" href="#">공지사항</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="#">지도</a></li>
      <li class="nav-item"><a class="nav-link text-white" href="#">상품등록</a></li>

      <!-- 알림 -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle text-white" href="#" data-bs-toggle="dropdown">
          <i class="bi bi-bell-fill fs-5"></i>
        </a>
        <ul class="dropdown-menu dropdown-menu-end mt-2">
          <li class="dropdown-header fw-bold px-3">🔔 알림</li>
          <li><hr class="dropdown-divider"></li>
          <li class="dropdown-item">새로운 알림이 없습니다.</li>
        </ul>
      </li>

      <!-- 프로필 -->
      <li class="nav-item dropdown">
        <a class="nav-link dropdown-toggle text-white d-flex align-items-center" href="#" data-bs-toggle="dropdown">
          <img src="${pageContext.request.contextPath}/volt/assets/img/team/profile-picture-3.jpg" class="rounded-circle me-2" width="28" height="28">
          <span>사용자</span>
        </a>
        <ul class="dropdown-menu dropdown-menu-end mt-2">
          <li><a class="dropdown-item" href="#">내 프로필</a></li>
          <li><a class="dropdown-item" href="#">설정</a></li>
          <li><hr class="dropdown-divider"></li>
          <li><a class="dropdown-item text-danger" href="#">로그아웃</a></li>
        </ul>
      </li>
    </ul>
  </div>
</nav>

<!-- 🔶 Main Content -->
<div class="container mt-4">

  <!-- 🔍 Search -->
  <div class="input-group mb-4">
    <input type="text" class="form-control" placeholder="주소, 지역명, 건물명, 단지명 등">
  </div>

  <!-- 카테고리 버튼 -->
  <div class="d-flex flex-wrap gap-2 mb-4">
    <button class="btn btn-outline-secondary">원룸, 투룸</button>
    <button class="btn btn-outline-secondary">주택/빌라</button>
    <button class="btn btn-outline-secondary">오피스텔</button>
    <button class="btn btn-outline-secondary">아파트/분양권</button>
    <button class="btn btn-outline-secondary">상가</button>
    <button class="btn btn-outline-secondary">사무실</button>
  </div>

  <!-- 배너 -->
  <div class="text-center mb-4">
    <img src="https://via.placeholder.com/728x90?text=Main+Banner" class="img-fluid" alt="Main Banner">
  </div>

  <!-- 태그 -->
  <div class="mb-3">
    <span class="badge bg-light text-dark me-2">#원룸</span>
    <span class="badge bg-light text-dark me-2">#인기매물</span>
    <span class="badge bg-light text-dark">#안심</span>
  </div>

  <!-- 매물 목록 -->
  <div class="row g-4">
    <div class="col-md-4">
      <div class="card property-card">
        <img src="https://via.placeholder.com/400x250?text=Room+1" class="card-img-top" alt="...">
        <div class="card-body">
          <p class="card-text">월세 1000/80<br>서울 강남구 논현동<br>반려동물 가능</p>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card property-card">
        <img src="https://via.placeholder.com/400x250?text=Room+2" class="card-img-top" alt="...">
        <div class="card-body">
          <p class="card-text">월세 500/80<br>서울 강남구 논현동<br>엘리베이터 有</p>
        </div>
      </div>
    </div>
    <div class="col-md-4">
      <div class="card property-card">
        <img src="https://via.placeholder.com/400x250?text=Room+3" class="card-img-top" alt="...">
        <div class="card-body">
          <p class="card-text">월세 1000/40<br>서울 강남구 논현동<br>반려동물 가능</p>
        </div>
      </div>
    </div>
  </div>

  <!-- 광고 섹션 -->
  <div class="mt-5">
    <h5 class="text-danger">📢 광고</h5>
    <img src="https://via.placeholder.com/728x90?text=광고+이미지" alt="광고 이미지" class="img-fluid">
  </div>

  <!-- 맛집 추천 -->
  <div class="mt-5">
    <h5 class="text-danger">🍴 여운선</h5>
    <p>나한테 맛집과 술이 맛있는 언덕 위의 작은 요리주점</p>
    <img src="https://via.placeholder.com/728x400?text=맛집+이미지" alt="맛집 이미지" class="img-fluid">
  </div>
</div>

<!-- 🔻 Footer -->
<footer>
  <div class="container">
    <div class="text-muted">
      상점정보 | 플랫폼소개 | 회사소개 | 고객지원<br>
      상품정책 | 이용약관 | 개인정보처리방침 | Q&A
    </div>
    <div class="mt-2 text-danger">Footer입니다</div>
  </div>
</footer>

<!-- Bootstrap JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
