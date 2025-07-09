package kr.or.ddit.util.security.auth;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import kr.or.ddit.main.mapper.MemberMapper;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
// 직접 bean 등록.
@Slf4j
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService{
	private final MemberMapper mapper;
	private final String deleteValue;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		MemberVO realUser = mapper.selectMemberByUsername(username);
		log.info("▶▶ realUser 조회 결과: {}", realUser);

		if(realUser==null) {
			throw new UsernameNotFoundException(String.format("%s 회원 없음", username));
		}
		
		return new MemberVOWrapper(realUser,deleteValue);
	}
}
