package kr.or.ddit.util.validate.exception;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OidcUserNotRegisteredException extends OAuth2AuthenticationException {
	public static final String NOT_REGISTERED_CODE = "register-required";
	
	private final OidcUser unRegisteredUser;
	private final ClientRegistration clientRegistration;

	public OidcUserNotRegisteredException(OidcUser unRegisteredUser, ClientRegistration clientRegistration) {
		super(NOT_REGISTERED_CODE);
		this.unRegisteredUser = unRegisteredUser;
		this.clientRegistration = clientRegistration;
	}
	
	public OidcUser getUnRegisteredUser() {
		return unRegisteredUser;
	}
	
	public ClientRegistration getClientRegistration() {
		return clientRegistration;
	}
	
	
}


