package kr.or.ddit.util.validate.exception;

public class CardException extends RuntimeException{
    public CardException() {
        super("카드 등록 처리중 오류가 발생했습니다.");  // 기본 메시지
    }

    public CardException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public CardException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public CardException(Throwable cause) {
        super(cause);  // 예외 원인만
    }


}
