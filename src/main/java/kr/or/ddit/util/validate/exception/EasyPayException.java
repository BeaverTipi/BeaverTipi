package kr.or.ddit.util.validate.exception;

public class EasyPayException extends RuntimeException{
    public EasyPayException() {
        super("간편 결제 등록 처리중 오류가 발생했습니다.");  // 기본 메시지
    }

    public EasyPayException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public EasyPayException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public EasyPayException(Throwable cause) {
        super(cause);  // 예외 원인만
    }


}
