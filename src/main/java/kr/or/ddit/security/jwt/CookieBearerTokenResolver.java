package kr.or.ddit.security.jwt;

import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.security.oauth2.server.resource.web.BearerTokenResolver;
import org.springframework.stereotype.Component;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class CookieBearerTokenResolver implements BearerTokenResolver {
	
	public static final String ACCESSTOKENCOOKIE = "access_token";

	@Override
	public String resolve(HttpServletRequest request) {
		return Optional.ofNullable(request.getCookies())
				.map(Arrays::stream)
				.orElse(Stream.empty())
				.filter(c->ACCESSTOKENCOOKIE.equals(c.getName()))
				.findFirst()
				.map(Cookie::getValue)
				.orElse(null);
	}

}
