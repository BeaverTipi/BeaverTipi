package kr.or.ddit.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.validate.exception.OidcUserNotRegisteredException;
import kr.or.ddit.validate.exception.UserNotRegisteredException;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final String oauth2RegisterUrl;
	
	public OAuth2AuthenticationFailureHandler(String oauth2RegisterUrl) {
		super();
		this.oauth2RegisterUrl = oauth2RegisterUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		saveException(request, exception); // 예외 정보를 세션에 저장
		   log.error("🔥 소셜 로그인 실패 발생");
		    log.error("🔥 예외 클래스: {}", exception.getClass().getName());
		    log.error("🔥 예외 메시지: {}", exception.getMessage());
		    log.error("🔥 예외 cause: {}", exception.getCause());
		// 미등록 사용자여서 발생한 예외인지 확인
		if(exception instanceof OidcUserNotRegisteredException) {
			// 가입 페이지로 리다이렉트
			getRedirectStrategy().sendRedirect(request, response, oauth2RegisterUrl);
		} else if (cause instanceof UserNotRegisteredException || cause instanceof OidcUserNotRegisteredException) {
		    getRedirectStrategy().sendRedirect(request, response, oauth2RegisterUrl);
		    return;
		}else{
			// 로그인 페이지로 리다이렉트
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}