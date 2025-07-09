package kr.or.ddit.admin.member.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.MemberVO;

public interface ManageMemberService {
    /**
     * 전체 회원 목록을 페이징 및 검색 조건에 따라 조회합니다.
     * PaginationInfo 객체 내부에 MemberSearchVO(detailSearch) 객체가 포함되어 상세 검색 조건을 전달합니다.
     * @param paging 페이징 정보 (현재 페이지, 페이지당 레코드 수, 검색 조건 포함)
     * @return 회원 목록
     */
    public List<MemberVO> getMemberList(PaginationInfo paging);

    /**
     * 페이징 및 검색 조건에 해당하는 전체 회원 수를 조회합니다.
     * @param paging 페이징 정보 (검색 조건 포함)
     * @return 전체 회원 수
     */
    public int getTotalRecord(PaginationInfo paging);

    /**
     * 회원 상태 업데이트 메서드.
     * @param mbrCd 회원 코드
     * @param mbrStatusCode 변경할 회원 상태 코드
     * @return 업데이트된 레코드 수
     */
    public int updateMemberStatus(String mbrCd, String mbrStatusCode);
}