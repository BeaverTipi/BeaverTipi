package kr.or.ddit.util.security.oauth2;

import java.io.IOException;

import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.ResponseCookie;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.util.security.jwt.CookieBearerTokenResolver;
import kr.or.ddit.util.security.jwt.JwtProvider;

public class OAuth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	private final JwtProvider jwtProvider;
	
	
	public OAuth2AuthenticationSuccessHandler(JwtProvider jwtProvider) {
		super();
		this.jwtProvider = jwtProvider;
	}


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		String encodedToken;
		try {
			encodedToken = jwtProvider.authenticationToToken(authentication);
			ResponseCookie tokenCookie = ResponseCookie.from(CookieBearerTokenResolver.ACCESSTOKENCOOKIE)
					.value(encodedToken)
					.path("/")
					.httpOnly(true)
					.sameSite(SameSite.STRICT.attributeValue())
					.maxAge(JwtProvider.VALID_TERM / 1000)
					.build();
			
			 Cookie servletCookie = new Cookie(tokenCookie.getName(), tokenCookie.getValue());
			    servletCookie.setPath(tokenCookie.getPath());
			    servletCookie.setMaxAge((int) tokenCookie.getMaxAge().getSeconds());
			    servletCookie.setHttpOnly(tokenCookie.isHttpOnly());

			 response.addCookie(servletCookie);

		        // 로그인 성공 후 리다이렉트
		       response.sendRedirect("/");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

}
