package kr.or.ddit.resident.unitResident.service;

import java.util.List;

import kr.or.ddit.vo.UnitResidentVO;

public interface UnitResidentService {

	List<UnitResidentVO> getUnitsByMember(String mbrCd);
}
