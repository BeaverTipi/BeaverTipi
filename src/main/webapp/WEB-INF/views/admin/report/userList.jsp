<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>
<head>
	<title>신고 회원 조회</title>
	<link rel="stylesheet" href="/app/css/admin/common_admin.css">
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="/app/js/admin/board/userList.js"></script>
</head>
<body>

<h2>신고 회원 조회</h2>

<div class="container">
    <%-- ⭐ form:form 태그가 여기서 시작됩니다. ⭐ --%>
    <form:form modelAttribute="detailSearch" action="/admin/report/userList" method="get" id="searchForm"> 
        <%-- SimpleSearch 필드가 있다면 hidden input으로 추가 (JSP에서 검색 입력 필드가 없다면) --%>
        <%-- <c:if test="${not empty simpleSearch}">
            <input type="hidden" name="simpleSearch.searchType" value="${simpleSearch.searchType}"/>
            <input type="hidden" name="simpleSearch.searchWord" value="${simpleSearch.searchWord}"/>
            <input type="hidden" name="simpleSearch.bldgId" value="${simpleSearch.bldgId}"/>
        </c:if> --%>

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
                        <form:option value="등록" label="등록"/>
                        <form:option value="접수처리중" label="접수처리중"/>
                        <form:option value="처리완료" label="처리완료"/>
                    </form:select>
                </div>
                <div class="search-item search-buttons-in-row">
                    <button type="button" id="resetButton" class="reset-button">초기화</button> 
                    <button type="submit">검색</button>
                </div>
            </div>
        </div>
        <div class="search-actions">
            </div>
    
        <div class="table-container">
            <table class="table" id="reportedUserTable"> 
                <thead>
                    <tr>
                        <th>제목</th>
                        <th>내용</th>
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
                                <td>${report.brdCont}</td>
                                <td>${report.mbrCd}</td>
                                <td>${report.formattedBrdPblsDtm}</td>
                                <td>
                                    <select class="report-status-select" name="rptStatusCode" data-report-id="${report.reportId}" data-original-status="${report.rptStatusCode}">
                                        <option value="등록" ${report.rptStatusCode eq '등록' ? 'selected' : ''}>등록</option>
                                        <option value="접수처리중" ${report.rptStatusCode eq '접수처리중' ? 'selected' : ''}>접수처리중</option>
                                        <option value="처리완료" ${report.rptStatusCode eq '처리완료' ? 'selected' : ''}>처리완료</option>
                                    </select>
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
        <%-- 렌더링된 HTML 출력 및 JavaScript 함수 추가 --%>
        <div class="paging-area">
            ${pagingHTML} <%-- 컨트롤러에서 생성된 페이징 HTML을 직접 출력 --%>
        </div>
    </form:form>
</div>
</body>
</html>