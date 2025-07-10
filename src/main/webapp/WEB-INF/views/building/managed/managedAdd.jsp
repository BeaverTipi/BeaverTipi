<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="fn" uri="jakarta.tags.functions" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>신규 건물 등록</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedAdd.css">
  <!-- 카카오 -->
  <script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
</head>

<body>

<h2>신규 건물 등록</h2>

<form:form modelAttribute="buildingVO" method="post" action="/building/managed/add">
  <form:hidden path="rentalPtyId" />

  <div class="form-container">
    <div class="form-box">
      <div class="form-split">

        <!-- 왼쪽 -->
        <div class="form-half">
          <div class="form-row">
            <label for="bldgNm">건물 이름</label>
            <form:input path="bldgNm" id="bldgNm" placeholder="입력해주세요" />
          </div>

          <div class="form-row">
            <label for="bldgZipNo">우편번호</label>
            <div class="zipcode-box">
              <form:input path="bldgZipNo" id="postcode" placeholder="우편번호" readonly="true" />
              <button type="button" onclick="execDaumPostcode()">주소 찾기</button>
            </div>
          </div>

          <div class="form-row">
            <label for="bldgAddr">기본주소</label>
            <form:input path="bldgAddr" id="address" placeholder="기본 주소" readonly="true" />
          </div>

          <div class="form-row">
            <label for="bldgDtlAddr">상세주소</label>
            <form:input path="bldgDtlAddr" id="detailAddress" placeholder="상세 주소" />
          </div>

          <div class="form-row">
            <input type="hidden" name="delYn" value="N">
            <label for="accNum">수납계좌</label>
            <select name="accNum" id="accNum" class="form-control" required>
              <c:choose>
                <c:when test="${empty buildingVO.accNum}">
                  <option value="" selected disabled>계좌를 선택하세요</option>
                </c:when>
                <c:otherwise>
                  <option value="" disabled>계좌를 선택하세요</option>
                </c:otherwise>
              </c:choose>

              <c:if test="${not empty buildingVO.accList}">
                <c:forEach var="account" items="${buildingVO.accList}">
                  <c:choose>
                    <c:when test="${buildingVO.accNum == account.accNum}">
                      <option value="${account.accNum}" selected>
                        ${account.accBank} / ${account.accNum}
                      </option>
                    </c:when>
                    <c:otherwise>
                      <option value="${account.accNum}">
                        ${account.accBank} / ${account.accNum}
                      </option>
                    </c:otherwise>
                  </c:choose>
                </c:forEach>
              </c:if>
            </select>
          </div>
        </div>

        <!-- 오른쪽 -->
        <div class="form-half">
          <div class="form-row">
            <label for="bldgTypeCode">건물 유형</label>
            <form:select path="bldgTypeCode" id="bldgTypeCode">
              <option value="">선택하세요</option>
              <option value="001">아파트</option>
              <option value="003">오피스텔</option>
              <option value="005">상가</option>
              <option value="008">기타</option>
            </form:select>
          </div>

          <div class="form-row">
            <label for="bldgCmpltnDt">준공일</label>
            <form:input path="bldgCmpltnDt" type="date" id="bldgCmpltnDt" />
          </div>

          <div class="form-row">
            <label for="bldgFlrCnt">층 수</label>
            <form:input path="bldgFlrCnt" type="number" id="bldgFlrCnt" placeholder="입력해주세요" />
          </div>

          <div class="form-row">
            <label for="bldgGrossArea">연 면적</label>
            <form:input path="bldgGrossArea" id="bldgGrossArea" placeholder="㎡ 단위" />
          </div>

          <div class="form-row">
            <label for="bldgUnitCnt">호실 수</label>
            <form:input path="bldgUnitCnt" type="number" id="bldgUnitCnt" placeholder="입력해주세요" />
          </div>
        </div>
      </div>
    </div>

    <!-- ✅ 이미지 등록 -->
    <div class="image-box">
      <img src="/images/sample-building.jpg" alt="건물 이미지 미리보기">
      <button type="button">이미지 등록</button>
    </div>
  </div>

  <!-- 하단 등록 버튼 -->
  <div style="margin-top: 40px; clear: both;">
    <button class="submit-btn" type="submit">건물 등록</button>
  </div>
</form:form>

<script src="/app/js/building/managed/managedAdd.js"></script>
<script>
function execDaumPostcode() {
  new daum.Postcode({
    oncomplete: function(data) {
      document.querySelector("#postcode").value = data.zonecode;
      document.querySelector("#address").value = data.address;
      document.querySelector("#detailAddress").focus();
    }
  }).open();
}
</script>
</body>
</html>
