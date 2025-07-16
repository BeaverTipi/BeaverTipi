package kr.or.ddit.resident.calendar.fullcalendar;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.calendar.service.ScheduleService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CalendarRestController {

    private final ScheduleService service;

    @GetMapping("/resident/calendar/events")
    public List<CalendarEvent> list() {
        return service.getAllSchedules()
                      .stream()
                      .map(CalendarEvent::new)
                      .toList();
    }
}
