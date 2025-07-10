package kr.or.ddit.util.validate.exception;

public class FileIOException extends RuntimeException{
    public FileIOException() {
        super("파일 처리 중 오류가 발생했습니다.");  // 기본 메시지
    }

    public FileIOException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public FileIOException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

    public FileIOException(Throwable cause) {
        super(cause);  // 예외 원인만
    }
}
