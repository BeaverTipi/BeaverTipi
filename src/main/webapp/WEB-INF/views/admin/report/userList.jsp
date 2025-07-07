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
</head>
<body>

<h2>신고 회원 조회</h2>

<div class="container">
    <%-- ⭐ modelAttribute="detailSearch" 로 변경 ⭐ --%>
    <form:form modelAttribute="detailSearch" action="/admin/report/userList" method="get" id="searchForm"> 
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
            <%-- 신고 조회 페이지에서는 '저장' 버튼이 필요 없을 수도 있습니다. 필요 없으면 제거 --%>
        </div>
    </form:form>

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
                            <td><fmt:formatDate value="${report.brdPblsDtm}" pattern="yyyy-MM-dd HH:mm"/></td>
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

    <%-- 페이징 영역 - 필드명 변경 및 쿼리스트링 생성 로직 변경 --%>
    <div class="paging-area">
        <ul class="pagination">
            <c:if test="${pagingVO.firstPageNoOnPageList > pagingVO.pageSize}">
                <li><a href="?page=${pagingVO.firstPageNoOnPageList - pagingVO.pageSize}${pagingVO.queryString}" class="prev">&laquo;</a></li>
            </c:if>
            <c:forEach begin="${pagingVO.firstPageNoOnPageList}" end="${pagingVO.lastPageNoOnPageList}" var="pageNo">
                <li <c:if test="${pageNo eq pagingVO.currentPageNo}">class="active"</c:if>>
                    <a href="?page=${pageNo}${pagingVO.queryString}">${pageNo}</a>
                </li>
            </c:forEach>
            <c:if test="${pagingVO.lastPageNoOnPageList < pagingVO.totalPageCount}">
                <li><a href="?page=${pagingVO.firstPageNoOnPageList + pagingVO.pageSize}${pagingVO.queryString}" class="next">&raquo;</a></li>
            </c:if>
        </ul>
    </div>

    <script>
        document.addEventListener('DOMContentLoaded', function() {
            document.getElementById('resetButton').addEventListener('click', function() {
                document.getElementById('searchTitle').value = '';
                document.getElementById('searchWriter').value = '';
                document.getElementById('brdPblsDtmFrom').value = '';
                document.getElementById('brdPblsDtmTo').value = '';
                document.getElementById('searchRptStatusCode').value = ''; 
                document.getElementById('searchForm').submit();
            });

            document.querySelectorAll('.report-status-select').forEach(function(selectElement) {
                selectElement.addEventListener('change', function() {
                    const reportId = this.dataset.reportId;
                    const newStatus = this.value;
                    const originalStatus = this.dataset.originalStatus;

                    if (confirm(`신고 번호 ${reportId}의 상태를 '${newStatus}'(으)로 변경하시겠습니까?`)) {
                        axios.post('/admin/report/updateStatus', {
                            reportId: reportId,
                            rptStatusCode: newStatus
                        })
                        .then(response => {
                            if (response.data.status === 'success') {
                                alert(response.data.message);
                                this.dataset.originalStatus = newStatus;
                            } else {
                                alert('상태 변경 실패: ' + response.data.message);
                                this.value = originalStatus;
                            }
                        })
                        .catch(error => {
                            console.error('AJAX 오류:', error);
                            alert('상태 변경 중 오류가 발생했습니다.');
                            this.value = originalStatus;
                        });
                    } else {
                        this.value = originalStatus;
                    }
                });
            });
        });
    </script>
</div>
</body>
</html>