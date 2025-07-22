package kr.or.ddit.util.validate.exception;

public class ListingException extends RuntimeException{
    public ListingException() {
        super("매물 옵션 등록 처리중 오류 발생하였습니다.");  // 기본 메시지
    }

    public ListingException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public ListingException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public ListingException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
