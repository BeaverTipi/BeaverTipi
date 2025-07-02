package kr.or.ddit.util.security.oauth2;

import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.core.oidc.OidcIdToken;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.RoleAchievedVO;
import lombok.ToString;

	/**
	 * oauth 로그인 성공 후, OidcUser 와 MemberVO 를 모두 래핑할 객체
	 */

@ToString
	public class OAuth2MemberVOWrapper implements OidcUser, RealUserWrapper<MemberVO>{
		
		private final MemberVO realUser;
		private final OidcUser oidcUser;
		private final Collection<GrantedAuthority> authorities;
		
		public OAuth2MemberVOWrapper(OidcUser oidcUser, MemberVO realUser, String...roles) {
			this.realUser = realUser;
			this.oidcUser = oidcUser;
			this.authorities = Stream.concat(
						oidcUser.getAuthorities().stream(), 
						AuthorityUtils.createAuthorityList(roles).stream()).toList();
		}
		
		@Override
		public MemberVO getRealUser() {
			return realUser;
		}
	
		@Override
		public Map<String, Object> getAttributes() {
			return oidcUser.getAttributes();
		}

		@Override
		public Collection<? extends GrantedAuthority> getAuthorities() {
			Collection<? extends GrantedAuthority> authorities1 = AuthorityUtils.createAuthorityList(realUser.getMemRoleList()
																												.stream()
																												.map(RoleAchievedVO::getUserRoleId)
																												.toArray(String[]::new));
			Collection<? extends GrantedAuthority> authorities2 = oidcUser.getAuthorities();
			
			return Stream.concat(authorities1.stream(), authorities2.stream()).collect(Collectors.toSet());
		}

		@Override
		public String getName() {
			return oidcUser.getName();
		}

		@Override
		public Map<String, Object> getClaims() {
			return oidcUser.getClaims();
		}

		@Override
		public OidcUserInfo getUserInfo() {
			return oidcUser.getUserInfo();
		}

		@Override
		public OidcIdToken getIdToken() {
			return oidcUser.getIdToken();
		}

	}

