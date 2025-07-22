package kr.or.ddit.resident.calendar.fullcalendar;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.calendar.service.ScheduleService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingScheduleVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class CalendarRestController {

    private final ScheduleService service;

    @GetMapping("/resident/calendar/events")
    public List<CalendarEvent> list(
        @RequestParam("bldgId") String bldgId,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO loginUser = principal.getRealUser();

        // 🔐 역할 확인
        boolean isAdmin   = loginUser.getMemRoleList().stream()
                                     .anyMatch(r -> "ADMIN".equals(r.getUserRoleId()));
        boolean isTenancy = loginUser.getMemRoleList().stream()
                                     .anyMatch(r -> "TENANCY".equals(r.getUserRoleId()));

        String rentalPtyId = null;

        // 👉 입주민(USER)은 전체 일정 조회 가능 (단지 보기만 가능)
        // 👉 임대인(TENANCY)은 본인 일정만 조회
        // 👉 관리자(ADMIN)는 전체 일정 조회
        if (isTenancy && !isAdmin) {
            rentalPtyId = service.getRentalPtyIdByMbrCd(loginUser.getMbrCd()); // 본인의 일정만 보기
        }
        
        String role = isAdmin ? "ADMIN" : isTenancy ? "TENANCY" : "USER";
        
        // 📌 일정 조회 (rentalPtyId가 null이면 전체 조회, 아니면 필터링)
        List<BuildingScheduleVO> schedules = service.getSchedulesByBuilding(bldgId, rentalPtyId);

        // 📦 VO → CalendarEvent DTO 변환
        return schedules.stream()
                .map(vo -> new CalendarEvent(vo, role)) // ✅ 역할 함께 전달
                .toList();
    }
}
