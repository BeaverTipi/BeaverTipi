package kr.or.ddit.admin.board.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.admin.board.mapper.NoticePostMapper;
import kr.or.ddit.vo.BoardVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NoticePostServiceImpl implements NoticePostService {
	
	public final NoticePostMapper mapper;
	
	@Override
	public List<BoardVO> readNoticeList() {
		return mapper.selectNoticeList();
	}

}
