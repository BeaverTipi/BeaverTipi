<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
    <title>신고 회원 조회</title>
    <link rel="stylesheet" href="/app/css/admin/common_admin.css">
    <!-- JQuery CDN 추가 (userList.js에서 사용될 수 있음) -->
    <script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <!-- Axios CDN은 필요하다면 유지. 여기서는 jQuery.ajax 또는 fetch로 통일 예정 -->
    <!-- <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script> -->
    <!-- userList.js 스크립트는 body 닫히기 전에 로드하는 것이 DOM 접근에 유리 -->
</head>
<body>

<h2>신고 회원 조회</h2>

<div class="container">
    <!-- 폼의 modelAttribute는 컨트롤러에서 @ModelAttribute("detailSearch")로 바인딩된 이름과 동일하게 "detailSearch" -->
    <form:form modelAttribute="detailSearch" action="/admin/report/userList" method="get" id="searchForm"> 
        <!-- ⭐ 추가: 페이징을 위한 현재 페이지 번호 hidden 필드 ⭐ -->
        <!-- 컨트롤러의 pagingVO.currentPageNo 값을 가져와 초기화합니다. -->
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
            <!-- 저장 버튼은 JS에서 동적으로 처리할 예정이므로 폼 외부에 두거나, 폼 내부에 두되 submit이 아닌 type="button"으로 처리 -->
            <!-- 현재 코드는 폼 밖에 있으므로 그대로 유지합니다. -->
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
                                        <option value="REG" ${report.rptStatusCode eq 'REG' ? 'selected' : ''}>등록</option>
                                        <option value="PROC" ${report.rptStatusCode eq 'PROC' ? 'selected' : ''}>접수처리중</option>
                                        <option value="COMP" ${report.rptStatusCode eq 'COMP' ? 'selected' : ''}>처리완료</option>
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
        <!-- 페이징 영역은 폼 내부에 두어 검색 조건과 함께 페이지 이동이 가능하도록 합니다. -->
        <div class="paging-area">
            ${pagingHTML}
        </div>
    </form:form>
</div>

<!-- JavaScript 파일 임포트 (DOMContentLoaded 이후 실행되도록 body 닫는 태그 직전에 위치) -->
<script src="/app/js/admin/board/userList.js"></script>

</body>
</html>