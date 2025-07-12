package kr.or.ddit.util.validate.exception;

public class ApprovedException extends RuntimeException{
    public ApprovedException() {
        super("승인 처리중 오류 발생하였습니다.");  // 기본 메시지
    }

    public ApprovedException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public ApprovedException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public ApprovedException(Throwable cause) {
        super(cause);  // 예외 원인만
    }
}
