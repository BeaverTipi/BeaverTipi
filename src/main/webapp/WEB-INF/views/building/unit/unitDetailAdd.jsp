<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form" %>
<%@ taglib prefix="c" uri="jakarta.tags.core" %>

<html>
<head>
  <title>세대 상세 입력</title>
  <link rel="stylesheet" href="/app/css/building/unit/unitDetailAdd.css" />
  
</head>
<body>

  <div class="container-wrap">
    <h2 class="mb-4">세대 입력</h2>

    <form action="/building/unitManaged/add" method="post" id="unitForm">
      <div class="mb-3 d-flex justify-content-center align-items-center gap-3">
        <label for="unitCount" class="form-label mb-0 flex-shrink-0">총 세대:</label>
        <input type="number" id="unitCount" min="1" class="form-control" style="max-width: 120px" required />
        <button type="button" class="btn btn-primary btn-generate" id="generateBtn">입력폼 생성</button>
      </div>

      <div id="unitInputContainer" class="unit-placeholder-box">
        <div class="text-muted text-center py-5 fs-6">
    총 세대를 입력하고 <strong>입력폼 생성</strong>을 눌러주세요.
  </div>
      </div>

      <input type="hidden" name="bldgId" value="${bldgId}" />
      <input type="hidden" name="rentalPtyId" value="${rentalPtyId}" />
<div class="submit-button-wrapper">
      <button type="submit" class="btn btn-submit mt-3">등록</button>
      </div>
    </form>
<script src="/app/js/building/unit/unitDetailAdd.js"></script>
</body>

</html>
