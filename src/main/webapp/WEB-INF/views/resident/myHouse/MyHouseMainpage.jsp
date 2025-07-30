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

.bottom-section {
	display: flex;
	flex: 0.5;
	border-top: 1px solid #ccc;
	background: rgba(255, 255, 255, 0.97);
	font-size: 15px;
	overflow: hidden;
}

.container {
	max-width: 1200px;
	margin: auto;
	background-color: #fff;
	border-radius: 8px;
	box-shadow: 0 0 10px rgba(0, 0, 0, 0.1);
	padding: 20px;
	display: flex;
}

.main-section {
	display: flex;
	flex-direction: column; /* 👉 수직 배치 */
	height: 100vh;
	overflow: hidden;
}

.image-wrapper {
	position: relative;
	flex: 1.2; /* 높이 비중 설정 */
	overflow: hidden;
	border: 1px solid #ccc;
}

.main-image {
	width: 100%;
	height: 100%;
	object-fit: cover; /* 비율 유지하며 꽉 채움 */
	border: 2px solid #000;
	opacity: 0;
	animation: imageFadeIn 1s ease forwards;
	animation-delay: 0s;
}

.mini-board {
	flex: 1;
	padding: 20px;
	overflow-y: auto;
}

.building-desc {
	flex: 1;
	padding: 20px;
	border-left: 1px solid #ccc;
	overflow-y: auto;
}

.building-desc h3 {
	margin-top: 0;
	color: #E17100;
	font-size: 18px;
	border-bottom: 1px solid #ddd;
	padding-bottom: 8px;
}

.building-desc p {
	line-height: 1.6;
	color: #333;
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
}

.description-box h1 span {
	opacity: 0;
	animation-duration: 0.6s;
	animation-fill-mode: forwards;
	transform: translateY(20px);
	display: inline-block;
}
/* 이미지 먼저 등장 */
@
keyframes imageFadeIn {from { opacity:0;
	transform: scale(1.05);
}

to {
	opacity: 1;
	transform: scale(1);
}

}

/* 하얀색으로 나타나는 애니메이션 */
@
keyframes fadeWhite {from { opacity:0;
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
@
keyframes fadeOrange {from { opacity:0;
	color: white;
	transform: translateY(20px);
}

to {
	opacity: 1;
	color: #E17100;
	transform: translateY(0);
}

}
@
keyframes fadeInUp {from { opacity:0;
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
	<div class="main-section">
		<!-- ✅ 상단: 이미지와 텍스트 설명 -->
		<div class="image-wrapper">
        <img class="main-image" src="${pageContext.request.contextPath}/volt/assets/img/다운로드 (18).png" alt="대표 주택 이미지">
			<div class="description-box">
				<h1>
					<span
						style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.0s;">아</span>
					<span
						style="animation: fadeOrange 0.6s ease forwards; animation-delay: 1.1s;">늑</span>
					<span
						style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.2s;">한</span>
					<span
						style="animation: fadeOrange 0.6s ease forwards; animation-delay: 1.3s;">&nbsp;</span>
					<span
						style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.4s;">우</span>
					<span
						style="animation: fadeOrange 0.6s ease forwards; animation-delay: 1.5s;">리</span>
					<span
						style="animation: fadeWhite 0.6s ease forwards; animation-delay: 1.6s;">집</span>
				</h1>
				<p>편안한 삶이 있는 곳</p>
			</div>
		</div>

		<!-- ✅ 하단: 게시판 + 건물 설명을 감싸는 flex 컨테이너 -->
		<div class="bottom-section">
			<div class="mini-board">
				<h3>📌 최근 소식</h3>
				<ul>
					<li><a href="#">[공지] 관리비 납부 기한 안내</a></li>
					<li><a href="#">입주민 회의 안내</a></li>
					<li><a href="#">주차장 공사 일정</a></li>
					<li><a href="#">엘리베이터 점검 공지</a></li>
				</ul>
			</div>

			<div class="building-desc">
				<h3>🏡 건물 정보</h3>
				<p>본 건물은 2021년에 준공된 신축 아파트로, 총 15층 규모에 2개의 동과 60세대가 입주하고 있습니다.
					최신 에너지 절감 시스템과 스마트 보안 기능이 도입되어 쾌적하고 안전한 생활 환경을 제공합니다.</p>
			</div>
		</div>
	</div>
</body>
</html>