package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.CommonCodeVO;

@Mapper
public interface CommonCodeMapper {
	public List<CommonCodeVO> selectCommonCodeList(String codeGroup);
}
