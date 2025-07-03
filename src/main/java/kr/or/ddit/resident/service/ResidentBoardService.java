package kr.or.ddit.resident.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.ResidentBoardVO;

public interface ResidentBoardService {

	public List<ResidentBoardVO> getBoardList(PaginationInfo paging);
	
	public int getTotalRecord(PaginationInfo paging);
	
	public ResidentBoardVO getBoard(String rsdBrdId);
	
	public void insertBoard(ResidentBoardVO boardVO);
	
	public int updateBoard(ResidentBoardVO boardVO);
	
	public int deleteBoard(String rsdBrdId);
	
	public void viewCount(String rsdBrdId);
	
}
