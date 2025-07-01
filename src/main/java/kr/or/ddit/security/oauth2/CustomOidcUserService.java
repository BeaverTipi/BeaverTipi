package kr.or.ddit.security.oauth2;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import kr.or.ddit.member.mapper.MemberMapper;
import kr.or.ddit.validate.exception.OidcUserNotRegisteredException;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j	
@RequiredArgsConstructor
public class CustomOidcUserService extends OidcUserService{
	private final MemberMapper mapper;
	private final String deleteValue;
	
	@Override
	public OidcUser loadUser(OidcUserRequest userRequest) throws OAuth2AuthenticationException {
		OidcUser oidcUser = super.loadUser(userRequest);
		 
		ClientRegistration clientRegistration = userRequest.getClientRegistration();
		
		log.info("oidc Provider : {}", clientRegistration);  
		log.info("oidc : {}", oidcUser);
		log.info("oidc user name : {}", oidcUser.getName());
		log.info("oidc other attribute : {}", oidcUser.getAttributes());
		log.info("oidc authorities : {}", oidcUser.getAuthorities());
		
		log.info("oidc full name : {}", oidcUser.getFullName());
		log.info("oidc nickname : {}", oidcUser.getNickName());
		log.info("oidc email : {}", oidcUser.getEmail());
		log.info("oidc email verified : {}", oidcUser.getEmailVerified());
		log.info("oidc picture : {}", oidcUser.getPicture());
		String userNameAttributeName =  clientRegistration.getProviderDetails().getUserInfoEndpoint()
				.getUserNameAttributeName();
		
		log.info("oidc user name : {}", oidcUser.getAttributes().get(userNameAttributeName));
		
		String username =  oidcUser.getName();
		
 		MemberVO realUser = mapper.selectMemberByMail(oidcUser.getEmail());
 		
		if(realUser==null) {
 		//clientRegistration -> 어디에서 가입하는데 가입이 안되었는지 client==우리어플리케이션. 이메일정보를 유지하면서 해줘야하는것임. 필요정보를 다 담아줘야함.
			throw new OidcUserNotRegisteredException(oidcUser, clientRegistration);
		}else if(realUser.getMbrStatusCode().equals(deleteValue)) {
			throw new OAuth2AuthenticationException(new OAuth2Error("deleted-user"), "이미 탈퇴한 회원입니다.");
		}
		
		 return new OAuth2MemberVOWrapper(oidcUser, realUser,"dummy");
	}

}
