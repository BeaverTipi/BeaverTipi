package kr.or.ddit.resident.service.unitResident;

import java.util.List;

import kr.or.ddit.vo.UnitResidentVO;

public interface UnitResidentService {

	List<UnitResidentVO> getUnitsByMember(String mbrCd);
}
