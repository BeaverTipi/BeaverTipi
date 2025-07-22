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

<!-- ✅ FAQ 게시판 리스트 -->
<table class="table">
	<thead>
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>게시일자</th>
			<th>카테고리</th>  <!-- FAQCT -->
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
						<td>${board.brdPblsDtmFormatted}</td>
						<td>
							<c:out value="${board.faqCtgryName}" />
						</td>
						<td>
							<img src="${pageContext.request.contextPath}/volt/assets/img/file-download-svgrepo-com.svg" 
							     width="20" height="20" alt="첨부파일" />
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
