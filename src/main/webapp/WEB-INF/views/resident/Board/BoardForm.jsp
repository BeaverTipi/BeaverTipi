<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>    <!-- 수정: JSTL core URI -->
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>

<!DOCTYPE html>                                          <!-- 수정: ‘!’ 앞에 '<' 추가 -->
<html>
<head>
  <meta charset="UTF-8">
  <title>새 게시글 작성</title>
  <style>
    table { width:600px; border-collapse: collapse; margin-top:20px; }
    th, td { padding:8px; border:1px solid #ccc; }
    th { background:#f4f4f4; text-align:left; width:120px; }
    .btn-group { margin-top:20px; }
    .btn-group button,
    .btn-group a { margin-right: 8px; padding:6px 12px; text-decoration:none; }
  </style>
</head>
<body>

  <h1>글쓰기</h1>

  <form:form modelAttribute="board"
             action="${pageContext.request.contextPath}/resident/board"
             method="post">

    <table>
      <tr>
        <th>제목</th>
        <td>
          <form:input path="rsdBrdTitl" cssClass="input-text" />
        </td>
      </tr>
      <tr>
        <th>내용</th>
        <td>
          <form:textarea path="rsdBrdCont" rows="10" cols="60" />
        </td>
      </tr>
      <tr>
        <th>게시판 종류</th>                             <!-- 수정: ‘게시판 코드’ → ‘게시판 종류’ -->
        <td>
          <form:select path="brdCode">                  <!-- 수정: select 박스로 변경 -->
            <form:option value="" label="--선택--" />
            <form:option value="RESIDENT" label="주민 게시판" />
            <form:option value="NOTICE"   label="공지사항" />
            <form:option value="FREE"     label="자유 게시판" />
          </form:select>
        </td>
      </tr>
      <!-- 작성자 코드는 세션에서 자동 주입하므로 히든 필드로 처리 -->
      <form:hidden path="mbrCd"/>                      <!-- 수정: 화면에서 숨김 -->
    </table>

    <div class="btn-group">
      <button type="submit">등록</button>
      <a href="${pageContext.request.contextPath}/resident/board">취소</a>
    </div>

  </form:form>

</body>
</html>
