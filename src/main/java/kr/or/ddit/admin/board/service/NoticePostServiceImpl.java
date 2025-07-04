package kr.or.ddit.admin.board.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.mapper.NoticePostMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticePostServiceImpl implements NoticePostService {
	
	private final NoticePostMapper mapper;
	
	@Override
	public List<BoardVO> readNoticeList(PaginationInfo<BoardVO> paging) {
		return mapper.selectNoticeList(paging);
	}

	@Override
	public Optional<BoardVO> readNotice(String brdNo) {
		return Optional.ofNullable(mapper.selectNoticeById(brdNo));
	}

	@Override
	public void createNotice(BoardVO board) {
		mapper.insertNotice(board);
	}

	@Override
	public void modifyNotice(BoardVO board) {
		mapper.updateNotice(board);
	}

	@Override
	public void deleteNotice(BoardVO board) {
		mapper.deleteNotice(board);
		
	}

	@Override
	public int getTotalNoticeRecord(PaginationInfo<BoardVO> paging) {
		return mapper.selectTotalNoticeRecord(paging);
	}

}
