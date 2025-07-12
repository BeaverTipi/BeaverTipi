package kr.or.ddit.util.validate.exception;

public class RejectedException extends RuntimeException{
    public RejectedException() {
        super("거절 처리중 오류 발생하였습니다.");  // 기본 메시지
    }

    public RejectedException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public RejectedException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public RejectedException(Throwable cause) {
        super(cause);  // 예외 원인만
    }
}
