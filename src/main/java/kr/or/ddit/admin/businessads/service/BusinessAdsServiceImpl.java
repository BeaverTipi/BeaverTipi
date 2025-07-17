package kr.or.ddit.admin.businessads.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import kr.or.ddit.admin.mapper.BusinessAdsMapper;
//import kr.or.ddit.util.file.mapper.FileMapper; // 사용하지 않으면 제거
import kr.or.ddit.util.page.PaginationInfo;
import kr.or.ddit.vo.BoardVO;

@Service
public class BusinessAdsServiceImpl implements BusinessAdsService {

	@Inject
    private BusinessAdsMapper businessAdsMapper;

	@Override
	public List<BoardVO> selectBusinessAdsList(PaginationInfo<BoardVO> pagingVO) {
		return businessAdsMapper.selectBusinessAdsList(pagingVO);
	}

	@Override
	public int selectBusinessAdsCount(PaginationInfo<BoardVO> pagingVO) {
		return businessAdsMapper.selectBusinessAdsCount(pagingVO);
	}

	// ⭐ 상세 정보 조회 메서드 구현 ⭐
	@Override
	public BoardVO selectBusinessAdsDetail(String brdNo) {
		return businessAdsMapper.selectBusinessAdsDetail(brdNo);
	}
}