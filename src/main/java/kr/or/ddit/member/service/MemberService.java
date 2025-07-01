package kr.or.ddit.member.service;

import java.util.List;

import kr.or.ddit.vo.MemberVO;

public interface MemberService {
	public void createMember(MemberVO member);
	public List<MemberVO> readMemberList();
	public MemberVO readMember(String username);
	public void modifyMember(MemberVO member);
	public void removeMember(String username, String password);
}
