package kr.or.ddit.resident.boardCartegory.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.BoardCartegoryMapper;
import kr.or.ddit.vo.BoardCartegoryVO;

/** 
*
*
* <pre>
* << 개정이력(Modification Information) >>
*   
*   수정일         수정자         수정내용
*  -----------      -------------    ---------------------------
*  2025. 7. 5.
*
* </pre>
*/
@Service
public class BoardCartegoryServiceImpl implements BoardCartegoryService {

	@Autowired
	private BoardCartegoryMapper mapper;

	@Override
	public List<BoardCartegoryVO> listAll() {
		return mapper.selectAllCartegories();
	}
	
	
	
}
