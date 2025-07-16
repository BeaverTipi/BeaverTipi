package kr.or.ddit.admin.businessads.service;

import java.util.List;

import org.springframework.stereotype.Service;

import jakarta.inject.Inject;
import kr.or.ddit.admin.mapper.BusinessAdsMapper;
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
	
	
	
}