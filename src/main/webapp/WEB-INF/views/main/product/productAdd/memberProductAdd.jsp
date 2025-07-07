<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<!DOCTYPE html>
<%@taglib uri="jakarta.tags.core" prefix="c" %>
<html lang="ko">

<head>
    <meta charset="UTF-8">
    <title>매물 등록</title>
    <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
<style>
       body {
            padding: 2rem;
            background-color: #f9f9f9;
        }
        h1 {
            margin-bottom: 1.5rem;
        }
        fieldset {
            border: 1px solid #ccc;
            padding: 1.5rem;
            margin-bottom: 2rem;
            background-color: #fff;
            border-radius: 0.5rem;
        }
        legend {
            font-size: 1.25rem;
            font-weight: bold;
            width: auto;
            padding: 0 0.5rem;
        }
        label {
            font-weight: 500;
            margin-top: 1rem;
        }
        .form-check-inline {
            margin-right: 1.5rem;
        }
        .form-check-label {
   			margin-left: 0 !important;
 		    padding-left: 0 !important;
  		}

  		.form-check-input {
    		margin-right: 4px;
    		vertical-align: middle;
 		}

  		.form-check {
    		display: inline-flex;
    		align-items: center;
    		gap: 4px;
    		margin-right: 16px;
 		 }

		    .room-info-wrapper {
		    display: grid;
		    grid-template-columns: 120px 1fr 1fr;
		    grid-template-rows: auto auto;
		    gap: 0.75rem 1rem;
		    align-items: center;
		  }
		
		  .room-info-wrapper .form-check {
		    margin: 0;
		    display: flex;
		    align-items: center;
		  }
		
		  .room-info-wrapper label {
		    margin-bottom: 0;
		  }
		
		  .room-info-wrapper input[type="number"] {
		    width: 80px;
		  }
		    .parking-wrap {
		    display: flex;
		    align-items: center;
		    gap: 0.75rem;
		  }
		
		  .parking-count {
		    max-width: 200px;
		    margin-left: auto;
		  }
		
		  /* 라디오 버튼 줄바꿈 방지용 */
		  .form-check {
		    white-space: nowrap;
		    margin-bottom: 0;
		  }
    </style>
</head>

<body>
<div class="container">
    <h1 class="text-center">매물등록</h1>

    <ul class="mb-4 text-muted small">
        <li>전/월세 매물만 등록할 수 있습니다.</li>
        <li>주소를 다르게 입력할 경우 허위매물로 신고될 수 있으니 정확히 입력 바랍니다.</li>
        <li>등록한 매물은 30일 간 노출됩니다.</li>
    </ul>

    <form action="${pageContext.request.contextPath}/" method="post">

        <!-- 매물 정보 -->
        <fieldset>
            <legend>매물 정보</legend>

            <div class="mb-3">
                <label class="form-label">매물유형 *</label><br>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="mainType" value="villa" checked>
                    <label class="form-check-label">주택/빌라</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="mainType" value="officetel">
                    <label class="form-check-label">오피스텔</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="mainType" value="apartment">
                    <label class="form-check-label">아파트</label>
                </div>
            </div>

            <div class="mb-3">
                <label class="form-label">소분류</label><br>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="subType" value="oneroom" checked>
                    <label class="form-check-label">원룸/투룸/다세대</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="subType" value="single">
                    <label class="form-check-label">단독주택</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="subType" value="mansion">
                    <label class="form-check-label">다가구주택</label>
                </div>
                <div class="form-check form-check-inline">
                    <input class="form-check-input" type="radio" name="subType" value="commercial">
                    <label class="form-check-label">상가주택</label>
                </div>
            </div>

			<div class="row mb-3">
			    <label class="form-label col-sm-2">주소 검색</label>
			    <div class="col-sm-8">
			        <input type="text" class="form-control" name="searchAddress" id="address" placeholder="예) 번동 10-1, 강북구 번동" readonly>
			    </div>
			    <div class="col-sm-2">
			        <button type="button" class="btn btn-dark w-100" onclick="execDaumPostcode()">검색</button>
			    </div>
			</div>
			
			<div class="row mb-3">
			    <label class="form-label col-sm-2">상세 주소</label>
			    <div class="col-sm-10">
			        <input type="text" class="form-control" id="detailAddress" name="detailAddress" placeholder="상세 주소 입력">
			    </div>
			</div>
			<input type="hidden" id="postcode" name="postcode">

            <div class="row mb-3">
                <label class="form-label col-sm-2">매물 주소 *</label>
                <div class="col-sm-3"><input type="text" class="form-control" name="road" placeholder="도로명"></div>
                <div class="col-sm-3"><input type="text" class="form-control" name="building" placeholder="건물번호"></div>
                <div class="col-sm-3"><input type="text" class="form-control" name="unitNumber" placeholder="호수"></div>
            </div>

            <div class="row mb-3">
                <label class="form-label col-sm-2">매물 크기</label>
                <div class="col-sm-3"><input type="text" class="form-control" name="areaPyeong" placeholder="평수"></div>
                <div class="col-sm-3"><input type="text" class="form-control" name="areaM2" placeholder="m²"></div>
            </div>
			<div class="mb-3">
			  <label class="form-label">방 정보</label>
			  <div class="d-flex align-items-center flex-wrap gap-4">
			
			    <!-- 방 수 -->
			    <div class="form-check d-flex align-items-center mb-0">
			      <span class="me-2">방 수</span>
			      <input type="number" class="form-control" name="roomCount" style="width: 80px;">
			    </div>
			
			    <!-- 신축 -->
			    <div class="form-check d-flex align-items-center mb-0">
			      <input class="form-check-input" type="checkbox" id="newRoom" name="roomFeature" value="신축">
			      <label class="form-check-label ms-1 mb-0" for="newRoom">신축</label>
			    </div>
			
			    <!-- 리모델링 -->
			    <div class="form-check d-flex align-items-center mb-0">
			      <input class="form-check-input" type="checkbox" id="remodelRoom" name="roomFeature" value="리모델링">
			      <label class="form-check-label ms-1 mb-0" for="remodelRoom">리모델링</label>
			    </div>
			
			    <!-- 오픈형 -->
			    <div class="form-check d-flex align-items-center mb-0">
			      <input class="form-check-input" type="radio" id="openType" name="roomType" value="오픈형">
			      <label class="form-check-label ms-1 mb-0" for="openType">오픈형</label>
			    </div>
			
			    <!-- 분리형 -->
			    <div class="form-check d-flex align-items-center mb-0">
			      <input class="form-check-input" type="radio" id="divideType" name="roomType" value="분리형">
			      <label class="form-check-label ms-1 mb-0" for="divideType">분리형</label>
			    </div>
			
			  </div>
			</div>
        </fieldset>
         <fieldset>
            <legend>추가 정보</legend>
			 <div class="row g-3">
			  <div class="col-md-6">
			    <!-- 총 층 수 -->
			    <label class="form-label">총 층 수</label>
			    <input class="form-control" type="number" name="floorAll">
			  </div>
			  <div class="col-md-6">
			    <!-- 해당 층 수 -->
			    <label class="form-label">해당 층 수</label>
			    <input class="form-control" type="number" name="floor">
			  </div>
			  <div class="col-md-6">
			    <!-- 욕실 수 -->
			    <label class="form-label">욕실 수 *</label>
			    <input class="form-control" type="number" name="bathroomCount" placeholder="개">
			  </div>
			  <div class="col-md-6">
			  <label class="form-label">주차 가능 여부 *</label>
			  <div class="parking-wrap">
			    <!-- 라디오 -->
			    <div class="form-check form-check-inline">
			      <input class="form-check-input" type="radio" name="parking" id="parkingYes" value="canpark">
			      <label class="form-check-label" for="parkingYes">가능</label>
			    </div>
			    <div class="form-check form-check-inline">
			      <input class="form-check-input" type="radio" name="parking" id="parkingNo" value="cannotpark">
			      <label class="form-check-label" for="parkingNo">불가능</label>
			    </div>

			      <input type="number" class="form-control" name="parkingCount" placeholder="주차 대 수" style="max-width: 200px;">
			    </div>
			  </div>
			</div>
        </fieldset>

        <!-- 시설 정보 -->
       <fieldset>
  <legend>시설 정보</legend>

  <!-- 난방 시설 -->
  <div class="mb-4">
    <label class="form-label d-block">난방 시설</label>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="heating" value="개별난방" id="heating1">
      <label class="form-check-label" for="heating1">개별난방</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="heating" value="중앙난방" id="heating2">
      <label class="form-check-label" for="heating2">중앙난방</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="radio" name="heating" value="지역난방" id="heating3">
      <label class="form-check-label" for="heating3">지역난방</label>
    </div>
  </div>

  <!-- 냉방 시설 -->
  <div class="mb-4">
    <label class="form-label d-block">냉방 시설</label>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="cooling" value="벽걸이형" id="cooling1">
      <label class="form-check-label" for="cooling1">벽걸이형</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="cooling" value="스탠드형" id="cooling2">
      <label class="form-check-label" for="cooling2">스탠드형</label>
    </div>
    <div class="form-check form-check-inline">
      <input class="form-check-input" type="checkbox" name="cooling" value="천장형" id="cooling3">
      <label class="form-check-label" for="cooling3">천장형</label>
    </div>
  </div>

  <!-- 생활 시설 -->
  <div class="mb-4">
    <label class="form-label d-block">생활 시설</label>
    <div class="form-check form-check-inline mb-2">
      <input class="form-check-input" type="checkbox" id="lifeAll">
      <label class="form-check-label" for="lifeAll">전체선택</label>
    </div>
    <div class="d-flex flex-wrap gap-2">
      <c:forEach var="life" items="${['침대','책상','옷장','식탁','소파','신발장','냉장고','세탁기','건조기','샤워부스','식기세척기','가스레인지','인덕션','전자레인지','가스오븐','TV','붙박이장']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="lifeFacility" value="${life}" id="life_${life}">
          <label class="form-check-label" for="life_${life}">${life}</label>
        </div>
      </c:forEach>
    </div>
  </div>

  <!-- 보안 시설 -->
  <div class="mb-4">
    <label class="form-label d-block">보안 시설</label>
    <div class="form-check form-check-inline mb-2">
      <input class="form-check-input" type="checkbox" id="securityAll">
      <label class="form-check-label" for="securityAll">전체선택</label>
    </div>
    <div class="d-flex flex-wrap gap-2">
      <c:forEach var="security" items="${['경비원','비디오폰','인터폰','카드키','CCTV','사설경비','현관보안','방범창']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="securityFacility" value="${security}" id="sec_${security}">
          <label class="form-check-label" for="sec_${security}">${security}</label>
        </div>
      </c:forEach>
    </div>
  </div>

  <!-- 기타 시설 -->
  <div class="mb-4">
    <label class="form-label d-block">기타 시설</label>
    <div class="d-flex flex-wrap gap-2">
      <c:forEach var="etc" items="${['화재경보기','베란다','테라스','마당','무인택배함']}">
        <div class="form-check form-check-inline">
          <input class="form-check-input" type="checkbox" name="etcFacility" value="${etc}" id="etc_${etc}">
          <label class="form-check-label" for="etc_${etc}">${etc}</label>
        </div>
      </c:forEach>
    </div>
  </div>
</fieldset>
<fieldset class="mb-4">
  <legend>사진 등록</legend>
  <div class="mb-3 d-flex align-items-center gap-3">
    <label class="form-label mb-0">일반 사진 *</label>
    <input type="file" class="form-control w-auto" name="imageUpload" id="imageUpload" accept="image/*" multiple>
    <button type="button" class="btn btn-outline-primary btn-sm">+ 사진 추가</button>
  </div>
  <p class="text-danger small mt-1">※ 사진 등록 전, 반드시 확인해주세요!</p>
</fieldset>

<!-- 상세 설명 -->
<fieldset class="mb-4">
  <legend>상세 설명</legend>
  
  <div class="mb-3">
    <label for="title" class="form-label">제목 *</label>
    <input type="text" class="form-control" name="title" id="title" maxlength="40" placeholder="리스팅에 노출되는 문구입니다. 40자 이내로 작성해주세요.">
    <div class="form-text text-muted">0 / 40</div>
    <small class="text-muted d-block mt-1">
      • 문장, 영어, 숫자, 허용된 특수문자( . / - , m² ) 이외의 문자는 입력할 수 없습니다.<br>
      • 광고성, 허위, 기타 매물과 무관한 정보는 삭제 처리 됩니다.
    </small>
  </div>

  <div class="mb-3">
    <label for="description" class="form-label">상세설명 (1000자 제한)</label>
    <textarea class="form-control" name="description" id="description" rows="6" maxlength="1000"
              placeholder="매물 상세 페이지에 노출되는 문구입니다. 1000자 이내로 작성해주세요."></textarea>
    <div class="form-text text-muted">0 / 1000</div>
    <small class="text-muted d-block mt-1">
      • 매물 외적인 광고문구는 입력할 수 없습니다.<br>
      • 매물등록자의 개인정보는 입력하지 말아주세요.<br>
      • 위 주의사항 위반 시 매물 미노출 또는 서비스 이용이 제한될 수 있습니다.
    </small>
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
