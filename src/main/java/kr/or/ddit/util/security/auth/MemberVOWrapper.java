package kr.or.ddit.util.security.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.RoleAchievedVO;

public class MemberVOWrapper extends User implements RealUserWrapper<MemberVO> {
	private final MemberVO realUser;
	
	
	public MemberVOWrapper(MemberVO realUser,String deleteValue) {
		super(
				realUser.getMbrId()
				, realUser.getMbrPw()
				, !realUser.getMbrStatusCode().equals(deleteValue)
				, true
				, true
				, true
				, AuthorityUtils.createAuthorityList(realUser.getMemRoleList()
															.stream()
					    									.map(RoleAchievedVO::getUserRoleId)
					    									.toArray(String[]::new))
				);
		this.realUser = realUser;
	}
	
	@Override
	public MemberVO getRealUser() {
		return realUser;
	}


}
