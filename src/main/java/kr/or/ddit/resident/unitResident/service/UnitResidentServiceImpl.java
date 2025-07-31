package kr.or.ddit.resident.unitResident.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.UnitResidentMapper;
import kr.or.ddit.vo.UnitResidentVO;

/** 
*
*
* <pre>
* << 개정이력(Modification Information) >>
*   
*   수정일         수정자         수정내용
*  -----------      -------------    ---------------------------
*  2025. 7. 4.
*
* </pre>
*/
@Service
public class UnitResidentServiceImpl implements UnitResidentService {

	@Autowired
	private UnitResidentMapper mapper;
	
	
	@Override
	public List<UnitResidentVO> getUnitsByMember(String mbrCd) {
		return mapper.selectByMember(mbrCd);
	}

	@Override
	public List<Map<String, Object>> getInitialBuildingPosts(String mbrCd) {
		
		return mapper.selectInitialBuildingPosts(mbrCd);
	}
	@Override
	public List<UnitResidentVO> selectMyUnitsInBuilding(String mbrCd, String bldgId) {
	    Map<String, Object> params = new HashMap<>();
	    params.put("mbrCd", mbrCd);
	    params.put("bldgId", bldgId);
	    return mapper.selectMyUnitsInBuilding(mbrCd, bldgId);
	}

	@Override
	public boolean isMyUnit(String mbrCd, String unitId) {
		   return mapper.isMyUnit(mbrCd, unitId) > 0;
	}
	

    @Override
    public List<UnitResidentVO> selectUnitResidentListByUnitId(String unitId) {
        return mapper.selectUnitResidentListByUnitId(unitId);
    }

}
