<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 정보</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/member.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
  <c:if test="${param.success eq 'true'}">
    <script>
      Swal.fire({
        icon: 'success',
        title: '결제가 완료되었습니다',
        text: '요금제가 성공적으로 적용되었습니다.',
        confirmButtonText: '확인'
      });
    </script>
  </c:if>
</head>
<body>
  <div class="register-wrapper">
    <div class="signup-container">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 class="signup-title">회원 정보</h2>
        <a href="${pageContext.request.contextPath}/account/update" class="btn-primary"
           style="text-decoration: none; text-align: center;">수정하기</a>
      </div>

      <div style="text-align: center; margin-top: 20px;">
  <c:choose>
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
  </c:choose>
</div>

<table class="table table-bordered" style="width: 100%; border-collapse: collapse; margin-top: 20px;">
        <tr><th>회원 ID</th><td>${member.mbrId}</td></tr>
        <tr><th>회원 실제 이름</th><td>${member.mbrNm}</td></tr>
        <tr><th>회원 닉네임</th><td>${member.mbrNnm}</td></tr>
        <tr><th>회원 휴대폰 전화번호</th><td>${member.mbrTelno}</td></tr>
        <tr><th>회원 전자 우편 주소</th><td>${member.mbrEmlAddr}</td></tr>
        <tr><th>회원 기본주소</th><td>${member.mbrBasicAddr}</td></tr>
        <tr><th>회원 상세주소</th><td>${member.mbrDetailAddr}</td></tr>
        <tr><th>회원 가입 일시</th><td>${member.mbrFrstRegDt}</td></tr>
        <tr><th>회원 상태</th>
          <td>
            <c:choose>
              <c:when test="${member.mbrStatusCode eq 'ACTIVE'}">활성</c:when>
              <c:when test="${member.mbrStatusCode eq 'INACTIVE'}">비활성</c:when>
              <c:when test="${member.mbrStatusCode eq 'BANNED'}">정지</c:when>
              <c:otherwise>알 수 없음</c:otherwise>
            </c:choose>
          </td>
        </tr>
        <tr><th>가입 경로</th>
          <td>
            <c:choose>
              <c:when test="${logInfo eq 'LOCAL'}">일반회원</c:when>
              <c:when test="${logInfo eq 'KAKAO'}">카카오</c:when>
              <c:when test="${logInfo eq 'GOOGLE'}">구글</c:when>
              <c:otherwise>소셜 로그인</c:otherwise>
            </c:choose>
          </td>
        </tr>
      </table>

      <c:if test="${not empty member.broker or not empty member.tenancy}">
        <div class="tab-buttons" style="margin-top: 24px;">
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
              <div class="form-group"><label>은행명</label><div class="form-control">${member.tenancy.rentalPtyBankNm}</div></div>
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
