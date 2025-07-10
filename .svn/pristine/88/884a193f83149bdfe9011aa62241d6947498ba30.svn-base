<%@ page language="java" contentType="text/html; charset=UTF-8"
         pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>

<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>${mode == 'edit' ? '게시글 수정' : '게시글 등록'}</title>
  <style>
    table { width:600px; border-collapse: collapse; margin-top:20px; }
    th, td { padding:8px; border:1px solid #ccc; }
    th { background:#f4f4f4; text-align:left; width:120px; }
    .btn-group { margin-top:20px; }
    .btn-group button, .btn-group a {
      margin-right: 8px; padding:6px 12px; text-decoration:none;
    }
  </style>
</head>
<body>

  <h1>
  <c:choose>
    <c:when test="${mode == 'edit'}">게시글 수정</c:when>
    <c:otherwise>${buildingName} 게시판에 글쓰기</c:otherwise>
  </c:choose>
</h1>


<form:form modelAttribute="board"
           action="${pageContext.request.contextPath}/resident/board"
           method="post">
  <form:hidden path="rsdBrdId"/>
  <form:hidden path="mbrCd"/>
  <form:hidden path="brdCode" value="R0001"/>

  <!-- ① bldgIdParam 히든 필드 추가 -->
<%--   <form:hidden path="bldgIdParam" value="${selectedBldgId}"/> --%>
<input type="hidden"
       name="bldgIdParam"
       value="${selectedBldgId}" />

  <table>
    <c:choose>
      <c:when test="${mode == 'edit'}">
        <!-- 수정 모드 땐 bldgIdParam 으로 건물을 고정 -->
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
        </c:otherwise>
    </c:choose>
      <tr>
        <th>제목</th>
        <td><form:input path="rsdBrdTitl" cssClass="input-text"/></td>
      </tr>
      <tr>
        <th>내용</th>
        <td><form:textarea path="rsdBrdCont" rows="10" cols="60"/></td>
      </tr>
      <c:if test="${mode == 'edit'}">
        <tr>
          <th>조회수</th>
          <td>${board.rsdBrdCnt}</td>
        </tr>
      </c:if>
    </table>

    <div class="btn-group">
	    <button type="submit">저장</button>
	    <!-- ② 취소 링크에도 bldgIdParam 붙이기 -->
		    <a href="<c:url value='/resident/board'>
		               <c:param name='bldgIdParam' value='${selectedBldgId}'/>
		             </c:url>">
		      	취소
	    	</a>
  	</div>
</form:form>




</body>
</html>