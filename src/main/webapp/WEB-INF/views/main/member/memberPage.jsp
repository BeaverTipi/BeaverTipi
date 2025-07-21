<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt"%>

<c:set var="defaultTabId" value="broker-info" />
<c:if test="${not empty member.tenancy and empty member.broker}">
	<c:set var="defaultTabId" value="tenancy-info" />
</c:if>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>회원 정보</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/member.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
<style>
  .modal {
    position: fixed;
    top: 0; left: 0;
    width: 100%; height: 100%;
    background-color: rgba(0, 0, 0, 0.4);
    display: flex; justify-content: center; align-items: center;
    z-index: 1000;
  }
  .modal-content {
    background-color: #fff;
    padding: 20px 30px;
    border-radius: 10px;
    box-shadow: 0 5px 15px rgba(0,0,0,0.3);
  }
  .hidden {
    display: none;
  }
</style>

</head>
<body>
	<div class="register-wrapper" data-default-tab="${defaultTabId}">
		<div class="signup-container">
			<div
				style="display: flex; justify-content: space-between; align-items: center;">
				<h2 class="signup-title">회원 정보</h2>
				<button type="button" class="btn-primary" onclick="showPasswordModal()">수정하기</button>
				
			</div>

			<div class="form-group">
				<label>프로필 이미지</label>
				<div style="text-align: center;">
					<img id="previewImage"
						src="${empty member.mbrProfilImage ? '/volt/assets/img/images/beaver.png' : member.memberFile.filePathUrl}"
						alt="프로필 이미지" class="profile-image-preview" />
				</div>
			</div>

			<div class="form-group-wrapper">
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
					<label>주소</label>
					<div class="form-control">${member.mbrBasicAddr}
						${member.mbrDetailAddr}</div>
				</div>
				<div class="form-group">
					<label>가입 경로</label>
					<div class="form-control">
						<c:choose>
							<c:when test="${logInfo eq 'LOCAL'}">일반회원</c:when>
							<c:when test="${logInfo eq 'KAKAO'}">카카오</c:when>
							<c:when test="${logInfo eq 'GOOGLE'}">구그룹</c:when>
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
			</div>
			<div class="subscription-row">
				<!-- 공인중개사 승인 정보 카드 -->
				<c:if test="${not empty member.broker}">
  <div class="subscription-card">
    <div class="subscription-header">
      <h5 class="subscription-title">공인중개사 승인 정보</h5>
    </div>
    <div class="subscription-content">
      <ul class="subscription-info-list">
        <li>
          <span class="label">공인중개사 승인 상태:</span>
          <c:choose>
            <c:when test="${member.broker.authApprYn eq 'Y'}"><span class="value text-success">✅ 승인</span></c:when>
            <c:when test="${member.broker.authApprYn eq 'N'}"><span class="value text-danger">❌ 거절</span></c:when>
            <c:when test="${member.broker.authApprYn eq 'W'}"><span class="value text-muted">⏳ 대기</span></c:when>
            <c:otherwise><span class="value text-muted">❓ 미확인</span></c:otherwise>
          </c:choose>
        </li>

        <c:if test="${not empty member.broker.authApprDt}">
          <fmt:parseDate value="${member.broker.authApprDt}" var="brokerParsedDate" pattern="yyyyMMdd" />
          <li>
            <span class="label">승인 일시:</span>
            <span class="value text-muted">
              <fmt:formatDate value="${brokerParsedDate}" pattern="yyyy년 MM월 dd일" />
            </span>
          </li>
        </c:if>
      </ul>
    </div>

    <div class="subscription-footer">
      <c:choose>
        <c:when test="${member.broker.authApprYn eq 'Y' and showPaymentBtnBroker}">
          <form action="/payment/business/broker" method="get">
            <button type="submit" class="btn btn-primary">결제하기</button>
          </form>
        </c:when>
        <c:otherwise>
          <div class="empty-button-space"><p>구독 변경만 가능합니다.</p></div>
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
							<ul class="subscription-info-list">
								<li><span class="label">임대인 승인 상태:</span> <c:choose>
										<c:when test="${member.tenancy.authApprYn eq 'Y'}">
											<span class="value text-success">✅ 승인</span>
										</c:when>
										<c:when test="${member.tenancy.authApprYn eq 'N'}">
											<span class="value text-danger">❌ 거절</span>
										</c:when>
										<c:when test="${member.tenancy.authApprYn eq 'W'}">
											<span class="value text-muted">⏳ 대기</span>
										</c:when>
										<c:otherwise>
											<span class="value text-muted">❓ 미확인</span>
										</c:otherwise>
									</c:choose></li>
								<c:if test="${not empty member.tenancy.authApprDt}">
									<fmt:parseDate value="${member.tenancy.authApprDt}"
										var="tenancyParsedDate" pattern="yyyyMMdd" />
									<li><span class="label">승인 일시:</span><span class="value text-muted">
											<fmt:formatDate value="${tenancyParsedDate}"
												pattern="yyyy년 MM월 dd일" />
									</span></li>
								</c:if>
							</ul>
						</div>
						<!-- 버튼 영역을 명확히 분리 -->
						<div class="subscription-footer">
							<c:choose>
								<c:when
									test="${member.tenancy.authApprYn eq 'Y' and showPaymentBtnTenancy}">
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
								<li><span class="label">구독 타이틀:</span> <span class="value">${sub.solution.solName}</span>
								</li>
								<li><span class="label">상태:</span> <c:choose>
										<c:when test="${sub.subsStatus eq '001'}">
											<span class="value text-success">✅ 사용 가능</span>
										</c:when>
										<c:when test="${sub.subsStatus eq '002'}">
											<span class="value text-muted">⏸ 일시 정지</span>
										</c:when>
										<c:when test="${sub.subsStatus eq '003'}">
											<span class="value text-muted">❌ 취소</span>
										</c:when>
										<c:when test="${sub.subsStatus eq '004'}">
											<span class="value text-muted">⏳ 결제 대기</span>
										</c:when>
										<c:otherwise>
											<span class="value text-danger">⛔ 사용 불가</span>
										</c:otherwise>
									</c:choose></li>
								<c:if test="${not empty sub.subsStartedAt}">
									<li><span class="label">구독 시작일:</span> <span
										class="value text-muted">${ sub.subsStartedAt}
									</span></li>
								</c:if>
							</ul>
						</div>

						<c:if test="${sub.subsStatus eq '001'}">
							<div class="subscription-footer">
								<button type="button"
									  class="btn-primary-custom"
									  onclick="storeSubscriptionInfoAndGo('${sub.solution.solCcCd eq '001' ? 'tenancy' : 'broker'}', '${sub.solId}', '${sub.subsId}')">
									  구독 변경
									</button>

								<form
									action="${pageContext.request.contextPath}/subscription/cancel"
									method="post" style="display: inline;">
									<input type="hidden" name="subsId" value="${sub.subsId}" />
									<button type="submit" class="btn-outline-danger-custom">구독
										취소</button>
								</form>
							</div>
						</c:if>
					</div>
				</c:forEach>
			</div>



			<c:if test="${not empty member.broker or not empty member.tenancy}">
				<div class="tab-buttons">
					<c:if test="${not empty member.broker}">
						<button class="tab-button" onclick="openTab('broker-info')">공인중개사
							정보</button>
					</c:if>
					<c:if test="${not empty member.tenancy}">
						<button class="tab-button" onclick="openTab('tenancy-info')">임대인
							정보</button>
					</c:if>
				</div>

				<c:if test="${not empty member.broker}">
					<div id="broker-info" class="tab-content">
						<div class="form-group-wrapper">
							<div class="form-group">
								<label>사무소 이름</label>
								<div class="form-control">${member.broker.brokNm}</div>
							</div>
							<div class="form-group">
								<label>사업자등록번호</label>
								<div class="form-control">${member.broker.brokRegNo}</div>
							</div>
							<div class="form-group">
								<label>자격증 번호</label>
								<div class="form-control">${member.broker.crtfNo}</div>
							</div>
							<div class="form-group">
								<label>대표자</label>
								<div class="form-control">${member.broker.reprNm}</div>
							</div>
							<div class="form-group">
								<label>전화번호</label>
								<div class="form-control">${member.broker.reprTelNo}</div>
							</div>
							<div class="form-group">
								<label>등록 일시</label>
								<div class="form-control">${member.broker.regDtm}</div>
							</div>
							<div class="form-group">
								<label>주소</label>
								<div class="form-control">${member.broker.brokAddr1}
									${member.broker.brokAddr2}</div>
							</div>
						</div>
					</div>
				</c:if>

				<c:if test="${not empty member.tenancy}">
					<div id="tenancy-info" class="tab-content">
						<div class="form-group-wrapper">
							<div class="form-group">
								<label>등록된 건물 수</label>
								<div class="form-control">${member.tenancy.rentalPtyRegBldgCnt}</div>
							</div>
							<div class="form-group">
								<label>사업자등록번호</label>
								<div class="form-control">${member.tenancy.rentalPtyBizRegNo}</div>
							</div>
							<div class="form-group">
								<label>임대유형</label>
								<div class="form-control">${member.tenancy.lsrTypeGroupCd}</div>
							</div>
							<div class="form-group">
								<label>임대인 유형</label>
								<div class="form-control">${member.tenancy.lsrYnTypeCd}</div>
							</div>
						</div>
					</div>
				</c:if>
			</c:if>
		</div>
	</div>

<!-- 비밀번호 확인 모달 -->
<div class="modal fade" id="passwordModal" tabindex="-1" aria-labelledby="passwordModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-dialog-centered">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="passwordModalLabel">비밀번호 확인</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
      </div>
      <div class="modal-body">
        <input type="password" id="passwordCheckInput" class="form-control" placeholder="비밀번호를 입력하세요" />
        <div id="passwordCheckError" class="text-danger mt-2 d-none">비밀번호가 일치하지 않습니다.</div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-primary" id="confirmPasswordBtn">확인</button>
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
      </div>
    </div>
  </div>
</div>





	
	<script
		src="${pageContext.request.contextPath}/app/js/main/member/memberPage.js"></script>
</body>
</html>
