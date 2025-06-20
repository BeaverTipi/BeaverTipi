package kr.or.ddit.mapper;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.MemberVO;

@Mapper
public interface MemberMapper {
	public MemberVO selectMemberByUsername(String username);
	public MemberVO selectMemberByMail(String username);
}
