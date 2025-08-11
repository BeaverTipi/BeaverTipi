package kr.or.ddit.main.member.controller;

import java.util.Map;

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
import kr.or.ddit.util.validate.exception.MemberStatusInactiveException;
import kr.or.ddit.util.validate.exception.MemberStatusSuspenedException;
import kr.or.ddit.util.validate.exception.MemberStatusWithdrawnException;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// 인증서버 역할. 위임장 token의 형태로 해줌.
@RestController
@Slf4j
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
				.httpOnly(true)
				.domain(".beavertipi.com")
				.secure(true) // HTTPS 통신 시 반드시 필요
			    .sameSite("None")
//				.sameSite(SameSite.STRICT.attributeValue()) // http..
				.maxAge(JwtProvider.VALID_TERM / 1000)
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
					.domain(".beavertipi.com")
					.secure(true) // ✅ HTTPS 통신 시 반드시 필요
				    .sameSite("None")
//					.sameSite(SameSite.STRICT.attributeValue()) // http..
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
		    log.error("로그인 실패 예외: {}", e.getClass().getName());
		    log.error("로그인 실패 메시지: {}", e.getMessage());
		    if (e.getCause() != null) {
		        log.error("▶ 원인 예외 타입: {}", e.getCause().getClass().getName());
		        log.error("▶ 원인 예외 메시지: {}", e.getCause().getMessage());
		    }

		    String errorCode = "INVALID_CREDENTIALS";
		    String errorMessage = "아이디 또는 비밀번호가 일치하지 않습니다.";

		    Throwable cause = e.getCause();
		    if (cause instanceof MemberStatusSuspenedException) {
		        errorCode = "SUSPENDED";
		        errorMessage = cause.getMessage();
		    } else if (cause instanceof MemberStatusInactiveException) {
		        errorCode = "INACTIVE";
		        errorMessage = cause.getMessage();
		    } else if (cause instanceof MemberStatusWithdrawnException) {
		        errorCode = "WITHDRAWN";
		        errorMessage = cause.getMessage();
		    }

		    return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
		            .body(Map.of(
		                    "error", 401,
		                    "code", errorCode,
		                    "message", errorMessage
		            ));
		}


	}

}
