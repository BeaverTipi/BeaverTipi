<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>내 정보 수정</title>
 <link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/member.css" />
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/memberUpdate.css" />
</head>
<body>
  <div class="register-wrapper">
    <div class="signup-container">
      <h2 class="signup-title">내 정보 수정</h2>
      <form:form class="signup-form" method="post" action="/member/update" modelAttribute="member">

        <form:input path="mbrId" type="hidden" class="form-control" />
        <form:input path="mbrCd" type="hidden" class="form-control" />
        <form:input path="mbrPw" type="hidden" class="form-control" />
        <form:input path="mbrEmlAddr" type="hidden" class="form-control" />
        <div class="form-group">
          <label for="mbrProfilImage">프로필 이미지</label>
          <div style="text-align: center; margin-bottom: 10px;">
            <c:choose>
              <c:when test="${not empty member.mbrProfilImage}">
                <img id="previewImage" src="${pageContext.request.contextPath}${member.mbrProfilImage}" alt="기존 프로필 이미지" class="profile-image-preview" />
              </c:when>
              <c:otherwise>
                <img id="previewImage" src="${pageContext.request.contextPath}/volt/assets/img/images/기본프로필.png" alt="기본 프로필 이미지" class="profile-image-preview" />
              </c:otherwise>
            </c:choose>
          </div>
        </div>

        <div class="form-group">
          <label for="mbrNm">이름</label>
          <form:input path="mbrNm" class="form-control" />
          <form:errors path="mbrNm" class="text-danger" />
        </div>

        <div class="form-group">
          <label for="mbrTelno">전화번호</label>
          <div class="phone-auth-group">
            <form:input path="mbrTelno" id="mbrTelno" class="form-control" />
            <button type="button" id="requestAuthBtn" class="btn-auth-code hidden">인증요청</button>
          </div>
        </div>

        <div class="form-group hidden" id="authCodeWrapper">
          <label for="authCode">인증번호</label>
          <input type="text" id="authCode" class="form-control" maxlength="6" />
          <span id="timerText"></span>
        </div>

        <div class="form-group">
          <label for="mbrProfilImage">프로필 이미지</label>
          <div class="custom-file-upload">
            <label for="mbrProfilImage" class="file-label">파일 선택</label>
            <span id="file-name" class="file-name">선택된 파일 없음</span>
            <form:input type="file" path="mbrProfilImage" id="mbrProfilImage" class="file-input" onchange="updateFileName(this)" />
          </div>
        </div>

        <div class="form-group">
          <label for="mbrNnm">닉네임</label>
          <form:input path="mbrNnm" class="form-control" />
        </div>

        <div class="form-group">
          <label for="mbrZip">우편번호</label>
          <div class="zip-code-group">
            <form:input path="mbrZip" class="form-control" id="postcode" readonly="true" />
            <button type="button" class="btn-address-search" onclick="execDaumPostcode()">주소 검색</button>
          </div>
        </div>

        <div class="form-group">
          <label for="mbrBasicAddr">기본주소</label>
          <form:input path="mbrBasicAddr" class="form-control" id="address" readonly="true" />
        </div>

        <div class="form-group">
          <label for="mbrDetailAddr">상세주소</label>
          <form:input path="mbrDetailAddr" class="form-control" id="detailAddress" />
        </div>

        <div class="form-actions">
          <button type="submit" class="btn-primary">수정하기</button>
          <button type="button" id="resetBtn" class="btn-outline">취소</button>
        </div>
      </form:form>
    </div>
  </div>

  <script src="${pageContext.request.contextPath}/app/js/main/member/memberUpdate.js"></script>

</body>
</html>
