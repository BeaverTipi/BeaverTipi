package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BuildingVO;


@Mapper
public interface BuildingManagedMapper {
	 // 건물 등록
    int insertBuilding(BuildingVO building);

    // 건물 기준 목록 조회 (세대 포함)
    List<BuildingVO> selectBuildingListByBldgId(String bldgId);

    // 건물 단건 조회 (세대 포함)
    BuildingVO selectBuildingById(String unitId);

    // 건물 세대 정보 수정 (UNIT 기준이지만 BUILDING 관리화면에서 처리)
    int updateBuilding(BuildingVO building);

    // 건물 세대 삭제
    int deleteBuilding(String unitId);
}

