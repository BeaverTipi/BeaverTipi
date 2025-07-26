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

	html, body {
	  height: 100vh;
	  margin: 0;
	  padding: 0;
	  overflow: hidden; /* 스크롤 없애기 */
	}
		
    .container {
      max-width: 1200px;
      margin: auto;
      background-color: #fff;
      border-radius: 8px;
      box-shadow: 0 0 10px rgba(0,0,0,0.1);
      padding: 20px;
      display: flex;
    }

    .main-section {
       flex: 1;
	  padding: 20px;
	  overflow: hidden;
	  height: calc(100vh - 80px); /* 화면 높이 - 헤더 예상 영역 */
	  box-sizing: border-box;
    }

    .image-wrapper {
       position: relative;
	  height: 100%;
	  overflow: hidden;
	  border: 1px solid #ccc;
	  padding: 10px;
	  box-sizing: border-box;
    }

    .main-image {
      width: 100%;
	  height: 100%;
	  object-fit: cover;  /* 비율 유지하며 꽉 채움 */
	  border: 2px solid #000;
	  opacity: 0;
	  animation: imageFadeIn 1s ease forwards;
	  animation-delay: 0s;
    }

  .description-box {
    position: absolute;
    top: 35%;
    right: 250px;
    transform: translateY(-50%);
    z-index: 2;
    text-align: right;
  }

  .description-box h1 {
    font-size: 80px;
    margin: 0;
    font-weight: bold;
    display: flex;
    justify-content: flex-end;
    gap: 6px;
    -webkit-text-stroke: 1.5px black; 
  }
  

    .text {
        font-size: 70px;
        font-weight: 900;
        -webkit-text-fill-color: transparent;
        -webkit-text-stroke: 1px red;
    }
    
    .outline-text {
  font-size: 80px;
  font-weight: bold;
  color: transparent;                      /* 내부 색 없애기 */
  -webkit-text-stroke: 2px red;            /* 외곽선만 빨간색으로 */
  -webkit-text-fill-color: transparent;    /* 사파리 대응 */
}



  .description-box h1 span {
    opacity: 0;
    animation-duration: 0.6s;
    animation-fill-mode: forwards;
    transform: translateY(20px);
    display: inline-block;
  }
	/* 이미지 먼저 등장 */
	@keyframes imageFadeIn {
	  from {
	    opacity: 0;
	    transform: scale(1.05);
	  }
	  to {
	    opacity: 1;
	    transform: scale(1);
	  }
	}

  /* 하얀색으로 나타나는 애니메이션 */
  @keyframes fadeWhite {
    from {
      opacity: 0;
      color: white;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      color: white;
      transform: translateY(0);
    }
  }

  /* 주황색으로 나타나는 애니메이션 */
  @keyframes fadeOrange {
    from {
      opacity: 0;
      color: white;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      color: #E17100;
      transform: translateY(0);
    }
  }
  @keyframes fadeInUp {
    from {
      opacity: 0;
      transform: translateY(20px);
    }
    to {
      opacity: 1;
      transform: translateY(0);
    }
  }
    .description-box h2 {
      margin: 0 0 10px;
      font-size: 20px;
    }

  .description-box p {
    opacity: 0;
    font-size: 35px;
    color: #555;
    font-style: italic;
    margin-top: 12px;
    animation: fadeInUp 0.6s ease forwards;
    animation-delay: 2.1s; /* 마지막 span 끝나고 0.5초 뒤 */
  }
    .top-row {
      display: flex;
    }
  </style>
</head>
<body>
<h1 class="outline-text">퇴사로</h1>
<p class="text">텍스트</p>
<div class="main-section">
      <div class="image-wrapper">
        <img class="main-image" src="${pageContext.request.contextPath}/volt/assets/img/다운로드 (18).png" alt="대표 주택 이미지">



 <div class="description-box">
  <h1>
    <span style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.0s;">아</span>
    <span style="animation: fadeOrange 0.6s ease forwards; animation-delay: 1.1s;">늑</span>
    <span style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.2s;">한</span>
    <span style="animation: fadeOrange 0.6s ease forwards; animation-delay: 1.3s;">&nbsp;</span>
    <span style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.4s;">우</span>
    <span style="animation: fadeOrange 0.6s ease forwards; animation-delay: 1.5s;">리</span>
    <span style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.6s;">집</span>
  </h1>
  
  
  <p>편안한 삶이 있는 곳</p>
</div>



        </div>
      </div>
  <script src="${pageContext.request.contextPath}/app/js/building/move-in/residentList.js"></script>
</body>
</html>