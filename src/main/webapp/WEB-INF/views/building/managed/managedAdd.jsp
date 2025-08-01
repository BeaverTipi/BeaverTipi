<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<%@taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@taglib uri="jakarta.tags.functions" prefix="fn"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>신규 건물 등록</title>
<link rel="stylesheet" href="/app/css/building/managed/managedAdd.css" />
<link rel="stylesheet" href="/app/css/building/unit/unitDetailAdd.css" />
</head>
<body>
<h2 class="board-title">건물 등록</h2>
	<div class="container-wrap">
		<form:form modelAttribute="buildingVO" method="post"
			action="/building/managed/add" enctype="multipart/form-data">
			<form:hidden path="rentalPtyId" />
			<form:hidden path="delYn" value="N" />

			<!-- 건물 기본 정보 카드 -->
			<div class="card mb-4">
				<div
					class="card-header d-flex justify-content-between align-items-center">
					<h3 class="mb-0">건물 필수 정보</h3>
					<button type="button" class="btn btn-outline-primary btn-sm"
						data-bs-toggle="modal" data-bs-target="#myListingsModal">
						내 매물정보 불러오기</button>
				</div>
				<div class="card-body">
					<div class="row g-3">
						<!-- 건물 유형 -->
						<div class="col-md-3">
							<label class="form-label fw-semibold">건물 유형 <span
								class="text-danger">*</span></label> <select
								class="form-select text-center text-center-select"
								id="bldgTypeCode" name="bldgTypeCode">
								<option value="">--선택--</option>
								<c:forEach items="${bldgTypeList}" var="bldgType">
									<option value="${bldgType.codeValue}">${bldgType.codeName}</option>
								</c:forEach>
							</select>
						</div>
						<!-- 건물 이름 -->
						<div class="col-md-9">
							<label class="form-label fw-semibold">건물 이름 <span
								class="text-danger">*</span></label> <input type="text"
								class="form-control" name="bldgNm" placeholder="예: 역삼동 현대타워" />
						</div>



						<!-- 공급면적 -->
						<div class="col-md-4">
							<label class="form-label fw-semibold">공급면적 (<span
								id="areaUnitLabel">평</span>)<span class="text-danger">*</span>
							</label>
							<div class="d-flex unit-input-group">
								<input type="number" class="form-control"
									name="supplyAreaDisplay" id="supplyAreaDisplay"
									placeholder="예: 36.92" />
								<button type="button" class="btn btn-unit-toggle"
									id="toggleUnitBtn">㎡ ▼</button>
							</div>
							<input type="hidden" name="bldgGrossArea" id="supplyAreaHidden" />
						</div>
						<!-- 준공일 -->
						<div class="col-md-4">
							<label class="form-label fw-semibold">준공일 <span
								class="text-danger">*</span></label> <input type="date"
								class="form-control" name="bldgCmpltnDt" />
						</div>
						<!-- 총 층수 -->
						<div class="col-md-4">
							<label class="form-label fw-semibold">총 층수 <span
								class="text-danger">*</span></label> <input type="number"
								class="form-control" name="bldgFlrCnt" placeholder="예: 15"
								min="1" />
						</div>

					</div>
				</div>
			</div>
			<!-- 🏠 주소 카드 -->
			<div class="card mb-4">
				<div class="card-header">
					<h3>주소 정보</h3>
				</div>
				<div class="card-body">
					<div class="row g-3">
						<!-- 우편번호 -->
						<div class="col-md-3">
							<label class="form-label fw-semibold">우편번호 <span
								class="text-danger">*</span></label> <input type="text"
								class="form-control" id="bldgZipNo" name="bldgZipNo" readonly />
						</div>

						<!-- 기본주소 -->
						<div class="col-md-6">
							<label class="form-label fw-semibold">기본 주소</label> <input
								type="text" class="form-control" id="bldgAddr" name="bldgAddr"
								readonly />
						</div>

						<!-- 주소 검색 버튼 -->
						<div class="col-md-3 d-flex align-items-end">
							<button type="button" class="btn btn-dark w-100 btn-addr-search"
								onclick="execDaumPostcode()">주소 검색</button>
						</div>

						<!-- 상세주소 -->
						<div class="col-md-12">
							<label class="form-label fw-semibold">상세주소 <span
								class="text-danger">*</span></label> <input type="text"
								class="form-control" name="bldgDtlAddr" placeholder="예: 101호"
								id="bldgDtlAddr" />
						</div>
					</div>
				</div>
			</div>


			<!--  이미지 업로드 -->
			<div class="card mb-4">
				<div class="card-header">
					<h3>건물 이미지</h3>
				</div>
				<div class="card-body">
					<div class="form-group">
						<label class="form-label d-block">대표 이미지</label>
						<div class="d-flex align-items-center gap-4">
							<div id="previewContainer" class="image-box">
								<!-- 기본은 안보이고 JS로 설정될 때만 보임 -->
								<img id="previewImg" alt="미리보기" style="display: none;" />
								<div class="image-placeholder-text">아직 등록된 이미지가 없습니다</div>
							</div>
							<div class="image-upload-wrapper">
								<input type="file" id="bldgImgFile" name="bldgImgFile"
									accept="image/*" style="display: none;" />
								<button type="button" class="btn btn-outline-dark mb-2"
									id="triggerImgUpload">이미지 등록</button>
								<div class="text-muted mt-2" style="font-size: 14px;">※ 한
									개의 이미지만 등록 가능합니다.</div>
							</div>
						</div>
					</div>
				</div>
			</div>
 <div class="card mb-4">
    <div class="card-header d-flex justify-content-between align-items-center">
      <h3 class="mb-0">세대 상세 정보</h3>
      <div class="d-flex align-items-center gap-3">
        <label class="mb-0">총 세대:</label>
        <input type="number" id="unitCount" name="bldgUnitCnt" min="1" class="form-control" style="max-width: 120px" />
        <button type="button" class="btn btn-primary btn-sm" id="generateBtn">입력폼 생성</button>
      </div>
    </div>
    <div class="card-body">
      <div id="unitInputContainer" class="unit-placeholder-box">
        <div class="text-muted text-center py-5 fs-6">
          총 세대를 입력하고 <strong>입력폼 생성</strong>을 눌러주세요.
        </div>
      </div>
    </div>
  </div>
	</div>


  <!-- 제출 버튼 -->
  <div class="submit-button-wrapper">
    <button type="submit" class="btn btn-submit">등록</button>
    <button type="button" class="btn btn-cancel" onclick="history.back();">취소</button>
  </div>

</form:form>

	<div class="modal fade" id="myListingsModal" tabindex="-1"
		aria-labelledby="myListingModalLabel" aria-hidden="true">
		<div class="modal-dialog modal-dialog-centered modal-lg">
			<div class="modal-content">

				<div class="modal-header">
					<h5 class="modal-title" id="myListingModalLabel">내 매물 목록</h5>
					<button type="button" class="btn-close" data-bs-dismiss="modal"
						aria-label="닫기"></button>
				</div>

				<div class="modal-body" style="max-height: 500px; overflow-y: auto;">
					<c:choose>
						<c:when test="${not empty listingList}">
							<div class="row row-cols-1 row-cols-md-2 g-3">
								<c:forEach var="lstg" items="${listingList}">
									<div class="col">
										<div class="card h-100 selectable-card"
											onclick="fillListingInfo('${lstg.lstgId}')">
											<div class="card-body">
												<h5 class="card-title mb-2">${lstg.lstgNm}</h5>
												<p class="card-text text-muted mb-1">${lstg.lstgAdd}
													${lstg.lstgAdd2}</p>
											</div>
										</div>
									</div>
								</c:forEach>
							</div>
						</c:when>

						<c:otherwise>
							<div style="text-align: center; padding: 2rem; color: #888;">
								<i class="bi bi-exclamation-circle fs-3 mb-2 d-block"></i> 등록된
								매물이 없습니다.
							</div>
						</c:otherwise>
					</c:choose>
				</div>


				<div class="modal-footer">
					<button type="button" class="btn btn-secondary"
						data-bs-dismiss="modal">닫기</button>
				</div>

			</div>
		</div>
	</div>


	<script src="/app/js/building/managed/managedAdd.js"></script>
	<script src="/app/js/building/unit/unitDetailAdd.js"></script>
</body>
</html>
