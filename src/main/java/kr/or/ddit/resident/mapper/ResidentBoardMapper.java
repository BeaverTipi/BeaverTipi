package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.ResidentBoardVO;

@Mapper
public interface ResidentBoardMapper {

	public List<ResidentBoardVO> selectBoardList(PaginationInfo paging);

	public int selectTotalCount(PaginationInfo paging);

	public ResidentBoardVO selectBoardById(ResidentBoardVO board);

	public int updateBoard(ResidentBoardVO boardVO);

	public int insertBoard(ResidentBoardVO boardVO);

	public int softDeleteBoard(String rsdBrdId);

	public int updateBoardViewCount(ResidentBoardVO board);
	
	public List<ResidentBoardVO> selectDeletedBoardList(PaginationInfo paging);

	public int selectDeletedTotalCount(PaginationInfo paging);
	
	public int restoreBoard(String rsdBrdId);
	
	public int permanentDeleteBoard(String rsdBrdId);

}
