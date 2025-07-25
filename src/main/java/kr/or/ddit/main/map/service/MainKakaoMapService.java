package kr.or.ddit.main.map.service;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.LstgViewLogVO;

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
			List<String> typeCode1List, // 대분류 필터
			List<String> typeCode2List, // 소분류 필터
			List<String> saleTypeList,   // 월세/전세/매매 등
			List<String> facilityOptionList, // 옵션 필터 (엘리베이터 등)
			String mbrCd, // 찜 확인용
			String parkingYn, // 주차 가능 여부
			Integer minFloor, Integer maxFloor, // 층수 범위
			Double minArea, Double maxArea, // 면적 범위
			Integer jeonseMin, Integer jeonseMax, // 전세 필터
			Integer depositMin, Integer depositMax, // 보증금
			Integer monthlyMin, Integer monthlyMax, // 월세
			Integer saleMin, Integer saleMax // 매매가
	);
	public List<Map<String, Object>> selectListingDetailList(String lstgId, String mbrCd);
	public List<FacilityOptionVO> selectFacilityOptionsByListingId(String lstgId);
	public int insertWishList(ListingWishlistVO vo);
	public int deleteWishList(ListingWishlistVO vo);
	public boolean isWishlisted(ListingWishlistVO vo);
	public int countWishListByLstgId(@Param("lstgId") String lstgId);
	
	public int countListingView(LstgViewLogVO vo);
}
