package kr.or.ddit.security.auth;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import kr.or.ddit.vo.MemberVO;

public class MemberVOWrapper extends User implements RealUserWrapper<MemberVO> {
	private final MemberVO realUser;
	
	public MemberVOWrapper(MemberVO realUser) {
		super(
				realUser.getMemId()
				, realUser.getMemPassword()
				, !realUser.isMemDelete()
				, true
				, true
				, true
				, AuthorityUtils.createAuthorityList(realUser.getMemRole())
				);
		this.realUser = realUser;
	}
	
	@Override
	public MemberVO getRealUser() {
		return realUser;
	}


}
