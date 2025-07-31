<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 22.     		 윤현식            
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<form id="deleteForm" method="post" action="${pageContext.request.contextPath}/admin/notice/delete">
  <input type="hidden" name="tab" value="${activeTab}" />
  <input type="hidden" id="deleteMode" value="false" />
  
<table class="table">
	<thead>
		<tr>
			<th class="delete-column" style="display: none;">
			  <input type="checkbox" id="selectAll" />
			</th>
			<th>No</th>
			<th>제목</th>
			<th>게시일자</th>
			<th>카테고리</th> 
			<th>첨부</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty boardList}">
				<c:forEach items="${boardList}" var="board" varStatus="status">
					<tr class="faq-item" data-index="${status.index}">
						<td class="delete-column" style="display: none;">
						  <input type="checkbox" name="brdNoList" value="${board.brdNo}" class="delete-checkbox" />
						</td>
						<td>
							<c:out value="${(paging.currentPageNo - 1) * paging.pageSize + status.index + 1}" />
						</td>
						<td class="faq-title" style="cursor:pointer;">
							<c:out value="${board.brdTitlNm}" />
						</td>
						<td><c:out value="${board.adFormattedBrdPblsDtm}" /></td>
						<td>
						  <c:choose>
						    <c:when test="${not empty board.faqCtgryNm}">
						      <c:out value="${board.faqCtgryNm}" />
						    </c:when>
						    <c:otherwise>-</c:otherwise>
						  </c:choose>
						</td>
						<td>
						  <c:if test="${not empty board.attachFiles}">
						    <img src="${pageContext.request.contextPath}/volt/assets/img/file-download-svgrepo-com.svg"
						         width="20" height="20" alt="첨부파일" />
						  </c:if>
						</td>
					</tr>
					<tr class="faq-content" data-index="${status.index}" style="display:none;">
						<td colspan="5">
							<div class="faq-answer-box">
								${board.brdCont}
							</div>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="5" class="no-data-center">등록된 FAQ가 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

