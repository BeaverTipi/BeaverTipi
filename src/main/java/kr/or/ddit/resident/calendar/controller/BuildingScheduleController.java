package kr.or.ddit.resident.calendar.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingScheduleVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class BuildingScheduleController {

    @Autowired
    private ScheduleService scheduleService;

    @Autowired
    private UnitResidentService unitResidentService;
    
    // 📅 일정 메인 뷰 반환
    @GetMapping("/calendar")
    public String getCalendarView(
            Model model,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO loginUser = principal.getRealUser();
        model.addAttribute("loginUser", loginUser);

        // 1. 로그인한 사용자가 입주한 건물 목록 조회
        List<UnitResidentVO> unitList = unitResidentService.getUnitsByMember(loginUser.getMbrCd());
        model.addAttribute("buildingList", unitList);

        // 2. 제일 먼저 입주한 건물의 ID를 selectedBldgId로 사용
        String selectedBldgId = unitList != null && !unitList.isEmpty()
                ? unitList.get(0).getBldgId()
                : null;
        model.addAttribute("selectedBldgId", selectedBldgId);

        return "resident/calendar/Calendar"; // JSP
    }

    // ✅ 일정 등록
    @PostMapping("/rest/schedules")
    public ResponseEntity<Void> createSchedule(@RequestBody BuildingScheduleVO schedule) {
        log.info("📥 일정 등록 요청: {}", schedule);
        scheduleService.createSchedule(schedule);
        return ResponseEntity.status(201).build();
    }

    // ✅ 일정 수정
    @PutMapping("/rest/schedules/{bscId}")
    public ResponseEntity<Void> updateSchedule(@PathVariable String bscId, @RequestBody BuildingScheduleVO schedule) {
        schedule.setBscId(bscId);
        scheduleService.updateSchedule(schedule);
        return ResponseEntity.ok().build();
    }

    // ✅ 일정 삭제 (소프트 딜리트)
    @DeleteMapping("/rest/schedules/{bscId}")
    public ResponseEntity<Void> deleteSchedule(@PathVariable String bscId) {
        scheduleService.deleteSchedule(bscId);
        return ResponseEntity.ok().build();
    }

    // ✅ 일정 상세 조회
    @GetMapping("/rest/schedules/{bscId}")
    public BuildingScheduleVO getScheduleById(@PathVariable String bscId) {
        return scheduleService.getScheduleById(bscId);
    }
}
