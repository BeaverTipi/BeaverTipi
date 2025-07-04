<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>매물 등록</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
  <style>
    /* [CSS 그대로 유지 — 생략 가능] */
  </style>
</head>
<body>
<div class="container">
  <h1 class="text-center">매물등록</h1>
  <form action="${pageContext.request.contextPath}/building/product/add" method="post" enctype="multipart/form-data">

    <!-- 매물 정보 -->
    <fieldset>
      <legend>매물 정보</legend>
      <div class="mb-3">
        <label class="form-label">매물유형 *</label><br>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="villa" checked>
          <label class="form-check-label">주택/빌라</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="officetel">
          <label class="form-check-label">오피스텔</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="apartment">
          <label class="form-check-label">아파트</label>
        </div>
      </div>

      <div class="mb-3">
        <label class="form-label">소분류</label><br>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="oneroom" checked>
          <label class="form-check-label">원룸/투룸/다세대</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="single">
          <label class="form-check-label">단독주택</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="mansion">
          <label class="form-check-label">다가구주택</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="lstgTypeCode" value="commercial">
          <label class="form-check-label">상가주택</label>
        </div>
      </div>

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

      <div class="row mb-3">
        <label class="form-label col-sm-2">매물 크기</label>
        <div class="col-sm-3"><input type="text" class="form-control" name="lstgXuar" placeholder="평수"></div>
        <div class="col-sm-3"><input type="text" class="form-control" name="lstgCmar" placeholder="m²"></div>
      </div>

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
          <input class="form-control" type="number" name="lstgBath"> <%-- VO에 추가하려면 반영 필요 --%>
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
      <div class="mb-4">
        <label class="form-label d-block">난방 시설</label>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="heating" value="개별난방">
          <label class="form-check-label">개별난방</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="heating" value="중앙난방">
          <label class="form-check-label">중앙난방</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="radio" name="heating" value="지역난방">
          <label class="form-check-label">지역난방</label>
        </div>
      </div>

      <div class="mb-4">
        <label class="form-label d-block">냉방 시설</label>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="coolingFacility" value="벽걸이형">
          <label class="form-check-label">벽걸이형</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="coolingFacility" value="스탠드형">
          <label class="form-check-label">스탠드형</label>
        </div>
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="coolingFacility" value="천장형">
          <label class="form-check-label">천장형</label>
        </div>
      </div>

      <div class="mb-4">
        <label class="form-label d-block">생활 시설</label>
        <c:forEach var="life" items="${['침대','책상','옷장']}">
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="checkbox" name="lifeFacility" value="${life}">
            <label class="form-check-label">${life}</label>
          </div>
        </c:forEach>
      </div>

      <div class="mb-4">
        <label class="form-label d-block">보안 시설</label>
        <c:forEach var="security" items="${['CCTV','비디오폰']}">
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="checkbox" name="securityFacility" value="${security}">
            <label class="form-check-label">${security}</label>
          </div>
        </c:forEach>
      </div>

      <div class="mb-4">
        <label class="form-label d-block">기타 시설</label>
        <c:forEach var="etc" items="${['화재경보기','테라스']}">
          <div class="form-check form-check-inline">
            <input class="form-check-input" type="checkbox" name="etcFacility" value="${etc}">
            <label class="form-check-label">${etc}</label>
          </div>
        </c:forEach>
      </div>
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
        document.querySelector("#postcode").value = data.zonecode;
        document.querySelector("#address").value = data.address;
        document.querySelector("#detailAddress").focus();
      }
    }).open();
  }
</script>
</body>
</html>
    