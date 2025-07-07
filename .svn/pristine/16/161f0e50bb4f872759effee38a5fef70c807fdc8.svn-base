package kr.or.ddit.building.managed.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.building.mapper.BuildingManagedMapper;
import kr.or.ddit.vo.BuildingVO;

@Service
public class BuildingManagedServiceImpl implements BuildingManagedService {

	@Autowired
	private BuildingManagedMapper buildingManagedMapper;

	@Override
	public int insertBuilding(BuildingVO buildingVO) {
		return buildingManagedMapper.insertBuilding(buildingVO);
	}

	@Override
	public List<BuildingVO> selectBuildingListByRentalPtyId(String rentalPtyId) {
		return buildingManagedMapper.selectBuildingListByRentalPtyId(rentalPtyId);
	}

	@Override
	public BuildingVO selectBuildingById(String bldgId) {
		return buildingManagedMapper.selectBuildingById(bldgId);
	}

	@Override
	public int updateBuilding(BuildingVO buildingVO) {
		return buildingManagedMapper.updateBuilding(buildingVO);
	}

	@Override
	public int deleteBuilding(String bldgId) {
		return buildingManagedMapper.deleteBuilding(bldgId);
	}
}
