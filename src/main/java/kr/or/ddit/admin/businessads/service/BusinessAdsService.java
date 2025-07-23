package kr.or.ddit.admin.businessads.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

public interface BusinessAdsService {

	public List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO);

	public int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO);

	public BoardVO selectBusinessAdsDetail(String brdNo);
	
	public int updateAdsStatus(BoardVO boardToUpdate);
	
	public List<BoardVO> selectApprovedAdsForMain(); // 메인 페이지에 표시할 승인된 광고 목록 조회
}