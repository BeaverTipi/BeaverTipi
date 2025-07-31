<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form"%>
<%@ taglib uri="jakarta.tags.core" prefix="c"%>

<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/admin/common_admin.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
<script
	src="${pageContext.request.contextPath}/app/js/admin/board/boardToggle.js"></script>

<div class="detail-container">

	<h2 class="board-title">${board.brdTitlNm}</h2>
	<h3>EL test: <c:out value="${loginMember.mbrId}" /></h3>
	<table class="table">
		<tr>
			<th>작성자</th>
			<td>${board.mbrCd}</td>
		</tr>
		<tr>
			<th>게시일</th>
			<td>${board.adFormattedBrdPblsDtm}</td>
		</tr>
		<tr>
			<th>유형</th>
			<td>${board.noticeTypeName}</td>
		</tr>
		<tr>
			<th>내용</th>
			<td style="text-align: left; white-space: pre-wrap;">${board.brdCont}</td>
		</tr>
	</table>

	<c:choose>
		<c:when test="${empty board.qnaAnsweredAt and isAdmin}">
			<h3>답변 등록</h3>

			<c:if test="${not empty loginMember}">
				<div style="margin-bottom: 10px; font-weight: bold;">로그인 사용자:
					${loginMember.mbrNm} (${loginMember.mbrId})</div>
			</c:if>

			<form method="post"
				action="${pageContext.request.contextPath}/admin/detail/qnaAnswer">
				<input type="hidden" name="brdNo" value="${board.brdNo}" /> <input
					type="hidden" name="qnaId" value="${board.qnaId}" /> <input
					type="hidden" name="qnaStatus" value="002" />

				<div class="form-group" style="margin-bottom: 10px;">
					<textarea name="answerCont" rows="8" class="form-control"
						style="width: 100%;" placeholder="답변을 입력하세요."></textarea>
				</div>

				<button type="submit" class="btn-success">답변 등록</button>
			</form>
		</c:when>

		<c:when test="${not empty board.qnaAnsweredAt}">
			<h3>답변 내용</h3>
			<div class="answer-box"
				style="padding: 10px; border: 1px solid #ccc; background: #f9f9f9; margin-bottom: 10px; white-space: pre-wrap;">
				${board.answerCont}</div>
			<div style="text-align: right; font-size: 0.9em; color: gray;">
				답변일시: ${board.qnaAnsweredAt}</div>

			<c:if test="${isAdmin}">
				<button type="button" class="btn-success"
					onclick="document.getElementById('answer-edit-form').style.display='block'">수정</button>

				<form id="answer-edit-form" method="post"
					action="${pageContext.request.contextPath}/admin/detail/qnaAnswer"
					style="margin-top: 10px; display: none;">
					<input type="hidden" name="brdNo" value="${board.brdNo}" /> <input
						type="hidden" name="qnaId" value="${board.qnaId}" /> <input
						type="hidden" name="qnaStatus" value="002" />

					<div class="form-group" style="margin-bottom: 10px;">
						<textarea name="answerCont" rows="8" class="form-control"
							style="width: 100%;">${board.answerCont}</textarea>
					</div>

					<button type="submit" class="btn-success">답변 수정</button>
				</form>
			</c:if>
		</c:when>
	</c:choose>

	<div class="write-buttons">
		<button class="btn-success"
			onclick="location.href='${pageContext.request.contextPath}/admin/notice/form?brdNo=${board.brdNo}'">수정</button>

		<form id="singleDeleteForm" method="post"
			action="${pageContext.request.contextPath}/admin/notice/delete"
			style="display: inline;">
			<input type="hidden" name="brdNoList" value="${board.brdNo}" /> <input
				type="hidden" name="tab" value="${activeTab}" />
			<button type="submit" class="btn-danger" id="singleDeleteBtn">삭제</button>
		</form>

		<button class="btn-dark" onclick="location.href='/admin/notice/list'">목록으로</button>
	</div>

</div>
