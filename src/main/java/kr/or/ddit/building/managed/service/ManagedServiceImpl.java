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
    public int insertBuilding(BuildingVO building) {
        return buildingManagedMapper.insertBuilding(building);
    }

    @Override
    public List<BuildingVO> selectBuildingListByBldgId(String bldgId) {
        return buildingManagedMapper.selectBuildingListByBldgId(bldgId);
    }

    @Override
    public BuildingVO selectBuildingById(String unitId) {
        return buildingManagedMapper.selectBuildingById(unitId);
    }

    @Override
    public int updateBuilding(BuildingVO building) {
        return buildingManagedMapper.updateBuilding(building);
    }

    @Override
    public int deleteBuilding(String unitId) {
        return buildingManagedMapper.deleteBuilding(unitId);
    }

	

}
