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
			<th>회원명</th>
			<th>카테고리</th>  
			<th>상태</th>      
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty boardList}">
				<c:forEach items="${boardList}" var="board" varStatus="status">
					<tr>
						<td class="delete-column" style="display: none;">
						  <input type="checkbox" name="brdNoList" value="${board.brdNo}" class="delete-checkbox" />
						</td>
						<td>
							<c:out value="${(paging.currentPageNo - 1) * paging.pageSize + status.index + 1}" />
						</td>
						<td>
							<a href="${pageContext.request.contextPath}/admin/detail/qna?brdNo=${board.brdNo}">
								${board.brdTitlNm}
							</a>
						</td>
						<td><c:out value="${board.mbrNm}" /></td> 
						<td>
						  <c:choose>
						    <c:when test="${not empty board.qnaCtgryNm}">
						      <c:out value="${board.qnaCtgryNm}" />
						    </c:when>
						    <c:otherwise>-</c:otherwise>
						  </c:choose>
						</td>
						<td>
							<c:choose>
								<c:when test="${board.qnaStatusName == '대기'}">
									<span class="badge badge-secondary">대기</span>
								</c:when>
								<c:when test="${board.qnaStatusName == '처리 중'}">
									<span class="badge badge-warning">처리 중</span>
								</c:when>
								<c:when test="${board.qnaStatusName == '답변 완료'}">
									<span class="badge badge-success">답변 완료</span>
								</c:when>
								<c:otherwise>
									<span class="badge badge-light">
										<c:out value="${board.qnaStatusName}" />
									</span>
								</c:otherwise>
							</c:choose>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="5" class="no-data-center">등록된 QnA가 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>
