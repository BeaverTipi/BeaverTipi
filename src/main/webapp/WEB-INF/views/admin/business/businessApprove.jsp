<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>비즈니스 계정 목록</title>
  <link rel="stylesheet" href="${pageContext.request.contextPath}/app/css/admin/common_admin.css" />
</head>
<body>
<h2 class="board-title">비즈니스 계정 목록</h2>
<div class="container-wrapper">
  <main class="container">

    <!-- 🔍 검색 영역 -->
    <div class="search-area">
      <form:form method="get" modelAttribute="search" id="searchForm" action="${pageContext.request.contextPath}/admin/business/approve" class="search-form">
        <input type="hidden" name="page" value="${pagingInfo.currentPageNo}" />

        <div class="search-conditions">
          <div class="search-item">
            <label>회원코드</label>
            <form:input path="mbrCd" cssClass="input-field" placeholder="회원코드 입력" />
          </div>
          <div class="search-item">
            <label>아이디</label>
            <form:input path="mbrId" cssClass="input-field" placeholder="아이디 입력" />
          </div>
          <div class="search-item">
            <label>이름</label>
            <form:input path="mbrNm" cssClass="input-field" placeholder="이름 입력" />
          </div>
          <div class="search-item">
            <label>상태</label>
            <form:select path="authApprYn" cssClass="select-field">
              <form:option value="">-- 전체 --</form:option>
              <c:forEach var="statusCode" items="${statusCodeList}">
                <form:option value="${statusCode.codeValue}">${statusCode.codeName}</form:option>
              </c:forEach>
            </form:select>
          </div>
          <div class="search-item">
            <label>유형</label>
            <form:select path="role" cssClass="select-field">
              <form:option value="">-- 전체 --</form:option>
              <c:forEach var="roleCode" items="${roleList}">
                <form:option value="${roleCode.codeValue}">${roleCode.codeName}</form:option>
              </c:forEach>
            </form:select>
          </div>
         <div class="search-item">
  <label>첨부파일 여부</label>
  <div class="radio-group">
    <span><form:radiobutton path="hasFile" value="" /> 전체</span>
     <c:forEach var="fileCode" items="${fileCodeList}">
    <span><form:radiobutton path="hasFile" value="${fileCode.codeValue}" /> ${fileCode.codeName}</span>
  	</c:forEach>
  </div>
</div>

        </div>

        <div class="search-button-area">
          <button type="reset" id="resetBtn" class="btn-warning">초기화</button>
          <button type="submit" id="searchBtn" class="btn-dark">검색</button>
        </div>
      </form:form>
    </div>

    <!-- 📋 승인 목록 테이블 -->
    <form:form method="post" id="bulkForm" action="/admin/business/bulkAction">
      <table class="table">
        <thead>
          <tr>
            <th><input type="checkbox" id="selectAll"></th>
            <th>번호</th>
            <th>회원코드</th>
            <th>아이디</th>
            <th>이름</th>
            <th>상태</th>
            <th>유형</th>
            <th>첨부파일</th>
          </tr>
        </thead>
        <tbody>
         	<c:forEach items="${approveList}" var="item" varStatus="stat">
  <c:set var="apprYn" value="${not empty item.broker.authApprYn ? item.broker.authApprYn : item.tenancy.authApprYn}" />
  <c:set var="isDisabled" value="${apprYn == 'Y' or apprYn == 'N'}" />
  <c:set var="userType" value="${item.broker.mbrCd != null ? 'BROKER' : (item.tenancy.mbrCd != null ? 'TENANCY' : '')}" />

  <tr>
    <!-- 체크박스: 기본 비활성화 & ID 구성 -->
    <td>
      <input type="checkbox" name="userIds"
             value="${item.mbrCd}"
             id="checkbox_${userType}_${item.mbrCd}"
             class="row-check"
             data-usertype="${userType}"
             <c:if test="${isDisabled}">disabled</c:if> />
    </td>

    <td>${pagingInfo.firstRecordIndex + stat.index}</td>
    <td>${item.mbrCd}</td>
    <td>${item.mbrId}</td>
    <td>${item.mbrNm}</td>

    <!-- 상태 -->
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

    <!-- 유형 -->
    <td>
      <c:choose>
        <c:when test="${userType == 'BROKER'}">중개인</c:when>
        <c:when test="${userType == 'TENANCY'}">임대인</c:when>
        <c:otherwise>-</c:otherwise>
      </c:choose>
    </td>

    <!-- 첨부파일 -->
<td>
  <c:choose>
    <c:when test="${apprYn == 'Y' or apprYn == 'N'}">
      확인 완료
    </c:when>

    <c:when test="${not empty item.fileListBroker}">
      <button type="button"
              id="fileBtn_${userType}_${item.mbrCd}"
              class="btn btn-sm btn-outline-info"
              onclick="openFilePopup('${item.mbrCd}', '${userType}')">
        보기
      </button>
    </c:when>

    <c:when test="${not empty item.fileListTenancy}">
      <button type="button"
              id="fileBtn_${userType}_${item.mbrCd}"
              class="btn btn-sm btn-outline-info"
              onclick="openFilePopup('${item.mbrCd}', '${userType}')">
        보기
      </button>
    </c:when>

    <c:otherwise>-</c:otherwise>
  </c:choose>
</td>

  </tr>
</c:forEach>

        </tbody>
      </table>

      <!-- 📄 페이징 -->
      <div class="pagination-wrapper">
        <c:out value="${pagingHTML}" escapeXml="false" />
      </div>

      <!-- ✔️ 일괄 버튼 -->
      <div class="write-buttons">
        <button type="submit" name="action" value="approve" class="btn-success">승인</button>
        <button type="submit" name="action" value="reject" class="btn-danger">거절</button>
      </div>
    </form:form>
  </main>
</div>
<script src="${pageContext.request.contextPath}/app/js/admin/business/businessApprove.js"></script>
</body>
</html>
