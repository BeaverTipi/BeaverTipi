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
	    padding: 10px;
	    border: 1px solid #ccc;
	    border-radius: 4px;
	  }
	
	  /* 제목과 건물 선택 너비 통일 */
	  input[name="brdTitlNm"],
	  select[name="bldgId"] {
	    width: 50%;
	  }
    .checkbox-group {
	  display: flex;
	  gap: 20px;  /* 라디오 항목 사이 간격 */
	  flex-wrap: wrap;  /* 항목이 많을 경우 줄바꿈 허용 */
	}
    
	.checkbox-group label {
	  margin-right: 0;  /* 기존 여백 제거 */
	  display: flex;
	  align-items: center;
	  gap: 6px;
	}
    .write-buttons {
      display: flex;
      justify-content: flex-end;
      gap: 10px;
      margin-top: 20px;
    }

	.btn-orange {
	  background-color: #E17100;  /* 기본 오렌지 */
	  color: white;
	  border: none;
	  padding: 10px 16px;
	  border-radius: 4px;
	  cursor: pointer;
	  transition: background-color 0.3s ease;
	}
	
	.btn-orange:hover {
	  background-color: #973C00;  /* Hover 진한 오렌지 */
	}
	.btn-gray {
	  background-color: #ccc;     /* 기본 회색 */
	  color: black;
	  padding: 10px 16px;
	  text-decoration: none;
	  border-radius: 4px;
	  display: inline-block;
	  transition: background-color 0.3s ease;
	}
	
	.btn-gray:hover {
	  background-color: #999;     /* Hover 어두운 회색 */
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

  <form method="post" action="/resident/notice/form" id="noticeForm">
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
      <button type="submit" class="btn-orange">${empty notice.noticeNo ? '저장' : '수정'}</button>
      <a href="#" class="btn-gray" id="cancelBtn">취소</a>
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

<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
  document.getElementById('cancelBtn')?.addEventListener('click', function (e) {
    e.preventDefault();
    const isEdit = '${not empty notice.noticeNo}';
    Swal.fire({
      title: isEdit === 'true' ? '공지 수정을 취소하시겠습니까?' : '공지 작성을 취소하시겠습니까?',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#E17100',
      cancelButtonColor: '#aaa',
      confirmButtonText: '네, 취소합니다',
      cancelButtonText: '아니요'
    }).then((result) => {
      if (result.isConfirmed) {
        window.location.href = '${pageContext.request.contextPath}/resident/notice';
      }
    });
  });
</script>
<script>
  document.addEventListener("DOMContentLoaded", function () {
    const form = document.getElementById("noticeForm");
    const submitBtn = document.querySelector(".btn-orange");

    submitBtn.addEventListener("click", function (e) {
      e.preventDefault(); // 기본 제출 막기

      const bldgIdHidden = document.getElementById("bldgIdHidden").value;
      const types = document.getElementsByName("noticeType");
      const isEdit = ${not empty notice.noticeNo};
      const checked = Array.from(types).some(r => r.checked);

      // ✅ 유효성 검사
      if (!bldgIdHidden) {
        Swal.fire({
          icon: 'warning',
          title: '건물 선택이 필요합니다',
          text: '건물을 선택하거나 전체 공지를 체크해 주세요',
          confirmButtonColor: '#E17100'
        });
        return;
      }

      if (!checked) {
        Swal.fire({
          icon: 'warning',
          title: '공지 유형을 선택해 주세요',
          confirmButtonColor: '#E17100'
        });
        return;
      }

      // ✅ SweetAlert 확인창
      Swal.fire({
        title: isEdit ? '공지사항을 수정하시겠습니까?' : '공지사항을 등록하시겠습니까?',
        icon: 'question',
        showCancelButton: true,
        confirmButtonColor: '#E17100',
        cancelButtonColor: '#aaa',
        confirmButtonText: '네, 진행합니다',
        cancelButtonText: '아니요'
      }).then((result) => {
        if (result.isConfirmed) {
          form.submit(); // 통과 시 폼 제출
        }
      });
    });
  });
</script>
<script src="${pageContext.request.contextPath}/app/js/resident/residentBuliding.js"></script>
</body>
</html>
