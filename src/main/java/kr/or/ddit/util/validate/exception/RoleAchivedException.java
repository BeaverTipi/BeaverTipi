package kr.or.ddit.util.validate.exception;

public class RoleAchivedException extends RuntimeException{
    public RoleAchivedException() {
        super("권한 설정중 오류가 발생했습니다.");  // 기본 메시지
    }

    public RoleAchivedException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public RoleAchivedException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public RoleAchivedException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
