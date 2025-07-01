package kr.or.ddit.member.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.MemberVO;

@Mapper
public interface MemberMapper {
	public List<MemberVO> selectMemberList();
	
	public MemberVO selectMemberByUsername(@Param("username")String username);
	
	public MemberVO selectMemberByMail(@Param("mail") String mail);
	
	public int insertMember(MemberVO member);
	
	public int updateMemDelete(String username);
	
	public int updateMember(MemberVO member);
}
