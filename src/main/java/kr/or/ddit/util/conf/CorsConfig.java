package kr.or.ddit.util.conf;

import java.util.List;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import kr.or.ddit.util.security.jwt.CookieBearerTokenResolver;
import lombok.Data;

@Data
@ConfigurationProperties(prefix = "cors")
@Configuration
public class CorsConfig {
	private List<String> allowedOrigins;
	private List<String> allowedMethods;
	private List<String> allowedHeaders;
	private boolean allowCredentials;

	@Bean
	public CorsConfigurationSource restCorsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		
		corsConfig.setAllowedOrigins(allowedOrigins);
		corsConfig.setAllowedMethods(allowedMethods);
		corsConfig.setAllowedHeaders(allowedHeaders);
		corsConfig.setAllowCredentials(allowCredentials);
		
		UrlBasedCorsConfigurationSource corsConfigSource = 
				new UrlBasedCorsConfigurationSource();
		corsConfigSource.registerCorsConfiguration("/rest/**", corsConfig);
		return corsConfigSource;
	}
	
	@Bean
	public CorsConfigurationSource authCorsConfigurationSource() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		
		corsConfig.setAllowedOrigins(allowedOrigins);
		corsConfig.setAllowedMethods(allowedMethods);
		corsConfig.setAllowedHeaders(allowedHeaders);
		corsConfig.setAllowCredentials(allowCredentials);
		
		UrlBasedCorsConfigurationSource corsConfigSource = 
				new UrlBasedCorsConfigurationSource();
		corsConfigSource.registerCorsConfiguration("/account/**", corsConfig);
		return corsConfigSource;
	}
}



