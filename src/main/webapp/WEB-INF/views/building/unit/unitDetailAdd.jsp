<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
  <title>호실 상세 입력</title>
  <script>
    function generateUnitInputs() {
      const count = document.getElementById("unitCount").value;
      const container = document.getElementById("unitInputContainer");
      container.innerHTML = ""; // 초기화

      for (let i = 0; i < count; i++) {
        const wrapper = document.createElement("div");
        wrapper.innerHTML = `
          <fieldset>
            <legend>호실 ${i + 1}</legend>
            <label>층수: <input type="number" name="unitList[${i}].unitFlrNo" required></label><br>
            <label>공급면적: <input type="text" name="unitList[${i}].unitCmar" required></label><br>
            <label>전용면적: <input type="text" name="unitList[${i}].unitXuar"></label><br>
            <label>월세 예상금액: <input type="text" name="unitList[${i}].unitDsrMnthRentAmt"></label><br>
            <label>전세 예상금액: <input type="text" name="unitList[${i}].unitDsrSaleAmt"></label><br>
            <label>보증금 예상금액: <input type="text" name="unitList[${i}].unitDpstAmt"></label><br>
            <label>상세설명: <textarea name="unitList[${i}].unitDtlDescCn"></textarea></label><br>
            <label>호실 수: <input type="text" name="unitList[${i}].unitRoom"></label><br>
            <input type="hidden" name="unitList[${i}].unitStatCd" value="REGISTERED">
          </fieldset><br>
        `;
        container.appendChild(wrapper);
      }
    }
  </script>
</head>
<body>

<h2>호실 입력</h2>

<form action="/building/unitManaged/add" method="post">
  <label>호실 개수:
    <input type="number" id="unitCount" min="1" required>
    <button type="button" onclick="generateUnitInputs()">입력폼 생성</button>
  </label>

  <div id="unitInputContainer"></div>

  <!-- 건물 ID, 임대인 ID는 hidden으로 전달 -->
  <input type="hidden" name="bldgId" value="${bldgId}" />
  <input type="hidden" name="rentalPtyId" value="${rentalPtyId}" />

  <button type="submit">등록</button>
</form>

</body>
</html>
