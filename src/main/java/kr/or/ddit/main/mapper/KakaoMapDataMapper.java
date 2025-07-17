package kr.or.ddit.main.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;

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
			@Param("keyword")String Keyword,
			@Param("typeCode1List") List<String> typeCode1List,
	        @Param("typeCode2List") List<String> typeCode2List,
	        @Param("saleTypeList") List<String> saleTypeList,
	        @Param("facilityOptionList") List<String> facilityOptionList,
	        @Param("mbrCd") String mbrCd,
	        @Param("parkingYn") String parkingYn,
	    	@Param("minFloor") Integer minFloor,
	    	@Param("maxFloor") Integer maxFloor,
	    	@Param("minArea") Double minArea,
	    	@Param("maxArea") Double maxArea
			);
	public List<Map<String, Object>> selectListingDetailList(
				@Param("lstgId") String lstgId,  @Param("mbrCd") String mbrCd);
	public List<FacilityOptionVO> selectFacilityOptionsByListingId(@Param("lstgId") String lstgId);
	public int insertWishList(ListingWishlistVO vo);
	public int deleteWishList(ListingWishlistVO vo);
	public boolean isWishlisted(ListingWishlistVO vo);
	public int countWishListByLstgId(@Param("lstgId") String lstgId);
}
