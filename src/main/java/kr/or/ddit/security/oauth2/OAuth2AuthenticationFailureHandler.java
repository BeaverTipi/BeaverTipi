package kr.or.ddit.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public class OAuth2AuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler {
	private final String registerUrl;
	
	public OAuth2AuthenticationFailureHandler(String registerUrl) {
		super();
		this.registerUrl = registerUrl;
	}

	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
			AuthenticationException exception) throws IOException, ServletException {
		
		saveException(request, exception); // 예외 정보를 세션에 저장
		
		// 미등록 사용자여서 발생한 예외인지 확인
		if(exception instanceof UserNotRegisteredException) {
			// 가입 페이지로 리다이렉트
			getRedirectStrategy().sendRedirect(request, response, registerUrl);
		}else {
			// 로그인 페이지로 리다이렉트
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}