package kr.or.ddit.main.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.LstgViewLogVO;

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
	    	@Param("maxArea") Double maxArea,
	    	@Param("jeonseMin") Integer jeonseMin,
	    	@Param("jeonseMax") Integer jeonseMax,
	    	@Param("depositMin") Integer depositMin,
	    	@Param("depositMax") Integer depositMax,
	    	@Param("monthlyMin") Integer monthlyMin,
	    	@Param("monthlyMax") Integer monthlyMax,
	    	@Param("saleMin") Integer saleMin,
	    	@Param("saleMax") Integer saleMax
		);
	public ListingVO selectListingDetailList(
			@Param("lstgId") String lstgId,  
			@Param("mbrCd") String mbrCd);
	public List<FacilityOptionVO> selectFacilityOptionsByListingId(@Param("lstgId") String lstgId);
	public int insertWishList(ListingWishlistVO vo);
	public int deleteWishList(ListingWishlistVO vo);
	public boolean isWishlisted(ListingWishlistVO vo);
	public int countWishListByLstgId(@Param("lstgId") String lstgId);
	
	public int insertLstgViewCont(LstgViewLogVO vo);
}
