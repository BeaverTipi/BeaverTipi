<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<!DOCTYPE html>
<html lang="ko">
<head>
	<meta charset="UTF-8">
	<title>회원 목록</title>
    <link href="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css" rel="stylesheet">
	<link rel="stylesheet" href="/app/css/admin/common_admin.css">
    <link rel="stylesheet" href="/app/css/admin/memberManagement/memberList.css">
</head>
<body>
	<script src="https://code.jquery.com/jquery-3.7.1.min.js"></script>
    <script src="https://cdn.jsdelivr.net/npm/axios/dist/axios.min.js"></script>
    <script src="https://stackpath.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.bundle.min.js"></script>
	<script src="/app/js/admin/memberManagement/memberList.js"></script> 

<h2 class="board-title">회원 상태 관리</h2>

<div class="container-wrapper">
    <main class="container">
    <div class="search-area">
        <form:form modelAttribute="search" action="/admin/member/list" method="get" id="searchForm" class="search-form"> 
            <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" id="currentPageNoInput">

            <div class="search-conditions">
                <div class="search-item">
                    <label>회원아이디</label>
                    <form:input path="mbrId" id="memberNameInput" cssClass="input-field" placeholder="회원아이디를 입력해주세요"/>
                </div>
                <div class="search-item">
                	<label>이름</label>
	                <form:input path="mbrNm" id="memberNameSearchInput" cssClass="input-field" placeholder="이름을 입력해주세요"/>
	            </div>
	            <div class="search-item">
	                <label>닉네임</label>
	                <form:input path="mbrNnm" id="memberNicknameSearchInput" cssClass="input-field" placeholder="닉네임을 입력해주세요"/>
	            </div>
                <div class="search-item">
                    <label>이메일</label>
                    <form:input path="mbrEmlAddr" id="memberEmailInput" cssClass="input-field" placeholder="이메일을 입력해주세요"/>
                </div>
                <div class="search-item">
                    <label>회원구분</label>
                    <form:select path="userRoleId" id="memberTypeSelect" cssClass="select-field">
                        <form:option value="">--전체--</form:option>
                        <form:option value="USER" label="일반회원"/>
                        <form:option value="TENANCY" label="임대인"/>
                        <form:option value="BROKER" label="중개인"/>
                        <form:option value="ADMIN" label="관리자"/> 
                    </form:select>
                </div>
                <div class="search-item">
                    <label>회원상태</label>
                    <form:select path="mbrStatusCode" id="memberStatusSelect" cssClass="select-field">
                        <form:option value="">--전체--</form:option>
                        <form:option value="ACTIVE" label="정상"/>
                        <form:option value="INACTIVE" label="비활성"/>
                        <form:option value="SUSPENDED" label="정지"/>
                        <form:option value="WITHDRAWN" label="탈퇴"/>
                    </form:select>
                </div>
                <div class="search-item">
                    <label>가입기간</label> 
                    <div class="date-range-group">
                        <form:input type="date" path="mbrFrstRegDtFrom" id="mbrFrstRegDtFrom" cssClass="input-field"/>
                        <span>~</span>
                        <form:input type="date" path="mbrFrstRegDtTo" id="mbrFrstRegDtTo" cssClass="input-field"/>
                    </div>
                </div>
            </div>
            <div class="search-button-area">
                <button type="reset" id="resetBtn" class="btn-warning">초기화</button> 
                <button type="submit" id="searchBtn" class="btn-dark">검색</button>
            </div>
        </form:form>
    </div>

    <div class="table-container">
        <table class="table"> 
            <thead>
                <tr>
                	<th>번호</th>
                    <th>회원구분</th>
                    <th>회원아이디</th>
                    <th>이름</th>
                    <th>닉네임</th>
                    <th>가입일</th>
                    <th>회원상태</th>
                    <th>이메일</th>
                </tr>
            </thead>
            <tbody>
                <c:if test="${not empty memberList}">
                  <c:forEach items="${memberList}" var="member" varStatus="status">
                    <%-- 각 행 클릭 시 모달이 뜨도록 data-mbr-cd와 member-row-clickable 클래스 추가 --%>
                    <tr class="member-row-clickable" data-mbr-cd="${member.mbrCd}">
                    	<td>${(pagingInfo.currentPageNo - 1) * pagingInfo.recordCountPerPage + status.index + 1}</td>
	                      <td>
		                      <c:if test="${not empty member.memRoleList}">
		                      	<c:forEach items="${member.memRoleList}" var="role" varStatus="status">
		                        	<c:choose>
		                				<c:when test="${role.userRoleId eq 'USER'}">일반회원</c:when>
		                				<c:when test="${role.userRoleId eq 'TENANCY'}">임대인</c:when>
		               					<c:when test="${role.userRoleId eq 'BROKER'}">중개인</c:when>
		               					<c:when test="${role.userRoleId eq 'ADMIN'}">관리자</c:when>
		                				<c:otherwise>${role.userRoleId}</c:otherwise> 
		            				</c:choose>
		           					<c:if test="${!status.last}">, </c:if>
		                      	</c:forEach>
		                      </c:if>
						  </td>
	                      <td>${member.mbrId}</td>
	                      <td>${member.mbrNm}</td>
	                      <td>${member.mbrNnm}</td>
	                      <td>${member.mbrFrstRegDt}</td>
	                      <td>
	                          <%-- 회원 상태를 select 대신 텍스트로 표시 --%>
	                          <c:choose>
                                  <c:when test="${member.mbrStatusCode eq 'ACTIVE'}">정상</c:when>
                                  <c:when test="${member.mbrStatusCode eq 'INACTIVE'}">비활성</c:when>
                                  <c:when test="${member.mbrStatusCode eq 'SUSPENDED'}">정지</c:when>
                                  <c:when test="${member.mbrStatusCode eq 'WITHDRAWN'}">탈퇴</c:when>
                                  <c:otherwise>${member.mbrStatusCode}</c:otherwise>
                              </c:choose>
						  </td>
	                      <td>${member.mbrEmlAddr}</td>
	                    </tr>
	                  </c:forEach>
	                </c:if>
	                <c:if test="${empty memberList }">
	                    <tr>
	                        <td colspan="8" class="no-data-center">조회된 회원이 없습니다.</td>
	                    </tr>
	                </c:if>
	            </tbody>
	        </table>
	    </div>
	
	    <div class="pagination-wrapper">
	        ${pagingHTML}
	    </div>
	     
	    <%-- 목록에서의 '저장하기' 버튼은 이제 필요 없으므로 제거 또는 주석 처리 --%>
	    <%-- <div class="write-buttons">
            <button type="button" id="saveBtn" class="btn-dark">저장하기</button> 
        </div> --%>
    </main>
</div>

<div class="modal fade" id="memberDetailModal" tabindex="-1" aria-labelledby="memberDetailModalLabel" aria-hidden="true">
    <div class="modal-dialog modal-lg">
        <div class="modal-content">
            <div class="modal-header">
                <h5 class="modal-title" id="memberDetailModalLabel">회원 상세 정보 및 상태 변경</h5>
                <button type="button" class="close" data-bs-dismiss="modal" aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
            </div>
            <div class="modal-body">
                <input type="hidden" id="modalMbrCd"> <%-- 회원 코드 저장을 위한 hidden input --%>
                <p><strong>이름:</strong> <span id="modalMbrNm"></span></p>
                <p><strong>회원 아이디:</strong> <span id="modalMbrId"></span></p>
                <p><strong>닉네임:</strong> <span id="modalMbrNnm"></span></p>
                <p><strong>회원 구분:</strong> <span id="modalUserRoleIds"></span></p>
                <p><strong>가입일:</strong> <span id="modalMbrFrstRegDt"></span></p>
                <p><strong>이메일:</strong> <span id="modalMbrEmlAddr"></span></p>
                
                <hr> <%-- 구분선 --%>

                <div class="form-group">
                    <label for="modalMbrStatusCode"><strong>회원 상태:</strong></label>
                    <select class="form-control" id="modalMbrStatusCode">
                        <option value="ACTIVE">정상</option>
                        <option value="INACTIVE">비활성</option>
                        <option value="SUSPENDED">정지</option>
                        <option value="WITHDRAWN">탈퇴</option>
                    </select>
                </div>
            </div>
            <div class="modal-footer">
                <button type="button" class="btn btn-primary" id="updateMemberStatusBtn">상태 변경</button>
                <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">닫기</button>
            </div>
        </div>
    </div>
</div>

</body>
</html>