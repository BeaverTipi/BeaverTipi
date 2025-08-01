package kr.or.ddit.util.validate.exception;

public class BuildingUnitException extends RuntimeException{
    public BuildingUnitException() {
        super("세대 정보 등록 처리중 오류 발생하였습니다.");  // 기본 메시지
    }

    public BuildingUnitException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public BuildingUnitException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public BuildingUnitException(Throwable cause) {
        super(cause);  // 예외 원인만
    }

}
