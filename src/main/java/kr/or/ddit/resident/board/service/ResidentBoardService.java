package kr.or.ddit.resident.board.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.ResidentBoardVO;

public interface ResidentBoardService {

	public List<ResidentBoardVO> getBoardList(PaginationInfo paging);
	
	public int getTotalRecord(PaginationInfo paging);
	
	public ResidentBoardVO getBoard(ResidentBoardVO board);
	
	public ResidentBoardVO getBoardById(String resBrdId);
	
	public void insertBoard(ResidentBoardVO boardVO);
	
	public int updateBoard(ResidentBoardVO boardVO);
	
	public int deleteBoard(String rsdBrdId);
	
	public void viewCount(ResidentBoardVO board);
	
}
