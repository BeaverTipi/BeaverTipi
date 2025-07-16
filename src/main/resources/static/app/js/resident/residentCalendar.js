document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("calendar");

  const calendar = new FullCalendar.Calendar(calendarEl, {
    themeSystem: "bootstrap5",
    initialView: "dayGridMonth",
    locale: "ko",
    events: "/resident/calendar/events",
    editable: true,
    selectable: true,
    unselectAuto: true,
    unselectCancel: ".modal",

    // ✅ 일정 선택 시 (날짜 클릭 or 드래그)
select: function (info) {
  resetModal();

  const startDate = info.startStr + "T09:00";

  const endDateObj = new Date(info.end);
  endDateObj.setDate(endDateObj.getDate() - 1);
  const yyyy = endDateObj.getFullYear();
  const mm = String(endDateObj.getMonth() + 1).padStart(2, '0');
  const dd = String(endDateObj.getDate()).padStart(2, '0');
  const endDate = `${yyyy}-${mm}-${dd}T18:00`; // 종료일 23:59까지

  document.getElementById("scheduleStart").value = startDate;
  document.getElementById("scheduleEnd").value = endDate;

  document.getElementById("scheduleModalLabel").textContent = "일정 등록";
  new bootstrap.Modal(document.getElementById("scheduleModal")).show();
}
,

    // ✅ 일정 클릭 시 모달에 정보 채우기
eventClick: function (info) {
  const event = info.event;

  function toDatetimeLocal(date) {
    return new Date(date).toISOString().slice(0, 16);
  }

  document.getElementById("scheduleId").value = event.id;
  document.getElementById("scheduleTitle").value = event.title;
  document.getElementById("scheduleStart").value = toDatetimeLocal(event.start);
  document.getElementById("scheduleEnd").value = event.end ? toDatetimeLocal(event.end) : toDatetimeLocal(event.start);
  document.getElementById("scheduleMemo").value = event.extendedProps?.scdCont || '';  // ✅ 수정

  document.getElementById("scheduleModalLabel").textContent = "일정 수정";
  document.getElementById("deleteBtn").classList.remove("d-none");
  new bootstrap.Modal(document.getElementById("scheduleModal")).show();
}
,

    // ✅ 드래그 이동 시 바로 수정 처리
    eventDrop: async function (info) {
      await updateEvent({
        scdId: info.event.id,
        scdTitlNm: info.event.title,
        scdStrDtm: info.event.startStr,
        scdEndDtm: info.event.endStr || info.event.startStr,
        scdLoc: info.event.extendedProps?.scdLoc || '',
        scdMemo: info.event.extendedProps?.scdMemo || '',
        mbrCd: "M2507000015"
      });
      calendar.refetchEvents();
    },

    headerToolbar: {
      start: "title",
      center: "",
      end: "today prev,next"
    }
  });

  calendar.render();

  // ✅ 등록/수정 form 제출
  document.getElementById("scheduleForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("scheduleId").value;
    const title = document.getElementById("scheduleTitle").value;
    const start = document.getElementById("scheduleStart").value;
    const end = document.getElementById("scheduleEnd").value;
    const location = document.getElementById("scheduleLocation").value;
    const memo = document.getElementById("scheduleMemo").value;

	const payload = {
	  scdTitlNm: title,
	  scdStrDtm: start,
	  scdEndDtm: end,
	  scdCont: memo, // ✅ scdMemo → scdCont로 통일
	  mbrCd: "M2507000015"
	};

    const modal = bootstrap.Modal.getInstance(document.getElementById("scheduleModal"));

    try {
      if (id) {
        payload.scdId = id;
        await updateEvent(payload);
      } else {
        await createEvent(payload);
      }
      calendar.refetchEvents();
      modal.hide();
      resetModal();
    } catch (error) {
      console.error("❌ 처리 중 오류:", error);
    }
  });

  // ✅ 삭제 버튼 클릭 시
  document.getElementById("deleteBtn").addEventListener("click", async function () {
    const id = document.getElementById("scheduleId").value;
    if (id) {
      await deleteEvent(id);
      calendar.getEventById(id)?.remove();
      bootstrap.Modal.getInstance(document.getElementById("scheduleModal")).hide();
    }
  });

  // ✅ 모달 초기화 함수
  function resetModal() {
    document.getElementById("scheduleForm").reset();
    document.getElementById("scheduleId").value = "";
    document.getElementById("deleteBtn").classList.add("d-none");
  }

  // ✅ 일정 등록 (C)
  async function createEvent(data) {
    const response = await fetch("/resident/rest/schedules", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      alert("❌ 일정 등록 실패");
      throw new Error("일정 등록 실패");
    }
    alert("✅ 일정이 등록되었습니다.");
  }

  // ✅ 일정 수정 (U)
  async function updateEvent(data) {
    const response = await fetch(`/resident/rest/schedules/${data.scdId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      alert("❌ 일정 수정 실패");
      throw new Error("일정 수정 실패");
    }
    alert("✅ 일정이 수정되었습니다.");
  }

  // ✅ 일정 삭제 (D)
  async function deleteEvent(id) {
    const response = await fetch(`/resident/rest/schedules/${id}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" }
    });

    if (!response.ok) {
      alert("❌ 일정 삭제 실패");
      throw new Error("일정 삭제 실패");
    }
    alert("🗑️ 일정이 삭제되었습니다.");
  }
});
