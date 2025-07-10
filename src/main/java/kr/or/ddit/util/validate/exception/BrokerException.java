package kr.or.ddit.util.validate.exception;

public class BrokerException extends RuntimeException{
    public BrokerException() {
        super("이전에 요청한 값이 있습니다.");  // 기본 메시지
    }

    public BrokerException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public BrokerException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public BrokerException(Throwable cause) {
        super(cause);  // 예외 원인만
    }
}
