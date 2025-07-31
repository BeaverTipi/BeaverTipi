<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>건물 상세정보</title>
  <link rel="stylesheet" href="/app/css/building/managed/managedDetail.css">
  <style>
    body                 { font-family: 'Noto Sans KR', sans-serif; background:#f4f6f9; padding:2rem; }
    .detail-container    { max-width:900px; margin:0 auto; padding:2rem; background:#fff; border-radius:12px; box-shadow:0 2px 6px rgba(0,0,0,.05); }
    .detail-container h2 { margin-bottom:1.5rem; }
    .unit-select         { margin:1.5rem 0; }
    .resident-table      { width:100%; border-collapse:collapse; }
    .resident-table th,
    .resident-table td   { border:1px solid #ddd; padding:8px; text-align:center; }
    .resident-table th   { background:#f5f7fa; }
  </style>
</head>
<body>
<div class="detail-container">
  <h2>${buildingVO.bldgNm} 상세정보</h2>

  <div style="margin-bottom:1rem;">
    <b>주소 :</b> ${buildingVO.bldgAddr} ${buildingVO.bldgDtlAddr}
    <c:if test="${not empty buildingVO.bldgImgPath}">
      <img src="${buildingVO.bldgImgPath}" style="max-width:150px; margin-left:2rem; vertical-align:middle;">
    </c:if>
  </div>

  <div class="unit-select">
    <label for="unitSelect"><b>호실 선택</b></label>
    <select id="unitSelect" name="unitId">
      <c:forEach var="unit" items="${unitList}">
        <option value="${unit.unitId}"
          <c:if test="${unit.unitId eq selectedUnitId}">selected</c:if>>
          ${unit.unitFlrNo}층 ${unit.unitRoom}호
        </option>
      </c:forEach>
    </select>
  </div>

  <h3>입주민 정보</h3>
  <table class="resident-table">
    <thead>
      <tr>
        <th>입주민ID</th>
        <th>면적(m²)</th>
        <th>월세(₩)</th>
        <th>전세(₩)</th>
        <th>계약종료일</th>
      </tr>
    </thead>
    <tbody id="residentTbody">
      <c:choose>
        <c:when test="${empty residentList}">
          <tr><td colspan="5">입주민 정보 없음</td></tr>
        </c:when>
        <c:otherwise>
          <c:forEach var="resident" items="${residentList}">
            <tr>
              <td>${resident.mbrCd}</td>
              <td>${resident.unit.unitCmar}</td>
              <td>${resident.unit.unitDsrMnthRentAmt}</td>
              <td>${resident.unit.unitDsrSaleAmt}</td>
              <td>
                <c:choose>
                  <c:when test="${not empty resident.moveOutDt}">
                    <fmt:parseDate value="${resident.moveOutDt}" pattern="yyyyMMdd" var="outDate"/>
                    <fmt:formatDate value="${outDate}" pattern="yyyy-MM-dd"/>
                  </c:when>
                  <c:otherwise>-</c:otherwise>
                </c:choose>
              </td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>
</div>

<script>
document.addEventListener("DOMContentLoaded", () => {
  const unitSelect = document.getElementById('unitSelect');
  unitSelect.addEventListener('change', () => loadResident(unitSelect.value));
  async function loadResident(unitId){
    try{
      const res  = await fetch(`/building/managed/detail/residentList?unitId=${unitId}`);
      const list = await res.json();
      const tbody = document.getElementById('residentTbody');
      tbody.innerHTML = '';
      if(!list || list.length === 0){
        tbody.innerHTML = '<tr><td colspan="5">입주민 정보 없음</td></tr>';
        return;
      }
      list.forEach(r=>{
        tbody.insertAdjacentHTML('beforeend',`
          <tr>
            <td>${r.mbrCd}</td>
            <td>${r.unit ? r.unit.unitCmar : ''}</td>
            <td>${r.unit ? r.unit.unitDsrMnthRentAmt : ''}</td>
            <td>${r.unit ? r.unit.unitDsrSaleAmt : ''}</td>
            <td>${r.moveOutDt ? r.moveOutDt.replace(/(\\d{4})(\\d{2})(\\d{2})/,'$1-$2-$3') : '-'}</td>
          </tr>
        `);
      });
    }catch(e){
      console.error(e);
      document.getElementById('residentTbody').innerHTML =
        '<tr><td colspan="5" style="color:#e74c3c;">입주민 정보를 불러오지 못했습니다.</td></tr>';
    }
  }
});
</script>
</body>
</html>
