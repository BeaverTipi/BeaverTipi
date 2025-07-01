package kr.or.ddit.admin.member.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.ddit.member.mapper.MemberMapper;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ManageMemberServiceImpl implements ManageMemberService {
	
	private final MemberMapper mapper;
	
	@Override
	public List<MemberVO> readMemberList(Map<String, Object> param) {
		return mapper.selectMemberList(param);
	}
	
}
