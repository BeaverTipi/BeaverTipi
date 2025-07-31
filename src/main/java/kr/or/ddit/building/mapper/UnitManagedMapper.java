package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.UnitVO;
@Mapper
public interface UnitManagedMapper {

	 public int insertUnit(UnitVO unit);
	 
	 List<UnitVO> selectUnitListByBldgId(String bldgId);
}
