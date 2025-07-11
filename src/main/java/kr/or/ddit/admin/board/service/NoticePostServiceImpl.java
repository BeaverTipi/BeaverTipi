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
	public List<BoardVO> readBoardList(PaginationInfo<BoardVO> paging) {
		return mapper.selectBoardList(paging);
	}

	@Override
	public Optional<BoardVO> readBoard(String brdNo) {
		return Optional.ofNullable(mapper.selectBoardById(brdNo));
	}

	@Override
	public int createBoard(BoardVO board) {
		return mapper.insertBoard(board);
	}

	@Override
	public int modifyBoard(BoardVO board) {
		return mapper.updateBoard(board);
	}

	@Override
	public int deleteBoard(BoardVO board) {
		return mapper.softDeleteBoard(board.getBrdNo());
	}

	@Override
	public int getTotalBoardRecord(PaginationInfo<BoardVO> paging) {
		return mapper.selectTotalBoardRecord(paging);
	}
}
