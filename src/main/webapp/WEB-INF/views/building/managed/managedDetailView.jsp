<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>


<link rel="stylesheet" href="/app/css/building/managed/managedDetailView.css">
<div class="quick-detail-outer">
  <div class="quick-detail-head">
    <div style="flex:1 1 0;">
      <div class="quick-detail-title">${buildingVO.bldgNm}</div>
      <div class="quick-detail-address">
        ${buildingVO.bldgAddr} ${buildingVO.bldgDtlAddr}
        <c:if test="${not empty buildingVO.bldgImgPath}">
          <img src="${buildingVO.bldgImgPath}" class="quick-detail-img" alt="건물사진">
        </c:if>
      </div>
    </div>
    <div class="quick-unit-select">
      <label for="unitSelect"></label>
      <select id="unitSelect" name="unitId">
        <c:forEach var="unit" items="${unitList}">
          <option value="${unit.unitId}" <c:if test="${unit.unitId eq selectedUnitId}">selected</c:if>>
            ${unit.unitFlrNo}층 ${unit.unitRoom}호
          </option>
        </c:forEach>
      </select>
    </div>
  </div>

  <table class="table quick-detail-table text-center resident-table">
    <thead>
      <tr>
        <th>입주민ID</th>
        <th>면적(m²)</th>
        <th>월세</th>
        <th>전세</th>
        <th>계약종료</th>
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
              <%
                String moveOutDt = ((kr.or.ddit.vo.UnitResidentVO)pageContext.getAttribute("resident")).getMoveOutDt();
                String moveOutDtFmt = "-";
                if(moveOutDt != null && moveOutDt.length() == 8) {
                  moveOutDtFmt = moveOutDt.substring(0,4) + "-" + moveOutDt.substring(4,6) + "-" + moveOutDt.substring(6,8);
                }
              %>
              <td><%= moveOutDtFmt %></td>
            </tr>
          </c:forEach>
        </c:otherwise>
      </c:choose>
    </tbody>
  </table>
</div>

<script>
(() => {
  const unitSel = document.getElementById('unitSelect');
  if (!unitSel) return;

  unitSel.addEventListener('change', () => loadResident(unitSel.value));

  async function loadResident(unitId) {
    try {
      const res  = await fetch(`/building/managed/detail/residentList?unitId=${unitId}`);
      const list = await res.json();
      const tbody = document.getElementById('residentTbody');
      tbody.innerHTML = '';

      if (!list || list.length === 0) {
        tbody.innerHTML = '<tr><td colspan="5">입주민 정보 없음</td></tr>';
        return;
      }
      list.forEach(r => {
        // moveOutDt 날짜포맷 JS에서 해줌
        let moveOutDt = '-';
        if (r.moveOutDt && r.moveOutDt.length === 8) {
          moveOutDt = r.moveOutDt.slice(0,4) + '-' + r.moveOutDt.slice(4,6) + '-' + r.moveOutDt.slice(6,8);
        }
        tbody.insertAdjacentHTML('beforeend', `
          <tr>
            <td>${r.mbrCd}</td>
            <td>${r.unit ? r.unit.unitCmar : ''}</td>
            <td>${r.unit ? r.unit.unitDsrMnthRentAmt : ''}</td>
            <td>${r.unit ? r.unit.unitDsrSaleAmt : ''}</td>
            <td>${moveOutDt}</td>
          </tr>
        `);
      });

    } catch (e) {
      console.error(e);
      document.getElementById('residentTbody').innerHTML =
        '<tr><td colspan="5" class="text-danger">입주민 정보를 불러오지 못했습니다.</td></tr>';
    }
  }
})();
</script>
