package kr.or.ddit.util.validate.exception;

public class TosspaymentsException extends RuntimeException{
    public TosspaymentsException() {
        super("토스 작업 처리중 오류 발생");  // 기본 메시지
    }

    public TosspaymentsException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public TosspaymentsException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public TosspaymentsException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
