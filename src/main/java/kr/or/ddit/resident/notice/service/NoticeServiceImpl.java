package kr.or.ddit.resident.notice.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.resident.mapper.NoticeResidentMapper;
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.NoticeVO;

@Service
public class NoticeServiceImpl implements NoticeService {

	@Autowired
	private NoticeResidentMapper mapper;

	@Override
	public NoticeVO getNotice(NoticeVO notice) {
		// TODO Auto-generated method stub
		return mapper.selectNoticeById(notice.getNoticeNo());
	}

	@Override
	public int softDeleteNotice(String noticeNo) {
		return mapper.softDeleteNotice(noticeNo);
	}

	@Override
	public NoticeVO getNoticeById(String noticeNo) {
		// TODO Auto-generated method stub
		return mapper.selectNoticeById(noticeNo);
	}

	@Override
	@Transactional
	public void insertNotice(NoticeVO noticeVO) {
		mapper.insertNotice(noticeVO);
	}

	@Override
	public int updateNotice(NoticeVO boardVO) {
		// TODO Auto-generated method stub
		return mapper.updateNotice(boardVO);
	}

	@Override
	public List<NoticeVO> getNoticeList(PaginationInfo paging) {
		// TODO Auto-generated method stub
		return mapper.selectNoticeList(paging);
	}

	@Override
	public int getTotalNoticeCount(PaginationInfo paging) {
		// TODO Auto-generated method stub
		return mapper.selectNoticeTotalCount(paging);
	}

	@Override
	public void viewCount(NoticeVO board) {
		mapper.updateNoticeViewCount(board);
	}
	@Override
	@Transactional
    public void registerNotice(NoticeVO notice) {
		String seq = mapper.getNextNoticeNo();
		notice.setNoticeNo(seq);
		notice.setBrdNo(seq);
		
		notice.setNoticeTypeGrpCd("NOTPE");
		
		notice.setBrdCode("N0001");
		notice.setBrdDelYn("N");
		notice.setNoticeDelYn("N");
        mapper.insertBoard(notice);
        mapper.insertNotice(notice);
    }

//	
//
//	@Override
//	public List<ResidentBoardVO> getDeletedBoardList(PaginationInfo paging) {
//		// TODO Auto-generated method stub
//		return mapper.selectDeletedBoardList(paging);
//	}
//
//	@Override
//	public int getDeletedTotalCount(PaginationInfo paging) {
//		// TODO Auto-generated method stub
//		return mapper.selectDeletedTotalCount(paging);
//	}
//
//	@Override
//	public int restoreBoard(String rsdBrdId) {
//		// TODO Auto-generated method stub
//		return mapper.restoreBoard(rsdBrdId);
//	}
//
//	@Override
//	public int permanentDeleteBoard(String rsdBrdId) {
//		// TODO Auto-generated method stub
//		return mapper.permanentDeleteBoard(rsdBrdId);
//	}

	@Override
	public void insertBoard(NoticeVO boardVO) {

		mapper.insertBoard(boardVO);
	}

}
