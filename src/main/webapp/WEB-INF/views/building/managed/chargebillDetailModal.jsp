<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<title>관리비 상세정보</title>
<link rel="stylesheet" href="/app/css/building/chargeBill/chargebillDetailModal.css">

<div class="chargebill-modal">
  <div class="charge-wrap">
    <div class="resident-header">
      <h2 class="resident-title">
        ${cbhDTO.bldgNm}${cbhDTO.unitRoom}호 ${cbhDTO.mbrNm}님
        ${fn:substring(cbhDTO.chgbillChargeMonth, 0, 4)}년
        ${fn:substring(cbhDTO.chgbillChargeMonth, 4, 6)}월 청구
      </h2>
      <button class="btn-close-modal" onclick="closeModal()">닫기</button>
    </div>

    <!-- 기본 청구 정보 -->
    <div class="charge-section">
      <div class="charge-row">
        <div class="charge-item">
          <label>청구일</label>
          <fmt:parseDate value="${cbhDTO.chgbillDate}" pattern="yyyyMMdd" var="parsedIssueDate" />
          <span><fmt:formatDate value="${parsedIssueDate}" pattern="yyyy년 M월 d일" /></span>
        </div>
        <div class="charge-item">
          <label>납기일</label>
          <fmt:parseDate value="${cbhDTO.chgbillDueDate}" pattern="yyyyMMdd" var="parsedDueDate" />
          <span><fmt:formatDate value="${parsedDueDate}" pattern="yyyy년 M월 d일" /></span>
        </div>
      </div>

      <div class="charge-row">
        <div class="charge-item">
          <label>납부일</label>
          <c:choose>
            <c:when test="${empty cbhDTO.chgbillPaidDate or cbhDTO.chgbillPaidDate == '0'}">
              <span></span>
            </c:when>
            <c:otherwise>
              <fmt:parseDate value="${cbhDTO.chgbillPaidDate}" pattern="yyyyMMdd" var="parsedPaidDate" />
              <span><fmt:formatDate value="${parsedPaidDate}" pattern="yyyy년 M월 d일" /></span>
            </c:otherwise>
          </c:choose>
        </div>
        <div class="charge-item">
          <label>납부상태</label>
          <span>
            <c:choose>
              <c:when test="${cbhDTO.chgbillStatus == '001'}">미납</c:when>
              <c:when test="${cbhDTO.chgbillStatus == '002'}">완납</c:when>
              <c:when test="${cbhDTO.chgbillStatus == '004'}">연체</c:when>
              <c:otherwise>기타</c:otherwise>
            </c:choose>
          </span>
        </div>
      </div>

      <div class="charge-row">
        <div class="charge-item">
          <label>청구계좌</label><span>${cbhDTO.chgbillAccNum}</span>
        </div>
        <div class="charge-item">
          <label>총 청구액</label><span>${cbhDTO.chgbillAmount}원</span>
        </div>
      </div>
    </div>

    <!-- 공용 관리비 항목 -->
    <div class="charge-section">
      <h3>공용 관리비 항목</h3>
      <c:set var="count" value="0" />
      <div class="charge-row">
        <c:forEach var="fee" items="${managementFee}" varStatus="vs">
          <div class="charge-item">
            <label>
              <c:choose>
                <c:when test="${fee.intManFeeCd == '001'}">청소비</c:when>
                <c:when test="${fee.intManFeeCd == '002'}">승강기 유지비</c:when>
                <c:when test="${fee.intManFeeCd == '003'}">공용 전기료</c:when>
                <c:when test="${fee.intManFeeCd == '004'}">공용 수도료</c:when>
                <c:when test="${fee.intManFeeCd == '005'}">일반 운영비</c:when>
                <c:when test="${fee.intManFeeCd == '006'}">경비 인건비</c:when>
                <c:when test="${fee.intManFeeCd == '007'}">방역 소독비</c:when>
                <c:when test="${fee.intManFeeCd == '008'}">소모품비</c:when>
                <c:when test="${fee.intManFeeCd == '009'}">소방 설비 유지비</c:when>
                <c:when test="${fee.intManFeeCd == '010'}">보안 시스템 유지비</c:when>
                <c:otherwise>기타 항목</c:otherwise>
              </c:choose>
            </label>
            <span>${fee.intgFeeAmount}원</span>
          </div>
          <c:set var="count" value="${count + 1}" />
          <c:if test="${count % 2 == 0 && !vs.last}">
            </div><div class="charge-row">
          </c:if>
        </c:forEach>
      </div>
    </div>

    <!-- 에너지 사용 내역 -->
    <div class="charge-section">
      <h3>에너지 사용 내역</h3>
      <c:forEach var="energy" items="${energyUsage}">
        <div class="charge-row">
          <div class="charge-item">
            <label>
              <c:choose>
                <c:when test="${energy.dumComp == '001'}">전기 사용량</c:when>
                <c:when test="${energy.dumComp == '002'}">수도 사용량</c:when>
                <c:when test="${energy.dumComp == '003'}">가스 사용량</c:when>
                <c:otherwise>기타 사용량</c:otherwise>
              </c:choose>
            </label>
            <span>${energy.totalEnergyUsageQty}
              <c:choose>
                <c:when test="${energy.dumComp == '001'}">kWh</c:when>
                <c:when test="${energy.dumComp == '002'}">㎥</c:when>
                <c:when test="${energy.dumComp == '003'}">㎥</c:when>
              </c:choose>
            </span>
          </div>
          <div class="charge-item">
            <label>청구금액</label>
            <span>${energy.totalEnergyChargeAmt}원</span>
          </div>
        </div>
      </c:forEach>
    </div>

    <!-- 청구 설명 -->
    <div class="charge-section">
      <label>청구 설명</label>
      <div class="desc-box">
        <span>${cbhDTO.chgbillDesc}</span>
      </div>
    </div>

  </div> 
</div> 
