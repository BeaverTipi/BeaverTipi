package kr.or.ddit.building.managed.service;

import java.util.List;

import kr.or.ddit.vo.BuildingVO;





public interface ManagedService {

    // 건물 등록
    int insertBuilding(BuildingVO building);

    // 건물 + 세대 목록 조회 (bldgId 기준)
    List<BuildingVO> selectBuildingListByBldgId(String bldgId);

    // 건물 단건 조회 (unitId 기준)
    BuildingVO selectBuildingById(String unitId);

    // 건물 세대 정보 수정
    int updateBuilding(BuildingVO building);

    // 건물 세대 삭제
    int deleteBuilding(String unitId);
}
