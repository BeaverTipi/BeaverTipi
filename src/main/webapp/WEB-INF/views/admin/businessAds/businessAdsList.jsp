<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring" %>

<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial=" initial-scale="1.0">
    <title>광고 관리</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/businessads/businessAdsList.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/business/filePopup.css">
</head>
<body>
<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
<script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
<script>
    var contextPath = '${pageContext.request.contextPath}';
</script>
<script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script> 
<script src="https://cdnjs.cloudflare.com/ajax/libs/pdf.js/3.4.120/pdf.min.js"></script>

<h2 class="board-title">광고 관리</h2>

<div class="container-wrapper">
  <main class="container">

    <div class="search-area">
      <form:form modelAttribute="detailSearch" action="${pageContext.request.contextPath}/admin/businessAds/businessAdsList" method="get" id="searchForm" class="search-form">
        <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput">
                                                                   
        <div class="search-conditions">
       <div class="search-item">
            <label for="searchAdsTitle">광고 제목:</label>
            <form:input path="searchAdsTitle" id="searchAdsTitle" placeholder="광고 제목" class="input-field"/>
          </div>

          <div class="search-item">                                                                                           
            <label for="searchAdsWriter">작성자 ID:</label>            
            <form:input path="searchAdsWriter" id="searchAdsWriter" placeholder="작성자 ID" class="input-field"/>
          </div>

          <div class="search-item">
            <label for="searchAdsPic">담당자:</label>
            <form:input path="searchAdsPic" id="searchAdsPic" placeholder="담당자명" class="input-field"/>
          </div>

          <div class="search-item">
            <label for="searchAdsBp">사업장명:</label>
            <form:input path="searchAdsBp" id="searchAdsBp" placeholder="사업장명" class="input-field"/>
          </div>

          <div class="search-item">
            <label for="searchAdsStatusCode">광고 상태:</label>
            <form:select path="searchAdsStatusCode" id="searchAdsStatusCode" class="select-field">
                <form:option value="">전체</form:option>
                <form:option value="대기">대기</form:option>
                <form:option value="반려">반려</form:option>
                <form:option value="승인">승인</form:option>
            </form:select>
          </div>

          <div class="search-item">
            <label for="searchAdsPicTelno">담당자 연락처:</label>
            <form:input path="searchAdsPicTelno" id="searchAdsPicTelno" placeholder="하이폰( - ) 없이 입력" class="input-field"/>
          </div>
        </div>

        <div class="search-button-area">
          <button type="reset" id="resetBtn" class="btn btn-warning">초기화</button>
          <button type="submit" id="searchBtn" class="btn btn-dark">검색</button>
        </div>
      </form:form>
    </div>

    <div class="table-container">
        <table class="table" id="businessAdsTable">
            <thead>
                <tr>
                    <th>번호</th>
                    <th>광고 제목</th>
                    <th>작성자 ID</th>
                    <th>담당자</th>
                    <th>사업장명</th>
                    <th>광고 상태</th>
                    <th>담당자 연락처</th>
                    <th>희망 게재 시작일</th>
                    <th>희망 게재 종료일</th>
                </tr>
            </thead>
            <tbody>
                <c:choose>
                    <c:when test="${not empty businessAdsList}">
                        <c:forEach var="ad" items="${businessAdsList}" varStatus="status">
                            <tr class="ads-row view-detail-btn" data-brd-no="${ad.brdNo}" data-toggle="modal" data-target="#adsDetailModal">
                                <td>${pagingVO.firstRecordIndex + status.index}</td>
                                <td>${ad.brdTitlNm}</td>
                                <td>${ad.mbrId}</td>
                                <td>${ad.adsClientVO.adsPic}</td>
                                <td>${ad.adsClientVO.adsBp}</td>
                                <td>${ad.adsClientVO.adsStatusCode}</td>
                                <td class="ads-pic-telno" data-telno="${ad.adsClientVO.adsPicTelno}"></td>
                                <td class="ads-start-dt" data-date="${ad.adsClientVO.adsReqPblsStartDt}"></td>
                                <td class="ads-end-dt" data-date="${ad.adsClientVO.adsReqPblsEndDt}"></td>
                            </tr>
                        </c:forEach>
                    </c:when>
                    <c:otherwise>
                        <tr>
                            <td colspan="9" class="no-data-center">조회된 광고 내역이 없습니다.</td>
                        </tr>
                    </c:otherwise>
                </c:choose>
            </tbody>
        </table>
    </div>

    <div class="pagination-wrapper">
        <nav aria-label="Page navigation example">
            <ul class="pagination justify-content-center">
                ${pagingHTML}
            </ul>
        </nav>
    </div>

  </main>
</div>

<div class="modal fade" id="adsDetailModal" tabindex="-1" aria-labelledby="adsDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="adsDetailModalLabel">광고 상세 정보</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p><strong>광고 번호:</strong> <span id="modalBrdNo"></span></p>
                <div class="form-group d-flex align-items-center">
                    <label for="modalAdsStatusCodeSelect" class="mr-2 mb-0"><strong>광고 상태:</strong></label>
                    <select class="form-control" id="modalAdsStatusCodeSelect">
                        <option value="대기">대기</option>
                        <option value="반려">반려</option>
                        <option value="승인">승인</option>
                    </select>
                </div>

                <div class="form-group" id="rejectMessageGroup" style="display: none;">
                    <label for="modalAdsRejectMessage"><strong>반려 내용:</strong></label>
                    <textarea class="form-control" id="modalAdsRejectMessage" rows="3" placeholder="반려 사유를 입력하세요."></textarea>
                </div>
                <p><strong>사업장명:</strong> <span id="modalAdsBp"></span></p>
                <p><strong>광고 제목:</strong> <span id="modalBrdTitlNm"></span></p>
                
                <hr>

                <p><strong>광고 내용:</strong></p>
                <div id="modalBrdCont" class="alert alert-secondary full-content"></div>

                <hr>
                
                <p><strong>담당자명:</strong> <span id="modalAdsPic"></span></p>
                <p><strong>담당자 연락처:</strong> <span id="modalAdsPicTelno"></span></p>
                <p><strong>희망 게재 시작일:</strong> <span id="modalAdsReqPblsStartDt"></span></p>
                <p><strong>희망 게재 종료일:</strong> <span id="modalAdsReqPblsEndDt"></span></p>
                <p><strong>작성자 ID:</strong> <span id="modalMbrId"></span></p>
                <p><strong>작성일시:</strong> <span id="modalBrdPblsDtm"></span></p>

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
            	<button type="button" class="btn btn-primary" id="saveAdsStatusBtn">저장</button>
                <button type="button" class="btn btn-secondary" data-dismiss="modal" id="closeReportDetailModalBtn">닫기</button>
            </div>
        </div>
    </div>
</div>

<script src="${pageContext.request.contextPath}/app/js/admin/businessads/businessAdsList.js"></script>
</body>
</html>