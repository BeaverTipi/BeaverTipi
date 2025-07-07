package kr.or.ddit.util.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.stereotype.Component;

import com.nimbusds.jose.JOSEException;
import com.nimbusds.jose.JWSAlgorithm;
import com.nimbusds.jose.JWSHeader;
import com.nimbusds.jose.JWSSigner;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;

@Component
public class JwtProvider {
	@Value("${jwt.secrete-key}")
	private byte[] secreteKey;
	public static final long VALID_TERM = 1000 * 60 * 30;

	public String authenticationToToken(Authentication authentication) {

		try {
			JWSSigner signer = new MACSigner(secreteKey);
			
			// 권한의 범위 표현. oauth에선 역할을 포함하지 않음. 시스템의 관리자가 누구인지 알수 있으니까 이구조 할려면 데이터베이스에 테이블 있어야함. permission을 역할기반으로 줄수 있음.
//			Map<String,List<String>> permissionTable=new HashMap<>();
//			permissionTable.put("c001",List.of("LPROD/READ","LPROD/WRITE","LPROD/DELETE","BUYER"));
//			permissionTable.put("qwer",List.of("LPROD/READ","LPROD/WRITE","LPROD/DELETE","BUYER"));
//			permissionTable.put("b001",List.of("LPROD/READ"));
//			permissionTable.put("1234",List.of("LPROD/READ"));
			// 클레임 집합으로 페이로드 생성
			// 표준 클래임(sub, iat, exp, iss) + 비표준 클레임(email,role,scope,...)
			JWTClaimsSet claimsSet = new JWTClaimsSet.Builder().subject(authentication.getName()) // 식별자
//													.issuer("http://www.part8.org") // 인증 관련 토큰 인증 서버가 없어서 이거 발행 못함.  
					.issueTime(new Date()) // 만든 시간
					.expirationTime(new Date(System.currentTimeMillis() + VALID_TERM)) // 최대시간
//														.claim("email", authentication안에서 이메일.)
//													.claim("role", "ROLE_ADMIN") // 이걸 주는건 계좌주자리를 넘겨주는 것과 같아서 잘 넘겨주지 않음.
					.claim("scope",
							authentication.getAuthorities().stream().map(GrantedAuthority::getAuthority).toList())
//					.claim("scope",permissionTable.get(authentication.getName()))
					.build();
			// 헤더 + 페이로드
			SignedJWT signedJWT = new SignedJWT(new JWSHeader(JWSAlgorithm.HS256), claimsSet);

			// 서명 객체로 토큰의 대상을 설명
			signedJWT.sign(signer);

			// 토큰 생성
			String token = signedJWT.serialize();
			return token;
		} catch (JOSEException e) {
			throw new RuntimeException(e);
		}
	}

	public Authentication tokenToAuthentication(String token) {
		SecretKey secretKey = new SecretKeySpec(secreteKey, JWSAlgorithm.HS256.getName());
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();

		Jwt jwt = decoder.decode(token);
		String username = jwt.getSubject();
		List<String> scope = jwt.getClaimAsStringList("scope");

		Authentication authentication = UsernamePasswordAuthenticationToken.authenticated(username, "",
				scope.stream().map(SimpleGrantedAuthority::new).toList());
		return authentication;

	}
	
	public boolean validateToken(String token) {
	    try {
	        SecretKey secretKey = new SecretKeySpec(secreteKey, JWSAlgorithm.HS256.getName());
	        NimbusJwtDecoder decoder = NimbusJwtDecoder
	            .withSecretKey(secretKey)
	            .macAlgorithm(MacAlgorithm.HS256)
	            .build();

	        decoder.decode(token); // 여기서 유효하지 않으면 예외 발생
	        return true;
	    } catch (Exception e) {
	        return false;
	    }
	}
}
