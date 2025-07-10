package kr.or.ddit.resident.notice.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.NoticeVO;

public interface NoticeService {

	public List<NoticeVO> getNoticeList(PaginationInfo paging);
	
	public int getTotalNoticeCount(PaginationInfo paging);
	
	public NoticeVO getNotice(NoticeVO board);
	
	public NoticeVO getNoticeById(String resBrdId);
	
	public void insertNotice(NoticeVO boardVO);
	
	public int updateNotice(NoticeVO boardVO);
	/**
	 * 게시글을 소프트 삭제합니다. (rsd_brd_del_yn = 'Y')
	 */
	public int softDeleteNotice(String rsdBrdId);
	
	public void viewCount(NoticeVO board);
	
	public void insertBoard(NoticeVO boardVO);
	
	public void registerNotice(NoticeVO notice);

	
//	
//	public List<ResidentBoardVO> getDeletedBoardList(PaginationInfo paging);
//	
//	public int getDeletedTotalCount(PaginationInfo paging);
//	
//	public int restoreBoard(String rsdBrdId);
//	
//	public int permanentDeleteBoard(String rsdBrdId);
	
	
	
	
	
}
