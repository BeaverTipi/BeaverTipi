package kr.or.ddit.building.resident.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.BuildingVO;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;
import kr.or.ddit.vo.UnitVO;

public interface MoveInService {

    int registerResident(UnitResidentVO vo);             // 등록
    int updateResident(UnitResidentVO vo);               // 수정
    int deleteResident(UnitResidentVO vo);               // 삭제
    List<UnitResidentVO> getResidentsByBldgId(String bldgId); // 목록 조회
    List<MemberVO> searchMember(String keyword);         // 모달검색
    List<BuildingVO> getBuildingsByRentalPtyId(String rentalPtyId); // 건물정보 셀렉트 줄거임
    List<Map<String, Object>> getUnitResidentWithVacancy(String bldgId); // 공실포함
    List<UnitVO> getVacantUnitList(String bldgId);		//어휴 복잡해 직접입력 모달 ㅡㅡ

    int restoreResident(UnitResidentVO vo);				//재등록 안되는거 이걸로 대체
    int setUnitMaster(Map<String, Object> param);		// 입주대표설정
    List<Map<String, Object>> selectUnitResidentWithMaster(String bldgId); // 입주자 여러명

}
