package kr.or.ddit.util.validate.exception;

import org.springframework.security.core.AuthenticationException;

public class MemberStatusSuspenedException extends AuthenticationException{
    public MemberStatusSuspenedException() {
        super("정지된 회원입니다.");  // 기본 메시지
    }

    public MemberStatusSuspenedException(String message) {
        super(message);  // 사용자 정의 메시지
    }

    public MemberStatusSuspenedException(String message, Throwable cause) {
        super(message, cause);  // 예외 원인 포함
    }

}
