package kr.or.ddit.util.validate.exception;

public class SubscriptionException extends RuntimeException{
    public SubscriptionException() {
        super("승인을 기다리는 중입니다.");  // 기본 메시지
    }

    public SubscriptionException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public SubscriptionException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public SubscriptionException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

	public static void main(String[] args) {
		throw new SubscriptionException();
	}
}
