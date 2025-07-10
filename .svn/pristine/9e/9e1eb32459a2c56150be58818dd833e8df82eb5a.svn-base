package kr.or.ddit.main.map.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.main.mapper.KakaoMapDataMapper;
import kr.or.ddit.vo.ListingVO;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class MainKakaoMapServiceImpl implements MainKakaoMapService {
	
	private final KakaoMapDataMapper kakaoMapDataMapper;

	@Override
	public List<ListingVO> selectNotLatLngList() {
		return kakaoMapDataMapper.selectNotLatLngList();
	}

	@Override
	public int updateLatLng(ListingVO vo) {
		return kakaoMapDataMapper.updateLatLng(vo);
	}

	@Override
	public List<ListingVO> selectLatLngMarkList(double swLat, double swLng, double neLat, double neLng, Integer category) {
		return kakaoMapDataMapper.selectLatLngMarkList(swLat, swLng, neLat, neLng, category);
	}
	
	@Override
	public List<ListingVO> selectCategory() {
		return kakaoMapDataMapper.selectCategory();
	}

	@Override
	public List<ListingVO> selectListingDetailList(String lstgId) {
		return kakaoMapDataMapper.selectListingDetailList(lstgId);
	}




}
