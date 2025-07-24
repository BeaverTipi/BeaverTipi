package kr.or.ddit.admin.board.service;

import java.util.List;
import java.util.Map;
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
	public List<Map<String, Object>> readBoardList(PaginationInfo<BoardVO> paging) {
		return mapper.selectBoardList(paging);
	}

	@Override
	public Optional<Map<String, Object>> readBoard(String brdNo) {
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
	public int deleteBoard(String board) {
		return mapper.deleteBoard(board);
	}

	@Override
	public int getTotalBoardRecord(PaginationInfo<BoardVO> paging) {
		return mapper.selectTotalBoardRecord(paging);
	}
}
