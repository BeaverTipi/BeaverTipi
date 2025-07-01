package kr.or.ddit.security.oauth2;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.user.OAuth2User;

import kr.or.ddit.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.RoleAchievedVO;

public class OAuth2MemberVOWrapperForKakao implements OAuth2User, RealUserWrapper<MemberVO> {

    private final OAuth2User delegate;
    private final MemberVO member;
    private final Collection<GrantedAuthority> authorities;
    
    public OAuth2MemberVOWrapperForKakao(OAuth2User delegate, MemberVO member,String...roles) {
        this.delegate = delegate;
        this.member = member;
		this.authorities = Stream.concat(
				delegate.getAuthorities().stream(), 
				AuthorityUtils.createAuthorityList(roles).stream()).toList();
    }

    @Override
    public Map<String, Object> getAttributes() {
        return delegate.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Stream.concat(
            delegate.getAuthorities().stream(),
            AuthorityUtils.createAuthorityList(
                member.getMemRoleList().stream()
                    .map(RoleAchievedVO::getUserRoleId)
                    .toArray(String[]::new)
            ).stream()
        ).collect(Collectors.toSet());
    }

    @Override
    public String getName() {
        return delegate.getName(); // Kakao는 "id" 를 반환
    }

    @Override
    public MemberVO getRealUser() {
        return member;
    }
}
