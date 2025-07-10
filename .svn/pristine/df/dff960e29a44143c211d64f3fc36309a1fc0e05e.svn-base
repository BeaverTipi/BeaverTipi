<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>신고 회원 조회</title>
    <link rel="stylesheet" href="/app/css/admin/common_admin.css">
    <link rel="stylesheet" href="/app/css/admin/board/userList.css">
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>
    <style>
        /* 추가적인 스타일링 (필요에 따라 userList.css로 이동 가능) */
        .report-title {
            cursor: pointer;
            color: #007bff; /* 링크 색상 */
            text-decoration: underline; /* 밑줄 */
        }
        .modal-body img {
            max-width: 100%;
            height: auto;
            display: block;
            margin-bottom: 10px;
            border: 1px solid #ddd;
            border-radius: 5px;
        }
        .modal-body .file-link {
            display: block;
            margin-bottom: 5px;
            color: #28a745;
            text-decoration: none;
            font-weight: bold;
        }
        .modal-body .file-link:hover {
            text-decoration: underline;
        }
        #attachFilesSection {
            margin-top: 15px;
            border-top: 1px solid #eee;
            padding-top: 15px;
        }
        #attachFilesSection h5 {
            margin-bottom: 10px;
            color: #343a40;
        }
        .modal-dialog.modal-lg .modal-body {
            max-height: calc(100vh - 200px);
            overflow-y: auto;
        }
        .alert-secondary {
            white-space: pre-wrap;
            word-break: break-all; /* 긴 문자열이 있을 때 넘치지 않도록 강제 줄바꿈 */
        }
    </style>
</head>
<body>

<h2>신고 회원 조회</h2>

<div class="container">
    <form:form modelAttribute="detailSearch" action="/admin/report/userList" method="get" id="searchForm">
        <input type="hidden" name="page" value="${pagingVO.currentPageNo}" id="currentPageNoInput">

        <div class="search-area">
            <div class="search-row top-row">
                <div class="search-item">
                    <label for="searchTitle">제목</label>
                    <form:input path="searchTitle" id="searchTitle" placeholder="제목" class="input-field"/>
                </div>
                <div class="search-item">
                    <label for="searchWriter">작성자ID</label>
                    <form:input path="searchWriter" id="searchWriter" placeholder="작성자ID" class="input-field"/>
                </div>
                <div class="search-item">
                    <label>신고일자</label>
                    <div class="date-range-group">
                        <form:input type="date" path="brdPblsDtmFrom" id="brdPblsDtmFrom" class="input-field"/>
                        <span>~</span>
                        <form:input type="date" path="brdPblsDtmTo" id="brdPblsDtmTo" class="input-field"/>
                    </div>
                </div>
            </div>
            <div class="search-row bottom-row">
                 <div class="search-item">
                    <label for="searchRptStatusCode">처리상태</label>
                    <form:select path="searchRptStatusCode" id="searchRptStatusCode" class="select-field">
                        <form:option value="">--전체--</form:option>
                        <form:option value="REG" label="등록"/>
                        <form:option value="PROC" label="접수처리중"/>
                        <form:option value="COMP" label="처리완료"/>
                    </form:select>
                </div>
                <div class="search-item search-buttons-in-row">
                    <button type="button" id="resetButton" class="reset-button">초기화</button>
                    <button type="submit">검색</button>
                </div>
            </div>
        </div>
        <div class="search-actions">
            <button type="button" id="saveButton" class="save-button">저장하기</button>
        </div>

        <div class="table-container">
            <table class="table" id="reportedUserTable">
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>신고된 대상</th>
                        <th>작성자ID</th>
                        <th>신고일자</th>
                        <th>신고처리</th>
                    </tr>
                </thead>
                <tbody>
                    <c:if test="${not empty reportedUserList}">
                        <c:forEach items="${reportedUserList}" var="report" varStatus="status"> <%-- varStatus 추가 --%>
                            <tr>
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
        <div class="paging-area">
            ${pagingHTML}
        </div>
    </form:form>
</div>

<div class="modal fade" id="reportDetailModal" tabindex="-1" aria-labelledby="reportDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="reportDetailModalLabel">신고 상세 내용</h5>
                <button type="button" class="close" data-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <p><strong>신고 ID:</strong> <span id="modalReportId"></span></p>
                <p><strong>게시글 제목:</strong> <span id="modalBrdTitlNm"></span></p>
                <p><strong>신고 내용:</strong></p>
                <div id="modalBrdCont" class="alert alert-secondary"></div>

                <p><strong>신고 대상 회원 ID:</strong> <span id="modalRptTargetId"></span></p>
                <p><strong>신고 대상 회원 현재 상태:</strong> <span id="modalRptTargetMbrStatus"></span></p>

                <div id="attachFilesSection" style="display: none;">
                    <h5>첨부 파일:</h5>
                    <div id="modalAttachFiles" class="d-flex flex-wrap">
                    </div>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-secondary" data-dismiss="modal">닫기</button>
            </div>
        </div>
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
                    <label for="newMbrStatus">새로운 상태:</label>
                    <select class="form-control" id="newMbrStatus">
                        <option value="NORMAL">정상</option>
                        <option value="SUSPENDED">정지</option>
                        <option value="BANNED">영구 정지</option>
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

<script src="/app/js/admin/board/userList.js"></script>

</body>
</html>