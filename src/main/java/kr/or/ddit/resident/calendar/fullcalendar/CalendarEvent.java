package kr.or.ddit.resident.calendar.fullcalendar;

import java.time.temporal.Temporal;

import kr.or.ddit.vo.ScheduleVO;

public class CalendarEvent extends AbstractFullCalendarEvent<ScheduleVO> {

	public CalendarEvent(ScheduleVO original) {
		super(original);
	}

	@Override
	public String getId() {
		return getExtendsProps().getScdId();
	}
	@Override
	public boolean isAllDay() {
		return false;
	}
	@Override
	public Temporal getStart() {
		return getExtendsProps().getScdStrDtm();
	}

	@Override
	public Temporal getEnd() {
		return getExtendsProps().getScdEndDtm();
	}

	@Override
	public String getTitle() {
		return getExtendsProps().getScdTitlNm();
	}

	@Override
	public String getTextColor() {
		return "black";
	}
	

}
