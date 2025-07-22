<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="security" uri="http://www.springframework.org/security/tags" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>main</title>

</head>
<body>
<div class="container py-4">

<div class="search-section">
  <div class="search-box">
    <input type="text" class="search-input" placeholder="지역, 지하철, 단지명 또는 매물번호를 입력해주세요...">
  </div>

  <div class="category-banner">
    <div class="categories">
	  <c:forEach var="code" items="${categoryList}">
	    <c:if test="${not empty code.codeValue and code.codeName ne '전체'}">
	      <button class="category-btn" onclick="location.href='/main/map?category=${code.codeValue}'">
	        <img id="mainCategoryImg"
	          src="${pageContext.request.contextPath}/volt/assets/img/icons/${code.codeValue}.svg" 
	          alt="${code.codeName}" />
	        <span>${code.codeName}</span>
	      </button>
	    </c:if>
	  </c:forEach>
	</div>
    <div class="banner">
      <img src="${pageContext.request.contextPath}/volt/assets/img/images/ourads.png" alt="광고 배너">
    </div>
  </div>
</div>

  <div class="mb-5 text-center tag-box">
    <button class="tag-btn">#원룸</button>
    <button class="tag-btn">#인기매물</button>
    <button class="tag-btn">#안심</button>
  </div>

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
    </div>

  <button class="carousel-control-prev" type="button" data-bs-target="#adSlider" data-bs-slide="prev"> 
    <span class="carousel-control-prev-icon"></span>
  </button>
  <button class="carousel-control-next" type="button" data-bs-target="#adSlider" data-bs-slide="next"> 
    <span class="carousel-control-next-icon"></span>
  </button>
</div>

</div> <%-- container py-4 끝 --%>

<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>

<script>
    // contextPath 변수는 JSP에서 직접 정의하여 JavaScript에서 사용 가능하게 합니다.
    var contextPath = '${pageContext.request.contextPath}';
    console.log("Debug Context Path: " + contextPath);
</script>

<script src="${pageContext.request.contextPath}/app/js/main/mainPageView.js"></script>
</body>
</html>