package kr.or.ddit.broker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.FacilityOptionVO;

@Mapper
public interface ListingMapper {
	public List<FacilityOptionVO> selectfacilityOptionList();
}
