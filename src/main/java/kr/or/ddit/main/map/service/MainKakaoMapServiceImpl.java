package kr.or.ddit.main.map.service;

import java.util.List;

import org.springframework.stereotype.Service;

import kr.or.ddit.main.mapper.KakaoMapDataMapper;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.LstgViewLogVO;
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

	public ListingVO selectListingDetailList(String lstgId, String mbrCd) {
		ListingVO vo = kakaoMapDataMapper.selectListingDetailList(lstgId, mbrCd); 
		List<FacilityOptionVO> options = kakaoMapDataMapper.selectFacilityOptionsByListingId(lstgId);
		vo.setFacOptions(options);
		return vo;
	}

	@Override
	public List<ListingVO> selectLatLngMarkList(double swLat, double swLng, double neLat, double neLng,
			Integer category, String keyword, List<String> typeCode1List, List<String> typeCode2List,
			List<String> saleTypeList, List<String> facilityOptionList, String mbrCd, String parkingYn,
			Integer minFloor, Integer maxFloor, Double minArea, Double maxArea, Integer jeonseMin,   
			Integer jeonseMax, Integer depositMin, Integer depositMax, Integer monthlyMin, Integer monthlyMax, 
			Integer saleMin, Integer saleMax  ) {

		return kakaoMapDataMapper.selectLatLngMarkList(swLat, swLng, neLat, neLng, category, keyword, typeCode1List,
				typeCode2List, saleTypeList, facilityOptionList, mbrCd, parkingYn, minFloor, maxFloor, minArea,
				maxArea, jeonseMin, jeonseMax, depositMin, depositMax, monthlyMin, monthlyMax, saleMin, saleMax);
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

	@Override
	public int countListingView(LstgViewLogVO vo) {
		return kakaoMapDataMapper.insertLstgViewCont(vo);
	}


}
