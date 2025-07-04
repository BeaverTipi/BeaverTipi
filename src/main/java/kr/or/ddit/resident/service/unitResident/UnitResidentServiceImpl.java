package kr.or.ddit.resident.service.unitResident;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.UnitResidentMapper;
import kr.or.ddit.vo.UnitResidentVO;

/** 
*
*
* <pre>
* << 개정이력(Modification Information) >>
*   
*   수정일         수정자         수정내용
*  -----------      -------------    ---------------------------
*  2025. 7. 4.
*
* </pre>
*/
@Service
public class UnitResidentServiceImpl implements UnitResidentService {

	@Autowired
	private UnitResidentMapper mapper;
	
	
	@Override
	public List<UnitResidentVO> getUnitsByMember(String mbrCd) {
		return mapper.selectByMember(mbrCd);
	}

}
