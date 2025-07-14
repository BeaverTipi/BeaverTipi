package kr.or.ddit.main.map.service;

import java.util.List;

import kr.or.ddit.vo.ListingVO;

public interface MainKakaoMapService {
	public List<ListingVO> selectNotLatLngList();
	public int updateLatLng(ListingVO vo);
	public List<ListingVO> selectLatLngMarkList(
			double swLat, 
			double swLng, 
			double neLat, 
			double neLng, 
			Integer category, 
			String keyword,
			List<Integer> typeCode1List,      // 대분류 필터
			List<Integer> typeCode2List,      // 소분류 필터
			List<Integer> saleTypeList,       // 거래 유형
			List<String> facilityOptionList,  // 시설 옵션
			String mbrCd,                     // 찜 여부 확인용
			String parkingYn,                 // 주차 가능 여부
			Integer minFloor,                 // 최소 층수
			Integer maxFloor,                 // 최대 층수
			Integer minArea,                  // 최소 공급면적(m²)
			Integer maxArea                   // 최대 공급면적(m²)
		);
	public List<ListingVO> selectCategory();
	public List<ListingVO> selectListingDetailList(String lstgId);
}
