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
		.account-list {
		  display: flex;
		  flex-direction: column;
		  gap: 8px;
		  margin-top: 5px;
		}
		
		.account-line {
		  display: flex;
		  align-items: center;
		  gap: 10px;
		}
		
		.account-input {
		  width: 280px;
  		  min-width: 280px;
		  box-sizing: border-box;
		  flex-grow: 1;
		  border: 1px solid #ccc;
		  border-radius: 10px;
		  padding: 10px 12px;
		  font-size: 0.9rem;
		  background-color: #f5f5f5;
		  color: #333;
		}
		.form-container {
		  display: flex;
		  gap: 30px;
		  align-items: flex-start;
		}
		
		/* 왼쪽 폼 */
		.form-box {
		  flex-grow: 1;
		  min-width: 600px;
		}
		f
		/* 오른쪽 이미지 등록 */
		.image-box {
		  width: 300px;
		  min-width: 300px;
		  border: 1px solid #ccc;
		  background: #f8f8f8;
		  padding: 10px;
		  text-align: center;
		}
		
		.image-box img {
		  width: 100%;
		  height: auto;
		  max-height: 250px;
		  object-fit: cover;
		  border-radius: 6px;
		  margin-bottom: 10px;
		}
		
		.image-box button {
		  width: 100%;
		  padding: 8px 0;
		  background: #333;
		  color: #fff;
		  border: none;
		  border-radius: 6px;
		  cursor: pointer;
		}
		.form-submit-row {
		  margin-top: 30px;
		  text-align: right;
		}
		
		.submit-btn {
		  padding: 10px 20px;
		  background-color: #3a80f6;
		  border: none;
		  color: white;
		  font-size: 1rem;
		  border-radius: 6px;
		  cursor: pointer;
		}
		
		.image-box {
		  max-width: 240px;
		  margin-top: 20px;
		}
	</style>
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
            <input type="hidden" name="delYn" value="N" />
			    <label>수납계좌</label>
			  <div class="account-list">
			    <c:forEach var="account" items="${buildingVO.accList}">
			      <div class="account-line">
			        <input type="text" class="account-input" 
			               value="${account.accBank} / ${account.accNum}" readonly />
			        <input type="checkbox" name="accNum" value="${account.accNum}"
			               <c:if test="${fn:contains(buildingVO.accNum, account.accNum)}">checked</c:if> />
			      </div>
			    </c:forEach>
			  </div>
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
                  <div class="image-box">
		  <img src="/images/sample-building.jpg" alt="건물 이미지 미리보기">
		  <button type="button">이미지 등록</button>
		</div>
          </div>
        </div>
        
       
      </div>
      
 
	    <div style="margin-top: 40px; clear: both;">
	  <button class="submit-btn" type="submit">건물 등록</button>
	</div>
      </form:form>
    </div>



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
