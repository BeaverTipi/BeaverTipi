<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비즈니스 계정 목록</title>

<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css" />
</head>
<body>
	<div class="container mt-5">
		<h2 class="mb-4">비즈니스 계정 목록</h2>

		<!-- 검색 폼 -->
		<form:form id="searchForm" modelAttribute="search" method="get"
			action="${pageContext.request.contextPath}/admin/business/approve"
			cssClass="border p-4 rounded bg-light">
			<input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />

			<div class="row align-items-end">
				<div class="col-md-2 mb-3">
					<label class="form-label">회원코드</label>
					<form:input path="mbrCd" cssClass="form-control" placeholder="회원코드 입력" />
				</div>
				<div class="col-md-2 mb-3">
					<label class="form-label">아이디</label>
					<form:input path="mbrId" cssClass="form-control" placeholder="아이디 입력" />
				</div>
				<div class="col-md-2 mb-3">
					<label class="form-label">이름</label>
					<form:input path="mbrNm" cssClass="form-control" placeholder="이름 입력" />
				</div>
				<div class="col-md-2 mb-3">
					<label class="form-label">상태</label>
					<form:select path="authApprYn" cssClass="form-select">
						<form:option value="">-- 전체 --</form:option>
						<c:forEach var="statusCode" items="${statusCodeList}">
							<form:option value="${statusCode.codeValue}">${statusCode.codeName}</form:option>
						</c:forEach>
					</form:select>
				</div>
				<div class="col-md-2 mb-3">
					<label class="form-label">유형</label>
					<form:select path="role" cssClass="form-select">
						<form:option value="">-- 전체 --</form:option>
						<c:forEach var="roleCode" items="${roleList}">
							<form:option value="${roleCode.codeValue}">${roleCode.codeName}</form:option>
						</c:forEach>
					</form:select>
				</div>
				<div class="col-md-2 mb-3">
					<label class="form-label">첨부파일 여부</label>
					<form:select path="hasFile" cssClass="form-select">
						<form:option value="">-- 전체 --</form:option>
						<c:forEach var="fileCode" items="${fileCodeList}">
							<form:option value="${fileCode.codeValue}">${fileCode.codeName}</form:option>
						</c:forEach>
					</form:select>
				</div>
			</div>

			<div class="row">
				<div class="col d-flex justify-content-end gap-2">
					<button type="reset" id="resetBtn" class="btn btn-warning">초기화</button>
					<button type="submit" id="searchBtn" class="btn btn-dark">검색</button>
				</div>
			</div>
		</form:form>

		<!-- 승인/거절 목록 및 일괄 처리 폼 -->
		<form:form id="bulkForm" method="post" action="/admin/business/bulkAction">
			<table class="table table-bordered mt-4">
				<thead class="table-light">
					<tr>
						<th><input type="checkbox" id="selectAll"></th>
						<th>번호</th>
						<th>회원코드</th>
						<th>아이디</th>
						<th>이름</th>
						<th>상태</th>
						<th>유형</th>
						<th>첨부파일</th>
						<th>승인 상태</th>
					</tr>
				</thead>
				<tbody>
					<c:choose>
						<c:when test="${not empty approveList}">
							<c:forEach items="${approveList}" var="item" varStatus="stat">
								<c:set var="apprYn" value="${not empty item.broker.authApprYn ? item.broker.authApprYn : item.tenancy.authApprYn}" />
								<c:set var="isDisabled" value="${apprYn == 'Y' or apprYn == 'N'}" />
								<c:set var="userType" value="${item.broker.mbrCd != null ? 'BROKER' : (item.tenancy.mbrCd != null ? 'TENANCY' : '')}" />
								<tr>
									<td>
										<input type="checkbox" name="userIds" value="${item.mbrCd}" class="row-check" data-usertype="${userType}" <c:if test="${isDisabled}">disabled</c:if> />
									</td>
									<td>${pagingInfo.firstRecordIndex + stat.index}</td>
									<td>${item.mbrCd}</td>
									<td>${item.mbrId}</td>
									<td>${item.mbrNm}</td>
									<td>
										<c:choose>
											<c:when test="${item.broker.authApprYn != null}">
												<c:forEach var="statusCode" items="${statusCodeList}">
													<c:if test="${statusCode.codeValue == item.broker.authApprYn}">
														${statusCode.codeName}
													</c:if>
												</c:forEach>
											</c:when>
											<c:when test="${item.tenancy.authApprYn != null}">
												<c:forEach var="statusCode" items="${statusCodeList}">
													<c:if test="${statusCode.codeValue == item.tenancy.authApprYn}">
														${statusCode.codeName}
													</c:if>
												</c:forEach>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${userType == 'BROKER'}">중개인</c:when>
											<c:when test="${userType == 'TENANCY'}">임대인</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${not empty item.fileListBroker}">
												<button type="button" class="btn btn-sm btn-outline-info" onclick="openFilePopup('${item.mbrCd}', 'BROKER')">보기</button>
											</c:when>
											<c:when test="${not empty item.fileListTenancy}">
												<button type="button" class="btn btn-sm btn-outline-info" onclick="openFilePopup('${item.mbrCd}', 'TENANCY')">보기</button>
											</c:when>
											<c:otherwise>-</c:otherwise>
										</c:choose>
									</td>
									<td>
										<c:choose>
											<c:when test="${apprYn == 'W' || empty apprYn}">
												<c:set var="approveLabel" value="" />
												<c:set var="rejectLabel" value="" />
												<c:forEach var="status" items="${statusCodeList}">
													<c:if test="${status.codeValue == 'Y'}">
														<c:set var="approveLabel" value="${status.codeName}" />
													</c:if>
													<c:if test="${status.codeValue == 'N'}">
														<c:set var="rejectLabel" value="${status.codeName}" />
													</c:if>
												</c:forEach>
												<button type="button" class="btn btn-sm btn-outline-success" onclick="submitApproval('${item.mbrCd}', '${userType}')">${approveLabel}</button>
												<button type="button" class="btn btn-sm btn-outline-danger" onclick="submitRejection('${item.mbrCd}', '${userType}')">${rejectLabel}</button>
											</c:when>
											<c:otherwise>
												<c:forEach var="status" items="${statusCodeList}">
													<c:if test="${status.codeValue == apprYn}">
														<c:choose>
															<c:when test="${status.codeValue == 'Y'}">
																<span class="strong-approved">${status.codeName}</span>
															</c:when>
															<c:when test="${status.codeValue == 'N'}">
																<span class="strong-rejected">${status.codeName}</span>
															</c:when>
															<c:otherwise>
																<span class="text-secondary">${status.codeName}</span>
															</c:otherwise>
														</c:choose>
													</c:if>
												</c:forEach>
											</c:otherwise>
										</c:choose>
									</td>
								</tr>
							</c:forEach>
						</c:when>
						<c:otherwise>
							<tr>
								<td colspan="9">조회된 비즈니스 계정이 없습니다.</td>
							</tr>
						</c:otherwise>
					</c:choose>
				</tbody>
			</table>

			<div class="pagination-wrapper d-flex justify-content-center mt-3">
				<div class="text-center w-100">
					<c:out value="${pagingHTML}" escapeXml="false" />
				</div>
			</div>

			<div class="d-flex justify-content-end gap-2 mt-3">
				<button type="submit" name="action" value="approve" class="btn btn-success">일괄 승인</button>
				<button type="submit" name="action" value="reject" class="btn btn-danger">일괄 거절</button>
			</div>
		</form:form>
	</div>

	<script src="${pageContext.request.contextPath}/app/js/admin/business/businessApprove.js"></script>

</body>
</html>
