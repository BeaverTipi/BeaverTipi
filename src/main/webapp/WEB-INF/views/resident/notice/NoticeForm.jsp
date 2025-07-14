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
      width: 100%; padding: 0.5rem;
      border: 1px solid #ccc; border-radius: 4px;
    }
    textarea { height: 200px; resize: vertical; }
    .btn-submit {
      padding: 0.5rem 1rem;
      background-color: #007bff; color: white;
      border: none; border-radius: 4px;
    }
    .checkbox-group { margin-bottom: 1rem; }
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

<form method="post" action="/resident/notice/form" onsubmit="return validateForm();">
	<c:if test="${not empty notice.noticeNo}">
	  <input type="hidden" name="noticeNo" value="${notice.noticeNo}" />
	</c:if>
  <input type="hidden" name="bldgIdHidden" id="bldgIdHidden">
  <sec:csrfInput/>

  <!-- 🏢 건물 선택 + 전체 공지 체크 -->
  <div class="form-group">
    <label for="bldgId">건물 선택</label>
    <select name="bldgId" id="bldgSelector" required>
      <option value="">-- 건물 선택 --</option>
      <c:forEach var="unit" items="${unitList}">
        <option value="${unit.bldgId}">${unit.building.bldgNm}</option>
      </c:forEach>
    </select>

    <div class="checkbox-group">
      <label>
        <input type="checkbox" id="isAllNotice" /> 🏢 전체 건물 공지로 등록
      </label>
    </div>

    <!-- 전체공지 안내문 (조건부 출력) -->
    <c:if test="${notice.bldgId == null}">
      <div style="margin: 10px 0; padding: 10px; background-color: #fff3cd; border-left: 4px solid #ffc107;">
        🏢 <strong>이 공지는 전체 공지로 등록되어 있습니다.</strong><br/>
        건물 선택이 비활성화되며, 전체 입주민 대상입니다.
      </div>
    </c:if>
  </div>

  <!-- 📋 공지 유형 체크박스 그룹 -->
  <div class="form-group">
    <label for="noticeType">공지 유형 선택</label>
    <div class="checkbox-group">
      <c:forEach var="code" items="${noticeTypeList}">
        <label style="margin-right: 1rem;">
          <input type="radio" name="noticeType" value="${code.codeValue}" 
            <c:if test="${notice.noticeType eq code.codeValue}">checked</c:if>> ${code.codeName}
        </label>
      </c:forEach>
    </div>
  </div>

  <!-- 📝 제목 -->
  <div class="form-group">
    <label for="brdTitlNm">제목</label>
    <input type="text" name="brdTitlNm" value="${notice.brdTitlNm}" required />
  </div>

  <!-- 📄 내용 -->
  <div class="form-group">
    <label for="brdCont">내용</label>
    <textarea name="brdCont" required>${notice.brdCont}</textarea>
  </div>

  <!-- ▶ 등록 버튼 -->
  	<c:choose>
	  <c:when test="${empty notice.noticeNo}">
	    <button type="submit" class="btn-submit">등록</button>
	  </c:when>
	  <c:otherwise>
	    <button type="submit" class="btn-submit">수정</button>
	  </c:otherwise>
	</c:choose>


  <c:if test="${not empty notice.noticeType}">
    <div style="margin-top: 1rem; padding: 8px; background-color: #f8f9fa; border-left: 4px solid #007bff;">
      📌 안내: 이 글은 <strong>${codeMap[notice.noticeType]}</strong> 유형으로 등록됩니다.
    </div>
  </c:if>
</form>

<!-- 🧠 건물 자동 선택 스크립트 + 전체 체크 연동 -->
<script>
document.addEventListener('DOMContentLoaded', () => {
  const selector = document.getElementById("bldgSelector");
  const checkbox = document.getElementById("isAllNotice");
  const hiddenInput = document.getElementById("bldgIdHidden");
  const bldgId = localStorage.getItem("selectedBuildingId");

  if (bldgId && selector && !checkbox.checked) {
    selector.value = bldgId;
    hiddenInput.value = bldgId;
  }

  checkbox.addEventListener("change", () => {
    if (checkbox.checked) {
      selector.disabled = true;
      selector.value = "";
      hiddenInput.value = "ALL";
    } else {
      selector.disabled = false;
      if (bldgId) {
        selector.value = bldgId;
        hiddenInput.value = bldgId;
      }
    }
  });

  selector.addEventListener("change", () => {
    hiddenInput.value = selector.value;
  });
});
</script>

<script>
function validateForm() {
  const bldgIdHidden = document.getElementById("bldgIdHidden").value;
  const noticeTypeRadios = document.getElementsByName("noticeType");
  let noticeTypeSelected = false;

  for (let radio of noticeTypeRadios) {
    if (radio.checked) {
      noticeTypeSelected = true;
      break;
    }
  }

  if (!bldgIdHidden || bldgIdHidden.trim() === "") {
    alert("📌 건물을 선택하거나 전체 공지로 체크해야 합니다.");
    return false;
  }

  if (!noticeTypeSelected) {
    alert("📌 공지 유형을 선택해 주세요.");
    return false;
  }

  return true;
}
</script>
<script src="${pageContext.request.contextPath}/app/js/building/move-in/residentList.js"></script>
</body>
</html>
