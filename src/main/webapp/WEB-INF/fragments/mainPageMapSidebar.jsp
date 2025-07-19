<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 7.     		 윤현식         생성   
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<meta charset="UTF-8">
<div id="main-content">
	<nav class="sidebar-inner">
		<div class="sidebar-category-list">
			<c:forEach var="code" items="${categoryList}">
				<c:if test="${not empty code.codeValue and code.codeName ne '전체'}">
					<button type="button" class="category-btn"
						data-category="${code.codeValue}">
						<img
							src="${pageContext.request.contextPath}/volt/assets/img/icons/${code.codeValue}.svg"
							alt="${code.codeName}"> <span>${code.codeName}</span>
					</button>
				</c:if>
			</c:forEach>
		</div>
	</nav>
</div>