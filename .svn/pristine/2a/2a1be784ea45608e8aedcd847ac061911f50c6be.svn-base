package kr.or.ddit.util.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.or.ddit.main.mapper.MemberMapper;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
// 직접 bean 등록.
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final MemberMapper mapper;
	private final String deleteValue;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO realUser = mapper.selectMemberByUsername(username);
		
		if(realUser==null) {
			throw new UsernameNotFoundException(String.format("%s 회원 없음", username));
		}
		
		return new MemberVOWrapper(realUser,deleteValue);
	}
}
