package kr.or.ddit.admin.member.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO; // MemberSearchVO import 추가 (getTotalRecord, getMemberList 메서드 타입 변경)

public interface ManageMemberService {
    /**
     * 전체 회원 목록을 페이징 및 검색 조건에 따라 조회합니다.
     * PaginationInfo 객체 내부에 MemberSearchVO(detailSearch) 객체가 포함되어 상세 검색 조건을 전달합니다.
     * @param paging 페이징 정보 (현재 페이지, 페이지당 레코드 수, 검색 조건 포함)
     * @return 회원 목록
     */
    // 제네릭 타입에 MemberSearchVO를 명시하여, PaginationInfo 내부의 detailSearch 객체 접근을 명확히 합니다.
    public List<MemberVO> getMemberList(PaginationInfo<MemberSearchVO> paging);

    /**
     * 페이징 및 검색 조건에 해당하는 전체 회원 수를 조회합니다.
     * @param paging 페이징 정보 (검색 조건 포함)
     * @return 전체 회원 수
     */
    // 제네릭 타입에 MemberSearchVO를 명시하여, PaginationInfo 내부의 detailSearch 객체 접근을 명확히 합니다.
    public int getTotalRecord(PaginationInfo<MemberSearchVO> paging);

    /**
     * 회원 상태 업데이트 메서드.
     * @param mbrCd 회원 코드
     * @param mbrStatusCode 변경할 회원 상태 코드
     * @return 업데이트된 레코드 수 (성공 여부를 boolean으로 반환하는 것이 더 명확할 수 있습니다.)
     */
    public boolean updateMemberStatus(String mbrCd, String mbrStatusCode); // int 대신 boolean으로 반환 타입 변경

    /**
     * ⭐ 새로 추가된 메서드: 특정 회원의 상세 정보를 조회합니다.
     * 이 메서드는 회원 상세 모달에 필요한 모든 정보를 반환해야 합니다.
     * @param mbrCd 조회할 회원의 고유 코드
     * @return 조회된 MemberVO 객체 (회원 역할 목록 등 상세 정보 포함)
     */
    public MemberVO getMemberDetailByMbrCd(String mbrCd);
}