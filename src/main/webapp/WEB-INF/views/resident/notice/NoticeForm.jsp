<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html>
<head>
  <title>공지사항 등록</title>
  <style>
    .form-group { margin-bottom: 1rem; }
    label { display: block; font-weight: bold; margin-bottom: 0.5rem; }
    input[type="text"], select, textarea {
      width: 100%; padding: 0.5rem; border: 1px solid #ccc; border-radius: 4px;
    }
    textarea { height: 200px; resize: vertical; }
    .btn-submit { padding: 0.5rem 1rem; background-color: #007bff; color: white; border: none; border-radius: 4px; }
  </style>
</head>
<body>

<h2>📢 공지사항 등록</h2>

<c:if test="${not empty error}">
  <div style="color: red;">${error}</div>
</c:if>

<c:if test="${not empty success}">
  <div style="margin-bottom: 15px; padding: 10px; background-color: #d4edda; color: #155724; border: 1px solid #c3e6cb; border-radius: 4px;">
    ✅ ${success}
  </div>
</c:if>


<form method="post" action="/resident/notice/form">
	<sec:csrfInput/>
  <!-- 건물 선택 -->
  <div class="form-group">
    <label for="bldgId">건물 선택</label>
    <select name="bldgId" required>
      <option value="">-- 건물 선택 --</option>
      <c:forEach var="unit" items="${unitList}">
        <option value="${unit.bldgId}" <c:if test="${selectedBldgId eq unit.bldgId}">selected</c:if>>
          ${unit.building.bldgNm}
        </option>
      </c:forEach>
    </select>
  </div>

  <!-- 공지 유형 -->
  <div class="form-group">
    <label for="noticeType">공지 유형</label>
    <select name="noticeType" required>
      <option value="">-- 유형 선택 --</option>
      <c:forEach var="code" items="${noticeTypeList}">
        <option value="${code.codeValue}" <c:if test="${notice.noticeType eq code.codeValue}">selected</c:if>>
          ${code.codeName}
        </option>
      </c:forEach>
    </select>
  </div>

  <!-- 제목 -->
  <div class="form-group">
    <label for="brdTitlNm">제목</label>
    <input type="text" name="brdTitlNm" value="${notice.brdTitlNm}" required />
  </div>

  <!-- 내용 -->
  <div class="form-group">
    <label for="brdCont">내용</label>
    <textarea name="brdCont" required>${notice.brdCont}</textarea>
  </div>

  <!-- 등록 버튼 -->
  <div class="form-group">
    <button type="submit" class="btn-submit">등록</button>
	<button type="button" onclick="location.href='/resident/notice'">취소</button>
  </div>
  
</form>

</body>
</html>