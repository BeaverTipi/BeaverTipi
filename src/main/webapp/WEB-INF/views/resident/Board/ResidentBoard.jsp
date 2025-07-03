<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>주민 게시판</title>
    <style>
        table { width:100%; border-collapse: collapse; }
        th, td { padding:8px; border:1px solid #ccc; text-align:center; }
        .paging { text-align:center; margin-top:16px; }
        .search-form { margin-bottom:16px; }
    </style>
</head>
<body>

<h1>주민 게시판</h1>

<form class="search-form" action="${pageContext.request.contextPath}/resident/board" method="get">
    <select name="searchType">
        <option value="TITLE"  <c:if test="${search.searchType == 'TITLE'}">selected</c:if>>제목</option>
        <option value="CONTENT"<c:if test="${search.searchType == 'CONTENT'}">selected</c:if>>내용</option>
        <option value="WRITER" <c:if test="${search.searchType == 'WRITER'}">selected</c:if>>작성자</option>
    </select>
    <input type="text" name="searchWord" value="${search.searchWord}" placeholder="검색어 입력" />
    <button type="submit">검색</button>
</form>

<table>
    <thead>
        <tr>
            <th>번호</th>
            <th>제목</th>
            <th>작성자</th>
            <th>작성일</th>
            <th>조회수</th>
        </tr>
    </thead>
    <tbody>
        <c:forEach var="board" items="${boardList}">
            <tr>
                <td>${board.rsdBrdId}</td>
                <td>
                    <a href="${pageContext.request.contextPath}/resident/board/detail?rsdBrdId=${board.rsdBrdId}">
                        ${board.rsdBrdTitl}
                    </a>
                </td>
                <td>${board.mbrCd}</td>
                <td>
                    <fmt:formatDate
                        value="${board.rsdBrdPblsDate}"
                        pattern="yyyy-MM-dd" />
                </td>
                <td>${board.rsdBrdCnt}</td>
            </tr>
        </c:forEach>
        <c:if test="${empty boardList}">
            <tr>
                <td colspan="5">등록된 게시글이 없습니다.</td>
            </tr>
        </c:if>
    </tbody>
</table>

<div class="paging">
    ${pagingHTML}
</div>

<div style="text-align:right; margin-top:16px;">
    <a href="${pageContext.request.contextPath}/resident/board/form">글쓰기</a>
</div>

</body>
</html>