<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@taglib uri="jakarta.tags.functions" prefix="fn" %>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>신규 건물 등록</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedAdd.css">
  <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
 	<style>
.form-wrapper {
  display: flex;
  flex-direction: column;
  gap: 20px;
  max-width: 700px;
  margin: 0 auto;
}

.form-row {
  display: flex;
  flex-direction: column;
}

.inline-group {
  display: flex;
  gap: 10px;
  align-items: center;
}

.image-box {
  border: 1px solid #ccc;
  padding: 10px;
  text-align: center;
  border-radius: 6px;
  background: #f9f9f9;
  max-width: 240px;
  margin: 0 auto;
}
</style>
</head>


<body>

<body>
<h2>신규 건물 등록</h2>

<form:form modelAttribute="buildingVO" method="post" action="/building/managed/add">
  <form:hidden path="rentalPtyId" />
  <form:hidden path="delYn" value="N" />

  <div class="form-wrapper">
    <div class="form-row">
      <label for="bldgNm">건물 이름</label>
      <form:input path="bldgNm" id="bldgNm" placeholder="입력해주세요" />
    </div>

    <c:if test="${not empty buildingVO.rentalPtyId}">
      <div class="form-row">
        <label>내 매물에서 불러오기</label>
        <div class="inline-group">
          <select class="form-select" id="listingSelectBox">
            <option value="">선택</option>
            <c:forEach var="listing" items="${listingList}">
              <option value="${listing.lstgId}">
                ${listing.lstgNm} - ${listing.lstgAdd} ${listing.lstgAdd2}
              </option>
            </c:forEach>
          </select>
          <button type="button" onclick="fillListingInfo()">불러오기</button>
        </div>
      </div>
    </c:if>

    <div class="form-row">
      <label for="bldgZipNo">우편번호</label>
      <div class="inline-group">
        <form:input path="bldgZipNo" id="bldgZipNo" readonly="true" />
        <button type="button" onclick="execDaumPostcode()">주소 찾기</button>
      </div>
    </div>

    <div class="form-row">
      <label for="bldgAddr">기본주소</label>
      <form:input path="bldgAddr" id="bldgAddr" readonly="true" />
    </div>

    <div class="form-row">
      <label for="bldgDtlAddr">상세주소</label>
      <form:input path="bldgDtlAddr" id="bldgDtlAddr" />
    </div>

    <div class="form-row">
      <label>수납계좌</label>
      <div class="account-list">
        <c:forEach var="account" items="${buildingVO.accList}">
          <div class="account-line">
            <input type="text" class="account-input" value="${account.accBank} / ${account.accNum}" readonly />
            <input type="checkbox" name="accNum" value="${account.accNum}"
              <c:if test="${fn:contains(buildingVO.accNum, account.accNum)}">checked</c:if> />
          </div>
        </c:forEach>
      </div>
    </div>

    <div class="form-row">
      <label for="bldgTypeCode">건물 유형</label>
		<form:select path="bldgTypeCode" id="bldgTypeCode">
		  <option value="">선택하세요</option>
		  <c:forEach var="code" items="${bldgTypeList}">
		    <option value="${code.codeValue}"
		      <c:if test="${code.codeValue == buildingVO.bldgTypeCode}">selected</c:if>>
		      ${code.codeName}
		    </option>
		  </c:forEach>
		</form:select>
    </div>

    <div class="form-row">
      <label for="bldgCmpltnDt">준공일</label>
      <form:input path="bldgCmpltnDt" type="date" id="bldgCmpltnDt" />
    </div>

   
    <div class="form-row">
      <label for="bldgGrossArea">공급면적</label>
      <form:input path="bldgGrossArea" id="bldgGrossArea" />
    </div>

    <!-- 이미지 -->
    <div class="form-row">
      <div class="image-box">
        <img src="/images/sample-building.jpg" alt="건물 이미지 미리보기" />
        <button type="button">이미지 등록</button>
      </div>
    </div>

    <div class="form-row" style="text-align: right;">
      <button type="submit" class="submit-btn">상세정보 입력</button>
    </div>
  </div>
</form:form>


<script src="/app/js/building/managed/managedAdd.js" defer></script>

</body>
</html>
