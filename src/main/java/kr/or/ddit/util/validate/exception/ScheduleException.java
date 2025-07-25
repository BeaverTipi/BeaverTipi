package kr.or.ddit.util.validate.exception;

public class ScheduleException extends RuntimeException{
    public ScheduleException() {
        super("일정 등록중 오류가 발생했습니다.");  // 기본 메시지
    }

    public ScheduleException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public ScheduleException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public ScheduleException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
