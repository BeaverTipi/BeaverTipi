<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/member.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
<c:if test="${param.success eq 'true'}">
<script>
Swal.fire({
	icon: 'success',
	title: '결제가 완료되었습니다',
	text: '요금제가 성공적으로 적용되었습니다.',
	confirmButtonText: '확인'
})
</script>
</c:if>
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
				<label>가입 경로</label>
				<div class="form-control">
					<c:choose>
						<c:when test="${logInfo eq 'LOCAL'}">일반회원</c:when>
						<c:when test="${logInfo eq 'KAKAO'}">카카오</c:when>
						<c:when test="${logInfo eq 'GOOGLE'}">구글</c:when>
						<c:otherwise>소셜 로그인</c:otherwise>
					</c:choose>
				</div>
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
			<c:if test="${not empty member.broker}">
	<hr />
	<h3 class="signup-title">공인중개사 정보</h3>

	<div class="form-group">
		<label>사무소 이름</label>
		<div class="form-control">${member.broker.brokNm}</div>
	</div>
	<div class="form-group">
		<label>공인중개사 사업자 등록번호</label>
		<div class="form-control">${member.broker.brokRegNo}</div>
	</div>
	<div class="form-group">
		<label>공인중개사 자격증</label>
		<div class="form-control">${member.broker.crtfNo}</div>
	</div>
	<div class="form-group">
		<label>대표자 이름</label>
		<div class="form-control">${member.broker.reprNm}</div>
	</div>
	<div class="form-group">
		<label>대표자 전화번호</label>
		<div class="form-control">${member.broker.reprTelNo}</div>
	</div>
	<div class="form-group">
		<label>등록 일시</label>
		<div class="form-control">${member.broker.regDtm}</div>
	</div>
	<div class="form-group">
		<label>기본 도로명 주소</label>
		<div class="form-control">${member.broker.brokAddr1}</div>
	</div>
	<div class="form-group">
		<label>동/호수/건물명 등</label>
		<div class="form-control">${member.broker.brokAddr2}</div>
	</div>
</c:if>
<c:if test="${not empty member.tenancy}">
	<hr />
	<h3 class="signup-title">임대인 정보</h3>

	<div class="form-group">
		<label>등록된 건물의 수</label>
		<div class="form-control">${member.tenancy.rentalPtyRegBldgCnt}</div>
	</div>
	<div class="form-group">
		<label>계좌번호(예:9999999999)</label>
		<div class="form-control">${member.tenancy.rentalPtyAcctNo}</div>
	</div>
	<div class="form-group">
		<label>기본 사업자등록번호</label>
		<div class="form-control">${member.tenancy.rentalPtyBizRegNo}</div>
	</div>
	<div class="form-group">
		<label>공동임대/1인임대/공동임차/1인임차</label>
		<div class="form-control">${member.tenancy.lsrYnTypeCd}</div>
	</div>
	<div class="form-group">
		<label>은행이름, 은행명칭</label>
		<div class="form-control">${member.tenancy.rentalPtyBankNm}</div>
	</div>
	<div class="form-group">
		<label>임대유형 코드</label>
		<div class="form-control">${member.tenancy.lsrTypeGroupCd}</div>
	</div>
</c:if>

			
		</div>
	</div>
</body>
</html>
