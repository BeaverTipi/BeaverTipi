package kr.or.ddit.admin.code.service;

import java.util.List;

import kr.or.ddit.vo.CommonCodeVO;

public interface CommonCodeService {
	public List<CommonCodeVO> readCommonCodeList(String codeGroup);
	public List<CommonCodeVO> readCommonCodeList(CommonCodeVO code);
}
