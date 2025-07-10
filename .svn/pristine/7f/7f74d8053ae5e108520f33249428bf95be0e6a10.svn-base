<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>결제 확인</title>
</head>
<body>
  <div class="container">
    <h2>결제 내역 확인</h2>
    <div class="form-group">
      <label>회원 이름</label>
      <div class="form-control">${member.mbrNm}</div>
    </div>

    <div class="form-group">
      <label>선택한 솔루션</label>
      <div class="form-control">${solution.solName} (${solution.solId})</div>
    </div>

    <div class="form-group">
      <label>결제 수단</label>
      <div class="form-control">${paymentMethod}</div>
    </div>

    <div class="form-group">
      <label>요금</label>
      <div class="form-control">${solution.price} 원</div>
    </div>

    <form action="${pageContext.request.contextPath}/billing/confirm" method="post">
      <input type="hidden" name="solution" value="${solution.solId}">
      <input type="hidden" name="paymentMethod" value="${paymentMethod}">
      <button type="submit" class="btn btn-primary">결제 확정</button>
    </form>
  </div>
</body>
<script src="/app/js/main/payments"></script>
</html>
