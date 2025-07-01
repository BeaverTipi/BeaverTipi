package kr.or.ddit.login;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.nimbusds.jose.shaded.gson.Gson;

import jakarta.servlet.http.HttpServletResponse;
import kr.or.ddit.security.jwt.JwtProvider;
import lombok.RequiredArgsConstructor;
// 인증서버 역할. 위임장 token의 형태로 해줌.
@RestController
@RequiredArgsConstructor
public class RestLoginController {
	private final AuthenticationManager authenticationManager;
	private final JwtProvider jwtProvider; 
	
	@PostMapping("/rest/auth")
	public ResponseEntity<?> authenticate(@RequestBody RestAuthVO auth) {
	UsernamePasswordAuthenticationToken inputData =
			UsernamePasswordAuthenticationToken.unauthenticated(auth.getUsername(), auth.getPassword());

	try {
		Authentication authentication = authenticationManager.authenticate(inputData);
		String encodedToken = jwtProvider.authenticationToToken(authentication);
		
		return ResponseEntity.ok()
							.body(Map.of("token",encodedToken));
		
		} catch (AuthenticationException e) {
		return ResponseEntity.status(HttpServletResponse.SC_UNAUTHORIZED)
							.body(Map.of("error", 401, "message",e.getMessage()));
	}

	}

}
