package kr.or.ddit.resident.service;

import java.util.List;

import kr.or.ddit.vo.Resident_BoardVO;

public interface Resident_BoardService {

	public List<Resident_BoardVO> getBoardList();
	
	public Resident_BoardVO getBoard(String rsdBrdId);
	
	public void insertBoard(Resident_BoardVO boardVO);
	
	public int updateBoard(Resident_BoardVO boardVO);
	
	public int deleteBoard(String rsdBrdId);
	
}
