package kr.or.ddit.resident.calendar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.resident.calendar.service.ScheduleService;
import kr.or.ddit.vo.ScheduleVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class ScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    // 일정 목록 조회
    @GetMapping("/calendar")
    public String getAllSchedules(Model model) {
        List<ScheduleVO> schedules = scheduleService.getAllSchedules();  // 전체 일정 조회
        model.addAttribute("schedules", schedules);  // 모델에 일정 리스트 추가
        return "resident/calendar/Calendar";  // calendar.jsp 뷰 반환
    }

    // 일정 생성
    @PostMapping("/rest/schedules")
    public ResponseEntity<Void> createSchedule(@RequestBody ScheduleVO schedule) {
    	log.info("📥 일정 등록 요청: {}", schedule);
        scheduleService.createSchedule(schedule);  // 일정 생성
        return ResponseEntity.status(201).build();  // 201 Created
    }

    // 일정 수정
    @PutMapping("/rest/schedules/{scdId}")
    public ResponseEntity<Void> updateSchedule(@PathVariable String scdId, @RequestBody ScheduleVO schedule) {
        schedule.setScdId(scdId);  // 일정 ID 설정
        scheduleService.updateSchedule(schedule);  // 일정 수정
        return ResponseEntity.ok().build();  // 200 OK
    }

    // 일정 삭제
    @DeleteMapping("/rest/schedules/{scdId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String scdId) {
        scheduleService.deleteSchedule(scdId);  // 일정 삭제
        return ResponseEntity.ok().build();  // 200 OK
    }

    // 일정 상세 조회
    @GetMapping("/rest/schedules/{scdId}")
    public ScheduleVO getScheduleById(@PathVariable String scdId) {
        return scheduleService.getScheduleById(scdId);  // 일정 상세 조회
    }
}
