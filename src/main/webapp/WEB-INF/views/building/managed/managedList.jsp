<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>건물 정보</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
  <style>
    .building-section {
      border: 1px solid #ccc;
      border-radius: 8px;
      padding: 20px;
      margin-bottom: 30px;
      background-color: #f9f9f9;
    }
    .building-title {
      font-size: 1.5rem;
      font-weight: bold;
      margin-bottom: 15px;
    }
    .building-info {
      display: flex;
    }
    .building-img {
      width: 180px;
      height: 120px;
      object-fit: cover;
      margin-right: 20px;
    }
    .info-box {
      flex-grow: 1;
    }
    .info-table {
      width: 100%;
      border-collapse: collapse;
      margin-bottom: 15px;
    }
    .info-table td {
      padding: 6px 10px;
    }
    .button-box {
      display: flex;
      gap: 10px;
    }
    .btn {
      padding: 6px 14px;
      border: none;
      border-radius: 4px;
      cursor: pointer;
    }
    .btn-edit {
      background-color: #4caf50;
      color: white;
    }
    .btn-delete {
      background-color: #f44336;
      color: white;
    }
  </style>
</head>
<body>

  <c:choose>
    <c:when test="${not empty buildingList}">
      <c:forEach var="building" items="${buildingList}">
        <div class="building-section">
          <div class="building-title">${building.bldgNm}</div>
          <div class="building-info">
            <img src="${building.bldgImgPath}" alt="${building.bldgNm}" class="building-img">
            <div class="info-box">
              <table class="info-table">
                <tr><td><strong>건물 이름</strong></td><td>${building.bldgNm}</td></tr>
                <tr><td><strong>주소</strong></td><td>${building.bldgAddr} ${building.bldgDtlAddr}</td></tr>
                <tr><td><strong>우편번호</strong></td><td>${building.bldgZipNo}</td></tr>
                <tr><td><strong>건물유형</strong></td><td>${building.bldgTypeCode}</td></tr>
                <tr><td><strong>층 수</strong></td><td>${building.bldgFlrCnt}</td></tr>
                <tr><td><strong>호실 수</strong></td><td>${building.bldgUnitCnt}</td></tr>
                <tr><td><strong>연면적</strong></td><td>${building.bldgGrossArea}㎡</td></tr>
                <tr><td><strong>준공일</strong></td><td>${building.bldgCmpltnDt}</td></tr>
                <tr><td><strong>임대인 계좌</strong></td>
                  <td>
                    <c:choose>
                      <c:when test="${not empty building.tenancyInfo}">
                        ${building.tenancyInfo.rentalPtyBankNm} / ${building.tenancyInfo.rentalPtyAcctNo}
                      </c:when>
                      <c:otherwise>
                        정보 없음
                      </c:otherwise>
                    </c:choose>
                  </td>
                </tr>
              </table>

              <div class="button-box">
                <form method="get" action="/building/managed/edit">
                  <input type="hidden" name="bldgId" value="${building.bldgId}" />
                  <button type="submit" class="btn btn-edit">수정</button>
                </form>
                <form method="post" action="/building/managed/delete" onsubmit="return confirm('정말 삭제하시겠습니까?');">
                  <input type="hidden" name="bldgId" value="${building.bldgId}" />
                  <button type="submit" class="btn btn-delete">삭제</button>
                </form>
              </div>
            </div>
          </div>
        </div>
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
