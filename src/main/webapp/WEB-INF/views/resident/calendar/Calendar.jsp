a<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="ko">
<head>
  <meta charset="UTF-8" />
  <title>캘린더 예시</title>
  <style>
    body { font-family: sans-serif; }
    .calendar {
      display: grid;
      grid-template-columns: repeat(7, 1fr);
      gap: 8px;
      margin-top: 20px;
    }
    .day {
      border: 1px solid #ccc;
      padding: 10px;
      height: 100px;
      position: relative;
    }
    .day .date {
      font-weight: bold;
      margin-bottom: 5px;
    }
    .event {
      font-size: 12px;
      background-color: #edf2fa;
      padding: 2px 5px;
      border-radius: 3px;
      margin-top: 4px;
    }
  </style>
</head>
<body>
  <h2>2025년 7월 캘린더 📅</h2>
  <div class="calendar" id="calendar"></div>

  <script>
    // 간단한 더미 일정
    const events = {
      3: ["관리비 납부일"],
      7: ["엘리베이터 점검"],
      15: ["입주민 회의", "소방 점검"],
      20: ["택배함 정기청소"],
      25: ["방충망 교체"],
    };

    // 7월은 31일까지
    const calendarEl = document.getElementById("calendar");
    for (let i = 1; i <= 31; i++) {
      const dayEl = document.createElement("div");
      dayEl.className = "day";

      const dateEl = document.createElement("div");
      dateEl.className = "date";
      dateEl.textContent = `7월 ${i}일`;

      dayEl.appendChild(dateEl);

      if (events[i]) {
        events[i].forEach(event => {
          const eventEl = document.createElement("div");
          eventEl.className = "event";
          eventEl.textContent = event;
          dayEl.appendChild(eventEl);
        });
      }

      calendarEl.appendChild(dayEl);
    }
  </script>
</body>
</html>
