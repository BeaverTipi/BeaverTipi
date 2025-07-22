package kr.or.ddit.resident.calendar.fullcalendar;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.Temporal;

import kr.or.ddit.vo.BuildingScheduleVO;

public class CalendarEvent extends AbstractFullCalendarEvent<BuildingScheduleVO> {

    // ✅ 'T' 없이 "2025-07-22 14:30" 형식의 날짜 파싱을 위한 포맷터
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    
    private final String role;
    
    public CalendarEvent(BuildingScheduleVO original,String role) {
        super(original);
        this.role = role;
    }

    @Override	
    public String getId() {
        return getExtendsProps().getBscId();
    }

    @Override
    public String getTitle() {
        return getExtendsProps().getBscTitlNm();
    }

    @Override
    public boolean isAllDay() {
        return false;
    }

    @Override
    public Temporal getStart() {
        return getExtendsProps().getBscStrDtm();  // 이미 LocalDateTime
    }

    @Override
    public Temporal getEnd() {
        return getExtendsProps().getBscEndDtm();  // 이미 LocalDateTime
    }

    @Override
    public String getTextColor() {
        return "black";
    }
    
    @Override
    public Boolean isEditable() {
        return "TENANCY".equals(role);
    }

    @Override
    public Boolean isStartEditable() {
        return isEditable();  // 시작일도 수정 가능
    }

    @Override
    public Boolean isDurationEditable() {
        return isEditable();  // 길이 변경도 가능
    }
    @Override
    public String getBackgroundColor() {
        return "TENANCY".equals(role) ? "#E17100" : "#BDBDBD";
    }

    @Override
    public String getClassName() {
        return "fc-event-" + role.toLowerCase();
    }

    @Override
    public String getDisplay() {
        return "auto";
    }
}
