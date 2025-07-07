package kr.or.ddit.admin.member.service;

import java.util.List;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;

public interface ManageMemberService {
    public List<MemberVO> readMemberList(MemberSearchVO searchCondition);

    // ⭐ 회원 상태 업데이트 메서드 추가 ⭐
    public int updateMemberStatus(String mbrCd, String mbrStatusCode);
}