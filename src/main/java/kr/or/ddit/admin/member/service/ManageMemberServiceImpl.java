package kr.or.ddit.admin.member.service;

import java.util.List;
// import java.util.Map; // Map 임포트는 더 이상 필요 없습니다.

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.ManageMemberMapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SearchConditionVO;
import lombok.RequiredArgsConstructor; // Lombok @RequiredArgsConstructor 사용

@Service
@RequiredArgsConstructor // final 필드를 주입받기 위해 사용
public class ManageMemberServiceImpl implements ManageMemberService {

    private final ManageMemberMapper mapper; // ManageMemberMapper 인터페이스 주입

    @Override
    public List<MemberVO> readMemberList(SearchConditionVO searchCondition) {
        // 이제 SearchConditionVO를 Map으로 변환할 필요 없이,
        // 매퍼 인터페이스의 메서드가 SearchConditionVO를 직접 받으므로 그대로 전달합니다.
        return mapper.selectMemberList(searchCondition); // 매퍼 인터페이스를 통해 호출
    }
}