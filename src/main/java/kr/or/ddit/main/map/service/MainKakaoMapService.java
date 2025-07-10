package kr.or.ddit.main.map.service;

import java.util.List;

import kr.or.ddit.vo.ListingVO;

public interface MainKakaoMapService {
	public List<ListingVO> selectNotLatLngList();
	public int updateLatLng(ListingVO vo);
	public List<ListingVO> selectLatLngMarkList(double swLat, double swLng, double neLat, double neLng, Integer category);
	public List<ListingVO> selectCategory();
	public List<ListingVO> selectListingDetailList(String lstgId);
}
