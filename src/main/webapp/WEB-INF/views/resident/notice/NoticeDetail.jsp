<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8"/>
  <title>공지사항 상세</title>
  <!-- 외부 CSS 파일 로드 -->
  <link rel="stylesheet" href="<c:url value='/css/theme.css'/>"/>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/resident/common_residentDetail.css" />
	<style type="text/css">
	.detail-info p {
	  margin: 4px 0;
	  font-size: var(--font-body);
	}
	
	.label {
	  display: inline-block;
	  width: 50px;
	  font-weight: bold;
	  color: var(--color-text);
	}
	.label-sm {
	  width: 37px;  /* ✅ 일반 label보다 좁게 설정 */
	}
	</style>
</head>
<body>
  <div class="detail-container">
    <!-- 제목 및 메타 정보 -->
    <h2><c:out value="${notice.brdTitlNm}"/></h2>
		<div class="detail-info">
		  <p><span class="label">작성자:</span> <c:out value="${notice.member.mbrNnm}"/></p>
		  <p><span class="label">조회수:</span> <c:out value="${notice.brdVwCnt}"/></p>
		  <p><span class="label">작성일:</span>
		     <fmt:formatDate value="${convertedDate}" pattern="yyyy-MM-dd HH:mm"/>
		  </p>
		  <p><span class="label label-sm">유형:</span> <c:out value="${notice.noticeTypeCode.codeName}" /></p>
		</div>
    <hr/>

    <!-- 내용 -->
    <div class="detail-content">
      <p><c:out value="${notice.brdCont}"/></p>
    </div>
	
    <!-- 권한 기반 수정/삭제 버튼 -->
    <c:set var="isAdmin" value="false"/>
    <c:set var="isAuthor" value="${notice.mbrCd == mbrCd}" />
		
    <c:forEach var="role" items="${memRoleList}">
      <c:if test="${role.userRoleId == 'ADMIN'}">
        <c:set var="isAdmin" value="true"/>
      </c:if>
    </c:forEach>
		
    <c:if test="${isAdmin or isAuthor}">
      <div class="btn-group">
        <!-- 수정 버튼 -->
        <c:url var="formUrl" value="/resident/notice/form">
          <c:param name="noticeNo"     value="${notice.noticeNo}" />
          <c:param name="bldgIdParam"  value="${bldgIdParam}"  />
          <c:param name="page"         value="${page}"         />
          <c:param name="noticeType"   value="${noticeType}"   />
          <c:param name="searchType"   value="${searchType}"   />
          <c:param name="searchWord"   value="${searchWord}"   />
        </c:url>
        <a href="${formUrl}" class="edit-btn">수정</a>

        <!-- 삭제 버튼 -->
        <form id="deleteForm" method="post" action="<c:url value='/resident/notice/delete'/>" style="display:inline;">
		  <input type="hidden" name="noticeNo"    value="${notice.noticeNo}" />
		  <input type="hidden" name="bldgIdParam" value="${bldgIdParam}" />
		  <input type="hidden" name="page"        value="${page}" />
		  <input type="hidden" name="noticeType"  value="${noticeType}" />
		  <input type="hidden" name="searchType"  value="${searchType}" />
		  <input type="hidden" name="searchWord"  value="${searchWord}" />
		  <button type="button" id="deleteBtn" class="delete-btn">삭제</button>
		</form>
      </div>
    </c:if>

    <!-- 목록으로 돌아가기 -->
    <c:url var="listUrl" value="/resident/notice">
      <c:param name="bldgIdParam" value="${bldgIdParam}"/>
      <c:param name="noticeType"  value="${noticeType}"/>
      <c:param name="page"        value="${page}"/>
      <c:param name="searchType"  value="${searchType}"/>
      <c:param name="searchWord"  value="${searchWord}"/>
    </c:url>
    <div class="btn-group">
      <a href="${listUrl}" class="btn-default">목록</a>
    </div>
  </div>
  
  
<script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>
<script>
  document.getElementById('deleteBtn')?.addEventListener('click', function () {
    Swal.fire({
      title: '정말 삭제하시겠습니까?',
      text: '삭제 후에는 해당 공지글이 삭제됩니다..',
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
