package kr.or.ddit.admin.board.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

public interface NoticePostService {
	public List<BoardVO> readBoardList(PaginationInfo<BoardVO> paging);
    public BoardVO readBoard(String brdNo);
    public int createBoard(BoardVO board);
    public int modifyBoard(BoardVO board);
    public int deleteBoard(String brdNo);
    public int getTotalBoardRecord(PaginationInfo<BoardVO> paging);
}
