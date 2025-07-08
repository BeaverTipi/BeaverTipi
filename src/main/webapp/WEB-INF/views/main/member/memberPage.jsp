<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 정보</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/member.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
</head>
<body>
  <div class="register-wrapper">
    <div class="signup-container">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 class="signup-title">회원 정보</h2>
        <a href="${pageContext.request.contextPath}/account/update" class="btn-primary"
           style="text-decoration: none; text-align: center;">수정하기</a>
      </div>

      <c:if test="${not empty solution}">
        <div class="form-group">
          <label>닉네임</label>
          <div class="form-control">${solution.solName}</div>
        </div>
      </c:if>

      <div class="form-group">
        <label>프로필 이미지</label>
        <div style="text-align: center;">
          <img id="previewImage"
               src="${pageContext.request.contextPath}${empty member.mbrProfilImage ? '/volt/assets/img/images/기본프로필.png' : member.mbrProfilImage}"
               alt="프로필 이미지"
               class="profile-image-preview" />
        </div>
      </div>

      <div class="form-group-wrapper">
        <div class="form-group"><label>이름</label><div class="form-control">${member.mbrNm}</div></div>
        <div class="form-group"><label>닉네임</label><div class="form-control">${member.mbrNnm}</div></div>
        <div class="form-group"><label>전화번호</label><div class="form-control">${member.mbrTelno}</div></div>
        <div class="form-group"><label>이메일</label><div class="form-control">${member.mbrEmlAddr}</div></div>
        <div class="form-group"><label>주소</label><div class="form-control">${member.mbrBasicAddr} ${member.mbrDetailAddr}</div></div>
        <div class="form-group"><label>가입 경로</label>
          <div class="form-control">
            <c:choose>
              <c:when test="${logInfo eq 'LOCAL'}">일반회원</c:when>
              <c:when test="${logInfo eq 'KAKAO'}">카카오</c:when>
              <c:when test="${logInfo eq 'GOOGLE'}">구글</c:when>
              <c:otherwise>소셜 로그인</c:otherwise>
            </c:choose>
          </div>
        </div>
        <div class="form-group"><label>가입 일시</label><div class="form-control">${member.mbrFrstRegDt}</div></div>
        <div class="form-group"><label>현 상태</label>
          <div class="form-control">
            <c:choose>
              <c:when test="${member.mbrStatusCode eq 'ACTIVE'}">활성</c:when>
              <c:when test="${member.mbrStatusCode eq 'INACTIVE'}">비활성</c:when>
              <c:when test="${member.mbrStatusCode eq 'BANNED'}">정지</c:when>
              <c:otherwise>알 수 없음</c:otherwise>
            </c:choose>
          </div>
        </div>
      </div>

      <c:if test="${not empty member.broker or not empty member.tenancy}">
        <div class="tab-buttons">
          <c:if test="${not empty member.broker}">
            <button class="tab-button active" onclick="openTab('broker-info')">공인중개사 정보</button>
          </c:if>
          <c:if test="${not empty member.tenancy}">
            <button class="tab-button" onclick="openTab('tenancy-info')">임대인 정보</button>
          </c:if>
        </div>

        <c:if test="${not empty member.broker}">
          <div id="broker-info" class="tab-content active">
            <div class="form-group-wrapper">
              <div class="form-group"><label>사무소 이름</label><div class="form-control">${member.broker.brokNm}</div></div>
              <div class="form-group"><label>사업자등록번호</label><div class="form-control">${member.broker.brokRegNo}</div></div>
              <div class="form-group"><label>자격증 번호</label><div class="form-control">${member.broker.crtfNo}</div></div>
              <div class="form-group"><label>대표자</label><div class="form-control">${member.broker.reprNm}</div></div>
              <div class="form-group"><label>전화번호</label><div class="form-control">${member.broker.reprTelNo}</div></div>
              <div class="form-group"><label>등록 일시</label><div class="form-control">${member.broker.regDtm}</div></div>
              <div class="form-group"><label>주소</label><div class="form-control">${member.broker.brokAddr1} ${member.broker.brokAddr2}</div></div>
            </div>
          </div>
        </c:if>

        <c:if test="${not empty member.tenancy}">
          <div id="tenancy-info" class="tab-content">
            <div class="form-group-wrapper">
              <div class="form-group"><label>등록된 건물 수</label><div class="form-control">${member.tenancy.rentalPtyRegBldgCnt}</div></div>
              <div class="form-group"><label>계좌번호</label><div class="form-control">${member.tenancy.rentalPtyAcctNo}</div></div>
              <div class="form-group"><label>사업자등록번호</label><div class="form-control">${member.tenancy.rentalPtyBizRegNo}</div></div>
              <div class="form-group"><label>은협명</label><div class="form-control">${member.tenancy.rentalPtyBankNm}</div></div>
              <div class="form-group"><label>임대유형</label><div class="form-control">${member.tenancy.lsrTypeGroupCd}</div></div>
              <div class="form-group"><label>임대자 유형</label><div class="form-control">${member.tenancy.lsrYnTypeCd}</div></div>
            </div>
          </div>
        </c:if>
      </c:if>
    </div>
  </div>

  <script src="/app/js/main/member/memberPage.js"></script>
</body>
</html>
