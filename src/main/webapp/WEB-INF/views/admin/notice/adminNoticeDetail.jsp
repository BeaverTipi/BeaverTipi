<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>

<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
<script src="${pageContext.request.contextPath}/app/js/admin/board/boardToggle.js"></script>

<div class="detail-container">

    <h2 class="board-title">${board.brdTitlNm}</h2>

    <table class="table">
        <tr>
            <th>작성자</th>
            <td>${board.mbrCd}</td>
        </tr>
        <tr>
            <th>게시일</th>
            <td>${board.adFormattedBrdPblsDtm}</td>
        </tr>
        <tr>
        	<th>유형</th>
        	<td>${board.noticeTypeName}</td>
        </tr>
        <tr>
            <th>내용</th>
            <td style="text-align: left; white-space: pre-wrap;">${board.brdCont}</td>
        </tr>
        <c:if test="${not empty board.brdEndDtm}">
            <tr>
                <th>공지 종료일</th>
                <td>${board.adFormattedBrdEndsDtm}</td>
            </tr>
        </c:if>
    </table>

    <div class="write-buttons">
	    <button class="btn-success" onclick="location.href='${pageContext.request.contextPath}/admin/notice/form?brdNo=${board.brdNo}'">수정</button>
	
	    <form id="singleDeleteForm" method="post" action="${pageContext.request.contextPath}/admin/notice/delete" style="display:inline;">
	        <input type="hidden" name="brdNoList" value="${board.brdNo}" />
	        <input type="hidden" name="tab" value="${activeTab}" />
	        <button type="submit" class="btn-danger" id="singleDeleteBtn">삭제</button>
	    </form>
	
	    <button class="btn-dark" onclick="location.href='/admin/notice/list'">목록으로</button>
	</div>

</div>
