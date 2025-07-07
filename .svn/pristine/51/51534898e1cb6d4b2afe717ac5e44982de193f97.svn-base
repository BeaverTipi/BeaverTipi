package kr.or.ddit.util.validate.exception;

import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

public class UserNotRegisteredException extends OAuth2AuthenticationException {
    public static final String NOT_REGISTERED_CODE = "register-required";
    
    private final OAuth2User unRegisteredUser;
    private final ClientRegistration clientRegistration;
    
    public UserNotRegisteredException(OAuth2User unRegisteredUser, ClientRegistration clientRegistration) {
    	super(new OAuth2Error(NOT_REGISTERED_CODE), "User not registered");
        this.unRegisteredUser = unRegisteredUser;
        this.clientRegistration = clientRegistration;
    }

    public OAuth2User getUnRegisteredUser() {
    	return unRegisteredUser;
    }

    public ClientRegistration getClientRegistration() {
    	return clientRegistration;
    }
}

