package kr.or.ddit.building.managed.service;

import java.util.List;

import kr.or.ddit.vo.BuildingVO;

public interface ManagedService {

    
    // 세대 등록
    int insertUnit(BuildingVO unit);
    
    // 세대 목록 조회 (건물 기준)
    List<BuildingVO> selectUnitListByBldgId(String bldgId);
    
    // 세대 상세 조회
    BuildingVO selectUnitById(String unitId);
    
    // 세대 수정
    int updateUnit(BuildingVO unit);
    
    // 세대 삭제
    int deleteUnit(String unitId);
}
