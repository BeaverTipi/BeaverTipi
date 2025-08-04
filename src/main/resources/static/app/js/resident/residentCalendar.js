document.addEventListener("DOMContentLoaded", function () {
  const calendarEl = document.getElementById("calendar");

  // ✅ 날짜 포맷 유틸
  function toDatetimeLocal(date) {
    return new Date(date).toISOString().slice(0, 16);
  }

  // ✅ 모달 초기화
  function resetModal() {
    document.getElementById("scheduleForm").reset();
    document.getElementById("scheduleId").value = "";
    document.getElementById("deleteBtn").classList.add("d-none");
  }

  // ✅ 일정 등록 API
  async function createEvent(data) {
    const response = await fetch("/resident/rest/schedules", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error("❌ 등록 실패:", errorText);
      throw new Error("일정 등록 실패");
    }
  }

  // ✅ 일정 수정 API
  async function updateEvent(data) {
    const response = await fetch(`/resident/rest/schedules/${data.bscId}`, {
      method: "PUT",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(data)
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error("❌ 수정 실패:", errorText);
      throw new Error("일정 수정 실패");
    }
  }

  // ✅ 일정 삭제 API (Soft Delete)
  async function deleteEvent(id) {
    const response = await fetch(`/resident/rest/schedules/${id}`, {
      method: "DELETE",
      headers: { "Content-Type": "application/json" }
    });

    if (!response.ok) {
      const errorText = await response.text();
      console.error("❌ 삭제 실패:", errorText);
      throw new Error("일정 삭제 실패");
    }
  }

  // ✅ FullCalendar 설정
  const calendar = new FullCalendar.Calendar(calendarEl, {
    themeSystem: "bootstrap5",
    initialView: "dayGridMonth",
    locale: "ko",
    height: '100%',
    contentHeight: 'auto',
    events: `/resident/calendar/events?bldgId=${selectedBldgId}`,
    editable: true,
    selectable: true,
    unselectAuto: true,
    unselectCancel: ".modal",

    // ✅ 일정 선택 (등록)
    select: function (info) {
      resetModal();

      const startDate = info.startStr + "T09:00";
      const endDateObj = new Date(info.end);
      endDateObj.setDate(endDateObj.getDate() - 1);
      const endDate = toDatetimeLocal(new Date(endDateObj.setHours(18, 0)));

      document.getElementById("scheduleStart").value = startDate;
      document.getElementById("scheduleEnd").value = endDate;

      document.getElementById("scheduleModalLabel").textContent = "일정 등록";
      new bootstrap.Modal(document.getElementById("scheduleModal")).show();
    },

    // ✅ 일정 클릭 (수정)
    eventClick: function (info) {
      const event = info.event;

      document.getElementById("scheduleId").value = event.id;
      document.getElementById("scheduleTitle").value = event.title;
      document.getElementById("scheduleStart").value = toDatetimeLocal(event.start);
      document.getElementById("scheduleEnd").value = event.end
        ? toDatetimeLocal(event.end)
        : toDatetimeLocal(event.start);
      document.getElementById("scheduleMemo").value = event.extendedProps?.bscCont || "";

      document.getElementById("scheduleModalLabel").textContent = "일정 수정";
      document.getElementById("deleteBtn").classList.remove("d-none");

      new bootstrap.Modal(document.getElementById("scheduleModal")).show();
    },

    // ✅ 드래그로 이동 시 업데이트
    eventDrop: async function (info) {
      try {
        await updateEvent({
          bscId: info.event.id,
          bscTitlNm: info.event.title,
          bscStrDtm: info.event.startStr,
          bscEndDtm: info.event.endStr || info.event.startStr,
          bscCont: info.event.extendedProps?.bscCont || "",
          rentalPtyId: currentUserCd,
          bldgId: selectedBldgId
        });
        calendar.refetchEvents();
      } catch (error) {
        Swal.fire("오류", "일정 이동 중 오류가 발생했습니다.", "error");
      }
    },

    headerToolbar: {
      start: "title",
      center: "",
      end: "today prev,next"
    }
  });

  calendar.render();

  // ✅ 일정 등록/수정 제출 처리
  document.getElementById("scheduleForm").addEventListener("submit", async function (e) {
    e.preventDefault();

    const id = document.getElementById("scheduleId").value;
    const title = document.getElementById("scheduleTitle").value;
    const start = document.getElementById("scheduleStart").value;
    const end = document.getElementById("scheduleEnd").value;
    const memo = document.getElementById("scheduleMemo").value;

    const payload = {
      bscTitlNm: title,
      bscStrDtm: start,
      bscEndDtm: end,
      bscCont: memo,
      rentalPtyId: currentUserCd,
      bldgId: selectedBldgId
    };

    const modal = bootstrap.Modal.getInstance(document.getElementById("scheduleModal"));

    try {
      if (id) {
        payload.bscId = id;
        await updateEvent(payload);
        Swal.fire("수정 완료", "일정이 수정되었습니다.", "success");
      } else {
        await createEvent(payload);
        Swal.fire("등록 완료", "일정이 등록되었습니다.", "success");
      }
      calendar.refetchEvents();
      modal.hide();
      resetModal();
    } catch (error) {
      Swal.fire("오류 발생", "임대인만 일정을 설정할 수 있습니다.", "error");
    }
  });

  // ✅ SweetAlert 적용된 삭제
  document.getElementById("deleteBtn").addEventListener("click", async function () {
    const id = document.getElementById("scheduleId").value;
    if (!id) return;

    Swal.fire({
      title: "일정을 삭제하시겠습니까?",
      text: "삭제된 일정은 복구할 수 없습니다.",
      icon: "warning",
      showCancelButton: true,
      confirmButtonColor: "#E17100",
      cancelButtonColor: "#ccc",
      confirmButtonText: "삭제",
      cancelButtonText: "취소"
    }).then(async (result) => {
      if (result.isConfirmed) {
        try {
          await deleteEvent(id);
          calendar.refetchEvents();
          bootstrap.Modal.getInstance(document.getElementById("scheduleModal")).hide();
          Swal.fire("삭제 완료", "일정이 삭제되었습니다.", "success");
        } catch (err) {
          Swal.fire("오류 발생", "삭제 중 문제가 발생했습니다.", "error");
        }
      }
    });
  });
});
