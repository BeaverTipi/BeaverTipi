package kr.or.ddit.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.ListingVO;

@Mapper
public interface KakaoMapDataMapper {
	public List<ListingVO> selectNotLatLngList();
	public int updateLatLng(ListingVO vo);
	public List<ListingVO> selectLatLngMarkList(double swLat, double swLng, double neLat, double neLng, Integer category);
	public List<ListingVO> selectCategory();
	public List<ListingVO> selectListingDetailList(String lstgId);
	public List<ListingVO> selectFilter();
}
