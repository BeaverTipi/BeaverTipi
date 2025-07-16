<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>${mode == 'edit' ? '게시글 수정' : '게시판 글쓰기'}</title>

  <!-- ✅ Summernote CSS/JS -->
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
      box-shadow: 0 0 8px rgba(0, 0, 0, 0.05);
      padding: 30px;
    }

    h1 {
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

    input[type="text"], select {
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

    .btn-group .save-btn {
      background-color: #fd7e14;
      color: white;
    }

    .btn-group .cancel-btn {
      background-color: #ccc;
      color: black;
    }

    .error-message {
      color: red;
      font-weight: bold;
      margin-bottom: 10px;
    }
  </style>
</head>
<body>

<div class="form-wrapper">
  <h1>
    <c:choose>
      <c:when test="${mode == 'edit'}">게시글 수정</c:when>
      <c:otherwise>${buildingName} 글쓰기</c:otherwise>
    </c:choose>
  </h1>

  <form:form modelAttribute="board"
             action="${pageContext.request.contextPath}/resident/board"
             method="post">
    <form:hidden path="rsdBrdId"/>
    <form:hidden path="mbrCd"/>
    <form:hidden path="brdCode" value="R0001"/>
    <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />

    <c:if test="${not empty error}">
      <p class="error-message">${error}</p>
    </c:if>

    <table>
      <c:choose>
        <c:when test="${mode == 'edit'}">
          <form:hidden path="bldgId"/>
          <tr>
            <th>건물</th>
            <td>${buildingName}</td>
          </tr>
        </c:when>
        <c:otherwise>
          <tr>
            <th>건물 선택</th>
            <td>
              <form:select path="bldgId">
                <form:option value="">건물 선택</form:option>
                <c:forEach var="unit" items="${unitList}">
                  <form:option value="${unit.bldgId}">
                    ${unit.building.bldgNm}
                  </form:option>
                </c:forEach>
              </form:select>
            </td>
          </tr>
        </c:otherwise>
      </c:choose>

      <tr>
        <th>제목</th>
        <td><form:input path="rsdBrdTitl" cssClass="form-control"/></td>
      </tr>

      <tr>
        <th>내용</th>
        <td><form:textarea path="rsdBrdCont" id="summernote"/></td>
      </tr>

      <c:if test="${mode == 'edit'}">
        <tr>
          <th>조회수</th>
          <td>${board.rsdBrdCnt}</td>
        </tr>
      </c:if>
    </table>

    <div class="btn-group">
      <button type="submit" class="save-btn">저장</button>
      <a href="<c:url value='/resident/board'>
                 <c:param name='bldgIdParam' value='${selectedBldgId}'/>
               </c:url>" class="cancel-btn">취소</a>
    </div>
  </form:form>
</div>

<script>
  $(document).ready(function () {
    $('#summernote').summernote({
      height: 300,
      placeholder: '내용을 입력하세요...',
      lang: 'ko-KR'
    });
  });

  document.addEventListener("DOMContentLoaded", () => {
    const form = document.querySelector("form");
    const mode = "${mode}";

    if (mode !== 'edit') {
      form.addEventListener("submit", function (e) {
        const buildingSelect = document.querySelector("select[name='bldgId']");
        if (buildingSelect && buildingSelect.value === "") {
          alert("건물을 선택해주세요.");
          e.preventDefault();
        }
      });
    }
  });
</script>
</body>
</html>
