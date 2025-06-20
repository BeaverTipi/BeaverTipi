package kr.or.ddit.conf;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import io.swagger.v3.oas.models.security.SecurityScheme.Type;

@Configuration
public class SwaggerConfig {
	@Bean
	public OpenAPI customOpenAPI() {
		final String SCHEME_KEY = "bearerAuth";
		SecurityScheme securityScheme = new SecurityScheme()
												.type(Type.HTTP)
												.scheme("bearer")
												.bearerFormat("JWT");
						
		return new OpenAPI()
					.components(
							new Components()
								.addSecuritySchemes(SCHEME_KEY, securityScheme)
							).addSecurityItem(new SecurityRequirement().addList(SCHEME_KEY));
		
	}
}
