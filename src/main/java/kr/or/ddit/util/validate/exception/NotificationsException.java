package kr.or.ddit.util.validate.exception;

public class NotificationsException extends RuntimeException{
    public NotificationsException() {
        super("알람처리중 오류 발생");  // 기본 메시지
    }

    public NotificationsException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public NotificationsException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public NotificationsException(Throwable cause) {
        super(cause);  // 예외 원인만
    }
}
