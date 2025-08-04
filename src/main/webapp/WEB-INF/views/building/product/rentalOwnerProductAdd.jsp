<%@ page contentType="text/html; charset=UTF-8" language="java"%>
<!DOCTYPE html>
<%@taglib uri="jakarta.tags.core" prefix="c"%>
<%@ taglib prefix="fn" uri="jakarta.tags.functions"%>

<link rel="stylesheet" href="/app/css/building/managed/managedList.css">
<title>매물 등록</title>

<h2 class="board-title">매물등록</h2>
<div class="container-wrap">
	<form method="post" action="/building/product/add"
		enctype="multipart/form-data" id="product-form">

		<!-- 📘 매물정보 -->
		<div class="card mb-4">
			<div
				class="card-header d-flex justify-content-between align-items-center">
				<h3 class="mb-0">매물정보</h3>
				<button type="button" class="btn btn-sm btn-outline-primary"
					id="loadMyBuildingBtn" data-bs-toggle="modal"
					data-bs-target="#myBuildingModal">
					<i class="bi bi-building me-1"></i> 내 건물 불러오기
				</button>
			</div>

			<div class="card-body">
				<%-- 매물유형 --%>
				<div class="form-group mb-3">
					<label class="form-label">매물유형 *</label>
					<div id="lstgTypeCode1Group">
						<c:forEach var="code" items="${lstg1List}">
							<c:if test="${code.codeValue ne '000' }">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio"
										name="lstgTypeCode1" id="lstg1_${code.codeValue}"
										value="${code.codeValue}"> <label
										class="form-check-label" for="lstg1_${code.codeValue}">${code.codeName}</label>
								</div>
							</c:if>
						</c:forEach>
					</div>
				</div>
				<div class="form-group mb-3">
					<label class="form-label">상세 유형 *</label>
					<div id="lstgTypeCode2Group" class="d-flex flex-wrap gap-2">
						<span class="text-muted">상위 매물유형을 선택하세요</span>
					</div>
				</div>
				<%-- 주소검색 --%>
				<div class="form-group row mb-3">
					<label class="col-sm-2 col-form-label">주소검색</label>
					<div class="col-sm-3">
						<input type="text" class="form-control" id="postcode"
							name="lstgPostal" value="${listingVO.lstgPostal}"
							placeholder="우편번호" readonly />
					</div>
					<div class="col-sm-5">
						<input type="text" class="form-control" id="address"
							name="lstgAdd" value="${listingVO.lstgAdd}" placeholder="주소"
							readonly />
					</div>
					<div class="col-sm-2">
						<button type="button" class="btn btn-primary w-100"
							onclick="execDaumPostcode()">검색</button>
					</div>
				</div>
				<div id="hiddenLocationFields"></div>
				<%-- 상세주소 --%>
				<div class="form-group row">
					<label class="col-sm-2 col-form-label">상세주소</label>
					<div class="col-sm-6">
						<input type="text" class="form-control" id="detailAddress"
							name="lstgAdd2" value="${listingVO.lstgAdd2}"
							placeholder="상세 주소 입력" />
					</div>
					<div class="col-sm-4">
						<input type="text" class="form-control" id="detailAddress2"
							name="lstgRoomNum" value="${listingVO.lstgRoomNum}"
							placeholder="층, 호 , 실 *" />
					</div>
				</div>
			</div>
		</div>

		<!-- 거래정보 +  추가정보 (양옆 배치) -->
		<div class="row mb-4 align-items-stretch">
			<!-- 거래정보 -->
			<div class="col-md-6">
				<div class="card h-100">
					<div class="card-header">
						<h3>거래정보</h3>
					</div>
					<div class="card-body">
						<%-- 거래유형 --%>
						<div class="form-group row mb-3">
							<div class="col-md-6">
								<label for="lstgTypeSale" class="form-label me-2">거래 유형
									*</label> <select class="form-select-sm" id="lstgTypeSale"
									name="lstgTypeSale" onchange="toggleLeaseFields()">
									<c:forEach var="lstgTypeSale" items="${lstgTypeSaleList}">
										<c:choose>
											<c:when
												test="${lstgTypeSale.codeValue == '000' || lstgTypeSale.codeName == '전체'}">
												<option value="${lstgTypeSale.codeValue}"
													<c:if test="${listingVO.lstgTypeSale == lstgTypeSale.codeValue}">selected</c:if>>
													선택</option>
											</c:when>
											<c:otherwise>
												<option value="${lstgTypeSale.codeValue}"
													<c:if test="${listingVO.lstgTypeSale == lstgTypeSale.codeValue}">selected</c:if>>
													${lstgTypeSale.codeName}</option>
											</c:otherwise>
										</c:choose>
									</c:forEach>
								</select>
							</div>
						</div>
						<!-- 패키지 가능 개별 가격정보 UI 수정 -->
						<div id="leaseFieldsWrapper">
							<!-- 안내 메시지 -->
							<div id="tradeTypeGuide"
								class="alert alert-light border text-center text-muted py-4">
								거래 유형을 선택해주세요.</div>
							<!-- 전세 -->
							<div class="form-group row mt-3" id="jeonseField"
								style="display: none;">
								<div class="col-md-6">
									<label class="form-label" id="jeonseLabel"
										data-label-base="전세가">전세가 (억)</label>
									<div class="unit-input-group">
										<input type="text" inputmode="numeric" 
											class="form-control" id="jeonseAmtView" placeholder="전세금">
										<button type="button" class="btn btn-unit-toggle"
											id="jeonseUnitBtn" onclick="toggleUnit('jeonse')">억
											→ 만원</button>
									</div>
									<input type="hidden" name="lstgLease" id="jeonseAmt">
								</div>
							</div>

							<!-- 월세 -->
							<div id="wolseField" class="form-group mt-3"
								style="display: none;">
								<div class="row">
									<div class="col-md-6">
										<label class="form-label" id="depositLabel"
											data-label-base="보증금">보증금 (만원)</label>
										<div class="unit-input-group">
											<input type="text" inputmode="numeric"
												class="form-control" id="depositAmtView" placeholder="보증금">
											<button type="button" class="btn btn-unit-toggle"
												id="depositUnitBtn" onclick="toggleUnit('deposit')">만원
												→ 원</button>
											<input type="hidden" name="lstgLeaseAmt" id="depositAmt">
										</div>
									</div>
									<div class="col-md-6">
										<label class="form-label" id="mnthRentLabel"
											data-label-base="월세">월세 (만원)</label>
										<div class="unit-input-group">
											<input type="text" inputmode="numeric" class="form-control"
												id="mnthRentAmtView" placeholder="월세">
											<button type="button" class="btn btn-unit-toggle"
												id="mnthRentUnitBtn" onclick="toggleUnit('mnthRent')">
												억 → 만원</button>
											<input type="hidden" name="lstgLeaseM" id="mnthRentAmt">
										</div>
									</div>
								</div>
							</div>

							<!-- 매매 -->
							<div class="form-group row mt-3" id="salePriceField"
								style="display: none;">
								<div class="col-md-6">
									<label class="form-label" id="saleAmtLabel"
										data-label-base="매매가">매매가 (억)</label>
									<div class="unit-input-group">
										<input type="text" inputmode="numeric" class="form-control"
											id="saleAmtView" placeholder="매매가">
										<button type="button" class="btn btn-unit-toggle"
											id="saleUnitBtn" onclick="toggleUnit('sale')">억 → 만원</button>
										<input type="hidden" name="meme" id="saleAmt">
									</div>
								</div>
							</div>

							<div class="row mt-3">
								<!-- 공급면적 -->
								<div class="col-md-6">
									<label for="supplyAreaField" class="form-label"
										id="supplyAreaLabel">공급면적 (평)</label>
									<div class="d-flex align-items-center gap-2">
										<input type="text" class="form-control" id="supplyAreaField"
											placeholder="예: 36.92"> <input type="hidden"
											name="lstgGrArea" value="${listingVO.lstgGrArea}">
										<button type="button" class="btn btn-unit-toggle"
											id="toggleSupplyUnit">평 → ㎡</button>
									</div>
								</div>

								<!-- 전용면적 -->
								<div class="col-md-6">
									<label for="exclusiveAreaField" class="form-label"
										id="exclusiveAreaLabel">전용면적 (평)</label>
									<div class="d-flex align-items-center gap-2">
										<input type="text" class="form-control"
											id="exclusiveAreaField" placeholder="예: 51.35"> <input
											type="hidden" name="lstgExArea"
											value="${listingVO.lstgExArea}">
										<button type="button" class="btn btn-unit-toggle"
											id="toggleExclusiveUnit">평 → ㎡</button>
									</div>
								</div>
							</div>
							<div class="row mt-3">

								<div class="col-md-6">
									<label class="form-label" id="mngmtLabel" data-label-base="관리비">관리비
										(원)</label>
									<div class="unit-input-group d-flex">
										<input type="text" inputmode="numeric" class="form-control"
											id="mngmtAmtView" placeholder="예: 10,000">
										<button type="button" class="btn btn-unit-toggle"
											id="mngmtUnitBtn" onclick="toggleUnit('mngmt')">원 ▼
										</button>
										<input type="hidden" name="lstgFee" id="mngmtAmt">
									</div>
								</div>
							</div>

							<%-- 중개인 연결 --%>
							<div class="form-group row mt-4">
								<label class="col-sm-4 col-form-label">연결 중개인</label>
								<div class="col-sm-8">
									<button type="button"
										class="btn btn-outline-dark broker-select-btn"
										id="openBrokerModalBtn" data-bs-target="#brokerModal">
										중개인 선택</button>
									<span id="brokerCountWrapper" class="badge bg-secondary ms-2"
										style="cursor: help;" hidden> 선택된 중개인 <span
										id="brokerCount">0</span>명
									</span> <span class="text-muted" id="noBrokerSelectedText">선택된
										중개인 없음</span>
								</div>
								<div id="selectedBrokers"></div>
								<div id="selectedBrokerInputs"></div>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- 추가정보 -->
			<div class="col-md-6">
				<div class="card h-100">
					<div class="card-header">
						<h3>추가 정보</h3>
					</div>
					<div class="card-body">
						<!-- 🧱 1번째 줄: 해당 층수 + 욕실 수 -->
						<div class="form-group row mb-3">
							<div class="col-sm-6">
								<label class="form-label">해당 층수</label> <input type="text"
									class="form-control" name="lstgFloor" placeholder="예: 3">
							</div>
							<div class="col-sm-6">
								<label class="form-label">욕실 수</label> <input type="text"
									class="form-control" name="lstgBathCnt" placeholder="예: 1">
							</div>
						</div>

						<div class="form-group row mb-3">
							<div class="col-sm-6">
								<label class="form-label">방 수</label> <input type="number"
									class="form-control" name="lstgRoomCnt" placeholder="예: 2">
							</div>

						</div>

						<div class="form-group row mb-3">
							<label class="col-sm-4 col-form-label">상태</label>
							<div class="col-sm-8 d-flex gap-3">
								<div class="form-check">
									<input class="form-check-input" type="radio" id="newRoom"
										value="신축"> <label class="form-check-label"
										for="newRoom">신축</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" id="remodel"
										value="리모델링"> <label class="form-check-label"
										for="remodel">리모델링</label>
								</div>
							</div>
						</div>

						<div class="form-group row mb-3">
							<label class="col-sm-4 col-form-label">방 구조</label>
							<div class="col-sm-8 d-flex gap-3">
								<div class="form-check">
									<input class="form-check-input" type="radio" id="openType"
										value="오픈형"> <label class="form-check-label"
										for="openType">오픈형</label>
								</div>
								<div class="form-check">
									<input class="form-check-input" type="radio" id="separateType"
										value="분리형"> <label class="form-check-label"
										for="separateType">분리형</label>
								</div>
							</div>
						</div>

						<div class="form-group row mb-3">
							<label class="col-sm-4 col-form-label">주차 가능 여부 *</label>
							<div class="col-sm-8 d-flex flex-wrap gap-3 align-items-center">
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="lstgParkYn"
										value="Y"
										<c:if test="${listingVO.lstgParkYn == 'Y'}">checked</c:if>>
									<label class="form-check-label">가능</label>
								</div>
								<div class="form-check form-check-inline">
									<input class="form-check-input" type="radio" name="lstgParkYn"
										value="N"
										<c:if test="${listingVO.lstgParkYn == 'N'}">checked</c:if>>
									<label class="form-check-label">불가능</label>
								</div>
								<input type="number" class="form-control" placeholder="주차 대 수"
									style="width: 120px;" />
							</div>
						</div>
					</div>
				</div>
			</div>
		</div>


		<!--  시설 정보 -->
		<div class="card mb-4">
			<div class="card-header">
				<h3>시설 정보</h3>
			</div>
			<div class="card-body">
				<!-- 난방 -->
				<div class="form-group mb-3">
					<label class="form-label d-block">난방 시설</label>
					<c:forEach var="opt" items="${facilityMap['004']}"
						varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input" type="radio"
								name="facOptions[${status.index + facilityMap['001'].size() + facilityMap['002'].size() + facilityMap['003'].size()}].facOptId"
								value="${opt.facOptId}" id="heat${status.index}"
								<label class="form-check-label" for="heat${status.index}">${opt.facOptNm}</label>
						</div>
					</c:forEach>
				</div>


				<!-- 냉방 -->
				<div class="form-group mb-3">
					<label class="form-label d-block">냉방 시설</label>
					<div class="form-check form-check-inline">
						<input class="form-check-input select-all" type="checkbox">
						<label class="form-check-label">전체선택</label>
					</div>
					<c:forEach var="cooling" items="${facilityMap['005']}"
						varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input option" type="checkbox"
								name="facOptions[${status.index  + facilityMap['001'].size() + facilityMap['002'].size() + facilityMap['003'].size()+ facilityMap['004'].size()}].facOptId"
								value="${cooling.facOptId}"
								<label class="form-check-label">${cooling.facOptNm}</label>
						</div>
					</c:forEach>
				</div>

				<!-- 생활시설 -->
				<div id="life-section" class="form-group mt-3">
					<label class="form-label d-block">생활 시설</label>
					<div class="form-check form-check-inline">
						<input class="form-check-input select-all" type="checkbox">
						<label class="form-check-label">전체선택</label>
					</div>
					<c:forEach var="life" items="${facilityMap['001']}"
						varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input option" type="checkbox"
								name="facOptions[${status.index}].facOptId"
								value="${life.facOptId}"
								<label class="form-check-label">${life.facOptNm}</label>
						</div>
					</c:forEach>
				</div>

				<!-- 보안시설 -->
				<div id="security-section" class="form-group mt-4">
					<label class="form-label d-block">보안 시설</label>
					<div class="form-check form-check-inline">
						<input class="form-check-input select-all" type="checkbox">
						<label class="form-check-label">전체선택</label>
					</div>
					<c:forEach var="security" items="${facilityMap['002']}"
						varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input option" type="checkbox"
								name="facOptions[${status.index + facilityMap['001'].size()}].facOptId"
								value="${security.facOptId}"
								<label class="form-check-label">${security.facOptNm}</label>
						</div>
					</c:forEach>
				</div>

				<!-- 기타시설 -->
				<div id="etc-section" class="form-group mt-4">
					<label class="form-label d-block">기타 시설</label>
					<div class="form-check form-check-inline">
						<input class="form-check-input select-all" type="checkbox">
						<label class="form-check-label">전체선택</label>
					</div>
					<c:forEach var="etc" items="${facilityMap['003']}"
						varStatus="status">
						<div class="form-check form-check-inline">
							<input class="form-check-input option" type="checkbox"
								name="facOptions[${status.index + facilityMap['001'].size() + facilityMap['002'].size()}].facOptId"
								value="${etc.facOptId}"
								<label class="form-check-label">${etc.facOptNm}</label>
						</div>
					</c:forEach>
				</div>
			</div>
		</div>

		<!--상세 설명 -->
		<div class="card mb-4">
			<div class="card-header">
				<h3>상세 설명</h3>
			</div>
			<div class="card-body">
				<div class="form-group mb-3">
					<label for="title" class="form-label">제목 *</label> <input
						type="text" class="form-control" name="lstgNm" id="title"
						maxlength="40" value="${listingVO.lstgNm}" placeholder="제목 입력" />
				</div>
				<div class="form-group mb-3">
					<label for="description" class="form-label">상세설명</label>
					<textarea class="form-control" name="lstgDtlDst" id="lstgDst"
						rows="6" maxlength="1000" placeholder="상세 설명 입력">${listingVO.lstgDst}</textarea>
				</div>
			</div>
		</div>

		<!--사진 등록 -->
		<div class="card mb-4">
			<div class="card-header">
				<h3>사진 등록</h3>
			</div>
			<div class="card-body">
				<div class="form-group d-flex align-items-center gap-3">
					<label class="form-label mb-0">일반 사진 *</label> <input type="file"
						class="form-control w-auto" name="imageUpload" id="imageUpload"
						accept="image/*" multiple />

					<button type="button" class="btn btn-outline-primary btn-sm">사진은
						최소 5장, 최대 10장 업로드 할수 있습니다.</button>
				</div>
			</div>
		</div>

		<!--  제출 버튼 -->
		<div class="text-center my-5 d-flex gap-3 submit-button-wrapper">
			<button type="submit" class="btn btn-submit btn-lg" id="submitBtn">매물
				등록</button>
			<button type="button" class="btn btn-cancel btn-lg"
				onclick="history.back();">취소</button>
		</div>
</div>
</form>

<!-- 중개인 선택 모달 -->
<div class="modal fade" id="brokerModal" tabindex="-1"
	aria-labelledby="brokerModalLabel" aria-hidden="true">
	<div
		class="modal-dialog modal-lg modal-dialog-centered  modal-dialog-scrollable">
		<div class="modal-content">
			<div class="modal-header">
				<h5 class="modal-title" id="brokerModalLabel">근처 중개인 선택</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="닫기"></button>
			</div>

			<div class="modal-body">
				<div class="form-check mb-2">
					<input type="checkbox" class="form-check-input"
						id="selectAllBrokers"> <label class="form-check-label"
						for="selectAllBrokers">전체 선택</label>
				</div>

				<!-- Ajax 결과로 동적 렌더링되는 영역 -->
				<div id="brokerListArea" class="row"></div>
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-secondary"
					data-bs-dismiss="modal">취소</button>
				<button type="button" class="btn btn-primary"
					id="confirmBrokerSelection">선택 완료</button>
			</div>
		</div>
	</div>
</div>
<div class="modal fade" id="myBuildingModal" tabindex="-1"
	aria-labelledby="myBuildingModalLabel" aria-hidden="true">
	<div class="modal-dialog modal-lg modal-dialog-centered">
		<div class="modal-content">
			<div class="modal-header justify-content-between">
				<h5 class="modal-title" id="myBuildingModalLabel">내 건물 불러오기</h5>
				<button type="button" class="btn-close" data-bs-dismiss="modal"
					aria-label="닫기"></button>
			</div>

			<div class="modal-body">
				<!-- 1단계: 건물 선택 -->
				<div id="buildingStep">
					<p class="mb-2 text-muted">내 보유 건물 목록에서 선택하세요</p>
					<div id="myBuildingListArea">
						<p class="text-muted">건물 목록을 불러오는 중입니다...</p>
					</div>
				</div>

				<!-- 2단계: 유닛 선택 -->
				<div id="unitStep" style="display: none;">
					<button class="btn btn-sm btn-outline-secondary mb-2"
						id="backToBuildings">← 다시 건물 선택</button>
					<p class="mb-2 text-muted">선택한 건물의 유닛을 선택하세요</p>

					<!-- 공실만 보기 필터 -->
					<div class="form-check mb-2">
						<input class="form-check-input" type="checkbox"
							id="onlyVacantToggle"> <label class="form-check-label"
							for="onlyVacantToggle">공실만 보기</label>
					</div>
					<div id="myUnitListArea">
						<p class="text-muted">유닛 목록을 불러오는 중입니다...</p>
					</div>
				</div>
			</div>

			<div class="modal-footer">
				<button type="button" class="btn btn-secondary"
					data-bs-dismiss="modal">닫기</button>
			</div>
		</div>
	</div>
</div>

<script
	src="//dapi.kakao.com/v2/maps/sdk.js?appkey=83437aacc12e4951bd2a5acb4a512ff0&libraries=services,clusterer,drawing&autoload=false"></script>
<!-- <script src="//dapi.kakao.com/v2/maps/sdk.js?appkey=${jsApiKey}&libraries=services,clusterer,drawing&autoload=false"></script> -->

<script src="/app/js/building/product/rentalOwnerProductAdd.js"></script>
