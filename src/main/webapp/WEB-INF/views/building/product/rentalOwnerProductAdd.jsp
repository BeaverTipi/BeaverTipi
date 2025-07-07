<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>매물 등록</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
</head>
<script>
  function toggleTradeFields() {
    const tradeType = document.querySelector("select[name='lstgTrdTypeCode']").value;

    const deposit = document.getElementById("depositField").closest(".col-md-4");
    const price = document.getElementById("priceField").closest(".col-md-4");
    const mngFee = document.getElementById("mngFeeField").closest(".col-md-4");

    // 초기화
    deposit.style.display = "block";
    price.style.display = "block";
    mngFee.style.display = "block";

    if (tradeType === "RENT") {
      // 월세: 보증금 + 월세 + 관리비
      deposit.style.display = "block";
      price.style.display = "block";
      mngFee.style.display = "block";
    } else if (tradeType === "LEASE") {
      // 전세: 보증금 + 관리비
      deposit.style.display = "block";
      price.style.display = "none";
      mngFee.style.display = "block";
    } else if (tradeType === "SALE") {
      // 매매: 매매가만
      deposit.style.display = "none";
      price.style.display = "block";
      mngFee.style.display = "none";
    }
  }

  // onload 시에도 실행
  window.addEventListener('DOMContentLoaded', toggleTradeFields);
</script>
<body>
<div class="container">
  <h1 class="text-center">매물등록</h1>
  <form action="${pageContext.request.contextPath}/building/product/add" method="post" enctype="multipart/form-data">

    <!-- 매물 정보 -->
    <fieldset>
      <legend>매물 정보</legend>

      <!-- 대분류 -->
      <div class="mb-3">
        <label class="form-label">매물유형 *</label><br>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeGroupCd" value="VILLA" checked>
          <label class="form-check-label">주택/빌라</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeGroupCd" value="OFFICETEL">
          <label class="form-check-label">오피스텔</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeGroupCd" value="APT">
          <label class="form-check-label">아파트</label>
        </div>
      </div>

      <!-- 소분류 -->
      <div class="mb-3">
        <label class="form-label">소분류</label><br>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="ONEROOM" checked>
          <label class="form-check-label">원룸/투룸/다세대</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="SINGLE">
          <label class="form-check-label">단독주택</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="MANSION">
          <label class="form-check-label">다가구주택</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="COMMERCIAL">
          <label class="form-check-label">상가주택</label>
        </div>
      </div>

      <!-- 주소 -->
      <div class="row mb-3">
        <label class="form-label col-sm-2">주소 검색</label>
        <div class="col-sm-8">
          <input type="text" class="form-control" name="addrRoad" id="address" placeholder="예) 번동 10-1, 강북구 번동" readonly>
        </div>
        <div class="col-sm-2">
          <button type="button" class="btn btn-dark w-100" onclick="execDaumPostcode()">검색</button>
        </div>
      </div>

      <div class="row mb-3">
        <label class="form-label col-sm-2">상세 주소</label>
        <div class="col-sm-10">
          <input type="text" class="form-control" id="detailAddress" name="addrDetail" placeholder="상세 주소 입력">
        </div>
      </div>
      <input type="hidden" id="postcode" name="addrPostcode">

      <div class="row mb-3">
        <label class="form-label col-sm-2">매물 주소 *</label>
        <div class="col-sm-3"><input type="text" class="form-control" name="addrRoad" placeholder="도로명"></div>
        <div class="col-sm-3"><input type="text" class="form-control" name="addrBuilding" placeholder="건물번호"></div>
        <div class="col-sm-3"><input type="text" class="form-control" name="addrUnitNumber" placeholder="호수"></div>
      </div>

      <!-- 면적 -->
      <div class="row mb-3">
        <label class="form-label col-sm-2">매물 크기</label>
        <div class="col-sm-3"><input type="text" class="form-control" name="lstgXuar" placeholder="공급면적(평)"></div>
        <div class="col-sm-3"><input type="text" class="form-control" name="lstgCmar" placeholder="전용면적(m²)"></div>
      </div>

      <!-- 방 -->
      <div class="mb-3">
        <label class="form-label">방 정보</label>
        <div class="d-flex align-items-center flex-wrap gap-4">
          <div class="form-check d-flex align-items-center mb-0">
            <span class="me-2">방 수</span>
            <input type="number" class="form-control" name="lstgRoom" style="width: 80px;">
          </div>
          <div class="form-check d-flex align-items-center mb-0">
            <input class="form-check-input" type="checkbox" name="roomFeature" value="신축">
            <label class="form-check-label ms-1 mb-0">신축</label>
          </div>
          <div class="form-check d-flex align-items-center mb-0">
            <input class="form-check-input" type="checkbox" name="roomFeature" value="리모델링">
            <label class="form-check-label ms-1 mb-0">리모델링</label>
          </div>
          <div class="form-check d-flex align-items-center mb-0">
            <input class="form-check-input" type="radio" name="roomType" value="오픈형">
            <label class="form-check-label ms-1 mb-0">오픈형</label>
          </div>
          <div class="form-check d-flex align-items-center mb-0">
            <input class="form-check-input" type="radio" name="roomType" value="분리형">
            <label class="form-check-label ms-1 mb-0">분리형</label>
          </div>
        </div>
      </div>
    </fieldset>
    <!-- 거래 정보 -->
		<fieldset>
		  <legend>거래 정보</legend>
		  <div class="row g-3">
		    <div class="col-md-4">
		      <label class="form-label">매물 상태</label>
		      <select class="form-select" name="lstgStatCode">
		        <option value="ACTIVE">활성</option>
		        <option value="CONTRACTED">계약완료</option>
		        <option value="CLOSED">비활성</option>
		      </select>
		    </div>
		
		    <div class="col-md-4">
		      <label class="form-label">상품 구분</label>
		     <select class="form-select" name="lstgTrdTypeCode" onchange="toggleTradeFields()">
			  <option value="RENT">월세</option>
			  <option value="LEASE">전세</option>
			  <option value="SALE">매매</option>
			</select>
		     
		    </div>
		
		    <div class="col-md-4">
		      <label class="form-label">거래 유형</label>
		      <select class="form-select" name="lstgTrdTypeCode">
		        <option value="RENT">월세</option>
		        <option value="LEASE">전세</option>
		        <option value="SALE">매매</option>
		      </select>
		    </div>
		
		    <div class="col-md-4">
		      <label class="form-label">거래 유형 그룹</label>
		      <select class="form-select" name="lstgTrdTypeGroupCd">
		        <option value="LEASE">임대</option>
		        <option value="SALE">매매</option>
		      </select>
		    </div>
		
		    <div class="col-md-4">
		      <label class="form-label">보증금</label>
		      <input type="number" class="form-control" name="lstgDepositAmount" min="0" placeholder="보증금">
		    </div>
		
		    <div class="col-md-4">
		      <label class="form-label">월세/매매가</label>
		      <input type="number" class="form-control" name="lstgPrice" min="0" placeholder="월세 or 매매가">
		    </div>
		
		    <div class="col-md-4">
		      <label class="form-label">관리비</label>
		      <input type="number" class="form-control" name="lstgMontMngFee" min="0" placeholder="관리비">
		    </div>
		  </div>
		</fieldset>
        <!-- 추가 정보 -->
    <fieldset>
      <legend>추가 정보</legend>
      <div class="row g-3">
        <div class="col-md-6">
          <label class="form-label">총 층 수</label>
          <input class="form-control" type="number" name="floorAll">
        </div>
        <div class="col-md-6">
          <label class="form-label">해당 층 수</label>
          <input class="form-control" type="number" name="floor">
        </div>
        <div class="col-md-6">
          <label class="form-label">욕실 수 *</label>
          <input class="form-control" type="number" name="lstgBath">
        </div>
        <div class="col-md-6">
          <label class="form-label">주차 가능 여부 *</label>
          <div class="parking-wrap">
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="parkingYn" value="Y">
              <label class="form-check-label">가능</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="parkingYn" value="N">
              <label class="form-check-label">불가능</label>
            </div>
            <input type="number" class="form-control" name="parkingCount" placeholder="주차 대 수" style="max-width: 200px;">
          </div>
        </div>
      </div>
    </fieldset>

    <!-- 시설 정보 -->
    <fieldset>
      <legend>시설 정보</legend>

      <label class="form-label d-block">난방 시설</label>
      <c:forEach var="heat" items="${['개별난방','중앙난방','지역난방']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="heating" value="${heat}">
          <label class="form-check-label">${heat}</label>
        </div>
      </c:forEach>

      <label class="form-label d-block mt-3">냉방 시설</label>
      <c:forEach var="cool" items="${['벽걸이형','스탠드형','천장형']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="coolingFacility" value="${cool}">
          <label class="form-check-label">${cool}</label>
        </div>
      </c:forEach>

      <label class="form-label d-block mt-3">생활 시설</label>
      <c:forEach var="life" items="${['침대','책상','옷장','식탁','소파','신발장','냉장고','세탁기','건조기','식기세척기','가스레인지','인덕션',
      									'전자레인지','가스오븐','TV','붙박이장']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="lifeFacility" value="${life}">
          <label class="form-check-label">${life}</label>
        </div>
      </c:forEach>

      <label class="form-label d-block mt-3">보안 시설</label>
      <c:forEach var="security" items="${['CCTV','비디오폰','경비원','인터폰','카드키','사설경비','현관보안','방범창']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="securityFacility" value="${security}">
          <label class="form-check-label">${security}</label>
        </div>
      </c:forEach>

      <label class="form-label d-block mt-3">기타 시설</label>
      <c:forEach var="etc" items="${['화재경보기','테라스','베란다','마당','무인택배함']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="etcFacility" value="${etc}">
          <label class="form-check-label">${etc}</label>
        </div>
      </c:forEach>
    </fieldset>

    <!-- 사진 등록 -->
    <fieldset class="mb-4">
      <legend>사진 등록</legend>
      <div class="mb-3 d-flex align-items-center gap-3">
        <label class="form-label mb-0">일반 사진 *</label>
        <input type="file" class="form-control w-auto" name="imageUpload" accept="image/*" multiple>
        <button type="button" class="btn btn-outline-primary btn-sm">+ 사진 추가</button>
      </div>
    </fieldset>

    <!-- 상세 설명 -->
    <fieldset class="mb-4">
      <legend>상세 설명</legend>
      <div class="mb-3">
        <label for="title" class="form-label">제목 *</label>
        <input type="text" class="form-control" name="lstgNm" id="title" maxlength="40">
      </div>
      <div class="mb-3">
        <label for="description" class="form-label">상세설명</label>
        <textarea class="form-control" name="lstgDtlDesc" id="description" rows="6" maxlength="1000"></textarea>
      </div>
    </fieldset>
   
    <div class="text-center mb-5">
      <button type="submit" class="btn btn-primary btn-lg">매물 등록</button>
    </div>
  </form>
</div>

<script src="https://t1.daumcdn.net/mapjsapi/bundle/postcode/prod/postcode.v2.js"></script>
<script>
  function execDaumPostcode() {
    new daum.Postcode({
      oncomplete: function(data) {
        document.querySelector(\"#postcode\").value = data.zonecode;
        document.querySelector(\"#address\").value = data.address;
        document.querySelector(\"#detailAddress\").focus();
      }
    }).open();
  }
</script>
</body>
</html>
    
