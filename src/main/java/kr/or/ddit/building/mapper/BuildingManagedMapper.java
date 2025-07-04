package kr.or.ddit.building.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.BuildingVO;

@Mapper
public interface BuildingManagedMapper {
    // 세대 등록
    int insertUnit(BuildingVO unit);

    // 세대 목록 조회 (특정 건물 기준 등)
    List<BuildingVO> selectUnitListByBldgId(String bldgId);

    // 세대 상세 조회
    BuildingVO selectUnitById(String unitId);

    // 세대 수정
    int updateUnit(BuildingVO unit);

    // 세대 삭제
    int deleteUnit(String unitId);
}
