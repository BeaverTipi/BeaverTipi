<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html>
<head>
<meta charset="UTF-8">
<title>비즈니스 계정 목록</title>

<!--  
 * == 개정이력(Modification Information) ==
 *   
 *   수정일               수정자           수정내용
 *  ============      ============== =======================
 *  2025. 7. 10.           김아린           최초 생성
-->

<link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/business/businessApprove.css">
<style>
  .table th, .table td {
    vertical-align: middle;
    text-align: center;
  }
</style>
</head>
<body>
<div class="container mt-5">
  <h2 class="mb-4">비즈니스 계정 목록</h2>

  <!-- 🔍 검색 필터 -->
  <form:form id="searchForm" modelAttribute="search"
             method="get" action="${pageContext.request.contextPath}/admin/business/approve"
             cssClass="border p-3 rounded bg-light">
    <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />
    <div class="row mb-3">
      <div class="col-md-2">
        <label class="form-label">회원코드</label>
        <form:input path="mbrCd" cssClass="form-control" placeholder="회원코드 입력"/>
      </div>
      <div class="col-md-2">
        <label class="form-label">아이디</label>
        <form:input path="mbrId" cssClass="form-control" placeholder="아이디 입력"/>
      </div>
      <div class="col-md-2">
        <label class="form-label">이름</label>
        <form:input path="mbrNm" cssClass="form-control" placeholder="이름 입력"/>
      </div>
      <div class="col-md-2">
        <label class="form-label">상태</label>
        <form:select path="authApprYn" cssClass="form-select">
          <form:option value="">-- 전체 --</form:option>
          <c:forEach var="statusCode" items="${statusCodeList}">
            <form:option value="${statusCode.codeValue}">${statusCode.codeName}</form:option>
          </c:forEach>
        </form:select>
      </div>
      <div class="col-md-2">
        <label class="form-label">유형</label>
        <form:select path="role" cssClass="form-select">
          <form:option value="">-- 전체 --</form:option>
          <c:forEach var="roleCode" items="${roleList}">
            <form:option value="${roleCode.codeValue}">${roleCode.codeName}</form:option>
          </c:forEach>
        </form:select>
      </div>
      <div class="col-md-2 d-flex align-items-end justify-content-end gap-2">
        <button type="reset" class="btn btn-secondary">초기화</button>
        <button type="submit" class="btn btn-primary">검색</button>
      </div>
    </div>
  </form:form>

  <!-- ✅ 목록 테이블 -->
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
          <th>상세보기</th>
        </tr>
      </thead>
      <tbody>
        <c:choose>
          <c:when test="${not empty approveList}">
            <c:forEach items="${approveList}" var="item" varStatus="stat">
              <tr>
                <td><input type="checkbox" name="userIds" value="${item.mbrCd}" class="row-check"></td>
                <td>${pagingInfo.firstRecordIndex + stat.index}</td>
                <td>${item.member.mbrCd}</td>
                <td>${item.member.mbrId}</td>
                <td>${item.member.mbrNm}</td>
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
                    <c:when test="${item.broker.mbrCd != null}">중개인</c:when>
                    <c:when test="${item.tenancy.mbrCd != null}">임대인</c:when>
                    <c:otherwise>-</c:otherwise>
                  </c:choose>
                </td>
                <td><button type="button" class="btn btn-sm btn-outline-primary" onclick="openDetailPopup('${item.member.mbrCd}')">상세보기</button></td>
              </tr>
            </c:forEach>
          </c:when>
          <c:otherwise>
            <tr>
              <td colspan="8">조회된 비즈니스 계정이 없습니다.</td>
            </tr>
          </c:otherwise>
        </c:choose>
      </tbody>
    </table>

    <!-- 📄 페이징 -->
    <div class="pagination-wrapper d-flex justify-content-center mt-3">
      <c:out value="${pagingHTML}" escapeXml="false"/>
    </div>

    <!-- ✅ 일괄 처리 버튼 -->
    <div class="d-flex justify-content-end gap-2 mt-3">
      <button type="submit" name="action" value="approve" class="btn btn-success">일괄 승인</button>
      <button type="submit" name="action" value="reject" class="btn btn-danger">일괄 거절</button>
    </div>
  </form:form>
</div>

<!-- ✅ 스크립트 -->
<script src="/app/js/admin/business/businessApprove.js"></script>

</body>
</html>
