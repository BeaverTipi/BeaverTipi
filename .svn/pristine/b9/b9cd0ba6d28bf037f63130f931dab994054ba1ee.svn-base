<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<!DOCTYPE html>
<html lang="ko">
<head>
<meta charset="UTF-8">
<title>공인중개사 신청</title>
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/member.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/member/memberPage.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/subscribe/form.css">
<link rel="stylesheet"
	href="${pageContext.request.contextPath}/app/css/main/subscribe/brokerForm.css">
</head>
<body class="bg-light">
	<div class="register-wrapper">
		<div class="signup-container">
			<h2 class="signup-title">공인중개사 신청</h2>

			<form:form modelAttribute="broker" method="post"
				enctype="multipart/form-data" action="/apply/broker">
				<div class="form-grid">

					<!-- 왼쪽 입력 컬럼 -->
					<div class="left-column">
						<!-- 사무소명 -->
						<div class="form-group">
							<label for="brokNm">사무소명</label>
							<form:input path="brokNm" cssClass="form-control" id="brokNm" />
							<form:errors path="brokNm" cssClass="text-danger small" />
						</div>

						<!-- 대표자명 -->
						<div class="form-group">
							<label for="reprNm">대표자명</label>
							<form:input path="reprNm" cssClass="form-control" id="reprNm" />
							<form:errors path="reprNm" cssClass="text-danger small" />
						</div>

						<!-- 중개사무소 등록번호 -->
						<div class="form-group">
							<label for="brokRegNo">중개사무소 등록번호</label>
							<form:input path="brokRegNo" cssClass="form-control"
								id="brokRegNo" />
							<form:errors path="brokRegNo" cssClass="text-danger small" />
						</div>

						<!-- 파일 업로드 -->
						<div class="form-group">
							<label for="brokerPdf">자격증 / 사업자 등록증 업로드</label>
							<form:input type="file" id="uploadFile" path="brokFile"
								class="form-control" accept="application/pdf,image/*" />
							<form:errors path="brokFile" cssClass="text-danger small" />
							<small id="fileNamePreview" class="text-muted"></small>
						</div>

						<!-- 자격증 번호 -->
						<div class="form-group">
							<label for="crtfNo">자격증 번호</label>
							<form:input path="crtfNo" cssClass="form-control" id="crtfNo" />
							<form:errors path="crtfNo" cssClass="text-danger small" />
						</div>

						<!-- 대표 전화번호 -->
						<div class="form-group">
							<label for="reprTelNo">대표 전화번호</label>
							<form:input path="reprTelNo" cssClass="form-control"
								id="reprTelNo" />
							<form:errors path="reprTelNo" cssClass="text-danger small" />
						</div>

						<!-- 우편 번호 -->
						<div class="form-group">
							<label for="postcode">우편 번호</label>
							<div style="display: flex; gap: 8px;">
								<form:input path="brokZip" cssClass="form-control" id="postcode"
									readonly="true" />
								<button type="button"
									class="btn btn-outline-secondary btn-address-search"
									onclick="execDaumPostcode()">주소 찾기</button>
								<form:errors path="brokZip" cssClass="text-danger small" />
							</div>
						</div>

						<!-- 주소 -->
						<div class="form-group">
							<label for="address">주소</label>
							<form:input path="brokAddr1" type="text" class="form-control"
								id="address" readonly="true" />
							<form:errors path="brokAddr1" cssClass="text-danger small" />
						</div>

						<!-- 상세 주소 -->
						<div class="form-group">
							<label for="brokAddr2">상세 주소</label>
							<form:input path="brokAddr2" cssClass="form-control"
								id="detailAddress" />
							<form:errors path="brokAddr2" cssClass="text-danger small" />
						</div>

					</div>

					<!-- 오른쪽 컬럼: 구독 솔루션 + PDF 미리보기 -->
					<div class="right-column">
						<div class="form-group mb-4">
							<label class="form-label d-block mb-2">구독 솔루션 선택</label>
							<c:forEach var="solution" items="${solutionList}"
								varStatus="status">
								<div class="form-check mb-2">
									<input class="form-check-input" type="radio" name="solution"
										id="solution_${solution.solId}" value="${solution.solId}"
										<%-- 첫 번째 항목은 무조건 checked, 그 외는 조건 비교 --%>
					<c:choose>
						<c:when test="${status.first && empty solutionId}">checked="checked"</c:when>
						<c:when test="${solution.solId == solutionId}">checked="checked"</c:when>
					</c:choose> />
									<label class="form-check-label"
										for="solution_${solution.solId}"> ${solution.solName}
									</label>
								</div>
							</c:forEach>
						</div>

						<div id="pdfViewer">
							<div id="emptyMessage" class="text-muted small">첨부된 파일이
								없습니다</div>
						</div>
					</div>
				</div>

				<!-- 신청 버튼 -->
				<div class="form-group text-end mt-4">
					<button type="submit" class="btn btn-dark px-4">신청하기</button>
				</div>
			</form:form>
		</div>
	</div>

	<script src="/app/js/main/subscribe/brokerForm.js"></script>
</body>
</html>
