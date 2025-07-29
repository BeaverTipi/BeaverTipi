package kr.or.ddit.admin.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Mapper
public interface NoticePostMapper {
    public List<BoardVO> selectBoardList(PaginationInfo<BoardVO> paging);
    public BoardVO selectBoardById(String brdNo);
    public int insertBoard(BoardVO board);
    public int updateBoard(BoardVO board);
    public int insertFAQ(BoardVO board);
    public int updateFAQ(BoardVO board);
    public int insertQna(BoardVO board);
    public int updateQna(BoardVO board);
    public int deleteBoard(String brdNo);
    public int selectTotalBoardRecord(PaginationInfo<BoardVO> paging);
}
