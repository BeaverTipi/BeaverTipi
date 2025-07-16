/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			윤현식            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.resident.complaint.service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.resident.mapper.ComplaintMapper;
import kr.or.ddit.vo.ResidentBoardVO;

/**
 * 
 * @author 권성운
 * @since 2025 07 09 첫 생성일
 * @see
 *
 *
 */
@Service
public class ComplaintServiceImpl implements ComplaintService {

	@Autowired
	private ComplaintMapper mapper;

	@Override
	public ResidentBoardVO selectComplaintById(String rsdBrdId) {
		return mapper.selectComplaintById(rsdBrdId);
	}

	@Override
	@Transactional
	public void insertComplaint(ResidentBoardVO complaint) {
		  // 1) PK 채번
        String nextId = mapper.getNextComplaintId();
        complaint.setRsdBrdId(nextId);

        // 2) 기본값 세팅
        complaint.setRsdBrdPblsDtm(LocalDateTime.now());
        if (complaint.getOpenYn() == null || complaint.getOpenYn().isBlank()) {
            complaint.setOpenYn("Y");
        }
        complaint.setReqStatus("001");

        // 3) BOARD + RESIDENT_BOARD 동시 INSERT
        mapper.insertComplaintBoard(complaint);
        mapper.insertComplaint(complaint);

	}

	@Override
	@Transactional
	public void updateComplaint(ResidentBoardVO complaint) {
		complaint.setRsdBrdModDtm(LocalDateTime.now());
		
		mapper.updateComplaintBoard(complaint);
		mapper.updateComplaint(complaint);
	}

	@Override
	@Transactional
	public void deleteComplaint(String rsdBrdId) {
		mapper.softDeleteResidentBoard(rsdBrdId);
		mapper.softDeleteComplaint(rsdBrdId);
	}

	@Override
	public String getNextComplaintId() {
		// TODO Auto-generated method stub
		return mapper.getNextComplaintId();
	}

	@Override
	public int selectComplaintCount(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return mapper.selectComplaintTotalCount(param);
	}

	@Override
	public List<ResidentBoardVO> selectComplaintList(Map<String, Object> param) {
		// TODO Auto-generated method stub
		return mapper.selectComplaintList(param);
	}

}
