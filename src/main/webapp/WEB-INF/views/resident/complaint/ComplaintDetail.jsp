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
	</style>
</head>
<body>

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
<script src="${pageContext.request.contextPath}/app/js/resident/residentBuliding.js"></script>
</body>
</html>
