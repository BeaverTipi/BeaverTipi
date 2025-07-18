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

<!-- ✅ 공지사항 전용 테이블 부분만 포함 -->
<table class="table table-striped table-hover">
	<thead>
		<tr>
			<th>No</th>
			<th>제목</th>
			<th>게시일자</th>
			<th>공지 유형</th>
			<th>조회수</th>
			<th>댓글 허용 여부</th>
			<th>첨부파일</th>
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
						<td><a
							href="${pageContext.request.contextPath}/admin/notice/detail?brdNo=${board.brdNo}">
								${board.brdTitlNm} </a></td>
						<td>${board.brdPblsDtmFormatted}</td>
						<td>
								<c:choose>
								  <c:when test="${board.notice[0].noticeType == '001'}">일반</c:when>
								  <c:when test="${board.notice[0].noticeType == '002'}">긴급</c:when>
								  <c:when test="${board.notice[0].noticeType == '003'}">이벤트</c:when>
								  <c:otherwise>기타</c:otherwise>
								</c:choose>
						</td>
						<td>${board.brdVwCnt}</td>
						<td><c:out
								value="${board.boardCartegory.brdCmntYn == 'Y' ? '허용' : '비허용'}" />
						</td>
						<td>
							<img src="${pageContext.request.contextPath}/volt/assets/img/file-download-svgrepo-com.svg" 
							 	width="24" height="24" alt="첨부파일 아이콘"/>
						</td>
					</tr>
				</c:forEach>
			</c:when>
			<c:otherwise>
				<tr>
					<td colspan="7" class="text-center">등록된 공지사항이 없습니다.</td>
				</tr>
			</c:otherwise>
		</c:choose>
	</tbody>
</table>

