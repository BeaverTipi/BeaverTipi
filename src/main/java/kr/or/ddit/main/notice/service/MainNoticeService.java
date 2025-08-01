package kr.or.ddit.main.notice.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

public interface MainNoticeService {
	public List<BoardVO> readBoardList(PaginationInfo<BoardVO> paging);
	public int getTotalMainNoticeCount(PaginationInfo<BoardVO> paging);
}
