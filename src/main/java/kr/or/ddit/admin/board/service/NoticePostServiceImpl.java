package kr.or.ddit.admin.board.service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

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
	public BoardVO readBoard(String brdNo) {
		return mapper.selectBoardById(brdNo);
	}

	@Override
	public int createBoard(BoardVO board) {
	    int rowcnt = mapper.insertBoard(board);

	    switch (board.getBrdCode()) {
	        case "S0001":
	            mapper.insertNotice(board);
	            break;
	        case "S0002":
	            mapper.insertQna(board);
	            break;
	        case "S0003":
	            mapper.insertFAQ(board);
	            break;
	    }
	    
	    return rowcnt;
	}

	@Override
	public int modifyBoard(BoardVO board) {
		int result = mapper.updateBoard(board);

		if ("S0001".equals(board.getBrdCode())) {
			mapper.updateNotice(board); 
		} else if ("S0002".equals(board.getBrdCode())) {
			mapper.updateQna(board);    
		} else if ("S0003".equals(board.getBrdCode())) {
			mapper.updateFAQ(board);    
		}

		return result;
	}

	@Override
	public int deleteBoard(String brdNo) {
		return mapper.deleteBoard(brdNo);
	}

	@Override
	public int getTotalBoardRecord(PaginationInfo<BoardVO> paging) {
		return mapper.selectTotalBoardRecord(paging);
	}

	@Override
	public int updateFAQ(BoardVO board) {
		return mapper.updateFAQ(board);
	}

	@Override
	public int updateQna(BoardVO board) {
		return mapper.updateQna(board);
	}

	@Override
	public BoardVO resolveBoardCondition(String tab, String ctgryCode, String statusCode, String keyword,
			String startDate, String endDate) {

		String brdCode = switch (tab) {
			case "notice" -> "S0001";
			case "qna"    -> "S0002";
			case "faq"    -> "S0003";
			default       -> "S0001";
		};

		BoardVO condition = new BoardVO();
		condition.setBrdCode(brdCode);
		condition.setBrdTitlNm(keyword);
		condition.setBrdCont(keyword);

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

		if (startDate != null && !startDate.isBlank()) {
			condition.setBrdPblsDtmStart(LocalDate.parse(startDate, formatter));
		}
		if (endDate != null && !endDate.isBlank()) {
			condition.setBrdPblsDtmEnd(LocalDate.parse(endDate, formatter));
		}

		if ("faq".equals(tab)) {
			condition.setFaqCtgry(ctgryCode);
		} else if ("qna".equals(tab)) {
			condition.setQnaCtgry(ctgryCode);
			condition.setQnaStatus(statusCode);
		}

		return condition;
	}

	@Override
	public int deleteBoardList(List<String> brdNoList) {
		return mapper.deleteBoardList(brdNoList);
	}

}
