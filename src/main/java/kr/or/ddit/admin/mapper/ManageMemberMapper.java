package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo; // PaginationInfo 임포트 추가
import kr.or.ddit.vo.MemberVO;

@Mapper
public interface ManageMemberMapper {
    /**
     * 전체 회원 목록을 페이징 및 검색 조건에 따라 조회합니다.
     * PaginationInfo 객체 내부에 MemberSearchVO(detailSearch) 객체가 포함되어 상세 검색 조건을 전달합니다.
     * @param paging 페이징 정보 (현재 페이지, 페이지당 레코드 수, 검색 조건 포함)
     * @return 회원 목록
     */
    public List<MemberVO> selectMemberList(PaginationInfo paging);

    /**
     * 페이징 및 검색 조건에 해당하는 전체 회원 수를 조회합니다.
     * @param paging 페이징 정보 (검색 조건 포함)
     * @return 전체 회원 수
     */
    public int selectTotalCount(PaginationInfo paging);

    /**
     * 회원 상태 업데이트 메서드.
     * @param memberVO 업데이트할 회원 정보 (회원 코드 및 상태 코드 포함)
     * @return 업데이트된 레코드 수
     */
    public int updateMemberStatus(MemberVO memberVO);

    // 컬렉션 매핑을 위한 서브 쿼리 (XML에 정의)
    // public List<RoleAchievedVO> selectRolesForMember(String mbrCd);
}