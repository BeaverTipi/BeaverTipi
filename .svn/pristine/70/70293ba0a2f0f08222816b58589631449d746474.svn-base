<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
  // JSTL 외에 script 코드 사용 없이 구현
%>
<c:set var="defaultTabId" value="broker-info" />
<c:if test="${not empty member.tenancy and empty member.broker}">
  <c:set var="defaultTabId" value="tenancy-info" />
</c:if>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>회원 정보</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/member.css">
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
</head>
<body>
  <div class="register-wrapper" data-default-tab="${defaultTabId}">
    <div class="signup-container">
      <div style="display: flex; justify-content: space-between; align-items: center;">
        <h2 class="signup-title">회원 정보</h2>
        <a href="${pageContext.request.contextPath}/account/update" class="btn-primary" style="text-decoration: none; text-align: center;">수정하기</a>
      </div>
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

      <c:if test="${not empty solutionSubscriptionList}">
        <div class="subscription-card-wrapper">
  <c:forEach var="subscription" items="${solutionSubscriptionList}">
    <div class="subscription-card">
      <div class="subscription-header">
        <h5 class="subscription-title">${subscription.solution.solName}</h5>
        <c:if test="${subscription.subsApprovalYn eq 'Y' and subscription.subsStatus ne '001'}">
          <form action="${pageContext.request.contextPath}/payment/solution" method="post">
            <input type="hidden" name="solId" value="${subscription.solution.solId}" />
            <button type="submit" class="btn btn-primary btn-sm">결제하기</button>
          </form>
        </c:if>
      </div>

      <ul class="subscription-details">
        <li>
          <strong>승인 여부:</strong>
          <c:choose>
            <c:when test="${subscription.subsApprovalYn eq 'Y'}"><span class="text-success">승인됨</span></c:when>
            <c:when test="${subscription.subsApprovalYn eq 'N'}"><span class="text-danger">미승인</span></c:when>
            <c:otherwise><span class="text-muted">확인 불가</span></c:otherwise>
          </c:choose>
        </li>
        <li>
          <strong>솔루션 활성 상태:</strong>
          <c:choose>
            <c:when test="${subscription.subsStatus eq '001'}"><span class="text-success">사용 가능</span></c:when>
            <c:when test="${subscription.subsStatus eq '002'}"><span class="text-success">일시 정지</span></c:when>
            <c:when test="${subscription.subsStatus eq '003'}"><span class="text-success">취소</span></c:when>
            <c:otherwise><span class="text-danger">사용 불가</span></c:otherwise>
          </c:choose>
        </li>
        <li><strong>결제 금액:</strong> <span>${subscription.solution.solPrice} 원</span></li>
      </ul>
    </div>
  </c:forEach>
</div>

      </c:if>

      <c:if test="${not empty member.broker or not empty member.tenancy}">
        <div class="tab-buttons">
          <c:if test="${not empty member.broker}">
            <button class="tab-button" onclick="openTab('broker-info')">공인중개사 정보</button>
          </c:if>
          <c:if test="${not empty member.tenancy}">
            <button class="tab-button" onclick="openTab('tenancy-info')">임대인 정보</button>
          </c:if>
        </div>

        <c:if test="${not empty member.broker}">
          <div id="broker-info" class="tab-content">
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

  <script src="${pageContext.request.contextPath}/app/js/main/member/memberPage.js"></script>
</body>
</html>
