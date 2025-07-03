<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">

</head>
<body>
	<div class="register-wrapper">
		<div class="signup-container">
			<h2 class="signup-title">회원 정보</h2>

			<div class="form-group">
				<label>프로필 이미지</label>
				<div style="text-align: center;">
					<c:choose>
						<c:when test="${not empty member.mbrProfilImage}">
							<img id="previewImage"
								src="${pageContext.request.contextPath}${member.mbrProfilImage}"
								alt="기존 프로필 이미지" class="profile-image-preview" />
						</c:when>
						<c:otherwise>
							<img id="previewImage"
								src="${pageContext.request.contextPath}/volt/assets/img/images/기본프로필.png"
								alt="기본 프로필 이미지" class="profile-image-preview" />
						</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-group">
				<label>ID</label>
				<div class="form-control">${member.mbrId}</div>
			</div>
			<div class="form-group">
				<label>이름</label>
				<div class="form-control">${member.mbrNm}</div>
			</div>
			<div class="form-group">
				<label>닉네임</label>
				<div class="form-control">${member.mbrNnm}</div>
			</div>
			<div class="form-group">
				<label>전화번호</label>
				<div class="form-control">${member.mbrTelno}</div>
			</div>
			<div class="form-group">
				<label>이메일</label>
				<div class="form-control">${member.mbrEmlAddr}</div>
			</div>
			<div class="form-group">
				<label>기본 주소</label>
				<div class="form-control">${member.mbrBasicAddr}</div>
			</div>
			<div class="form-group">
				<label>상세 주소</label>
				<div class="form-control">${member.mbrDetailAddr}</div>
			</div>
			<div class="form-group">
				<label>가입 일시</label>
				<div class="form-control">${member.mbrFrstRegDt}</div>
			</div>
			<div class="form-group">
				<label>현 상태</label>
				<div class="form-control">
					<c:choose>
						<c:when test="${member.mbrStatusCode eq 'ACTIVE'}">활성</c:when>
						<c:when test="${member.mbrStatusCode eq 'INACTIVE'}">비활성</c:when>
						<c:when test="${member.mbrStatusCode eq 'BANNED'}">정지</c:when>
						<c:otherwise>알 수 없음</c:otherwise>
					</c:choose>
				</div>
			</div>
			<div class="form-actions">
				<a href="${pageContext.request.contextPath}/account/update"
					class="btn-primary"
					style="text-decoration: none; display: inline-block; text-align: center;">수정하기</a>
			</div>
		</div>
	</div>
</body>
</html>
