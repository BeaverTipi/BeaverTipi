<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>
<%@ taglib prefix="fmt" uri="jakarta.tags.fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>${board.rsdBrdTitl}</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_residentDetail.css" />
	<style type="text/css">
	
		.label {
	  display: inline-block;
	  width: 50px;
	  font-weight: bold;
	  color: var(--color-text);
	}
	.label-sm {
	  width: 37px;  /* ✅ 일반 label보다 좁게 설정 */
	}
	.label-dt{
	 width: 48px;
	}
	</style>

</head>
<body>

  <div class="detail-container">
    <!-- 🔸 제목 -->
    <h2>${board.rsdBrdTitl}</h2>

    <!-- 🔸 작성 정보 -->
    <div class="detail-info">
      <p><span class="label label-sm">건물:</span>${board.bldgNm}</p>
      <p><span class="label">조회수:</span>${board.rsdBrdCnt}</p>
      <p><span class="label">작성자:</span>${board.mbrNnm}</p>
      <p><span class="label label-dt">작성일:</span>
        <fmt:formatDate value="${board.rsdBrdPblsDate}" pattern="yyyy-MM-dd HH:mm" />
      </p>
    </div>

    <hr/>

    <!-- 🔸 본문 내용 -->
    <div class="detail-content">
      <p><c:out value="${board.rsdBrdCont}" escapeXml="false" /></p>
    </div>

    <!-- 🔸 버튼 영역 -->
    <div class="btn-group">
		<a href="#" id="goListBtn">목록</a>

      <c:if test="${board.mbrCd eq loginUser.mbrCd}">
		<a href="${pageContext.request.contextPath}/resident/board/form?rsdBrdId=${board.rsdBrdId}&bldgIdParam=${selectedBldgId}&page=${page}" id="goEditBtn">수정</a>

        <!-- 삭제 버튼 -->
		<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/resident/board/delete" style="display:inline;">
		  <input type="hidden" name="rsdBrdId" value="${board.rsdBrdId}" />
		  <input type="hidden" name="bldgIdParam" value="${selectedBldgId}" />
		  <button type="button" id="deleteBtn" class="delete-btn">삭제</button>
		</form>
      </c:if>
    </div>
  </div>
  
<!-- SweetAlert2 CDN -->
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  document.getElementById('deleteBtn')?.addEventListener('click', function () {
    Swal.fire({
      title: '정말 삭제하시겠습니까?',
      text: '삭제 후에는 해당 게시글이 삭제됩니다.',
      icon: 'warning',
      showCancelButton: true,
      confirmButtonColor: '#E17100',
      cancelButtonColor: '#aaa',
      confirmButtonText: '네, 삭제합니다',
      cancelButtonText: '취소'
    }).then((result) => {
      if (result.isConfirmed) {
        document.getElementById('deleteForm').submit();
      }
    });
  });
</script>

<script>
  document.getElementById("goListBtn").addEventListener("click", function (e) {
    e.preventDefault();

    const selectedBldgId = localStorage.getItem("selectedBuildingId") || "${selectedBldgId}";
    const page = "${page}" || "1"; // 기본값 1

    // 이동
    const url = `${pageContext.request.contextPath}/resident/board?bldgIdParam=${selectedBldgId}&page=${page}`;
    window.location.href = url;
  });
</script>





<!-- JSP 파일의 하단 -->

</body>
</html>
