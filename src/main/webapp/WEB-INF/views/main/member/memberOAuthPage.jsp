<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
</head>
<body>
	<tr>
		<th>회원 ID</th>
		<td>${member.mbrId}</td>
	</tr>
	<tr>
		<th>회원 실제 이름</th>
		<td>${member.mbrNm}</td>
	</tr>
	<tr>
		<th>회원 닉네임</th>
		<td>${member.mbrNnm}</td>
	</tr>
	<tr>
		<th>회원 휴대폰 전화번호</th>
		<td>${member.mbrTelno}</td>
	</tr>
	<tr>
		<th>회원 전자 우편 주소</th>
		<td>${member.mbrEmlAddr}</td>
	</tr>
	<tr>
		<th>회원 기본주소</th>
		<td>${member.mbrBasicAddr}</td>
	</tr>
	<tr>
		<th>회원 상세주소</th>
		<td>${member.mbrDetailAddr}</td>
	</tr>
	<tr>
		<th>프로필 이미지</th>
		<td><c:choose>
				<c:when test="${not empty member.mbrProfilImage}">
					<img id="previewImage"
						src="${pageContext.request.contextPath}${member.mbrProfilImage}"
						alt="기존 프로필 이미지"
						style="width: 120px; height: 120px; object-fit: cover; border-radius: 50%; border: 2px solid #ccc;" />
				</c:when>
				<c:otherwise>
					<img id="previewImage"
						src="${pageContext.request.contextPath}/volt/assets/img/images/기본프로필.png"
						alt="기본 프로필 이미지"
						style="width: 120px; height: 120px; object-fit: cover; border-radius: 50%; border: 2px dashed #ccc;" />
				</c:otherwise>
			</c:choose></td>
	</tr>
	<tr>
		<th>회원 가입 일시</th>
		<td>${member.mbrFrstRegDt}</td>
	</tr>
	<tr>
		<th>회원 상태</th>
		<td>${member.mbrStatusCode}</td>
	</tr>
</body>
</html>