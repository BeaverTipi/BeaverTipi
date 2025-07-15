package kr.or.ddit.admin.mapper;
import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;

@Mapper
public interface ManageMemberMapper {
    /**
     * 전체 회원 목록을 페이징 및 검색 조건에 따라 조회합니다.
     * PaginationInfo 객체 내부에 MemberSearchVO(detailSearch) 객체가 포함되어 상세 검색 조건을 전달합니다.
     * @param paging 페이징 정보 (현재 페이지, 페이지당 레코드 수, 검색 조건 포함)
     * @return 회원 목록
     */
    // PaginationInfo의 제네릭 타입 명시: 일관성을 위해 MemberSearchVO 사용
    public List<MemberVO> selectMemberList(PaginationInfo<MemberSearchVO> paging);

    /**
     * 페이징 및 검색 조건에 해당하는 전체 회원 수를 조회합니다.
     * @param paging 페이징 정보 (검색 조건 포함)
     * @return 전체 회원 수
     */
    // PaginationInfo의 제네릭 타입 명시: 일관성을 위해 MemberSearchVO 사용
    public int selectTotalRecord(PaginationInfo<MemberSearchVO> paging); // 메서드명 'selectTotalRecord'로 변경 (일관성 유지)

    /**
     * ⭐ 변경된 메서드: 회원 상태 업데이트.
     * @param mbrCd 회원 코드
     * @param mbrStatusCode 변경할 회원 상태 코드
     * @return 업데이트된 레코드 수
     */
    // MemberVO 객체 대신 mbrCd와 mbrStatusCode를 개별 파라미터로 받도록 변경 (@Param 사용)
    public int updateMemberStatus(@Param("mbrCd") String mbrCd, @Param("mbrStatusCode") String mbrStatusCode);

    /**
     * ⭐ 새로 추가된 메서드: 특정 회원의 상세 정보를 조회합니다.
     * 이 메서드는 회원 상세 모달에 필요한 모든 정보를 반환해야 합니다.
     * @param mbrCd 조회할 회원의 고유 코드
     * @return 조회된 MemberVO 객체 (회원 역할 목록 등 상세 정보 포함)
     */
    public MemberVO selectMemberDetailByMbrCd(@Param("mbrCd") String mbrCd);

    // 컬렉션 매핑을 위한 서브 쿼리 (XML에 정의)는 보통 매퍼 인터페이스에 직접 정의하지 않습니다.
    // Mybatis의 <collection> 태그 내의 select 속성을 사용하거나, JOIN으로 처리합니다.
    // 따라서 이 주석 처리된 라인(selectRolesForMember)은 필요 없습니다.
    // public List<RoleAchievedVO> selectRolesForMember(String mbrCd);
}