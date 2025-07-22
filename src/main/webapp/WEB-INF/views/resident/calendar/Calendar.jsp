<%@ page contentType="text/html; charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>일정 캘린더</title>

  <!-- ✅ FullCalendar -->
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/locales-all.global.min.js"></script>

  <!-- ✅ Bootstrap -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

  <!-- ✅ SweetAlert2 -->
  <script src="https://cdn.jsdelivr.net/npm/sweetalert2@11"></script>

  <style>
    #calendar {
      max-width: 1000px;
      margin: 30px auto;
    }
    .calendar-title {
      text-align: center;
      font-size: 1.8rem;
      font-weight: bold;
      color: #E17100;
      background: linear-gradient(to right, #fff3e0, #ffe0b2);
      padding: 12px 0;
      border-radius: 10px;
      margin: 0 auto 20px auto;
      max-width: 1000px;
      box-shadow: 0 2px 6px rgba(0, 0, 0, 0.05);
    }

    .fc .fc-button {
      background-color: #E17100;
      border: none;
      color: white;
      font-weight: 500;
      padding: 6px 12px;
      border-radius: 6px;
      transition: background-color 0.3s ease;
    }

    .fc .fc-button:hover {
      background-color: #973C00;
    }

    .fc .fc-col-header-cell-cushion,
    .fc .fc-daygrid-day-number {
      color: #E17100 !important;
      text-decoration: none !important;
    }

    .fc .fc-day-today {
      background-color: #fff3e0 !important;
      border: 1px solid #E17100;
    }

    .btn-primary {
      background-color: #E17100;
      border-color: #E17100;
    }

    .btn-primary:hover {
      background-color: #973C00;
      border-color: #973C00;
    }

    .btn-danger:hover {
      background-color: #a10000;
    }

    .building-select-wrapper {
      max-width: 1000px;
      margin: 0 auto 20px;
      text-align: right;
    }

    .building-select-wrapper select {
      display: inline-block;
      padding: 6px 12px;
      border-radius: 6px;
      border: 1px solid #ccc;
    }
    .building-selector {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 12px;
  margin-bottom: 20px;
  font-size: 1rem;
  font-weight: 500;
  color: #333;
}
.building-selector select {
  padding: 6px 12px;
  border: 1px solid #ccc;
  border-radius: 6px;
}
    
  </style>
</head>
<body>

  <h2 class="calendar-title">📅 일정 캘린더</h2>

  <!-- ✅ 건물 선택 드롭다운 -->
<div class="building-selector">
  <label for="buildingSelect">🏢 건물 선택:</label>
  <select id="buildingSelect" name="bldgIdParam">
    <c:forEach var="unit" items="${buildingList}">
		<option value="${unit.bldgId}" <c:if test="${unit.bldgId eq selectedBldgId}">selected</c:if>>
		  ${unit.building.bldgNm}
		</option>
    </c:forEach>
  </select>
</div>

  <!-- ✅ FullCalendar -->
  <div id="calendar"></div>

  <!-- ✅ 일정 등록/수정 모달 -->
  <div class="modal fade" id="scheduleModal" tabindex="-1" aria-labelledby="scheduleModalLabel" aria-hidden="true">
    <div class="modal-dialog">
      <form id="scheduleForm" class="modal-content">
        <div class="modal-header">
          <h5 class="modal-title" id="scheduleModalLabel">일정 등록</h5>
          <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="닫기"></button>
        </div>
        <div class="modal-body">
          <input type="hidden" id="scheduleId">
          <div class="mb-3">
            <label for="scheduleTitle" class="form-label">제목</label>
            <input type="text" class="form-control" id="scheduleTitle" required>
          </div>
          <div class="mb-3">
            <label for="scheduleStart" class="form-label">시작</label>
            <input type="datetime-local" class="form-control" id="scheduleStart" required>
          </div>
          <div class="mb-3">
            <label for="scheduleEnd" class="form-label">종료</label>
            <input type="datetime-local" class="form-control" id="scheduleEnd" required>
          </div>
          <div class="mb-3">
            <label for="scheduleMemo" class="form-label">메모</label>
            <textarea class="form-control" id="scheduleMemo"></textarea>
          </div>
        </div>
        <div class="modal-footer">
          <button type="button" id="deleteBtn" class="btn btn-danger d-none">삭제</button>
          <button type="submit" class="btn btn-primary">저장</button>
          <button type="button" class="btn btn-secondary" data-bs-dismiss="modal">취소</button>
        </div>
      </form>
    </div>
  </div>

  <!-- ✅ 스크립트: 공통 + 변수 초기화 + 모듈 -->
  <script>
    const currentUserCd = '${loginUser.mbrCd}';
    const defaultBldgId = '${selectedBldgId}';
    const selectedBldgId = localStorage.getItem("selectedBldgId") || defaultBldgId;
  </script>
  <script src="${pageContext.request.contextPath}/app/js/resident/commonBuildingSelect.js"></script>
  <script>
    setupGlobalBuildingSelector({
      param: 'bldgIdParam',
      storageKey: 'selectedBldgId',
      onChange: function (bldgId) {
        localStorage.setItem("selectedBldgId", bldgId);
        location.reload();
      }
    });
  </script>
  <script src="${pageContext.request.contextPath}/app/js/resident/residentCalendar.js"></script>

</body>
</html>
