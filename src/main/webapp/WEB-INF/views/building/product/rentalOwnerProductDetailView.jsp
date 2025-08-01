<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<link rel="stylesheet" href="/app/css/building/product/listingDetailQuickView.css">

<div class="quick-detail-outer">
  <div class="quick-detail-head">
    <div style="flex:1 1 0;">
      <div class="quick-detail-title">${listingVO.lstgNm}</div>
      <div class="quick-detail-address">
        ${listingVO.lstgAddr}
        <c:if test="${not empty listingVO.lstgImgPath}">
          <img src="${listingVO.lstgImgPath}" class="quick-detail-img" alt="매물사진">
        </c:if>
      </div>
    </div>
    <div class="quick-unit-select">
      <label for="roomSelect">호실</label>
      <select id="roomSelect" name="unitId">
        <c:forEach var="unit" items="${unitList}">
          <option value="${unit.unitId}" <c:if test="${unit.unitId eq selectedUnitId}">selected</c:if>>
            ${unit.unitFlrNo}층 ${unit.unitRoom}호
          </option>
        </c:forEach>
      </select>
    </div>
  </div>

  <!-- 동적 테이블: 헤더/바디 모두 JS로 제어 -->
  <table class="table quick-detail-table text-center resident-table">
    <thead id="listingTableHead"></thead>
    <tbody id="listingDetailTbody"></tbody>
  </table>
</div>
<script src="/app/js/building/product/rentalOwnerProductDetailView.js"></script>

<!-- JS는 별도 파일 or 바로 아래 붙여도 됨 -->

