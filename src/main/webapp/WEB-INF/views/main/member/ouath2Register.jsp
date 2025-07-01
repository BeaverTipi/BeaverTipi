<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/security/tags" prefix="security" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 가입 페이지</title>

  <!-- CSS -->
  <link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/intl-tel-input@17.0.19/build/css/intlTelInput.min.css" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/member/register.css">

  <style>

  </style>
</head>
<body>
  <!-- container -->
<div class="register-wrapper">
  <div class="signup-container">
    <h2 class="signup-title">회원가입</h2>

    <form:form class="signup-form" id="signup-form" method="post" action="/member/register" modelAttribute="member">
      
      <!-- 양쪽 배치용 Flex -->
      <div class="form-flex-wrapper">
        <!-- ✅ 필수 정보 왼쪽 -->
        <div class="form-section left-section">
          <h3>기본 정보 입력 (필수)</h3>
          <!-- 아이디 -->
   		  <div class="form-group">
            <label for="mbrId">회원 ID</label>
            <form:input path="mbrId" class="form-control" placeholder="회원 ID (선택)" />
            <form:errors path="mbrId" class="text-danger" />
          </div>
          <!-- 비밀번호 -->
          <div class="form-group">
            <label for="mbrPw">비밀번호</label>
            <form:input path="mbrPw" type="password" class="form-control" placeholder="비밀번호 입력" required="true" />
            <form:errors path="mbrPw" class="text-danger" />
          </div>
          <!-- 이름 -->
          <div class="form-group">
            <label for="mbrNm">이름</label>
            <form:input path="mbrNm" class="form-control" placeholder="이름 입력" required="true" />
            <form:errors path="mbrNm" class="text-danger" />
          </div>

          <!-- 전화번호 -->
          <div class="form-group">
            <label for="mbrTelno">전화번호</label>
            <div class="phone-auth-group">
              <form:input path="mbrTelno" type="tel" class="form-control" placeholder="전화번호 입력" required="true" />
              <button type="button" id="requestAuthBtn" class="btn-auth-code">인증요청</button>
            	
            </div>
            <form:errors path="mbrTelno" class="text-danger" />
          </div>

          <!-- 인증번호 -->
          <div class="form-group hidden" id="authCodeWrapper">
            <label for="authCode">인증번호</label>
            <input type="text" name="authCode" id="authCode" class="form-control" placeholder="인증번호 6자 입력" maxlength="6" required />
			  
			</div>
        </div>



        <!-- ✅ 선택 정보 오른쪽 -->
        <div class="form-section right-section">
          <h3 class="optional-toggle-heading" onclick="toggleOptionalInfo()">추가 정보 입력 (선택)</h3>
          <!-- 닉네임 -->
          <div id="optionalInfo" style="display: none;">
          <div class="form-group">
            <label for="mbrNnm">닉네임</label>
            <form:input path="mbrNnm" class="form-control" placeholder="회원 닉네임" />
          </div>

          <!-- 이메일 -->
          <div class="form-group">
            <label for="mbrEmlAddr">이메일</label>
            <form:input path="mbrEmlAddr" class="form-control" placeholder="회원 이메일" />
            <form:errors path="mbrEmlAddr" class="text-danger" />
          </div>

          <!-- 주소 -->
          <!-- 우편번호 -->
		<div class="form-group">
			<label for="mbrZip">우편번호</label>
			  <div style="display: flex; gap: 8px;">
			    <form:input path="mbrZip" class="form-control" id="postcode" placeholder="우편번호" readonly="true" />
			    <button type="button" class="btn-address-search" onclick="execDaumPostcode()">주소검색</button>
			  </div>
		</div>

			
			<!-- 기본주소 -->
			<div class="form-group">
			  <label for="mbrBasicAddr">기본주소</label>
			  <form:input path="mbrBasicAddr" class="form-control" id="address" readonly="true" />
			</div>
			
			<!-- 상세주소 -->
			<div class="form-group">
			  <label for="mbrDetailAddr">상세주소</label>
			  <form:input path="mbrDetailAddr" class="form-control" id="detailAddress" />
			</div>


          <!-- 프로필 이미지 -->
        <div class="form-group">
		  <label for="mbrProfilImage">프로필 이미지</label>
		  <div class="custom-file-upload">
		    <label for="mbrProfilImage" class="file-label">파일 선택</label>
		    <span id="file-name" class="file-name">선택된 파일 없음</span>
		    <form:input type="file" path="mbrProfilImage" id="mbrProfilImage" class="file-input" onchange="updateFileName(this)" />
		  </div>
		</div>

        </div>
      </div>
</div>
      <!-- 버튼 -->
      <div class="form-actions">
        <button type="submit" class="btn-primary">가입하기</button>
        <button type="reset" id="resetBtn" class="btn-outline">뒤로가기</button>
      </div>
    </form:form>
  </div>
</div>


  <!-- JS 라이브러리 -->
  <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@17.0.19/build/js/intlTelInput.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/intl-tel-input@17.0.19/build/js/utils.js"></script>
<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
  <!-- 커스텀 JS -->
  <script src="${pageContext.request.contextPath}/app/js/main/member/register.js"></script>


</body>
</html>
