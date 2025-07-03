package kr.or.ddit.resident.mapper;

import java.util.List;

import kr.or.ddit.vo.Resident_BoardVO;

public interface ResidentBoardMapper {

	public List<Resident_BoardVO> selectBoardList();
	
	public Resident_BoardVO selectBoardById(String brdNo);
	
	public int updateBoard(Resident_BoardVO boardVO);
	
	public int insertBoard(Resident_BoardVO boardVO);
	
	public int deleteBoard(String brdNo);
}
