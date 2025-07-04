<<<<<<< HEAD
package kr.or.ddit.building.managed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.BuildingManagedMapper;
import kr.or.ddit.vo.BuildingVO;


@Service
public class ManagedServiceImpl implements ManagedService {
		
	@Autowired
    private BuildingManagedMapper buildingManagedMapper;

	    @Override
	    public int insertUnit(BuildingVO unit) {
	        return buildingManagedMapper.insertUnit(unit);
	    }

	    @Override
	    public List<BuildingVO> selectUnitListByBldgId(String bldgId) {
	        return buildingManagedMapper.selectUnitListByBldgId(bldgId);
	    }

	    @Override
	    public BuildingVO selectUnitById(String unitId) {
	        return buildingManagedMapper.selectUnitById(unitId);
	    }

	    @Override
	    public int updateUnit(BuildingVO unit) {
	        return buildingManagedMapper.updateUnit(unit);
	    }

	    @Override
	    public int deleteUnit(String unitId) {
	        return buildingManagedMapper.deleteUnit(unitId);
	    }


}
=======
package kr.or.ddit.building.managed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.BuildingManagedMapper;
import kr.or.ddit.vo.BuildingVO;
@Service
public class ManagedServiceImpl implements ManagedService {
	@Autowired
	private BuildingManagedMapper buildingManagedMapper;
	
	@Override
	public int insertUnit(BuildingVO unit) {
		// TODO Auto-generated method stub
		return buildingManagedMapper.insertUnit(unit);
	}

	@Override
	public List<BuildingVO> selectUnitListByBldgId(String bldgId) {
		// TODO Auto-generated method stub
		return buildingManagedMapper.selectUnitListByBldgId(bldgId);
	}

	@Override
	public BuildingVO selectUnitById(String unitId) {
		// TODO Auto-generated method stub
		return buildingManagedMapper.selectUnitById(unitId);
	}

	@Override
	public int updateUnit(BuildingVO unit) {
		// TODO Auto-generated method stub
		return buildingManagedMapper.updateUnit(unit);
	}

	@Override
	public int deleteUnit(String unitId) {
		// TODO Auto-generated method stub
		return buildingManagedMapper.deleteUnit(unitId);
	}

}
>>>>>>> branch 'developer_KSW' of https://github.com/BeaverTipi/BeaverTipi.git
