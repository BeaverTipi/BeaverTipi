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
      background-color: #E17100;
      color: white;
    }
	
	.btn-group .save-btn:hover {
	  background-color: #973C00; /* Hover 시 진한 주황색 */
	}
	
    .btn-group .cancel-btn {
      background-color: #ccc;
      color: black;
    }
	
	.btn-group .cancel-btn:hover {
	  background-color: #999; /* Hover 시 어두운 회색 */
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
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>

const form = document.querySelector("form");
const saveBtn = document.querySelector(".save-btn");

saveBtn.addEventListener("click", function (e) {
	  e.preventDefault();

	  const mode = "${mode}";
	  const buildingSelect = document.querySelector("select[name='bldgId']");
	  const titleInput = document.querySelector("input[name='rsdBrdTitl']");
	  const content = $('#summernote').summernote('code');

	  // ✅ 건물 선택 필수 (글쓰기 모드일 때만)
	  if (mode !== 'edit' && buildingSelect && buildingSelect.value === "") {
	    Swal.fire({
	      icon: 'warning',
	      title: '건물 선택이 필요합니다',
	      text: '건물을 선택해주세요.',
	      confirmButtonColor: '#E17100'
	    });
	    return;
	  }

	  // ✅ 제목 필수
	  if (!titleInput.value.trim()) {
	    Swal.fire({
	      icon: 'warning',
	      title: '제목이 비어있습니다',
	      text: '제목을 입력해주세요.',
	      confirmButtonColor: '#E17100'
	    });
	    return;
	  }

	  // ✅ 내용 필수 (summernote는 <p><br></p> 처럼 내용 없는 HTML도 있음 → 제거 후 확인)
	  const plainText = $('<div>').html(content).text().trim();
	  if (!plainText) {
	    Swal.fire({
	      icon: 'warning',
	      title: '내용이 비어있습니다',
	      text: '내용을 입력해주세요.',
	      confirmButtonColor: '#E17100'
	    });
	    return;
	  }

	  // ✅ 모두 통과 → 저장 확인창
	  Swal.fire({
	    title: mode === 'edit' ? '게시글을 수정하시겠습니까?' : '게시글을 등록하시겠습니까?',
	    icon: 'question',
	    showCancelButton: true,
	    confirmButtonColor: '#E17100',
	    cancelButtonColor: '#aaa',
	    confirmButtonText: '네, 진행합니다',
	    cancelButtonText: '취소'
	  }).then((result) => {
	    if (result.isConfirmed) {
	      form.submit();
	    }
	  });
	});

</script>
<script>
  $(document).ready(function () {
    $('#summernote').summernote({
      height: 300,
      placeholder: '내용을 입력하세요...',
      lang: 'ko-KR'
    });
  });
</script>
<script>
  document.addEventListener("DOMContentLoaded", () => {
    const buildingSelect = document.querySelector("select[name='bldgId']");
    const selectedBldgId = localStorage.getItem("selectedBuildingId");

    // 페이지가 '글쓰기' 모드이고, selectedBldgId가 존재한다면 선택 처리
    if (buildingSelect && selectedBldgId) {
      buildingSelect.value = selectedBldgId;
    }
  });
</script>

<script src="https://cdnjs.cloudflare.com/ajax/libs/summernote/0.8.18/lang/summernote-ko-KR.min.js"></script>
</body>
</html>
