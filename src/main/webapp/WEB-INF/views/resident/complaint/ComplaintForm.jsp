<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>
    민원
    <c:choose>
      <c:when test="${empty complaint.rsdBrdId}">등록</c:when>
      <c:otherwise>수정</c:otherwise>
    </c:choose>
  </title>

  <!-- ✅ Summernote + jQuery -->
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>

  <style>
    body {
      font-family: 'Noto Sans KR', sans-serif;
      background: #f4f6f8;
      padding: 40px;
      color: #333;
    }

    .form-wrapper {
      max-width: 800px;
      margin: 0 auto;
      background: #fff;
      border-radius: 6px;
      box-shadow: 0 0 8px rgba(0,0,0,0.05);
      padding: 30px;
    }

    h2 {
      font-size: 24px;
      margin-bottom: 24px;
    }

    table {
      width: 100%;
      border-collapse: collapse;
    }

    th, td {
      padding: 12px;
      border-bottom: 1px solid #eee;
      vertical-align: top;
    }

    th {
      width: 120px;
      background: #fafafa;
      text-align: left;
      font-weight: 600;
    }

    input[type="text"], textarea, select {
      width: 100%;
      padding: 8px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    .btn-group {
      margin-top: 20px;
      display: flex;
      justify-content: flex-end;
      gap: 10px;
    }

    .btn-group button,
    .btn-group a {
      padding: 10px 16px;
      font-size: 14px;
      text-decoration: none;
      border-radius: 4px;
      border: none;
      cursor: pointer;
      min-width: 100px;
      text-align: center;
    }

    .btn-group .btn-success {
      background-color: #fd7e14;
      color: white;
    }

    .btn-group .btn-primary {
      background-color: #ccc;
      color: black;
    }

    label {
      margin-right: 15px;
    }
  </style>
</head>
<body>

<div class="form-wrapper">
  <h2>
    <c:choose>
      <c:when test="${empty complaint.rsdBrdId}">민원 등록</c:when>
      <c:otherwise>민원 수정</c:otherwise>
    </c:choose>
  </h2>

  <form action="${pageContext.request.contextPath}/resident/complaint/save" method="post">
    <!-- 기본 키값 -->
    <input type="hidden" name="rsdBrdId"    value="${complaint.rsdBrdId}" />
    <input type="hidden" name="bldgId"      value="${complaint.bldgId}" />
    <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />

    <table>
      <tr>
        <th>제목</th>
        <td>
          <input type="text" name="rsdBrdTitl" value="${complaint.rsdBrdTitl}" required />
        </td>
      </tr>

      <tr>
        <th>내용</th>
        <td>
          <textarea name="rsdBrdCont" id="summernote">${complaint.rsdBrdCont}</textarea>
        </td>
      </tr>

      <tr>
        <th>공개여부</th>
        <td>
          <c:forEach var="c" items="${openYnList}">
            <label>
              <input type="radio" name="openYn" value="${c.codeValue}"
                     <c:if test="${c.codeValue eq complaint.openYn}">checked</c:if> />
              ${c.codeName}
            </label>
          </c:forEach>
        </td>
      </tr>
    </table>

    <div class="btn-group">
      <button class="btn btn-success" type="submit">저장</button>
      <a class="btn btn-primary" href="${pageContext.request.contextPath}/resident/complaint?bldgIdParam=${selectedBldgId}">취소</a>
    </div>
  </form>
</div>

<script>
  $(document).ready(function () {
    $('#summernote').summernote({
      height: 300,
      placeholder: '내용을 입력하세요...',
      lang: 'ko-KR'
    });
  });
</script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/buildingSelect.js"></script>
</body>
</html>
