package kr.or.ddit.admin.board.service;

import java.util.List;
import java.util.Optional;

import kr.or.ddit.vo.BoardVO;

public interface NoticePostService {
	public List<BoardVO> readNoticeList();
	public Optional<BoardVO> readNotice(String brdNo);
	public void createNotice(BoardVO board);
	public void modifyNotice(BoardVO board);
	public void deleteNotice(BoardVO board);

}
