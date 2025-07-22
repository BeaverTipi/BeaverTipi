package kr.or.ddit.util.validate.exception;

public class ListingOptionException extends RuntimeException{
    public ListingOptionException() {
        super("매물 처리중 오류 발생하였습니다.");  // 기본 메시지
    }

    public ListingOptionException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public ListingOptionException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public ListingOptionException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
