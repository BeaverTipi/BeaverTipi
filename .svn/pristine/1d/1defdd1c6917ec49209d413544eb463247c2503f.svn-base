package kr.or.ddit.admin.member.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.ManageMemberMapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.MemberSearchVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManageMemberServiceImpl implements ManageMemberService {

    private final ManageMemberMapper mapper;

  //회원 상태 조회 메서드 구현
    @Override
    public List<MemberVO> readMemberList(MemberSearchVO searchCondition) {
        return mapper.selectMemberList(searchCondition);
    }

    //회원 상태 업데이트 메서드 구현
    @Override
    public int updateMemberStatus(String mbrCd, String mbrStatusCode) {
        MemberVO memberVO = new MemberVO();
        memberVO.setMbrCd(mbrCd);
        memberVO.setMbrStatusCode(mbrStatusCode);
        return mapper.updateMemberStatus(memberVO); // 매퍼의 updateMemberStatus 호출 (아래 6번에서 추가 예정)
    }
}