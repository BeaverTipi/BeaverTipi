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
			List<Integer> typeCode1List, // 대분류 필터
			List<Integer> typeCode2List, // 소분류 필터
			List<Integer> saleTypeList,   // 월세/전세/매매 등
			List<String> facilityOptionList, // 옵션 필터 (엘리베이터 등)
			String mbrCd // 찜 확인용
	);
	public List<ListingVO> selectCategory();
	public List<ListingVO> selectListingDetailList(String lstgId);
}
