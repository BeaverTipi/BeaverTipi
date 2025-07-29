package kr.or.ddit.admin.board.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

public interface NoticePostService {
	public List<BoardVO> readBoardList(PaginationInfo<BoardVO> paging);
    public BoardVO readBoard(String brdNo);
    public int createBoard(BoardVO board);
    public int modifyBoard(BoardVO board);
    public int deleteBoard(String brdNo);
    public int createFAQ(BoardVO board);
    public int updateFAQ(BoardVO board);
    public int createQna(BoardVO board);
    public int updateQna(BoardVO board);
    public int getTotalBoardRecord(PaginationInfo<BoardVO> paging);
}
