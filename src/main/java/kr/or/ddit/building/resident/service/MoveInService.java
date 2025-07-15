package kr.or.ddit.building.resident.service;

import java.util.List;

import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;

public interface MoveInService {

    int registerResident(UnitResidentVO vo);             // 등록
    int updateResident(UnitResidentVO vo);               // 수정
    int deleteResident(UnitResidentVO vo);               // 삭제
    List<UnitResidentVO> getResidentsByBldgId(String bldgId); // 목록 조회
    List<MemberVO> searchMember(String keyword);         // 모달검색

}
