package kr.or.ddit.util.validate.exception;

public class VirtualAccountException extends RuntimeException{
    public VirtualAccountException() {
        super("가상 계좌 등록 처리중 오류가 발생했습니다.");  // 기본 메시지
    }

    public VirtualAccountException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public VirtualAccountException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public VirtualAccountException(Throwable cause) {
        super(cause);  // 예외 원인만
    }


}
