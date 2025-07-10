package kr.or.ddit.admin.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.ManageMemberMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManageMemberServiceImpl implements ManageMemberService {

    private final ManageMemberMapper mapper;

    /**
     * 회원 목록을 페이징 및 검색 조건에 따라 조회합니다.
     * @param paging 페이징 정보 (현재 페이지, 페이지당 레코드 수, 검색 조건 포함)
     * @return 회원 목록
     */
    @Override
    public List<MemberVO> getMemberList(PaginationInfo paging) {
        return mapper.selectMemberList(paging);
    }

    /**
     * 페이징 및 검색 조건에 해당하는 전체 회원 수를 조회합니다.
     * @param paging 페이징 정보 (검색 조건 포함)
     * @return 전체 회원 수
     */
    @Override
    public int getTotalRecord(PaginationInfo paging) {
        return mapper.selectTotalCount(paging);
    }

    /**
     * 회원 상태 업데이트 메서드 구현.
     * @param mbrCd 회원 코드
     * @param mbrStatusCode 변경할 회원 상태 코드
     * @return 업데이트된 레코드 수
     */
    @Override
    public int updateMemberStatus(String mbrCd, String mbrStatusCode) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMbrCd(mbrCd);
        memberVO.setMbrStatusCode(mbrStatusCode);
        return mapper.updateMemberStatus(memberVO);
    }
}