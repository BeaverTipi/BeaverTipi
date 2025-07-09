<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>신고 회원 조회</title>
    <link rel="stylesheet" href="/app/css/admin/common_admin.css">
    <link rel="stylesheet" href="/app/css/admin/board/userList.css"> <%-- 페이징 CSS 파일 분리 시 추가 --%>
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
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
                        <c:forEach items="${reportedUserList}" var="report">
                            <tr>
                                <td>${report.brdTitlNm}</td>
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

<script src="/app/js/admin/board/userList.js"></script>

</body>
</html>