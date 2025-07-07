package kr.or.ddit.admin.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;

@Mapper
public interface ManageMemberMapper {
	//회원 상태 조회 메서드
    public List<MemberVO> selectMemberList(MemberSearchVO MemberSearch);

    //회원 상태 업데이트 메서드
    public int updateMemberStatus(MemberVO memberVO);
}