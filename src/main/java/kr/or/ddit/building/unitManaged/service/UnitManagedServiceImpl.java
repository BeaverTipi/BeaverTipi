package kr.or.ddit.building.unitManaged.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.UnitManagedMapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.UnitVO;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Service
public class UnitManagedServiceImpl implements UnitManagedService {

	 @Autowired
	    private UnitManagedMapper unitMapper;

	    @Override
	    public int insertUnitList(List<UnitVO> unitList) {
	        int count = 0;
	        for (UnitVO unit : unitList) {
	            log.info("유닛테이블에 등록할거: {}", unit);
	            count += unitMapper.insertUnit(unit);
	        }
	        return count;
	    }
	    
	    @Override
	    public List<UnitVO> selectUnitListByBldgId(String bldgId) {
	        return unitMapper.selectUnitListByBldgId(bldgId);
}

}
