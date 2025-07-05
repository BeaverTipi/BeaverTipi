<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>건물 정보</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
</head>
<body>

  <c:choose>
    <c:when test="${not empty unitList}">
      <c:forEach var="unit" items="${unitList}">
        <div class="building-section">
          <div class="building-title">${unit.bldgNm}</div>
          <div class="building-info">
            <img src="${unit.bldgImgPath}" alt="${unit.bldgNm}" class="building-img">
            <div class="info-box">
              <table class="info-table">
                <tr><td><strong>건물 이름</strong></td><td>${unit.bldgNm}</td></tr>
                <tr><td><strong>우편번호 / 상세 주소</strong></td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty unit.tenancyInfo}">
                        ${unit.tenancyInfo.rentalPtyId} / ${unit.tenancyInfo.rentalPtyBankNm}
                      </c:when>
                      <c:otherwise>
                        정보 없음
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
                <tr><td><strong>유형 코드</strong></td><td>${unit.unitStatCd}</td></tr>
                <tr><td><strong>층수</strong></td><td>${unit.unitFlrNo}층</td></tr>
                <tr><td><strong>호실 수</strong></td><td>${unit.unitId}</td></tr>
                <tr><td><strong>연면적</strong></td><td>${unit.unitXuar}㎡</td></tr>
                <tr><td><strong>준공일</strong></td><td>${unit.unitDtlDescCn}</td></tr>
              </table>

              <div class="button-box">
                <form method="get" action="/building/managed/edit">
                  <input type="hidden" name="unitId" value="${unit.unitId}" />
                  <button type="submit" class="btn btn-edit">수정</button>
                </form>
                <form method="post" action="/building/managed/delete">
                  <input type="hidden" name="unitId" value="${unit.unitId}" />
                  <input type="hidden" name="bldgId" value="${unit.bldgId}" />
                  <button type="submit" class="btn btn-delete">삭제</button>
                </form>
              </div>
            </div>
          </div>
        </div>
        <hr>
      </c:forEach>
    </c:when>

    <c:otherwise>
      <p style="text-align: center; margin-top: 2rem; font-size: 1.2rem;">
        등록된 건물이 없습니다.
      </p>
    </c:otherwise>
  </c:choose>
</body>
</html>
