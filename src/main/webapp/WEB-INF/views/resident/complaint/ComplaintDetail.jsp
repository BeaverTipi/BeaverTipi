<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<html>
<head>
  <title>민원 상세</title>
  <link rel="stylesheet" href="<c:url value='/css/style.css'/>" />
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>" />
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_residentDetail.css" />
  <style type="text/css">
	
		.label {
	  display: inline-block;
	  width: 50px;
	  font-weight: bold;
	  color: var(--color-text);
	}
	.label-sm {
	  width: 60px;  /* ✅ 일반 label보다 좁게 설정 */
	}
	.label-dt{
	 width: 60px;
	}
	.reply-box {
	  border-left: 5px solid #2a8a43;
	  background-color: #f4fdf6;
	  padding: 16px;
	  margin-top: 20px;
	  border-radius: 6px;
	}
	
	</style>
</head>
<body>
	<h5>임대인 여부: ${isLandlord}</h5>
  <div class="detail-container">
    <h2>${complaint.rsdBrdTitl}</h2>

    <div class="detail-info">
      <p><span class="label">작성자:</span> ${complaint.mbrNnm}</p>
      <p><span class="label">게시일:</span>
        <fmt:formatDate value="${complaint.rsdBrdPblsDate}" pattern="yyyy-MM-dd HH:mm"/>
      </p>
      <p><span class="label">수정일:</span>
        <fmt:formatDate value="${complaint.rsdBrdModDate}" pattern="yyyy-MM-dd HH:mm"/>
      </p>
      <p><span class="label label-dt">공개여부:</span>
		  <c:forEach var="code" items="${openYnList}">
		    <c:if test="${code.codeValue eq complaint.openYn}">
		      ${code.codeName}
		    </c:if>
		  </c:forEach>
		</p>
      <p><span class="label label-sm">처리상태:</span>
		  <c:forEach var="code" items="${reqStatusList}">
		    <c:if test="${code.codeValue eq complaint.reqStatus}">
		      ${code.codeName}
		    </c:if>
		  </c:forEach>
		</p>
    </div>

    <hr/>

    <div class="detail-content">
     <div class="content-html">${complaint.rsdBrdCont}</div>
    </div>
	
	<!-- ✅ 답변 내용 표시 -->
	<c:if test="${not empty complaint.replyCont}">
	  <hr/>
	  <div class="reply-box" style="margin-top: 20px; background: #f4fdf6; padding: 15px; border-radius: 8px; border-left: 5px solid #2a8a43;">
	    <h4 style="margin-bottom: 10px; color: #2a8a43;">🛠 처리 답변</h4>
	    <div class="reply-content" style="white-space: pre-wrap;">${complaint.replyCont}</div>
	  </div>
	</c:if>
	
	<!-- ✅ 답변 작성 폼: 임대인만 표시 -->
	<c:if test="${isLandlord}">
	  <button type="button" id="toggleReplyBtn" class="button button-orange" style="margin-top: 20px;">답글 달기</button>
	
	  <form id="replyForm"
	        method="post"
	        action="${pageContext.request.contextPath}/resident/complaint/reply"
	        style="display: none; margin-top: 10px;">
	    <textarea name="replyCont" rows="5" style="width: 100%; padding: 8px;"></textarea>
	    <input type="hidden" name="rsdBrdId" value="${complaint.rsdBrdId}" />
	    <input type="hidden" name="bldgIdParam" value="${complaint.bldgId}" />
	    <button type="submit" class="button button-success" style="margin-top: 8px;">답글 저장</button>
	  </form>
	</c:if>
	
    <div class="btn-group">
      <c:if test="${loginMember.mbrCd == complaint.mbrCd}">
        <a class="button button-success"
           href="${pageContext.request.contextPath}/resident/complaint/form?rsdBrdId=${complaint.rsdBrdId}&bldgIdParam=${complaint.bldgId}">수정</a>

       <form id="deleteForm" action="${pageContext.request.contextPath}/resident/complaint/delete" method="post" style="display:inline;">
		  <input type="hidden" name="rsdBrdId" value="${complaint.rsdBrdId}"/>
		  <input type="hidden" name="bldgIdParam" value="${complaint.bldgId}"/>
		  <button type="button" class="button button-danger" id="deleteBtn">삭제</button>
		</form>
      </c:if>

      <a class="button button-primary"
         href="${pageContext.request.contextPath}/resident/complaint?bldgIdParam=${complaint.bldgId}">목록</a>
    </div>
  </div>


<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

<script>
  const toggleBtn = document.getElementById("toggleReplyBtn");
  if (toggleBtn) {
    toggleBtn.addEventListener("click", function () {
      const form = document.getElementById("replyForm");
      if (form) {
        form.style.display = form.style.display === "none" ? "block" : "none";
      }
    });
  }
</script>

<script>
  document.getElementById('deleteBtn').addEventListener('click', function () {
    Swal.fire({
      title: '정말 삭제하시겠습니까?',
      text: '삭제 후에는 해당 게시글이 삭제됩니다!',
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
</body>
</html>
