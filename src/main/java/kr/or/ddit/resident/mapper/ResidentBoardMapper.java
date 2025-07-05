package kr.or.ddit.resident.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.ResidentBoardVO;

@Mapper
public interface ResidentBoardMapper {

	List<ResidentBoardVO> selectBoardList(PaginationInfo paging);

	int selectTotalCount(PaginationInfo paging);

	ResidentBoardVO selectBoardById(ResidentBoardVO board);

	int updateBoard(ResidentBoardVO boardVO);

	int insertBoard(ResidentBoardVO boardVO);

	int deleteBoard(String rsdBrdId);

	int updateBoardViewCount(ResidentBoardVO board);

}
