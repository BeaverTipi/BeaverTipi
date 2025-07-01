<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>    
<!DOCTYPE html>
<html>
<head>
<title>회원 목록</title>
<style>
    /* 기본적인 스타일링을 추가하여 이미지와 유사하게 보이도록 합니다. */
    body {
        font-family: Arial, sans-serif;
        margin: 20px;
        background-color: #f9f9f9;
    }
    .container {
        background-color: white;
        border: 1px solid #ddd;
        border-radius: 8px;
        padding: 20px;
        box-shadow: 0 2px 5px rgba(0,0,0,0.05);
    }
    .search-area {
        display: flex;
        flex-wrap: wrap; /* 작은 화면에서 줄바꿈 */
        gap: 20px 30px; /* 행과 열 사이의 간격 */
        margin-bottom: 30px;
        padding-bottom: 20px;
        border-bottom: 1px solid #eee;
    }
    .search-item {
        display: flex;
        flex-direction: column;
        gap: 5px;
    }
    .search-item label {
        font-weight: bold;
        color: #555;
        font-size: 0.9em;
    }
    .search-item select,
    .search-item input[type="text"] {
        padding: 8px 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
        font-size: 1em;
        min-width: 150px; /* 검색 입력 필드 최소 너비 */
    }
    .table-container {
        overflow-x: auto; /* 테이블이 너무 넓을 경우 스크롤바 생성 */
    }
    .table {
        width: 100%;
        border-collapse: collapse;
        margin-top: 20px;
    }
    .table th, .table td {
        border: 1px solid #eee;
        padding: 12px 15px;
        text-align: left;
    }
    .table thead th {
        background-color: #f2f2f2;
        font-weight: bold;
        color: #333;
    }
    .table tbody tr:nth-child(even) {
        background-color: #fcfcfc;
    }
    .table tbody tr:hover {
        background-color: #f0f8ff; /* 호버 시 배경색 변경 */
    }
    .table tbody td a {
        color: #007bff;
        text-decoration: none;
    }
    .table tbody td a:hover {
        text-decoration: underline;
    }
    .no-data-center {
        text-align: center !important;
    }
</style>
<script>
	document.addEventListener("DOMContentLoaded", ()=>{
		// 필요한 JavaScript 코드를 여기에 추가할 수 있습니다.
		// 예: 검색 필드 초기화, 동적 데이터 로드 등
	});
</script>
</head>
<body>
<div class="container">
    <div class="search-area">
        <div class="search-item">
            <label for="memberTypeSelect">회원구분</label>
            <select id="memberTypeSelect">
                <option value="">--선택--</option>
                <option value="입주민">입주민</option>
                <option value="임차인">임차인</option>
                <option value="중개인">중개인</option>
                </select>
        </div>
        <div class="search-item">
            <label for="memberNameInput">회원명</label>
            <input type="text" id="memberNameInput" placeholder="회원명을 입력해주세요">
        </div>
        <div class="search-item">
            <label for="mbrFrstRegDtFrom"></label>
            <input type="date" id="mbrFrstRegDtFrom">
        </div>
        <div class="search-item">
            <label for="mbrFrstRegDtTo"></label>
            <input type="date" id="mbrFrstRegDtTo">
        </div>
        <div class="search-item">
            <label for="memberStatusSelect">회원상태</label>
            <select id="memberStatusSelect">
                <option value="">--선택--</option>
                <option value="정상">정상</option>
                <option value="정지">정지</option>
                <option value="탈퇴">탈퇴</option>
            </select>
        </div>
        <div class="search-item">
            <label for="memberEmailInput">이메일</label>
            <input type="text" id="memberEmailInput" placeholder="이메일을 입력해주세요">
        </div>
    </div>

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
                <c:if test="${not empty memberList }">
                    <c:forEach items="${memberList }" var="member">
                        <tr>
<%--                             상세 페이지 링크는 필요에 따라 구현 (예: memberDetail.do) --%>
<%--                             <c:url value="/member/memberDetail.do" var="detailURL"> --%>
<%--                                 <c:param name="what" value="${member.memberId }" /> --%>
<%--                             </c:url> --%>
                            <td>${member.memCd }</td>
                            <td>${member.memNm }</td>
                            <td>${member.memFrstRegDt }</td>
                            <td>${member.memMbrStatusCode }</td>
                            <td>${member.memMbrEmlAddr }</td>
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