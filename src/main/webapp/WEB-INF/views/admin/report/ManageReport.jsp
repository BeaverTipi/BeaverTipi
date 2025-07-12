<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="jakarta.tags.functions" prefix="fn" %>

<!DOCTYPE html>
<html>
<head>
    <title>신고 상세 내용</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/app/css/admin/board/manageReport.css">
</head>
<body>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
	<script src="/app/js/admin/board/manageReport.js"></script>
	
    <div class="container-fluid">
        <h4 class="mb-4">신고 상세 내용</h4>

        <div class="card">
            <div class="card-body">
                <p><strong>신고 ID:</strong> <span id="popupReportId">${reportDetail.reportId}</span></p>
                <p><strong>게시글 제목:</strong> <span id="popupBrdTitlNm">${reportDetail.brdTitlNm}</span></p>
                <p><strong>신고 내용:</strong></p>
                <div id="popupBrdCont" class="alert alert-secondary">
                    <c:out value="${reportDetail.brdCont}" escapeXml="false"/>
                </div>

                <p><strong id="popupTargetIdLabel">
                    <c:choose>
                        <c:when test="${reportDetail.rptCode eq 'LSTG'}">피신고매물ID:</c:when>
                        <c:otherwise>피신고자ID:</c:otherwise>
                    </c:choose>
                </strong> <span id="popupRptTargetId">${reportDetail.rptTargetId}</span></p>

                <c:if test="${reportDetail.rptCode eq 'MEMB'}">
                    <div id="memberSpecificInfo">
                        <p><strong id="popupMbrStatusLabel">피신고자 현재 상태:</strong>
                            <span id="popupRptTargetMbrStatus">
                            	<c:choose>
                                    <c:when test="${reportDetail.rptTargetMbrStatus eq 'ACTIVE'}">정상</c:when>
                                    <c:when test="${reportDetail.rptTargetMbrStatus eq 'INACTIVE'}">비활성</c:when>
                                    <c:when test="${reportDetail.rptTargetMbrStatus eq 'SUSPENDED'}">정지</c:when>
                                    <c:when test="${reportDetail.rptTargetMbrStatus eq 'WITHDRAWN'}">탈퇴</c:when>
                                    <c:otherwise>정보 없음</c:otherwise>
                                </c:choose>
                            </span>
                            <c:if test="${not empty reportDetail.rptTargetMbrCd}">
                                <button type="button" class="btn btn-sm btn-info ml-2" id="popupChangeMemberStatusBtn"
                                        data-mbr-cd="${reportDetail.rptTargetMbrCd}"
                                        data-current-status="${reportDetail.rptTargetMbrStatus}">
                                    상태 변경
                                </button>
                            </c:if>
                        </p>
                    </div>
                </c:if>

                <c:if test="${reportDetail.rptCode eq 'LSTG'}">
                    <div id="listingSpecificInfo">
                        <p><strong>피신고 매물 현재 상태:</strong>
                            <span id="popupLstgDel">
                                <c:choose>
                                    <c:when test="${reportDetail.lstgDel eq 'Y'}">삭제됨</c:when>
                                    <c:when test="${reportDetail.lstgDel eq 'N'}">활성</c:when>
                                    <c:otherwise>정보 없음</c:otherwise>
                                </c:choose>
                            </span>
                            <c:if test="${not empty reportDetail.rptTargetId}">
                                <button type="button" class="btn btn-sm btn-info ml-2" id="popupChangeListingStatusBtn"
                                        data-lstg-id="${reportDetail.rptTargetId}"
                                        data-current-del="${reportDetail.lstgDel}">
                                    상태 변경
                                </button>
                            </c:if>
                        </p>
                    </div>
                </c:if>

                <c:if test="${not empty reportDetail.attachFiles}">
                    <div id="attachFilesSection" class="mt-4">
                        <h5>첨부 파일:</h5>
                        <div id="popupAttachFiles" class="d-flex flex-wrap">
                            <c:forEach items="${reportDetail.attachFiles}" var="file">
                                <%-- file.fileMime이 null이거나 비어있지 않을 경우에만 이미지/링크 로직 실행 --%>
	                            <c:if test="${not empty file.fileMime}">
	                                <c:choose>
	                                    <c:when test="${fn:startsWith(file.fileMime,'image')||fn:contains(file.fileMime,'image')}">
	                                        <img src="${file.filePathUrl}" alt="${file.fileOriginalname}" class="img-fluid">
	                                    </c:when>
	                                    <c:otherwise>
	                                        <a href="${file.filePathUrl}" target="_blank" class="file-link">${file.fileOriginalname}</a>
	                                    </c:otherwise>
	                                </c:choose>
                                </c:if>
                                <%-- file.fileMime이 null이거나 비어있을 경우 표시할 내용 (선택 사항) --%>
								<c:if test="${empty file.fileMime}">
								    <span class="text-muted">파일 MIME 타입 정보 없음</span>
								</c:if>
                            </c:forEach>
                        </div>
                    </div>
                </c:if>
            </div>
        </div>

        <div class="footer-buttons">
            <button type="button" class="btn btn-secondary" onclick="window.close()">닫기</button>
        </div>
    </div>

    <div class="modal fade" id="statusChangeModal" tabindex="-1" aria-labelledby="statusChangeModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="statusChangeModalLabel">회원 상태 변경</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>선택된 회원 ID: <strong id="selectedMbrCd"></strong></p>
                    <p>현재 상태: <strong id="currentMbrStatus"></strong></p>
                    <div class="form-group">
                        <label for="newMbrStatus">변경할 상태:</label>
                        <select class="form-control" id="newMbrStatus">
                            <option value="ACTIVE">정상</option>
                            <option value="INACTIVE">비활성</option>
                            <option value="SUSPENDED">정지</option>
                            <option value="WITHDRAWN">탈퇴</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" class="btn btn-primary" id="btnUpdateMemberStatus">변경</button>
                </div>
            </div>
        </div>
    </div>

    <div class="modal fade" id="listingStatusChangeModal" tabindex="-1" aria-labelledby="listingStatusChangeModalLabel" aria-hidden="true">
        <div class="modal-dialog">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="listingStatusChangeModalLabel">매물 삭제 상태 변경</h5>
                    <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                        <span aria-hidden="true">&times;</span>
                    </button>
                </div>
                <div class="modal-body">
                    <p>선택된 매물 ID: <strong id="selectedLstgId"></strong></p>
                    <p>현재 상태: <strong id="currentLstgDel"></strong></p>
                    <div class="form-group">
                        <label for="newLtsgDel">변경할 상태:</label>
                        <select class="form-control" id="newLtsgDel">
                            <option value="N">미삭제 (활성)</option>
                            <option value="Y">삭제 (비활성)</option>
                        </select>
                    </div>
                </div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-secondary" data-dismiss="modal">취소</button>
                    <button type="button" class="btn btn-primary" id="btnUpdateListingDeleteStatus">변경</button>
                </div>
            </div>
        </div>
    </div>
</body>
</html>