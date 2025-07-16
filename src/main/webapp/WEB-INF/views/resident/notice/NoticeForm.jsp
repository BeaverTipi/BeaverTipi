<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="sec" uri="http://www.springframework.org/security/tags" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>공지사항 ${empty notice.noticeNo ? '등록' : '수정'}</title>

  <!-- ✅ Summernote CDN -->
  <link href="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.css" rel="stylesheet">
  <script src="https://code.jquery.com/jquery-3.6.0.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/dist/summernote-lite.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/summernote@0.8.18/lang/summernote-ko-KR.min.js"></script>

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

    h2 {
      font-size: 22px;
      margin-bottom: 20px;
    }

    .form-group {
      margin-bottom: 1.2rem;
    }

    label {
      display: block;
      font-weight: bold;
      margin-bottom: 0.5rem;
    }

    input[type="text"], select {
      width: 100%;
      padding: 10px;
      border: 1px solid #ccc;
      border-radius: 4px;
    }

    .checkbox-group label {
      margin-right: 1.5rem;
    }

    .write-buttons {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 20px;
    }

    .btn-orange {
      background-color: #fd7e14;
      color: white;
      border: none;
      padding: 10px 16px;
      border-radius: 4px;
      cursor: pointer;
    }

    .btn-gray {
      background-color: #ccc;
      color: black;
      padding: 10px 16px;
      text-decoration: none;
      border-radius: 4px;
      display: inline-block;
    }

    .info-box {
      margin: 10px 0;
      padding: 10px;
      background-color: #fff3cd;
      border-left: 4px solid #ffc107;
    }

    .error-box {
      margin-bottom: 10px;
      color: red;
      font-weight: bold;
    }

    .success-box {
      margin-bottom: 15px;
      padding: 10px;
      background-color: #d4edda;
      color: #155724;
      border: 1px solid #c3e6cb;
      border-radius: 4px;
    }
  </style>
</head>
<body>

<div class="form-wrapper">
  <h2>📢 공지사항 ${empty notice.noticeNo ? '등록' : '수정'}</h2>

  <c:if test="${not empty error}">
    <div class="error-box">${error}</div>
  </c:if>

  <c:if test="${not empty success}">
    <div class="success-box">✅ ${success}</div>
  </c:if>

  <form method="post" action="/resident/notice/form" onsubmit="return validateForm();">
    <sec:csrfInput/>
    <input type="hidden" name="bldgIdHidden" id="bldgIdHidden" />
    <c:if test="${not empty notice.noticeNo}">
      <input type="hidden" name="noticeNo" value="${notice.noticeNo}" />
    </c:if>

    <!-- 건물 선택 -->
    <div class="form-group">
      <label>건물 선택</label>
      <select name="bldgId" id="bldgSelector">
        <option value="">-- 건물 선택 --</option>
        <c:forEach var="unit" items="${unitList}">
          <option value="${unit.bldgId}">${unit.building.bldgNm}</option>
        </c:forEach>
      </select>
      <label style="margin-top: 8px; display: block;">
        <input type="checkbox" id="isAllNotice" /> 전체 건물 공지로 등록
      </label>
    </div>

    <c:if test="${notice.bldgId == null}">
      <div class="info-box">
        🏢 <strong>전체 공지</strong>로 등록된 상태입니다.
      </div>
    </c:if>

    <!-- 공지 유형 -->
    <div class="form-group">
      <label>공지 유형</label>
      <div class="checkbox-group">
        <c:forEach var="code" items="${noticeTypeList}">
          <label>
            <input type="radio" name="noticeType" value="${code.codeValue}" 
              <c:if test="${notice.noticeType eq code.codeValue}">checked</c:if> />
            ${code.codeName}
          </label>
        </c:forEach>
      </div>
    </div>

    <!-- 제목 -->
    <div class="form-group">
      <label>제목</label>
      <input type="text" name="brdTitlNm" value="${notice.brdTitlNm}" required />
    </div>

    <!-- 내용 (Summernote 적용) -->
    <div class="form-group">
      <label>내용</label>
      <textarea id="brdContEditor" name="brdCont" required>${notice.brdCont}</textarea>
    </div>

    <!-- 버튼 -->
    <div class="write-buttons">
      <button type="submit" class="btn-orange">${empty notice.noticeNo ? '등록' : '수정'}</button>
      <a href="${pageContext.request.contextPath}/resident/notice" class="btn-gray">취소</a>
    </div>

    <c:if test="${not empty notice.noticeType}">
      <div style="margin-top: 1rem; padding: 8px; background-color: #f8f9fa; border-left: 4px solid #007bff;">
        📌 이 공지는 <strong>${codeMap[notice.noticeType]}</strong> 유형입니다.
      </div>
    </c:if>
  </form>
</div>

<!-- Summernote 초기화 -->
<script>
  $(document).ready(function () {
    $('#brdContEditor').summernote({
      height: 250,
      placeholder: '공지사항 내용을 입력하세요...',
      lang: 'ko-KR'
    });
  });
</script>

<!-- 전체공지 체크 및 유효성 검사 -->
<script>
document.addEventListener('DOMContentLoaded', () => {
  const selector = document.getElementById("bldgSelector");
  const checkbox = document.getElementById("isAllNotice");
  const hiddenInput = document.getElementById("bldgIdHidden");
  const saved = localStorage.getItem("selectedBuildingId");

  if (saved && !checkbox.checked) {
    selector.value = saved;
    hiddenInput.value = saved;
  }

  checkbox.addEventListener("change", () => {
    if (checkbox.checked) {
      selector.disabled = true;
      selector.value = "";
      hiddenInput.value = "ALL";
    } else {
      selector.disabled = false;
      if (saved) {
        selector.value = saved;
        hiddenInput.value = saved;
      }
    }
  });

  selector.addEventListener("change", () => {
    hiddenInput.value = selector.value;
  });
});

function validateForm() {
  const bldgIdHidden = document.getElementById("bldgIdHidden").value;
  const types = document.getElementsByName("noticeType");
  let checked = Array.from(types).some(r => r.checked);

  if (!bldgIdHidden) {
    alert("📌 건물을 선택하거나 전체 공지를 체크하세요.");
    return false;
  }
  if (!checked) {
    alert("📌 공지 유형을 선택해 주세요.");
    return false;
  }
  return true;
}
</script>

</body>
</html>
