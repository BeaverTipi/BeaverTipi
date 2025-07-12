<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>회원 & 매물 신고 관리</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="/app/css/admin/common_admin.css">
    <link rel="stylesheet" href="/app/css/admin/board/userList.css">
</head>
<body>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <script src="/app/js/admin/board/userList.js"></script>
<h2>회원 & 매물 신고 관리</h2>

<div class="container">
    <form:form modelAttribute="detailSearch" action="/admin/report/userList" method="get" id="searchForm">
        <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput">
        <input type="hidden" name="searchRptCode" value="${detailSearch.searchRptCode}" id="searchRptCodeInput">
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
            <div class="search-row top-row">
                <div class="search-item">
                    <label for="searchTitle">제목</label>
                    <form:input path="searchTitle" id="searchTitle" placeholder="제목" class="input-field"/>
                </div>
                <div class="search-item">
                    <label for="searchWriter">신고자ID</label>
                    <form:input path="searchWriter" id="searchWriter" placeholder="신고자ID" class="input-field"/>
                </div>
                <%-- ⭐ "처리상태" 필드 div를 "신고기간" 필드보다 위로 이동 ⭐ --%>
                <div class="search-item">
                    <label for="searchRptStatusCode">처리상태</label>
                    <form:select path="searchRptStatusCode" id="searchRptStatusCode" class="select-field">
                        <form:option value="">--전체--</form:option>
                        <form:option value="REG" label="등록"/>
                        <form:option value="PROC" label="접수처리중"/>
                        <form:option value="COMP" label="처리완료"/>
                    </form:select>
                </div>
            </div>
            <div class="search-row bottom-row">
                <%-- ⭐ "신고기간" 필드 div를 "처리상태" 필드 자리로 이동 ⭐ --%>
                <div class="search-item">
                    <label>신고기간</label>
                    <div class="date-range-group">
                        <form:input type="date" path="brdPblsDtmFrom" id="brdPblsDtmFrom" class="input-field"/>
                        <span>~</span>
                        <form:input type="date" path="brdPblsDtmTo" id="brdPblsDtmTo" class="input-field"/>
                    </div>
                </div>
                <div class="search-item search-buttons-in-row">
                    <button type="button" id="resetButton" class="reset-button">초기화</button>
                    <button type="submit" id="searchButton">검색</button>
                </div>
            </div>
        </div>

        <div class="table-container">
            <table class="table" id="reportedUserTable">
                <thead>
                    <tr>
                    	<th>번호</th>
                        <th>제목</th>
                        <th id="reportedTarget">신고된 대상</th>
                        <th>신고자ID</th>
                        <th>신고일시</th>
                        <th>신고처리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty reportedUserList}">
                        <c:forEach items="${reportedUserList}" var="report" varStatus="status">
                            <tr>
                            	<td>${(pagingInfo.currentPageNo - 1) * pagingInfo.recordCountPerPage + status.index + 1}</td>
                                <td><a href="#" class="report-title" data-report-id="${report.reportId}">${report.brdTitlNm}</a></td>
                                <td>${report.rptTargetId}</td>
                                <td>${report.mbrCd}</td>
                                <td>${report.formattedBrdPblsDtm}</td>
                                <td>
                                    <select class="report-status-select" name="rptStatusUpdates[${status.index}].rptStatusCode" data-report-id="${report.reportId}" data-original-status="${report.rptStatusCode}">
                                        <option value="REG" ${report.rptStatusCode eq 'REG' ? 'selected' : ''}>등록</option>
                                        <option value="PROC" ${report.rptStatusCode eq 'PROC' ? 'selected' : ''}>접수처리중</option>
                                        <option value="COMP" ${report.rptStatusCode eq 'COMP' ? 'selected' : ''}>처리완료</option>
                                    </select>
                                    <input type="hidden" name="rptStatusUpdates[${status.index}].reportId" value="${report.reportId}">
                                </td>
                            </tr>
                        </c:forEach>
                    </c:if>
                    <c:if test="${empty reportedUserList }">
                        <tr>
                            <td colspan="5" class="no-data-center">신고된 게시글이 없습니다.</td>
                        </tr>
                    </c:if>
                </tbody>
            </table>
        </div>
        <div class="pagination-wrapper">
            ${pagingHTML}
        </div>
        <div class="search-actions">
            <button type="button" id="saveButton" class="save-button">저장하기</button>
        </div>
    </form:form>
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
                <button type="button" class="btn btn-secondary" id="cancelStatusChangeModalBtn">취소</button>
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
                <button type="button" class="btn btn-secondary" id="cancelListingStatusChangeModalBtn">취소</button>
                <button type="button" class="btn btn-primary" id="btnUpdateListingDeleteStatus">변경</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>