package kr.or.ddit.main.map.service;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.ddit.main.mapper.KakaoMapDataMapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
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
	public List<Map<String, Object>> selectListingDetailList(String lstgId, String mbrCd) {
		List<Map<String, Object>> listingList = kakaoMapDataMapper.selectListingDetailList(lstgId, mbrCd);

		List<FacilityOptionVO> options = kakaoMapDataMapper.selectFacilityOptionsByListingId(lstgId);
		for (Map<String, Object> listing : listingList) {
			listing.put("facilityOptionList", options);
		}

		return listingList;
	}

	@Override
	public List<ListingVO> selectLatLngMarkList(double swLat, double swLng, double neLat, double neLng,
			Integer category, String keyword, List<String> typeCode1List, List<String> typeCode2List,
			List<String> saleTypeList, List<String> facilityOptionList, String mbrCd, String parkingYn,
			Integer minFloor, Integer maxFloor, Double minArea, Double maxArea) {

		return kakaoMapDataMapper.selectLatLngMarkList(swLat, swLng, neLat, neLng, category, keyword, typeCode1List,
				typeCode2List, saleTypeList, facilityOptionList, mbrCd, parkingYn, minFloor, maxFloor, minArea,
				maxArea);
	}

	@Override
	public List<FacilityOptionVO> selectFacilityOptionsByListingId(String lstgId) {
		return kakaoMapDataMapper.selectFacilityOptionsByListingId(lstgId);
	}

	@Override
	public int insertWishList(ListingWishlistVO vo) {
		return kakaoMapDataMapper.insertWishList(vo);
	}

	@Override
	public int deleteWishList(ListingWishlistVO vo) {
		return kakaoMapDataMapper.deleteWishList(vo);
	}

	@Override
	public boolean isWishlisted(ListingWishlistVO vo) {
		return kakaoMapDataMapper.isWishlisted(vo);
	}

	@Override
	public int countWishListByLstgId(String lstgId) {
		return kakaoMapDataMapper.countWishListByLstgId(lstgId);
	}

}
