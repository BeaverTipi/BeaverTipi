package kr.or.ddit.resident.unitResident.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.UnitResidentVO;

public interface UnitResidentService {

	List<UnitResidentVO> getUnitsByMember(String mbrCd);
	
    List<Map<String, Object>> getInitialBuildingPosts(String mbrCd);
    
    List<UnitResidentVO> selectMyUnitsInBuilding(String mbrCd, String bldgId);
    
    boolean isMyUnit(String mbrCd, String unitId);

}
