package kr.or.ddit.main.notice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.main.mapper.MainNoticeMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainNoticeServiceImpl implements MainNoticeService {
	
	private final MainNoticeMapper mapper;

	@Override
	public List<BoardVO> readBoardList(PaginationInfo<BoardVO> paging) {
		return mapper.selectMainNoticeList(paging);
	}

	@Override
	public int getTotalMainNoticeCount(PaginationInfo<BoardVO> paging) {
		return mapper.getTotalMainNoticeCount(paging);
	}
	
}
