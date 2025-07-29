<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 18.     		 윤현식            
 *
-->
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<table class="table">
	<thead>
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>게시일자</th>
			<th>종료일자</th>
			<th>공지상태</th>
			<th>첨부</th>
		</tr>
	</thead>
	<tbody>
		<c:choose>
			<c:when test="${not empty boardList}">
				<c:forEach items="${boardList}" var="board" varStatus="status">
					<tr>
						<td>
							<c:out value="${(paging.currentPageNo - 1) * paging.pageSize + status.index + 1}" />
						</td>
						<td>
							<a href="${pageContext.request.contextPath}/admin/notice/detail?brdNo=${board.brdNo}">
								${board.brdTitlNm}
							</a>
						</td>
							<td>${board.adFormattedBrdPblsDtm}</td>
							<td>${board.adFormattedBrdEndsDtm}</td>
						<td>
						  <c:choose>
							  <c:when test="${board.noticeTypeName == '일반'}">
							    <span class="badge badge-normal">일반</span>
							  </c:when>
							  <c:when test="${board.noticeTypeName == '긴급'}">
							    <span class="badge badge-danger">긴급</span>
							  </c:when>
							  <c:when test="${board.noticeTypeName == '이벤트'}">
							    <span class="badge badge-event">이벤트</span>
							  </c:when>
							  <c:otherwise>
							    <span class="badge badge-etc">${board.noticeTypeName != null ? board.noticeTypeName : '-'}</span>
							  </c:otherwise>
							</c:choose>
						</td>
						<td>
							<c:if test="${not empty board.attachFiles}">
							  <img src="${pageContext.request.contextPath}/volt/assets/img/file-download-svgrepo-com.svg" 
							       width="20" height="20" alt="첨부파일" />
							</c:if>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="6" class="no-data-center">등록된 공지사항이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

