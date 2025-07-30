<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>
<style>
  .container-wrap {
    max-width: 1100px;
    margin: 0 auto;
    padding: 0 16px;
  }
</style>
<link rel="stylesheet" href="/app/css/building/managed/managedList.css">

<div class="container-wrap">
<div class="row">
  <div class="col-md-12">
    <h2>매물등록</h2>
    <div class="lead">
      <code>매물관리 > 매물등록</code>
    </div>
  </div>

  <form method="post" action="/building/product/add" enctype="multipart/form-data"> 
	<input type="hidden" name="rentalPtyId" value="${listingVO.rentalPtyId}">
    <!-- 매물정보 -->
    <div class="col-md-12 mt-3">
      <div class="card">
        <div class="card-header">
          <h3>매물정보</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <div class="form-check">
              <label>매물유형 *</label>
            </div>
            <div id="lstgTypeListArea"></div>
          </div>
          <div class="form-group mt-3">
            <div class="form-check">
              <label>소분류 *</label>
            </div>
            <div id="lstgType2ListArea"></div>
          </div>
          <div class="form-group row mt-3">
            <label class="col-sm-1 col-form-label">주소검색</label>
            <div class="col-sm-3">
              <input type="text" class="form-control" id="postcode" name="lstgPostal" placeholder="우편번호" value="${listingVO.lstgPostal}" readonly>
            </div>
            <div class="col-sm-5">
              <input type="text" class="form-control" name="lstgAdd" id="address" placeholder="예) 번동 10-1, 강북구 번동" value="${listingVO.lstgAdd}" readonly>
            </div>
            <div class="col-sm-2">
              <button type="button" class="btn btn-primary w-100" onclick="execDaumPostcode()">검색</button>
            </div>
          </div>
          <div class="form-group row mt-3">
            <label class="col-sm-1 col-form-label">상세주소</label>
            <div class="col-sm-6">
              <input type="text" class="form-control" id="detailAddress" name="lstgAdd2" placeholder="상세 주소 입력" value="${listingVO.lstgAdd2}">
            </div>
            <div class="col-sm-3">
              <input type="text" class="form-control" id="detailAddress2" name="lstgRoomNum" placeholder="층, 호 , 실 *" value="${listingVO.lstgRoomNum}">
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 거래정보 -->
    <div class="col-md-12 mt-3">
      <div class="card">
        <div class="card-header">
          <h3>거래정보</h3>
        </div>
        <div class="card-body">
        <!-- 거래유형 + 전세/월세/매매 금액 -->
			<div class="form-group row mt-3">
			  <label class="col-sm-2 col-form-label">거래 유형</label>
			  <div class="col-sm-10">
			  	<div class="col-sm-4">
				    <select class="form-select" name="lstgTypeSale" id="lstgTypeSale" onchange="toggleLeaseFields()">
				      <option value="">선택</option>
				      <option value="1" <c:if test="${listingVO.lstgTypeSale == 1}">selected</c:if>>전세</option>
				      <option value="2" <c:if test="${listingVO.lstgTypeSale == 2}">selected</c:if>>월세</option>
				      <option value="3" <c:if test="${listingVO.lstgTypeSale == 3}">selected</c:if>>매매</option>
				    </select>
				</div>
			
			    <!-- 전세가 -->
			    <div class="form-group row mt-3" id="jeonseField" style="display: none;">
			      <label class="col-sm-2 col-form-label">전세</label>
			      <div class="col-sm-5 d-flex gap-2">
			        <input type="number" class="form-control" name="lstgLease" placeholder="전세가" value="${listingVO.lstgLease}">
			        <input type="number" class="form-control" name="lstgFee" placeholder="보증금" value="${listingVO.lstgFee}">
			      </div>
			    </div>
			
			    <!-- 월세 -->
				<div class="form-group row mt-3" id="wolseField" style="display: none;">
				  <label class="col-sm-2 col-form-label">월세</label>
				  <div class="col-sm-5 d-flex gap-2">
				    <input type="number" class="form-control" name="lstgLeaseM" placeholder="월세" value="${listingVO.lstgLeaseM}">
				    <input type="number" class="form-control" name="lstgFee" placeholder="보증금" value="${listingVO.lstgFee}">
				  </div>
				</div>
			
			    <!-- 매매 -->
			    <div class="form-group row mt-3" id="salePriceField" style="display: none;">
			      <label class="col-sm-2 col-form-label">&nbsp; 매매가</label>
			      <div class="col-sm-4">
			        <input type="number" class="form-control" name="meme" placeholder="매매가">
			      </div>
			    </div>
			  </div>
			</div>
			
			<!-- 공급면적 -->
			<div class="form-group row mt-3">
			  <label class="col-sm-2 col-form-label">공급면적 (평)</label>
			  <div class="col-sm-4">
			    <input type="text" class="form-control" name="lstgGrArea" placeholder="예) 24.5" value="${listingVO.lstgGrArea}">
			  </div>
			</div>

        </div>
      </div>
    </div>

    <!-- 추가정보 -->
    <div class="col-md-12 mt-3">
      <div class="card">
        <div class="card-header">
          <h3>추가정보</h3>
        </div>
        <div class="card-body">

          <div class="form-group row">
            <div class="col-md-2">
              <label class="form-label">총 층 수</label>
              <input class="form-control" type="number" name="lstgFloor" value="${listingVO.lstgFloor}">
            </div>
            <div class="col-md-2">
              <label class="form-label">해당 층 수</label>
              <input class="form-control" type="number" name="floor">
            </div>
            <div class="col-md-2">
              <label class="form-label">욕실 수 *</label>
              <input class="form-control" type="number" name="lstgBath">
            </div>
          </div>
			<!-- 방 수 -->
			<div class="form-group row">
			  <div class="col-md-3">
			    <label class="form-label">방 수</label>
			    <input type="number" class="form-control" name="lstgRoomCnt" placeholder="방 갯수" value="${listingVO.lstgRoomCnt}">
			  </div>
			</div>
			
			<!-- 신축 / 리모델링 체크박스: 방수 아래 -->
			<div class="form-group row mt-3 mb-2 ms-1"> 
			  <div class="col-md-12 d-flex gap-4 align-items-center">
			    <div class="form-check">
			      <input class="form-check-input" type="checkbox" name="roomFeature" value="신축" id="newRoom">
			      <label class="form-check-label" for="newRoom">신축</label>
			    </div>
			    <div class="form-check">
			      <input class="form-check-input" type="checkbox" name="roomFeature" value="리모델링" id="remodel">
			      <label class="form-check-label" for="remodel">리모델링</label>
			    </div>
			  </div>
			</div>
			
			<!-- 오픈형 / 분리형 라디오버튼: 그 다음 줄 -->
			<div class="form-group row mb-3 ms-1">
			  <div class="col-md-12 d-flex gap-4 align-items-center">
			    <div class="form-check">
			      <input class="form-check-input" type="radio" name="roomType" value="오픈형" id="openType">
			      <label class="form-check-label" for="openType">오픈형</label>
			    </div>
			    <div class="form-check">
			      <input class="form-check-input" type="radio" name="roomType" value="분리형" id="separateType">
			      <label class="form-check-label" for="separateType">분리형</label>
			    </div>
			  </div>
			</div>


		<!-- 주차 가능 여부 -->
		 <div class="form-group row mt-3">
		   <label class="col-sm-2 col-form-label">주차 가능 여부 *</label>
		   <div class="col-sm-10 d-flex align-items-center gap-3 flex-wrap">
		     <div class="form-check form-check-inline mb-0">
		       <input class="form-check-input" type="radio" name="lstgParkYn" value="Y"
		         <c:if test="${listingVO.lstgParkYn == 'Y'}">checked</c:if>>
		       <label class="form-check-label">가능</label>
		    </div>
		
		    <div class="form-check form-check-inline mb-0">
		      <input class="form-check-input" type="radio" name="lstgParkYn" value="N"
		        <c:if test="${listingVO.lstgParkYn == 'N'}">checked</c:if>>
		      <label class="form-check-label">불가능</label>
		    </div>
		
		    <input type="number" class="form-control" name="parkingCount"
		      placeholder="주차 대 수" style="width: 150px;">
		  </div>
		</div>
          </div>

        </div>
      </div>
    </div>

    <!-- 시설정보 -->
    <div class="col-md-12 mt-3">
      <div class="card">
        <div class="card-header">
          <h3>시설정보</h3>
        </div>
        <div class="card-body">
          <div class="form-group">
            <!-- 난방 -->
            <label class="form-label d-block">난방 시설</label>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="heating" value="INDIVIDUAL">
              <label class="form-check-label">개별난방</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="heating" value="CENTRAL">
              <label class="form-check-label">중앙난방</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="radio" name="heating" value="DISTRICT">
              <label class="form-check-label">지역난방</label>
            </div>

            <!-- 냉방 -->
            <label class="form-label d-block mt-3">냉방 시설</label>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" name="cooling" value="WALL">
              <label class="form-check-label">벽걸이형</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" name="cooling" value="STAND">
              <label class="form-check-label">스탠드형</label>
            </div>
            <div class="form-check form-check-inline">
              <input class="form-check-input" type="checkbox" name="cooling" value="CEILING">
              <label class="form-check-label">천장형</label>
            </div>

            <!-- 생활/보안/기타 시설 -->
            <div id="life-section" class="mt-3">
              <label class="form-label d-block">생활 시설</label>
              <div class="form-check form-check-inline">
                <input class="form-check-input select-all" type="checkbox">
                <label class="form-check-label">전체선택</label>
              </div>
              <c:forEach var="life" items="${facilityMap['1']}" varStatus="status">
                <div class="form-check form-check-inline">
                  <input class="form-check-input option" type="checkbox" name="facOptions[${status.index}].facOptId" value="${life.facOptId}">
                  <label class="form-check-label">${life.facOptNm}</label>
                </div>
              </c:forEach>
            </div>

            <div id="security-section" class="mt-3">
              <label class="form-label d-block">보안 시설</label>
              <div class="form-check form-check-inline">
                <input class="form-check-input select-all" type="checkbox">
                <label class="form-check-label">전체선택</label>
              </div>
              <c:forEach var="security" items="${facilityMap['2']}" varStatus="status">
                <div class="form-check form-check-inline">
                  <input class="form-check-input option" type="checkbox" name="facOptions[${status.index + facilityMap['1'].size()}].facOptId" value="${security.facOptId}">
                  <label class="form-check-label">${security.facOptNm}</label>
                </div>
              </c:forEach>
            </div>

            <div id="etc-section" class="mt-3">
              <label class="form-label d-block">기타 시설</label>
              <div class="form-check form-check-inline">
                <input class="form-check-input select-all" type="checkbox">
                <label class="form-check-label">전체선택</label>
              </div>
              <c:forEach var="etc" items="${facilityMap['3']}" varStatus="status">
                <div class="form-check form-check-inline">
                  <input class="form-check-input option" type="checkbox" name="facOptions[${status.index + facilityMap['1'].size() + facilityMap['2'].size()}].facOptId" value="${etc.facOptId}">
                  <label class="form-check-label">${etc.facOptNm}</label>
                </div>
              </c:forEach>
            </div>

          </div>
        </div>
      </div>
    </div>
<!-- 중개인 연결 영역 -->
<div class="form-group mt-4">
  <label class="form-label d-block">연결할 중개인</label>
  <button type="button" class="btn btn-outline-primary" data-bs-toggle="modal" data-bs-target="#brokerModal">
    중개인 선택
  </button>
  <div id="selectedBrokers" class="mt-2 text-muted small">
    선택된 중개인 없음
  </div>
</div>

    <!-- 상세설명 -->
    <fieldset class="mb-4">
      <legend>상세 설명</legend>
      <div class="mb-3">
        <label for="title" class="form-label">제목 *</label>
        <input type="text" class="form-control" name="lstgNm" id="title" maxlength="40" value="${listingVO.lstgNm}">
      </div>
      <div class="mb-3">
        <label for="description" class="form-label">상세설명</label>
        <textarea class="form-control" name="lstgDtlDst" id="lstgDst" rows="6" maxlength="1000">${listingVO.lstgDst}</textarea>
        
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

    <!-- 제출 버튼 -->
    <div class="text-center mb-5">
      <button type="submit" class="btn btn-primary btn-lg">매물 등록</button>
    </div>

  </form> 

</div>
</div> 
<!-- 중개인 선택 모달 -->
<div class="modal fade" id="brokerModal" tabindex="-1" aria-labelledby="brokerModalLabel" aria-hidden="true">
  <div class="modal-dialog modal-lg modal-dialog-scrollable">
    <div class="modal-content">
      <div class="modal-header">
        <h5 class="modal-title" id="brokerModalLabel">근처 중개인 선택</h5>
        <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
      </div>
      <div class="modal-body">
        <div class="form-check mb-2">
          <input type="checkbox" class="form-check-input" id="selectAllBrokers">
          <label class="form-check-label" for="selectAllBrokers">전체 선택</label>
        </div>

        <div id="brokerListArea" class="row">
          <!-- 여기에 중개인 리스트가 반복됨 -->
          <c:forEach var="broker" items="${nearbyBrokers}">
            <div class="col-md-6">
              <div class="form-check">
                <input class="form-check-input broker-check" type="checkbox" value="${broker.brokerId}" id="broker-${broker.brokerId}">
                <label class="form-check-label" for="broker-${broker.brokerId}">
                  ${broker.name} (${broker.officeName})
                </label>
              </div>
            </div>
          </c:forEach>
        </div>
      </div>
      <div class="modal-footer">
        <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
        <button type="button" class="btn btn-primary" id="confirmBrokerSelection">선택 완료</button>
      </div>
    </div>
  </div>
</div>

<script src="/app/js/building/product/rentalOwnerProductAdd.js"></script>
