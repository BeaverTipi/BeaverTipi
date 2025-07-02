package kr.or.ddit.admin.member.service;

import java.util.List;
// import java.util.Map; // Map 임포트는 더 이상 필요 없습니다.

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SearchConditionVO; // SearchConditionVO를 임포트합니다.

public interface ManageMemberService {
    // 파라미터 타입을 Map에서 SearchConditionVO로 변경합니다.
    public List<MemberVO> readMemberList(SearchConditionVO searchCondition);
}