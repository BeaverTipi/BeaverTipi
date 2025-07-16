<%@ page contentType="text/html; charset=UTF-8" %>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8">
  <title>일정 캘린더</title>

  <!-- ✅ FullCalendar CDN -->
  <link href="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/main.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/index.global.min.js"></script>
  <script src="https://cdn.jsdelivr.net/npm/fullcalendar@6.1.8/locales-all.global.min.js"></script>

  <!-- ✅ Bootstrap (for modal) -->
  <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css" rel="stylesheet">
  <script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/js/bootstrap.bundle.min.js"></script>

  <style>
    #calendar {
      max-width: 1000px;
      margin: 50px auto;
    }
  </style>
</head>
<body>
  <h2 class="text-center mt-4">📅 일정 캘린더</h2>
  <div id="calendar"></div>

  <!-- 📌 모달 -->
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
          <input type="hidden" id="scheduleAllDay" value="false">
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

  <!-- ✅ 외부 JS 연결 -->
  <script src="${pageContext.request.contextPath}/app/js/resident/residentCalendar.js"></script>
</body>
</html>
