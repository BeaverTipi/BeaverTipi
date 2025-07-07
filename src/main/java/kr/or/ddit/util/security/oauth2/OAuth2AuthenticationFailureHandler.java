package kr.or.ddit.util.security.oauth2;

import java.io.IOException;

import org.springframework.security.core.AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.util.validate.exception.OidcUserNotRegisteredException;
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
		  
		// 미등록 사용자여서 발생한 예외인지 확인
		if(exception instanceof OidcUserNotRegisteredException) {
			// 가입 페이지로 리다이렉트
			getRedirectStrategy().sendRedirect(request, response, oauth2RegisterUrl);
		} else if (exception instanceof OAuth2AuthenticationException authEx) {
	        String errorCode = authEx.getError().getErrorCode(); // 여기에 "register-required" 가 들어올 것
	        log.error("🔥 인증 실패 코드: {}", errorCode);

	        if ("register-required".equals(errorCode)) {
	            getRedirectStrategy().sendRedirect(request, response, oauth2RegisterUrl); // ex: "/member/register"
	            return;
	        }
	    }else{
			// 로그인 페이지로 리다이렉트
			super.onAuthenticationFailure(request, response, exception);
		}
	}
}