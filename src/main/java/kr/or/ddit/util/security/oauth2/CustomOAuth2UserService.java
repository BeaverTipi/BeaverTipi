package kr.or.ddit.util.security.oauth2;

import java.util.Map;

import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.or.ddit.main.mapper.MemberMapper;
import kr.or.ddit.util.validate.exception.UserNotRegisteredException;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberMapper memberMapper;
    private final String deleteValue;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        // 기본 OAuth2UserService로부터 사용자 정보 받아옴
        DefaultOAuth2UserService delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        // 어떤 서비스(google, kakao 등)인지 확인
        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = null;

        if ("kakao".equals(registrationId)) {
            Map<String, Object> kakaoAccount = (Map<String, Object>) attributes.get("kakao_account");
            email = (String) kakaoAccount.get("email");
        } else {
            // fallback – 다른 OAuth2 공급자 처리할 경우
            email = (String) attributes.get("email");
        }

        log.info("소셜 로그인 이메일: {}", email);

        MemberVO member = memberMapper.selectMemberByMail(email);

        if (member == null) {
            throw new OAuth2AuthenticationException(
                new OAuth2Error("register-required"),
                new UserNotRegisteredException(oAuth2User, userRequest.getClientRegistration())
            );
        } else if (deleteValue.equals(member.getMbrStatusCode())) {
            throw new OAuth2AuthenticationException( 
            		new OAuth2Error("deleted-user", "탈퇴한 회원입니다.", null)
            		);
        }

        return new OAuth2MemberVOWrapperForKakao(oAuth2User, member);
    }
}
