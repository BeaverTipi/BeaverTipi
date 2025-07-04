package kr.or.ddit.resident.service.board;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.ResidentBoardMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.ResidentBoardVO;

@Service
public class ResidentBoardServiceImpl implements ResidentBoardService {

	@Autowired
	private ResidentBoardMapper mapper;

	@Override
	public ResidentBoardVO getBoard(String rsdBrdId) {
		return mapper.selectBoardById(rsdBrdId);
	}

	@Override
	public void insertBoard(ResidentBoardVO boardVO) {
		String id = UUID.randomUUID().toString().substring(0,8);
		boardVO.setRsdBrdId(id);
		
		boardVO.setRsdBrdPblsDtm(LocalDateTime.now());
		boardVO.setRsdBrdDelYn("N");
		boardVO.setRsdBrdCnt(0);
		
		mapper.insertBoard(boardVO);
		
	}

	@Override
	public int updateBoard(ResidentBoardVO boardVO) {
		return mapper.updateBoard(boardVO);
	}

	@Override
	public int deleteBoard(String rsdBrdId) {
		return mapper.deleteBoard(rsdBrdId);
	}

	@Override
	public void viewCount(String rsdBrdId) {
		mapper.updateBoardViewCount(rsdBrdId);
	}

	@Override
	public List<ResidentBoardVO> getBoardList(PaginationInfo paging) {
		// TODO Auto-generated method stub
		return mapper.selectBoardList(paging);
	}

	@Override
	public int getTotalRecord(PaginationInfo paging) {
		// TODO Auto-generated method stub
		return mapper.selectTotalCount(paging);
	}

	
	

}
