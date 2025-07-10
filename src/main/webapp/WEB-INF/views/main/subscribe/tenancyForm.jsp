<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>임대인 신청</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/member.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/subscribe/form.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/main/subscribe/tenancyForm.css">
</head>
<body class="bg-light">
	<div class="register-wrapper">
		<div class="signup-container">
			<h2 class="signup-title">임대인 신청</h2>

			<form:form modelAttribute="tenancy" method="post" enctype="multipart/form-data" action="/apply/tenancy">
				<div class="form-grid">
					<!-- 왼쪽 컬럼 -->
					<div class="left-column">
						<div class="form-group">
							<label for="uploadFile">사업자등록증 파일</label>
							<form:input path="tenancyFile" type="file" cssClass="form-control" id="uploadFile"
								accept="application/pdf,image/*" />
							<form:errors path="tenancyFile" cssClass="text-danger small" />
							<small id="fileNamePreview" class="text-muted"></small>
						</div>

						<div class="form-group">
							<label for="rentalPtyBizRegNo">사업자등록번호</label>
							<form:input path="rentalPtyBizRegNo" cssClass="form-control" id="rentalPtyBizRegNo" readonly="readonly" />
							<form:errors path="rentalPtyBizRegNo" cssClass="text-danger small" />
						</div>

						<div class="form-group">
							<label for="rentalPtyAcctNo">계좌번호</label>
							<form:input path="rentalPtyAcctNo" cssClass="form-control" id="rentalPtyAcctNo" />
							<form:errors path="rentalPtyAcctNo" cssClass="text-danger small" />
						</div>

						<div class="form-group">
							<label for="rentalPtyBankNm">은행명</label>
							<form:input path="rentalPtyBankNm" cssClass="form-control" id="rentalPtyBankNm" />
							<form:errors path="rentalPtyBankNm" cssClass="text-danger small" />
						</div>

						<div class="form-group mb-4">
							<label class="form-label d-block mb-2">구독 솔루션 선택</label>

							<!-- 솔루션 선택 안 함 -->
							<div class="form-check mb-2">
								<input class="form-check-input" type="radio" name="solution" id="solution_none" value=""
									${empty solutionId ? 'checked="checked"' : ''} />
								<label class="form-check-label" for="solution_none">솔루션 선택 안 함</label>
							</div>

							<!-- 솔루션 목록 -->
							<c:forEach var="solution" items="${solutionList}">
								<div class="form-check mb-2">
									<input class="form-check-input" type="radio" name="solution"
										id="solution_${solution.solId}" value="${solution.solId}"
										${solution.solId == solutionId ? 'checked="checked"' : ''} />
									<label class="form-check-label" for="solution_${solution.solId}">${solution.solName}</label>
								</div>
							</c:forEach>
						</div>
					</div>

					<!-- 오른쪽 컬럼 -->
					<div class="right-column">
						<div id="pdfViewer">
							<div id="emptyMessage" class="text-muted small">첨부된 파일이 없습니다</div>
						</div>
					</div>
				</div>

				<!-- ✅ 버튼은 form-grid 바깥에 있어야 하단 정렬됨 -->
				<div class="form-group text-end mt-4">
					<button type="submit" class="btn btn-dark px-4">신청하기</button>
				</div>
			</form:form>
		</div>
	</div>

	<script src="/app/js/main/subscribe/tenancyForm.js"></script>
</body>
</html>
