<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %> <%-- Spring Form 태그 라이브러리 선언 --%>
<!DOCTYPE html>
<html>
<head>
<title>회원 목록</title>
<link rel="stylesheet" href="/app/css/admin/memberManagement/memberList.css">
</head>
<body>
<div class="container">
    <%-- modelAttribute는 컨트롤러에서 Model에 담아준 객체의 이름을 지정 --%>
    <form:form modelAttribute="searchCondition" action="/admin/member/list" method="get">
        <div class="search-area">
            <div class="search-item">
                <label for="memberTypeSelect">회원구분</label>
                <%-- path는 모델 객체(searchCondition)의 필드 이름을 지정 --%>
                <form:select path="userRoleId" id="memberTypeSelect" class="select-field" multiple="true">
                    <form:option value="USER" label="일반회원"/>
                    <form:option value="" label="입주민"/>
                    <form:option value="TENANCY" label="임차인"/>
                    <form:option value="BROKER" label="중개인"/>
                </form:select>
            </div>
            <div class="search-item">
                <label for="memberNameInput">회원명</label>
                <form:input path="mbrId" id="memberNameInput" placeholder="회원명" class="input-field"/>
            </div>
            <div class="search-item">
                <label for="mbrFrstRegDtFrom">가입일(시작)</label>
                <form:input type="date" path="mbrFrstRegDtFrom" id="mbrFrstRegDtFrom" class="input-field"/>
            </div>
            <div class="search-item">
                <label for="mbrFrstRegDtTo">가입일(종료)</label>
                <form:input type="date" path="mbrFrstRegDtTo" id="mbrFrstRegDtTo" class="input-field"/>
            </div>
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
        </div>
        <div class="search-actions">
            <button type="submit">검색</button>
        </div>
    </form:form>

    <div class="table-container">
        <table class="table">
            <thead>
                <tr>
                    <th>회원구분</th>
                    <th>회원명</th>
                    <th>가입일</th>
                    <th>회원상태</th>
                    <th>이메일</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty memberList}">
                  <c:forEach items="${memberList}" var="member">
                    <tr>
                      <td>
                      	<c:forEach items="${member.memRoleList}" var="role" varStatus="status">
                              ${role.userRoleId}<c:if test="${!status.last}">, </c:if>
                        </c:forEach>
					  </td>
                      <td>${member.mbrNm}</td>
                      <td>${member.mbrFrstRegDt}</td>
                      <td>${member.mbrStatusCode}</td>
                      <td>${member.mbrEmlAddr}</td>
                    </tr>
                  </c:forEach>
                </c:if>
                <c:if test="${empty memberList }">
                    <tr>
                        <td colspan="5" class="no-data-center">회원 없음</td>
                    </tr>
                </c:if>
            </tbody>
        </table>
    </div>
</div>
</body>
</html>