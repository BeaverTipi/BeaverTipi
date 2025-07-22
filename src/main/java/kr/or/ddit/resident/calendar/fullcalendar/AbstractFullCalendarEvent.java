package kr.or.ddit.resident.calendar.fullcalendar;

import java.time.temporal.Temporal;
import java.util.Objects;

public abstract class AbstractFullCalendarEvent<T> {
	
	private transient T original;
	
	 public java.util.Map<String, Object> getExtendedProps() {
	        return java.util.Collections.emptyMap();
	    }
	
	public AbstractFullCalendarEvent(T original) {
		this.original = original;
	}
	
	public abstract String getId();
	public boolean isAllDay() {
		return true;
	};
	public abstract Temporal getStart();
	public abstract Temporal getEnd();
	public String getStartStr() {
		return Objects.toString(getStart(), "");
	};
	public String getEndStr() {
		return Objects.toString(getEnd(), "");
	};
	public abstract String getTitle();
	public abstract String getTextColor();
	
	public String getBackgroundColor() { return null; }
	public String getBorderColor() { return null; }
	public Boolean isEditable() { return null; }
	public Boolean isStartEditable() { return null; }
	public Boolean isDurationEditable() { return null; }
	public String getDisplay() { return null; }
	public String getClassName() { return null; }
	
	
	
	public T getExtendsProps() {
		return original;
	}
}
