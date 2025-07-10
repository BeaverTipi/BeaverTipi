package kr.or.ddit.admin.code.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.CommonCodeMapper;
import kr.or.ddit.vo.CommonCodeVO;
import lombok.RequiredArgsConstructor;
@Service
@RequiredArgsConstructor
public class CommonCodeServiceImpl implements CommonCodeService {
	private final CommonCodeMapper mapper;
	@Override
	public List<CommonCodeVO> readCommonCodeList(String codeGroup) {
		return mapper.selectCommonCodeList(codeGroup);
	}
	@Override
	public List<CommonCodeVO> readCommonCodeList(CommonCodeVO code) {
		return mapper.selectCommonCodeVOList(code);
	}

}
