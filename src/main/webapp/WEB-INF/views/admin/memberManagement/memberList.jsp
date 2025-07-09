<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html>
<head>
	<title>회원 목록</title>
	<link rel="stylesheet" href="/app/css/admin/common_admin.css">
	<link rel="stylesheet" href="/app/css/admin/memberManagement/memberList.css">
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
	</head>
<body>

<h2>회원 상태 관리</h2>

<div class="container">
    <form:form modelAttribute="search" action="/admin/member/list" method="get" id="searchForm"> 
        <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" id="currentPageNoInput">

        <div class="search-area">
            <div class="search-row top-row">
                <div class="search-item">
                    <label for="memberTypeSelect">회원구분</label>
                    <form:select path="userRoleId" id="memberTypeSelect" class="select-field">
                        <form:option value="">--선택--</form:option>
                        <form:option value="USER" label="일반회원"/>
                        <form:option value="TENANCY" label="임차인"/>
                        <form:option value="BROKER" label="중개인"/>
                        <form:option value="ADMIN" label="관리자"/> 
                    </form:select>
                </div>
                <div class="search-item">
                    <label for="memberNameInput">회원아이디</label>
                    <form:input path="mbrId" id="memberNameInput" placeholder="회원아이디를 입력해주세요" class="input-field"/>
                </div>
                <div class="search-item">
                    <label>가입일</label> 
                    <div class="date-range-group">
                        <form:input type="date" path="mbrFrstRegDtFrom" id="mbrFrstRegDtFrom" class="input-field"/>
                        <span>~</span>
                        <form:input type="date" path="mbrFrstRegDtTo" id="mbrFrstRegDtTo" class="input-field"/>
                    </div>
                </div>
            </div>
            <div class="search-row bottom-row">
                <div class="search-item">
                    <label for="memberStatusSelect">회원상태</label>
                    <form:select path="mbrStatusCode" id="memberStatusSelect" class="select-field">
                        <form:option value="">--선택--</form:option>
                        <form:option value="ACTIVE" label="정상"/>
                        <form:option value="INACTIVE" label="비활성"/>
                        <form:option value="SUSPENDED" label="정지"/>
                        <form:option value="WITHDRAWN" label="탈퇴"/>
                    </form:select>
                </div>
                <div class="search-item">
                    <label for="memberEmailInput">이메일</label>
                    <form:input path="mbrEmlAddr" id="memberEmailInput" placeholder="이메일을 입력해주세요" class="input-field"/>
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
    </form:form>

    <div class="table-container">
        <table class="table" id="memberTable"> <thead>
                <tr>
                    <th>회원구분</th>
                    <th>회원아이디</th>
                    <th>가입일</th>
                    <th>회원상태</th>
                    <th>이메일</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty memberList}">
                  <c:forEach items="${memberList}" var="member">
                    <tr data-mbr-cd="${member.mbrCd}"> 
                      <td>
	                      <c:if test="${not empty member.memRoleList}">
	                      	<c:forEach items="${member.memRoleList}" var="role" varStatus="status">
	                        	<c:choose>
	                				<c:when test="${role.userRoleId eq 'USER'}">일반회원</c:when>
	                				<c:when test="${role.userRoleId eq 'TENANCY'}">임차인</c:when>
	               					<c:when test="${role.userRoleId eq 'BROKER'}">중개인</c:when>
	               					<c:when test="${role.userRoleId eq 'ADMIN'}">관리자</c:when>
	                				<c:otherwise>${role.userRoleId}</c:otherwise> <%-- 매칭되지 않는 경우 원본 출력 --%>
	            				</c:choose>
	           					<c:if test="${!status.last}">, </c:if>
	                      	</c:forEach>
	                      </c:if>
					  </td>
                      <td>${member.mbrId}</td>
                      <td>${member.mbrFrstRegDt}</td>
                      <td>
                          <select class="member-status-select" name="mbrStatusCode" data-original-status="${member.mbrStatusCode}">
                              <option value="ACTIVE" <c:if test="${member.mbrStatusCode eq 'ACTIVE'}">selected</c:if>>정상</option>
                              <option value="INACTIVE" <c:if test="${member.mbrStatusCode eq 'INACTIVE'}">selected</c:if>>비활성</option>
                              <option value="SUSPENDED" <c:if test="${member.mbrStatusCode eq 'SUSPENDED'}">selected</c:if>>정지</option>
                              <option value="WITHDRAWN" <c:if test="${member.mbrStatusCode eq 'WITHDRAWN'}">selected</c:if>>탈퇴</option>
                          </select>
					  </td>
                      <td>${member.mbrEmlAddr}</td>
                    </tr>
                  </c:forEach>
                </c:if>
                <c:if test="${empty memberList }">
                    <tr>
                        <td colspan="5" class="no-data-center">조회된 회원이 없습니다.</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>

    <div class="pagination-container" aria-label="Page navigation">
        ${pagingHTML}
    </div>

</div>

<script src="/app/js/admin/memberManagement/memberList.js"></script> 

</body>
</html>