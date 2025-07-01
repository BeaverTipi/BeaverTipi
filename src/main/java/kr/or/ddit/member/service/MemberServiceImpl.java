package kr.or.ddit.member.service;

import java.util.List;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import kr.or.ddit.member.mapper.MemberMapper;
import kr.or.ddit.validate.exception.PKDuplicatedException;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MemberServiceImpl implements MemberService {
	private final MemberMapper mapper;
	
	private final PasswordEncoder passwordEncoder;
	
	private final AuthenticationManager authenticationManager; 
	
	@Override
	public void createMember(MemberVO member) {
		if(mapper.selectMemberByUsername(member.getMbrId())==null) {
			String encoded = passwordEncoder.encode(member.getMbrPw());
			member.setMbrPw(encoded);
			mapper.insertMember(member);
		}else {
			throw new PKDuplicatedException(member.getMbrId());
		}
	}

	@Override
	public List<MemberVO> readMemberList() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public MemberVO readMember(String username) {
		return mapper.selectMemberByUsername(username);
	}

	@Override
	public void modifyMember(MemberVO member) {
		UsernamePasswordAuthenticationToken inputData =
				UsernamePasswordAuthenticationToken.unauthenticated(member.getMbrId(), member.getMbrPw()); // 식별용으로 토큰이 필요하면 unauthenticated -> 검증용..
		
		authenticationManager.authenticate(inputData); // 인증을 위해 필요한 정보 줘라. 인증 실패면 exception뜬다.

		mapper.updateMember(member);
		
//		기존 인증 객체 변경
		changeAuthentication(member);
	}

	@Override
	public void removeMember(String username, String password) {
		UsernamePasswordAuthenticationToken inputData =
				UsernamePasswordAuthenticationToken.unauthenticated(username, password); // 식별용으로 토큰이 필요하면 unauthenticated -> 검증용..
		
		authenticationManager.authenticate(inputData); // 인증을 위해 필요한 정보 줘라. 인증 실패면 exception뜬다.
		mapper.updateMemDelete(username);
	}

	private void changeAuthentication(MemberVO member) {
		UsernamePasswordAuthenticationToken inputData =
				UsernamePasswordAuthenticationToken.unauthenticated(member.getMbrId(), member.getMbrPw());
		SecurityContext context = SecurityContextHolder.getContext();
		
		UsernamePasswordAuthenticationToken before =
				(UsernamePasswordAuthenticationToken) context.getAuthentication(); // 기존 인증 객체
		Object datails = before.getDetails();
		
		UsernamePasswordAuthenticationToken newAuthentication =
				(UsernamePasswordAuthenticationToken)authenticationManager.authenticate(inputData);
		newAuthentication.setDetails(datails);
		
		context.setAuthentication(newAuthentication);
	}

}
