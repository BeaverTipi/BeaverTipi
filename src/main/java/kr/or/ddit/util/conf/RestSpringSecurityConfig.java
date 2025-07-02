package kr.or.ddit.util.conf;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfigurationSource;

import com.nimbusds.jose.JWSAlgorithm;

import lombok.Data;
@Data
@ConfigurationProperties(prefix = "cors")
@Configuration
//@EnableWebSecurity
public class RestSpringSecurityConfig {
	@Autowired
	private CorsConfigurationSource restCorsConfigurationSource;

	@Value("${jwt.secrete-key}")
	private byte[] secreteKey;
	
	// 프로바이더 역할 대신..
	@Bean
	public JwtDecoder jwtDecoder() {
		SecretKey secretKey = new SecretKeySpec(secreteKey, JWSAlgorithm.HS256.getName());
		NimbusJwtDecoder decoder = NimbusJwtDecoder.withSecretKey(secretKey).macAlgorithm(MacAlgorithm.HS256).build();
		return decoder;
	}
	
	@Bean
	@Order(1)
	public SecurityFilterChain restSecurityFilterChain(HttpSecurity http) throws Exception {
		http.securityMatcher("/rest/**")
			.csrf(csrf->csrf.disable())
			.cors(cors ->
				cors.configurationSource(restCorsConfigurationSource)
					)
			.authorizeHttpRequests(authorize ->
				authorize.requestMatchers("/rest/auth").permitAll()
						 .requestMatchers("/rest/buyer/**").hasRole("ADMIN")
//						 .requestMatchers("/rest/buyer/**").hasAuthority("SCOPE_BUYER")
//						 .requestMatchers(HttpMethod.GET,"/rest/lprod/**").hasAuthority("SCOPE_LPROD/READ")
//						 .requestMatchers(HttpMethod.DELETE,"/rest/lprod/**").hasAuthority("SCOPE_LPROD/DELETE")
//						 .requestMatchers("/rest/lprod/**").hasAuthority("SCOPE_LPROD/WRITE")
						 .requestMatchers("/rest/lprod/**").hasRole("ADMIN") //둘 같이 하는건 불가능 하진 않음..!
						 .anyRequest().authenticated()
			)
			.sessionManagement(session->
			session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // 세션을 관리하지 않겠다. ==> 세션기반의 인증 사용 못함. form로그인..이런거 못씀.
			)
			.oauth2ResourceServer(oauth2RS ->
			oauth2RS.jwt(jwt-> jwt.decoder(jwtDecoder()))// 생략해도 자동으로 들어가긴함. 눈으로 볼려고 하는거..
					);
		return http.build();
	}
	
}
