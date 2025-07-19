package kr.or.ddit.admin.businessads.service;

import java.util.List;

import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

public interface BusinessAdsService {

	public List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO);

	public int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO);

	public BoardVO selectBusinessAdsDetail(String brdNo);
	
	public int updateAdsStatus(String brdNo, String adsStatusCode);
}