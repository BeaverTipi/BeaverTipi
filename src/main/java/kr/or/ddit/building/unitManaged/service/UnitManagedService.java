package kr.or.ddit.building.unitManaged.service;

import java.util.List;

import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.UnitVO;

public interface UnitManagedService {
	
	public int insertUnitList(List<UnitVO> unitList);
	//건물정보에 상세정보를 뿌려줄라고
	List<UnitVO> selectUnitListByBldgId(String bldgId);
}
