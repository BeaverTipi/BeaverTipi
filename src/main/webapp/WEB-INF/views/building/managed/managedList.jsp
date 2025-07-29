<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ page isELIgnored="false" %>


<style>
  .building-link {
    color: #007bff;
    text-decoration: underline;
    font-weight: bold;
    cursor: pointer;
  }
  .building-link:hover {
    color: #0056b3;
    text-decoration: underline;
  }
</style>

<div class="container mt-4">
  <div class="card">
    <div class="card-body">
      <h2 class="mb-4">내 건물 관리</h2>

      <!-- 리스트 테이블 -->
      <div class="table-responsive">
        <table class="table table-bordered table-hover text-center">
          <thead class="thead-light">
            <tr>
              <th>순번</th>
              <th>건물명</th>
              <th>주소</th>
              <th>유형</th>
              <th>층수</th>
              <th>호실수</th>
            </tr>
          </thead>
          <tbody>
            <c:choose>
              <c:when test="${empty buildingList}">
                <tr><td colspan="6">등록된 건물이 없습니다.</td></tr>
              </c:when>
              <c:otherwise>
                <c:forEach var="building" items="${buildingList}" varStatus="status">
                  <tr>
                    <td>${status.index + 1}</td>
                    <td>
                      <a href="/building/managed/detail?bldgId=${building.bldgId}" class="building-link">
                        ${building.bldgNm}
                      </a>
                    </td>
                    <td>${building.bldgAddr} ${building.bldgDtlAddr}</td>
                    <td>${building.bldgTypeCode}</td>
                    <td>${building.bldgFlrCnt}</td>
                    <td>${building.bldgUnitCnt}</td>
                  </tr>
                </c:forEach>
              </c:otherwise>
            </c:choose>
          </tbody>
        </table>
      </div>
      
      <!-- 우측 하단 새 건물등록 버튼 -->
      <div style="text-align:right; margin-top:20px;">
        <a href="/building/managed/add" class="btn btn-success">새 건물등록</a>
      </div>
    </div>
  </div>
</div>