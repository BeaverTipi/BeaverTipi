<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
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
              <c:when test="${logInfo eq 'GOOGLE'}">구그룹</c:when>
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
<div class="subscription-row">
  <!-- 공인중개사 승인 정보 카드 -->
  <c:if test="${not empty member.broker}">
    <div class="subscription-card">
      <div class="subscription-header">
        <h5 class="subscription-title">공인중개사 승인 정보</h5>
      </div>
      <div class="subscription-content">
        <ul>
          <li><strong>공인중개사 승인 상태:</strong>
            <c:choose>
              <c:when test="${member.broker.authApprYn eq 'Y'}"><span class="text-success">✅ 승인</span></c:when>
              <c:when test="${member.broker.authApprYn eq 'N'}"><span class="text-danger">❌ 거절</span></c:when>
              <c:when test="${member.broker.authApprYn eq 'W'}"><span class="text-muted">⏳ 대기</span></c:when>
              <c:otherwise><span class="text-muted">❓ 미확인</span></c:otherwise>
            </c:choose>
          </li>
        </ul>
      </div>
      <!-- 버튼 영역을 명확히 분리 -->
      <div class="subscription-footer">
  <c:choose>
    <c:when test="${member.broker.authApprYn eq 'Y' and showPaymentBtnBroker}">
      <form action="/payment/business/broker" method="get">
        <button type="submit" class="btn btn-primary">결제하기</button>
      </form>
    </c:when>
    <c:otherwise>
      <div class="empty-button-space"></div>
    </c:otherwise>
  </c:choose>
</div>

    </div>
  </c:if>
  <!-- 임대인 승인 정보 카드 -->
  <c:if test="${not empty member.tenancy}">
    <div class="subscription-card">
      <div class="subscription-header">
        <h5 class="subscription-title">임대인 승인 정보</h5>
      </div>
      <div class="subscription-content">
        <ul>
          <li><strong>임대인 승인 상태:</strong>
            <c:choose>
              <c:when test="${member.tenancy.authApprYn eq 'Y'}"><span class="text-success">✅ 승인</span></c:when>
              <c:when test="${member.tenancy.authApprYn eq 'N'}"><span class="text-danger">❌ 거절</span></c:when>
              <c:when test="${member.tenancy.authApprYn eq 'W'}"><span class="text-muted">⏳ 대기</span></c:when>
              <c:otherwise><span class="text-muted">❓ 미확인</span></c:otherwise>
            </c:choose>
          </li>
        </ul>
      </div>
      <!-- 버튼 영역을 명확히 분리 -->
      <div class="subscription-footer">
  <c:choose>
    <c:when test="${member.tenancy.authApprYn eq 'Y' and showPaymentBtnTenancy}">
      <form action="/payment/business/tenancy" method="get">
        <button type="submit" class="btn btn-primary">결제하기</button>
      </form>
    </c:when>
    <c:otherwise>
      <div class="empty-button-space"></div>
    </c:otherwise>
  </c:choose>
</div>

    </div>
  </c:if>

<!-- 구독 정보 카드 -->
<c:forEach var="sub" items="${solutionSubscriptionList}">
  <div class="subscription-card">
    <div class="subscription-header">
      <h5 class="subscription-title">구독정보</h5>
    </div>

    <div class="subscription-content">
      <ul class="subscription-info-list">
        <li>
          <span class="label">구독 타이틀:</span>
          <span class="value">${sub.solution.solName}</span>
        </li>
        <li>
          <span class="label">상태:</span>
          <c:choose>
            <c:when test="${sub.subsStatus eq '001'}"><span class="value text-success">✅ 사용 가능</span></c:when>
            <c:when test="${sub.subsStatus eq '002'}"><span class="value text-muted">⏸ 일시 정지</span></c:when>
            <c:when test="${sub.subsStatus eq '003'}"><span class="value text-muted">❌ 취소</span></c:when>
            <c:when test="${sub.subsStatus eq '004'}"><span class="value text-muted">⏳ 결제 대기</span></c:when>
            <c:otherwise><span class="value text-danger">⛔ 사용 불가</span></c:otherwise>
          </c:choose>
        </li>
      </ul>
    </div>

    <c:if test="${sub.subsStatus eq '001'}">
      <div class="subscription-footer">
        <form action="/payment/business/${sub.solution.solCcCd eq '001' ? 'tenancy' : 'broker'}" method="get" style="display:inline;">
          <button type="submit" class="btn-primary-custom">구독 변경</button>
        </form>
        <form action="${pageContext.request.contextPath}/subscription/cancel" method="post" style="display:inline;">
          <input type="hidden" name="subsId" value="${sub.subsId}" />
          <button type="submit" class="btn-outline-danger-custom">구독 취소</button>
        </form>
      </div>
    </c:if>
  </div>
</c:forEach>
</div>



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
              <div class="form-group"><label>사업자등록번호</label><div class="form-control">${member.tenancy.rentalPtyBizRegNo}</div></div>
              <div class="form-group"><label>임대유형</label><div class="form-control">${member.tenancy.lsrTypeGroupCd}</div></div>
              <div class="form-group"><label>임대인 유형</label><div class="form-control">${member.tenancy.lsrYnTypeCd}</div></div>
            </div>
          </div>
        </c:if>
      </c:if>
    </div>
  </div>
  <script src="${pageContext.request.contextPath}/app/js/main/member/memberPage.js"></script>
</body>
</html>
