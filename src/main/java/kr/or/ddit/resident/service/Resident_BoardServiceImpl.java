package kr.or.ddit.resident.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.resident.mapper.ResidentBoardMapper;
import kr.or.ddit.vo.Resident_BoardVO;

@Service
public class Resident_BoardServiceImpl implements Resident_BoardService {

	@Autowired
	private ResidentBoardMapper mapper;
	
	@Override
	public List<Resident_BoardVO> getBoardList() {
		return mapper.selectBoardList();
	}

	@Override
	public Resident_BoardVO getBoard(String rsdBrdId) {
		return mapper.selectBoardById(rsdBrdId);
	}

	@Override
	public void insertBoard(Resident_BoardVO boardVO) {
		mapper.insertBoard(boardVO);
		
	}

	@Override
	public int updateBoard(Resident_BoardVO boardVO) {
		return mapper.updateBoard(boardVO);
	}

	@Override
	public int deleteBoard(String rsdBrdId) {
		return mapper.deleteBoard(rsdBrdId);
	}

}
