package kr.or.ddit.building.managed.service;

import java.util.List;

import kr.or.ddit.vo.BuildingVO;





public interface ManagedService {
	    int insertUnit(BuildingVO unit);

	    List<BuildingVO> selectUnitListByBldgId(String bldgId);

	    BuildingVO selectUnitById(String unitId);

	    int updateUnit(BuildingVO unit);

	    int deleteUnit(String unitId);
}
