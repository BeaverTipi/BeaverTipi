<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <title>회원 & 매물 신고 관리</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/userList.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/business/filePopup.css">
<%--     <link rel="styleSheet" href="${pageContext.request.contextPath }/app/css/main/mainMap/kakaoMap.css"> --%>
</head>
<body>
	<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>
    <script>
    	// pdf.worker.min.js가 CDN에서 로드되는지 확인
        pdfjsLib.GlobalWorkerOptions.workerSrc = 'https://cdnjs.cloudflare.com/ajax/libs/pdf.js/2.11.338/pdf.worker.min.js';
    </script> 
    <script>
    	var contextPath = '${pageContext.request.contextPath}'; // contextPath 변수 선언 추가
	</script>
    <script src="${pageContext.request.contextPath}/app/js/admin/board/userList.js"></script>

<h2 class="board-title">회원 & 매물 신고 관리</h2>

<div class="container-wrapper">
  <main class="container">

    <ul class="nav nav-tabs" id="reportTabs" role="tablist">
        <li class="nav-item">
            <a class="nav-link ${detailSearch.searchRptCode eq 'MEMB' ? 'active' : (empty detailSearch.searchRptCode ? 'active' : '')}"
               id="memb-tab" data-toggle="tab" href="#memberReports" role="tab"
               aria-controls="memberReports" aria-selected="${detailSearch.searchRptCode eq 'MEMB' ? 'true' : (empty detailSearch.searchRptCode ? 'true' : 'false')}"
               data-rpt-code="MEMB">회원</a>
        </li>
        <li class="nav-item">
            <a class="nav-link ${detailSearch.searchRptCode eq 'LSTG' ? 'active' : ''}"
               id="prod-tab" data-toggle="tab" href="#productReports" role="tab"
               aria-controls="productReports" aria-selected="${detailSearch.searchRptCode eq 'LSTG' ? 'true' : 'false'}"
               data-rpt-code="LSTG">매물</a>
        </li>
    </ul>

    <div class="search-area">
      <form:form modelAttribute="detailSearch" action="${pageContext.request.contextPath}/admin/report/userList" method="get" id="searchForm" class="search-form">
        <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput">
        <input type="hidden" name="searchRptCode" value="${detailSearch.searchRptCode}" id="searchRptCodeInput">

        <div class="search-conditions">
          <div class="search-item">
            <label for="searchTitle">제목</label>
            <form:input path="searchTitle" id="searchTitle" placeholder="제목" class="input-field"/>
          </div>
          <div class="search-item">
            <label for="searchReportedTargetId" id="labelReportedTargetId">피신고 ID</label>
            <form:input path="searchReportedTargetId" id="searchReportedTargetId" placeholder="피신고자 ID" class="input-field"/>
          </div>
          <div class="search-item">
            <label for="searchWriter">신고자ID</label>
            <form:input path="searchWriter" id="searchWriter" placeholder="신고자 ID" class="input-field"/>
          </div>
          <div class="search-item">
            <label for="searchRptStatusCode">처리상태</label>
            <form:select path="searchRptStatusCode" id="searchRptStatusCode" class="select-field">
                <form:option value="">--전체--</form:option>
                <form:option value="REG" label="등록"/>
                <form:option value="PROC" label="접수처리중"/>
                <form:option value="COMP" label="처리완료"/>
            </form:select>
          </div>
          <div class="search-item">
            <label>신고기간</label>
            <div class="date-range-group">
                <form:input type="date" path="brdPblsDtmFrom" id="brdPblsDtmFrom" class="input-field"/>
                <span>~</span>
                <form:input type="date" path="brdPblsDtmTo" id="brdPblsDtmTo" class="input-field"/>
            </div>
          </div>
        </div>

        <div class="search-button-area">
          <button type="reset" id="resetBtn" class="btn-warning">초기화</button>
          <button type="submit" id="searchBtn" class="btn-dark">검색</button>
        </div>
      </form:form>
    </div>

    <div class="table-container">
        <table class="table" id="reportedUserTable">
            <thead>
                <tr>
                	<th>번호</th>
                    <th>제목</th>
                    <th id="reportedTarget">신고된 대상</th>
                    <th>신고자 ID</th>
                    <th>신고일자</th>
                    <th>처리상태</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty reportedUserList}">
                    <c:forEach items="${reportedUserList}" var="report" varStatus="status">
                        <tr class="report-row" data-report-id="${report.rptId}">
                        	<td>${(pagingVO.currentPageNo - 1) * pagingVO.recordCountPerPage + status.index + 1}</td>
                            <td>${report.brdTitlNm}</td>
                            <td>${report.rptTargetId}</td>
                            <td>${report.mbrId}</td>
                            <td>
                                ${report.getFormattedBrdPblsDtm()}
                            </td>
                            <td>
                                <c:choose>
                                    <c:when test="${report.rptStatusCode eq 'REG'}">등록</c:when>
                                    <c:when test="${report.rptStatusCode eq 'PROC'}">접수처리중</c:when>
                                    <c:when test="${report.rptStatusCode eq 'COMP'}">처리완료</c:when>
                                    <c:otherwise>${report.rptStatusCode}</c:otherwise>
                                </c:choose>
                            </td>
                        </tr>
                    </c:forEach>
                </c:if>
                <c:if test="${empty reportedUserList }">
                    <tr>
                        <td colspan="6" class="no-data-center">신고된 게시글이 없습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div class="pagination-wrapper">
        ${pagingHTML}
    </div>

  </main>
</div>

<div class="modal fade" id="reportDetailModal" tabindex="-1" aria-labelledby="reportDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportDetailModalLabel">신고 상세 내용</h5>
                <button type="button" class="close" data-dismiss="modal" id="closeReportDetailModalBtnX" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="d-flex align-items-center mb-2">
                    <strong class="label-width">신고 ID :</strong> <span id="modalReportId"></span>
                </div>
                <div class="d-flex align-items-center mb-2">
                    <strong class="label-width">게시글 제목 :</strong> <span id="modalBrdTitlNm"></span>
                </div>
                <div class="d-flex align-items-center mb-2">
                    <strong class="label-width" id="modalTargetIdLabel"></strong><span id="modalRptTargetId"></span>
                </div>
				<div class="d-flex align-items-center mb-2">
                	<strong class="label-width align-self-start">신고 내용 :</strong>
                	<div id="modalBrdCont" class="alert alert-secondary"></div>
				</div>
                <div id="memberSpecificInfo" style="display: none;">
                    <hr>
                    <div class="form-group d-flex align-items-center">
                        <label for="modalNewMbrStatus" class="mr-2 mb-0 label-width"><strong>피신고자 상태 :</strong></label>
                        <select class="form-control" id="modalNewMbrStatus">
                            <option value="ACTIVE">정상</option>
                            <option value="INACTIVE">비활성</option>
                            <option value="SUSPENDED">정지</option>
                            <option value="WITHDRAWN">탈퇴</option>
                        </select>
                    </div>
                </div>
                <div id="listingSpecificInfo" style="display: none;">
                    <div class="form-group d-flex align-items-center">
                        <label for="modalNewLtsgDel" class="mr-2 mb-0 label-width"><strong>피신고매물 삭제 상태 : </strong></label>
                        <select class="form-control" id="modalNewLtsgDel">
                            <option value="N">미삭제 (활성)</option>
                            <option value="Y">삭제 (비활성)</option>
                        </select>
                    </div>
                </div>
                <div class="form-group d-flex align-items-center">
                    <label for="modalRptStatusCode" class="mr-2 mb-0 label-width"><strong>신고 처리 상태 :</strong></label>
                    <select class="form-control" id="modalRptStatusCode">
                        <option value="REG">등록</option>
                        <option value="PROC">접수처리중</option>
                        <option value="COMP">처리완료</option>
                    </select>
                </div>
                
                <hr>
                <h5>첨부파일</h5>
                <div id="fileDataHolder" data-filelist=''></div> 
                <div class="file-preview-area">
                    <div class="file-list-section">
                        <button id="toggleFileListBtn" type="button" class="btn btn-sm btn-info mb-2">첨부파일 목록 보기</button>
                        <table border="1" id="fileTable" class="table table-bordered table-sm">
                            <thead>
                                <tr><th>파일명</th><th>크기</th></tr>
                            </thead>
                            <tbody></tbody>
                        </table>
                    </div>
                    <div class="file-display-section">
                        <canvas id="pdfCanvas" class="img-fluid border"></canvas>
                        <div id="pdf-controls" class="d-flex justify-content-center mt-2">
                            <button id="prevBtn" class="btn btn-sm btn-outline-secondary mr-1">이전</button>
                            <span>페이지 <span id="fileIndex">0</span> / <span id="totalCount">0</span></span>
                            <button id="nextBtn" class="btn btn-sm btn-outline-secondary ml-1">다음</button>
                        </div>
                    </div>
                </div>

            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="btnProcessAllChanges">저장</button>
                <button type="button" class="btn btn-secondary" id="closeReportDetailModalBtn">닫기</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="listingDetailModal" tabindex="-1" aria-labelledby="listingDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="listingDetailModalLabel">매물 상세 정보</h5>
                <button type="button" class="close" data-dismiss="modal" id="closeListingDetailModalBtnX" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <div class="listing-image-gallery mb-4">
                    <div class="main-image image-item">
                        <img id="mainListingImage" class="img-fluid mb-2 border" style="max-height: 400px; object-fit: contain; cursor: pointer;" src="${pageContext.request.contextPath}/assets/img/illustrations/no-image.png" alt="대표 이미지">
                    </div>
                    <div class="thumbnail-grid d-flex flex-wrap justify-content-center">
                        </div>
                </div>

                <div id="listingBasicInfo" class="mb-4">
                    <h3 class="mb-3"><span id="detailListingTitle"></span></h3>
                    <p><span id="detailListingAddress"></span></p>

                    <h2>가격 정보</h2>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">거래유형:</strong> <span id="detailListingTypeSale"></span>
                    </div>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">가격:</strong> <span id="detailListingPrice"></span>
                    </div>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">관리비:</strong> <span id="detailListingMaintFee"></span>
                    </div>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">면적:</strong> <span id="detailListingArea"></span>
                    </div>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">방 개수:</strong> <span id="detailListingRoomCnt"></span>
                    </div>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">층수:</strong> <span id="detailListingFloor"></span>
                    </div>
                    <div class="d-flex align-items-center mb-2">
                        <strong class="label-width">주차:</strong> <span id="detailListingParkYn"></span>
                    </div>

                    <h2 class="mt-4">시설 옵션</h2>
                    <div id="detailListingOption" class="d-flex flex-wrap"></div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" id="closeListingDetailModalBtn">닫기</button>
            </div>
        </div>
    </div>
</div>

<div class="modal fade" id="imageGalleryModal" tabindex="-1" aria-labelledby="imageGalleryModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-xl modal-dialog-centered">
        <div class="modal-content bg-transparent border-0">
            <div class="modal-body text-center p-0">
                <img id="galleryFullImage" class="img-fluid" style="max-height: 90vh; object-fit: contain;">
                <button type="button" class="close text-white position-absolute" id="CloseImageGalleryModalBtnX" style="top: 15px; right: 15px; font-size: 2rem;" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <a class="carousel-control-prev" href="#" role="button" data-slide="prev" id="galleryPrevBtn">
                    <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                    <span class="sr-only">Previous</span>
                </a>
                <a class="carousel-control-next" href="#" role="button" data-slide="next" id="galleryNextBtn">
                    <span class="carousel-control-next-icon" aria-hidden="true"></span>
                    <span class="sr-only">Next</span>
                </a>
            </div>
        </div>
    </div>
</div>

</body>
</html>