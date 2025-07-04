<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>새 공지사항 등록</title>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/board/adminNotice.css">
	<script src="${pageContext.request.contextPath}/app/js/admin/board/boardToggle.js"></script>
</head>
<body>
	<h1>새 공지사항 등록</h1>
	<div class="card">
		<div class="card-body">
			<form:form modelAttribute="board" method="post">
				<div>
					<div class="form-control">
						<label class="label" for="brdTitlNm">제목</label>
						<form:input path="brdTitlNm" placeholder="제목을 입력하세요" cssClass="" />
						<form:errors path="brdTitlNm" cssClass="text-danger" />
					</div>
					<div class="form-control">
						<label class="label" for="brdCode">공지유형</label>
						<form:radiobutton path="brdCode" value="N001" />공지사항
						<form:radiobutton path="brdCode" value="F001" />FAQ
						<form:radiobutton path="brdCode" value="Q001" />QnA
						<form:errors path="brdCode" cssClass="text-danger" />			
					</div>
						<div class="form-detail" id="noticeDetailBox" style="display:none">
							<label class="label" for="notice">상세설정</label>
								<c:forEach var="noticeItem" items="${board.notice }" varStatus="status">
									<div>
										<label class="label" for="notice[${status.index}].noticeType">공지사항 유형</label>
										<form:radiobutton path="notice[${status.index}].noticeType" value="일반" />일반
										<form:radiobutton path="notice[${status.index}].noticeType" value="긴급" />긴급
										<form:radiobutton path="notice[${status.index}].noticeType" value="이벤트" />이벤트
										<form:errors path="notice[${status.index}].noticeType" cssClass="text-danger" />							
									</div>
									<div>
										<label class="label" for="notice[${status.index}].noticeTop">상단고정</label>
										<form:radiobutton path="notice[${status.index}].noticeTop" value="Y" />사용
										<form:radiobutton path="notice[${status.index}].noticeTop" value="N" />안함
										<form:errors path="notice[${status.index}].noticeTop" cssClass="text-danger" />							
									</div>
									<div>
										<label class="label" for="notice[${status.index}].noticeEndDtm">종료일시</label>
										<form:input path="notice[${status.index}].noticeEndDtm" type="datetime-local" />
										<form:errors path="notice[${status.index}].noticeEndDtm" cssClass="text-danger" />							
									</div>
								</c:forEach>
						</div>
						<div class="form-detail" id="faqDetailBox" style="display:none">
							<label class="label" for="faq">상세설정</label>
								<c:forEach var="faqItem" items="${board.faq }" varStatus="status">
									<div>
										<label class="label" for="faq[${status.index}].faqCtgry">FAQ 유형</label>
										<form:radiobutton path="faq[${status.index}].faqCtgry" value="결제" />결제
										<form:radiobutton path="faq[${status.index}].faqCtgry" value="계정" />계정
										<form:radiobutton path="faq[${status.index}].faqCtgry" value="서비스이용" />서비스이용
										<form:errors path="faq[${status.index}].faqCtgry" cssClass="text-danger" />							
									</div>
								</c:forEach>
						</div>
						<div class="form-detail" id="qnaDetailBox" style="display:none">
							<label class="label" for="qna">상세설정</label>
								<c:forEach var="qnaItem" items="${board.qna }" varStatus="status">
									<div>
										<label class="label" for="qna[${status.index}].qnaCtgry">QnA 유형</label>
										<form:radiobutton path="qna[${status.index}].qnaCtgry" value="결제" />결제
										<form:radiobutton path="qna[${status.index}].qnaCtgry" value="계정" />계정
										<form:radiobutton path="qna[${status.index}].qnaCtgry" value="서비스이용" />서비스이용
										<form:errors path="qna[${status.index}].qnaCtgry" cssClass="text-danger" />							
									</div>
								</c:forEach>
						</div>
					<div class="form-control">
						<label class="label" for="brdCont">내용</label>
						<form:textarea path="brdCont" placeholder="내용을 입력하세요" cssClass="" />
						<form:errors path="brdCont" cssClass="text-danger" />			
					</div>
				</div>
			</form:form>
		</div>
	</div>
	<div class="form-control">
    	<label class="label">댓글 허용 여부</label>
    	<span>
	        	<form:radiobutton path="board.boardCartegory.brdCmntYn" value="Y" /> 허용
	        	<form:radiobutton path="board.boardCartegory.brdCmntYn" value="N" /> 비허용
	        	<form:errors path="board.boardCartegory.brdCmntYn" cssClass="text-danger" />
    	</span>
	</div>
		<div class="card-footer">
			<div class="button-group">
				<button type="submit" class="btn btn-outline-success">등록</button>
				<button type="reset" class="btn btn-outline-danger">취소</button>
			</div>
		</div>
</body>
</html>