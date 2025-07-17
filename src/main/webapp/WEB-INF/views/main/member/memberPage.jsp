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
              </div>
              <table class="subscription-table">
                <tbody>
                  <tr>
                    <th>승인 여부</th>
                    <td>
                      <c:choose>
                        <c:when test="${subscription.solution.solCcCd eq '001'}">
                          <span class="${member.tenancy.authApprYn eq 'Y' ? 'text-success' : (member.tenancy.authApprYn eq 'N' ? 'text-danger' : 'text-muted')}">
                            <c:choose>
                              <c:when test="${member.tenancy.authApprYn eq 'Y'}">승인</c:when>
                              <c:when test="${member.tenancy.authApprYn eq 'N'}">승인 거절</c:when>
                              <c:when test="${member.tenancy.authApprYn eq 'W'}">승인 대기</c:when>
                              <c:otherwise>확인 불가</c:otherwise>
                            </c:choose>
                          </span>
                        </c:when>
                        <c:when test="${subscription.solution.solCcCd eq '002'}">
                          <span class="${member.broker.authApprYn eq 'Y' ? 'text-success' : (member.broker.authApprYn eq 'N' ? 'text-danger' : 'text-muted')}">
                            <c:choose>
                              <c:when test="${member.broker.authApprYn eq 'Y'}">승인</c:when>
                              <c:when test="${member.broker.authApprYn eq 'N'}">승인 거절</c:when>
                              <c:when test="${member.broker.authApprYn eq 'W'}">승인 대기</c:when>
                              <c:otherwise>확인 불가</c:otherwise>
                            </c:choose>
                          </span>
                        </c:when>
                        <c:otherwise>
                          <span class="text-muted">해당 없음</span>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr>
                    <th>솔루션 활성 상태</th>
                    <td>
                      <c:choose>
                        <c:when test="${subscription.subsStatus eq '001'}"><span class="text-success">사용 가능</span></c:when>
                        <c:when test="${subscription.subsStatus eq '002'}"><span class="text-muted">일시 정지</span></c:when>
                        <c:when test="${subscription.subsStatus eq '003'}"><span class="text-muted">취소</span></c:when>
                        <c:when test="${subscription.subsStatus eq '004'}"><span class="text-muted">결제 대기</span></c:when>
                        <c:otherwise><span class="text-danger">사용 불가</span></c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                  <tr>
                    <th>결제 금액</th>
                    <td>
                      <c:choose>
                        <c:when test="${member.tenancy.authApprYn eq 'Y' and subscription.subsStatus ne '001'}">
                          <c:choose>
                            <c:when test="${subscription.solution.solCcCd eq '001'}">
                              <c:url var="payActionUrl" value="/payment/bussiness/tenancy" />
                              <c:set var="userType" value="tenancy"/>
                            </c:when>
                            <c:when test="${subscription.solution.solCcCd eq '002'}">
                              <c:url var="payActionUrl" value="/payment/bussiness/broker" />
                              <c:set var="userType" value="broker"/>
                            </c:when>
                            <c:otherwise>
                              <c:url var="payActionUrl" value="/payment/bussiness" />
                              <c:set var="userType" value="unknown"/>
                            </c:otherwise>
                          </c:choose>
                          <div class="payment-row">
                            ${subscription.solution.solPrice} 원
                            <form action="${payActionUrl}" method="get" style="display: inline;">
                              <button type="submit" class="btn btn-primary btn-sm">결제</button>
                            </form>
                          </div>
                        </c:when>
                        <c:when test="${(member.broker.authApprYn eq 'Y' or member.tenancy.authApprYn eq 'Y') and subscription.subsStatus eq '001'}">
                          <c:choose>
                            <c:when test="${subscription.solution.solCcCd eq '001'}">
                              <c:url var="payActionUrl" value="/payment/bussiness/tenancy" />
                            </c:when>
                            <c:when test="${subscription.solution.solCcCd eq '002'}">
                              <c:url var="payActionUrl" value="/payment/bussiness/broker" />
                            </c:when>
                            <c:otherwise>
                              <c:url var="payActionUrl" value="/payment/bussiness" />
                            </c:otherwise>
                          </c:choose>
                          <div class="inline-payment-form">
                            ${subscription.solution.solPrice} 원
                            <form action="${payActionUrl}" method="get" style="display: inline;">
                              <input type="hidden" name="solId" value="${subscription.subsId}" />
                              <button type="submit" class="btn btn-primary btn-sm">구독 변경</button>
                            </form>
                            <form action="${pageContext.request.contextPath}/subscription/cancel" method="post" style="display: inline;">
                              <input type="hidden" name="solId" value="${subscription.solution.solId}" />
                              <button type="submit" class="btn btn-outline-danger btn-sm">구독 취소</button>
                            </form>
                          </div>
                        </c:when>
                        <c:otherwise>
                          <div class="text-muted">결제 불가</div>
                        </c:otherwise>
                      </c:choose>
                    </td>
                  </tr>
                </tbody>
              </table>
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
              <div class="form-group"><label>사업자등록번호</label><div class="form-control">${member.tenancy.rentalPtyBizRegNo}</div></div>
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