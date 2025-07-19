<!-- 
 * == 개정이력(Modification Information) ==
 *   
 *   수정일      			수정자           수정내용
 *  ============   	============== =======================
 *  2025. 7. 19.     		김재윤           최초 생성
 *
-->

<%@ page contentType="text/html; charset=UTF-8" language="java" pageEncoding="UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html>
<head>
  <meta charset="UTF-8">
  <title>호수 선택</title>
  <style>
    body { font-family: Pretendard, sans-serif; padding: 1rem; }
    h2 { font-size: 1.3rem; margin-bottom: 1rem; }
    table { width: 100%; border-collapse: collapse; margin-bottom: 1rem; }
    th, td { border: 1px solid #ccc; padding: 0.6rem; text-align: center; }
    th:first-child, td:first-child { width: 60px; }
    .btn-group { text-align: right; margin-top: 1rem; }
    button { padding: 0.4rem 0.8rem; margin-left: 0.5rem; cursor: pointer; }
  </style>
</head>
<body>

  <h2>호수 선택</h2>

  <table>
    <thead>
      <tr>
        <th><input type="checkbox" id="selectAll" onclick="toggleAll(this)"></th>
        <th>호수</th>
      </tr>
    </thead>
    <tbody>
      <c:forEach var="unit" items="${unitList}">
        <tr>
          <td>
            <input type="checkbox" class="unitCheckbox"
              data-id="${unit.unitId}"
              data-name="${unit.unitRoom}">
          </td>
          <td>${unit.unitRoom}</td>
        </tr>
      </c:forEach>
    </tbody>
  </table>

  <div class="btn-group">
    <button type="button" onclick="submitSelection()">선택 완료</button>
    <button type="button" onclick="window.close()">취소</button>
  </div>

  <script>
    function toggleAll(masterBox) {
      document.querySelectorAll('.unitCheckbox').forEach(cb => cb.checked = masterBox.checked);
    }

    function submitSelection() {
      const selected = [...document.querySelectorAll('.unitCheckbox:checked')];

      if (selected.length === 0) {
        alert("선택된 호수가 없습니다");
        return;
      }

      selected.forEach(cb => {
        const id = cb.dataset.id;
        const ho = cb.dataset.name;

        if (window.opener && window.opener.addResidentBlock) {
          if (!window.opener.document.getElementById(`resident_${id}`)) {
            window.opener.addResidentBlock(id, ho);
          }
        }
      });

      window.close();
    }
  </script>

</body>
</html>