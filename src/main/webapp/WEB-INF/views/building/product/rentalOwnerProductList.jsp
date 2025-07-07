<%@ page contentType="text/html; charset=UTF-8" language="java" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>매물 정보</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedList.css">
</head>
<body>

  <c:choose>
    <c:when test="${empty productList}">
      <p>등록된 매물이 없습니다.</p>
    </c:when>
    <c:otherwise>
      <c:forEach var="prod" items="${productList}">
        <div class="building-section">
          <div class="building-title">${prod.lstgNm}</div>
          <div class="building-info">
            <img src="${prod.lstgImgUrl != null ? prod.lstgImgUrl : '/images/no-image.jpg'}" alt="${prod.lstgNm}" class="building-img">

            <div class="info-box">
              <table class="info-table">
                <tr><td><strong>건물 이름</strong></td><td>${prod.lstgNm}</td></tr>
                <tr><td><strong>등록일</strong></td><td>${prod.regDate}</td></tr>
              </table>

              <div class="button-box">
                <button class="btn btn-edit" onclick="location.href='/building/product/detail?lstgId=${prod.lstgId}'">상세보기</button>
                <button class="btn btn-edit" onclick="location.href='/building/product/update?lstgId=${prod.lstgId}'">수정</button>
                <button class="btn btn-delete" onclick="location.href='/building/product/delete?lstgId=${prod.lstgId}'">삭제</button>
              </div>
            </div>
          </div>
        </div>
        <hr>
      </c:forEach>
    </c:otherwise>
  </c:choose>

</body>
</html>
