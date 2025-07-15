<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>결제하기</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/subscribe/payments.css" />
</head>
<body>

<div class="container">
  <div class="title">결제하기</div>
  <div class="desc">선택한 서비스에 대해 결제를 진행합니다.</div>



<!-- 🔵 회원 정보 -->
<div class="info-block">
  <p><strong>회원명:</strong> <span id="memberName">${member.mbrNm}</span></p>
  <p><strong>이메일:</strong> <span id="memberEmail">${member.mbrEmlAddr}</span></p>
  <p><strong>전화번호:</strong> <span id="memberPhone">${member.mbrTelno}</span></p>
</div>


  <!-- 🔸 솔루션 선택 -->
  <form action="${pageContext.request.contextPath}/payment/process" method="post">
    <div><strong>구독 솔루션 선택</strong></div>
<c:set var="selectedSolId" value="${solutionSubscription.solId}" />

<div class="solution-wrapper">
  <c:forEach var="sol" items="${solutionList}">
    <label class="solution-card ${sol.solId == selectedSolId ? 'selected' : ''}">
      <input type="radio"
             name="solutionCode"
             value="${sol.solId}"
             data-name="${sol.solName} 솔루션"
             data-price="${sol.solPrice}"
             data-cycle="${sol.solCycle}"
             ${sol.solId == selectedSolId ? 'checked' : ''}>
             
      <div class="title">${sol.solName}</div>
      <div class="desc">${sol.solDesc}</div>
      <div class="price">
        <fmt:formatNumber value="${sol.solPrice}" type="number" groupingUsed="true"/> 원
        <div class="price-desc">* VAT 포함</div>
      </div>
    </label>
  </c:forEach>
</div>



    <div class="summary">
      <p><strong>선택한 상품:</strong> <span id="productNameText">베이직 솔루션</span></p>
      <p><strong>가격:</strong> <span id="priceText">9,900</span> 원</p>
    </div>

<!-- 🔁 정기 결제 수단 -->
<div><strong>정기 결제 수단</strong></div>
<div class="payment-wrapper" id="billing-group">

  <!-- ✅ 선택 안 함 -->
  <label class="payment-card selected">
    <input type="radio"
           name="billingMethod"
           value=""
           data-group="BILLING"
           checked>
    <div class="icon"><i class="bi bi-slash-circle"></i></div>
    <div class="method">선택 안 함</div>
  </label>

  <c:forEach var="rcPayMethod" items="${commonCodeRcPayList}" varStatus="status">
    <label class="payment-card">
      <input type="radio"
             name="billingMethod"
             value="${rcPayMethod.codeName}"
             data-group="BILLING">
      <div class="icon">
        <c:choose>
          <c:when test="${rcPayMethod.codeValue == '001'}"><i class="bi bi-credit-card"></i></c:when>
          <c:when test="${rcPayMethod.codeValue == '002'}"><i class="bi bi-cash-stack"></i></c:when>
          <c:otherwise><i class="bi bi-question-circle"></i></c:otherwise>
        </c:choose>
      </div>
      <div class="method">${rcPayMethod.codeName}</div>
    </label>
  </c:forEach>
</div>


<!-- 🔵 일반 결제 수단 -->
<div><strong>일반 결제 수단</strong></div>
<div class="payment-wrapper" id="normal-group">

  <!-- ✅ 선택 안 함 -->
  <label class="payment-card selected">
    <input type="radio"
           name="normalMethod"
           value=""
           data-group="NORMAL"
           checked>
    <div class="icon"><i class="bi bi-slash-circle"></i></div>
    <div class="method">선택 안 함</div>
  </label>

  <c:forEach var="method" items="${commonCodePayList}" varStatus="status">
    <label class="payment-card">
      <input type="radio"
             name="normalMethod"
             value="${method.codeName}"
             data-group="NORMAL">
      <div class="icon">
        <c:choose>
          <c:when test="${method.codeValue == '001'}"><i class="bi bi-credit-card"></i></c:when>
          <c:when test="${method.codeValue == '002'}"><i class="bi bi-cash-stack"></i></c:when>
          <c:otherwise><i class="bi bi-question-circle"></i></c:otherwise>
        </c:choose>
      </div>
      <div class="method">${method.codeName}</div>
    </label>
  </c:forEach>
</div>
   <!-- 기존 버튼에서 type="submit" 제거하고 onclick 추가 -->
<button type="button" class="pay-btn" onclick="handlePayment()">결제하기</button>


  </form>
</div>

<!-- 모달 -->
<div class="modal fade" id="billingModal" tabindex="-1">
  <div class="modal-dialog modal-dialog-centered modal-lg">
    <div class="modal-content">
      <form:form id="billingForm" method="post" modelAttribute="billingCard">
        <div class="modal-header">
          <h5 class="modal-title fw-bold">카드 등록 <span class="text-muted fs-6">(정기결제용)</span></h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal"></button>
        </div>

        <div class="modal-body row">
          <!-- ✅ 카드 등록 입력 폼 -->
          <div class="col-md-8">
            <div class="mb-3">
              <label class="form-label fw-semibold">카드번호</label>
              <form:input path="cardNumber" type="text" cssClass="form-control"
                          placeholder="예: 4455 1234 5678 9010"
                          pattern="\d{15,16}" maxlength="16" required="true" />
            </div>

            <div class="row">
              <div class="col mb-3">
                <label class="form-label fw-semibold">유효년 (YY)</label>
                <form:input path="cardExpirationYear" type="text" cssClass="form-control"
                            placeholder="예: 26" pattern="\d{2}" maxlength="2" required="true" />
              </div>
              <div class="col mb-3">
                <label class="form-label fw-semibold">유효월 (MM)</label>
                <form:input path="cardExpirationMonth" type="text" cssClass="form-control"
                            placeholder="예: 07" pattern="(0[1-9]|1[0-2])" maxlength="2" required="true" />
              </div>
            </div>

            <div class="row">
              <div class="col mb-3">
                <label class="form-label fw-semibold">비밀번호 앞 2자리</label>
                <form:password path="cardPassword" cssClass="form-control"
                               placeholder="예: 12" pattern="\d{2}" maxlength="2" required="true" />
              </div>
              <div class="col mb-3">
                <label class="form-label fw-semibold">생년월일 6자리</label>
                <form:input path="customerIdentityNumber" type="text" cssClass="form-control"
                            placeholder="예: 990101" pattern="\d{6}" maxlength="6" required="true" />
              </div>
            </div>

            <form:hidden path="customerKey" value="test-cust-001" />
          </div>

          <!-- ✅ 상품 요약 정보 -->
          <div class="col-md-4 d-flex align-items-center justify-content-end">
            <div class="modal-summary-card">
              <div>
                <div class="label">상품명</div>
                <div class="value" id="modalSolName">-</div>
              </div>
              <div>
                <div class="label">결제 금액</div>
                <div class="price" id="modalSolPrice">-</div>
              </div>
              <div>
                <div class="label">결제 주기</div>
                <div class="value">매 <span id="modalSolCycle">-</span>일마다 자동결제</div>
              </div>
            </div>
          </div>
        </div>

        <div class="modal-footer">
          <button type="submit" class="submit-btn">등록하기</button>
        </div>
      </form:form>
    </div>
  </div>
</div>


<script src="https://js.tosspayments.com/v1"></script>
<script src="${pageContext.request.contextPath}/app/js/main/payments.js"></script>


</body>
</html>
