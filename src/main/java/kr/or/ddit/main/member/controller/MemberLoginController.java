package kr.or.ddit.main.member.controller;

import java.util.Map;

import org.springframework.boot.web.server.Cookie.SameSite;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.security.web.context.SecurityContextRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.util.security.jwt.CookieBearerTokenResolver;
import kr.or.ddit.util.security.jwt.JwtProvider;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
// 인증서버 역할. 위임장 token의 형태로 해줌.
@RestController
@RequiredArgsConstructor
public class MemberLoginController {
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider; 
	private final SecurityContextRepository securityContextRepository;
	private final LogoutHandler logoutHandler;
	
	@PostMapping("${myapp.logout-url}")
	public ResponseEntity<?> logout(
		HttpServletRequest req
		, HttpServletResponse resp
		, Authentication authentication
	) {
		
		// 토큰 기반 인증 상태를 로그아웃으로 처리
		String tokenCookie = ResponseCookie.from(CookieBearerTokenResolver.ACCESSTOKENCOOKIE)
				.value("")
				.path("/")
//				.domain(".naver.com")
				.httpOnly(true)
//				.secure(true)
				.sameSite(SameSite.STRICT.attributeValue())
				.maxAge(0)
				.build().toString();
		
		// 세션 기반 인증 상태를 로그아웃으로 처리
		logoutHandler.logout(req, resp, authentication);
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, tokenCookie)
				.build();
	}
	
	
	@PostMapping("${myapp.login-url}")
	public ResponseEntity<?> login(@RequestBody MemberVO auth
			,HttpServletRequest req
			, HttpServletResponse resp
			) {
	UsernamePasswordAuthenticationToken inputData =
			UsernamePasswordAuthenticationToken.unauthenticated(auth.getMbrId(), auth.getMbrPw());

	try {
		Authentication authentication = authenticationManager.authenticate(inputData);
		String encodedToken = jwtProvider.authenticationToToken(authentication);
		
		
		String tokenCookie = ResponseCookie.from(CookieBearerTokenResolver.ACCESSTOKENCOOKIE)
					.value(encodedToken)
					.path("/")
					.httpOnly(true)
//					.sameSite(SameSite.STRICT.attributeValue())
					.sameSite("None") //0703_KCY
					.secure(false) //0703_KCY
					.maxAge(JwtProvider.VALID_TERM / 1000)
					.build().toString();
		
		SecurityContext newContext = SecurityContextHolder.createEmptyContext();
		newContext.setAuthentication(authentication);
		SecurityContextHolder.setContext(newContext);
		securityContextRepository.saveContext(newContext, req, resp);
		
		
		
		return ResponseEntity.ok()
				.header(HttpHeaders.SET_COOKIE, tokenCookie)
				.build();
		} catch (AuthenticationException e) {
		return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
							.body(Map.of("error", 401, "message",e.getMessage()));
	}

	}

}
