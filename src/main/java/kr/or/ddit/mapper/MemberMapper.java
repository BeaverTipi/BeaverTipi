package kr.or.ddit.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.MemberVO;

@Mapper
public interface MemberMapper {
	public MemberVO selectMemberByUsername(String username);
	public MemberVO selectMemberByMail(String username);
	
	public List<MemberVO> selectMemberList();
}
