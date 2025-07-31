<%@ page contentType="text/html;charset=UTF-8" pageEncoding="UTF-8" language="java" %>
<%@ taglib uri="jakarta.tags.core" prefix="c" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

<!-- ── 건물 상세 퀵뷰 ── -->
<div class="detail-container border rounded p-3 bg-white">
  <!-- 건물 기본정보 -->
  <h4 class="mb-3">${buildingVO.bldgNm}</h4>
  <p class="mb-2">
    <b>주소 :</b> ${buildingVO.bldgAddr} ${buildingVO.bldgDtlAddr}
    <c:if test="${not empty buildingVO.bldgImgPath}">
      <img src="${buildingVO.bldgImgPath}" style="max-width:120px; margin-left:1.5rem; vertical-align:middle;">
    </c:if>
  </p>

  <!-- 호실 선택 -->
  <div class="unit-select mb-3">
    <label for="unitSelect"><b>호실 선택</b></label>
    <select id="unitSelect" class="form-control d-inline-block w-auto" name="unitId">
      <c:forEach var="unit" items="${unitList}">
        <option value="${unit.unitId}"
          <c:if test="${unit.unitId eq selectedUnitId}">selected</c:if>>
          ${unit.unitFlrNo}층 ${unit.unitRoom}호
        </option>
      </c:forEach>
    </select>
  </div>

  <!-- 입주민 테이블 -->
  <h5 class="mb-2">입주민 정보</h5>
  <table class="table table-bordered text-center resident-table">
    <thead class="thead-light">
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

<!-- ── 스크립트 : 호실 변경 시 입주민 리스트 갱신 ── -->
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
        tbody.insertAdjacentHTML('beforeend', `
          <tr>
            <td>${r.mbrCd}</td>
            <td>${r.unit ? r.unit.unitCmar : ''}</td>
            <td>${r.unit ? r.unit.unitDsrMnthRentAmt : ''}</td>
            <td>${r.unit ? r.unit.unitDsrSaleAmt : ''}</td>
            <td>${r.moveOutDt ? r.moveOutDt.replace(/(\\d{4})(\\d{2})(\\d{2})/,'$1-$2-$3') : '-'}</td>
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
