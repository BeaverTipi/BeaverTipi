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
	public List<ListingVO> selectListingDetailList(String lstgId) {
		return kakaoMapDataMapper.selectListingDetailList(lstgId);
	}

	@Override
	public List<ListingVO> selectLatLngMarkList(
			double swLat, double swLng, double neLat, double neLng,
			Integer category, String keyword, 
			List<Integer> typeCode1List, List<Integer> typeCode2List,
			List<Integer> saleTypeList, List<String> facilityOptionList, String mbrCd,
			String parkingYn, Integer minFloor, Integer maxFloor,
			Double minArea, Double maxArea) {

		return kakaoMapDataMapper.selectLatLngMarkList(
			swLat, swLng, neLat, neLng,
			category, keyword,
			typeCode1List, typeCode2List,
			saleTypeList, facilityOptionList,
			mbrCd, parkingYn, minFloor, maxFloor, minArea, maxArea
		);
	}


}
