package kr.or.ddit.util.validate.exception;

public class NoLoginException extends RuntimeException{
    public NoLoginException() {
        super("로그인이 되지 않았습니다.");  // 기본 메시지
    }

    public NoLoginException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public NoLoginException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public NoLoginException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
