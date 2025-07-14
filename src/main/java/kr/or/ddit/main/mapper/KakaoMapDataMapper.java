package kr.or.ddit.main.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.ListingVO;

@Mapper
public interface KakaoMapDataMapper {
	public List<ListingVO> selectNotLatLngList();
	public int updateLatLng(ListingVO vo);
	public List<ListingVO> selectLatLngMarkList(
			@Param("swLat")double swLat, 
			@Param("swLng")double swLng, 
			@Param("neLat")double neLat, 
			@Param("neLng")double neLng, 
			@Param("category")Integer category,
			@Param("keyword")String keyword,
			@Param("typeCode1List") List<Integer> typeCode1List,
	        @Param("typeCode2List") List<Integer> typeCode2List,
	        @Param("saleTypeList") List<Integer> saleTypeList,
	        @Param("facilityOptionList") List<String> facilityOptionList,
	        @Param("mbrCd") String mbrCd,
	        @Param("parkingYn") String parkingYn,
	        @Param("minFloor") Integer minFloor,
	        @Param("maxFloor") Integer maxFloor,
	        @Param("minArea") Integer minArea,
	        @Param("maxArea") Integer maxArea
			);
	public List<ListingVO> selectCategory();
	public List<ListingVO> selectListingDetailList(String lstgId);
}
