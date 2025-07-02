package kr.or.ddit.admin.member.mapper; // 매퍼 인터페이스가 위치할 패키지

import java.util.List;

import org.apache.ibatis.annotations.Mapper; // MyBatis 매퍼임을 나타내는 어노테이션

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SearchConditionVO;

@Mapper // Spring이 이 인터페이스를 MyBatis 매퍼로 스캔하도록 지시합니다.
public interface ManageMemberMapper {

    /**
     * 검색 조건에 맞는 회원 목록을 조회합니다.
     * @param searchCondition 검색 조건을 담은 VO 객체
     * @return MemberVO 객체 리스트
     */
    public List<MemberVO> selectMemberList(SearchConditionVO searchCondition);

    // 필요하다면 여기에 다른 회원 관리 관련 DB 작업 메서드를 추가합니다.
    // 예: public int updateMember(MemberVO member);
    // 예: public int deleteMember(String mbrCd);
}