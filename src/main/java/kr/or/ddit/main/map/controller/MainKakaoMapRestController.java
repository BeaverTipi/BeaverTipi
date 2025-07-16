package kr.or.ddit.main.map.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.main.map.service.MainKakaoMapService;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/map/api")
public class MainKakaoMapRestController {
	
	private final MainKakaoMapService service;
	
	@GetMapping("/mark")
	public ResponseEntity<List<ListingVO>> getAllMarkerData(
		@RequestParam double swLat,
		@RequestParam double swLng,
		@RequestParam double neLat,
		@RequestParam double neLng,
		@RequestParam(required = false) Integer category,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) List<String> typeCode1List,
		@RequestParam(required = false) List<String> typeCode2List,
		@RequestParam(required = false) List<String> saleTypeList,
		@RequestParam(required = false) List<String> facilityOptionList,
		@RequestParam(required = false) String mbrCd,
		@RequestParam(required = false) String parkingYn,
		@RequestParam(required = false) Integer minFloor,
		@RequestParam(required = false) Integer maxFloor,
		@RequestParam(required = false) Double minArea,
		@RequestParam(required = false) Double maxArea
	) {
		List<ListingVO> result = service.selectLatLngMarkList(
			swLat, swLng, neLat, neLng,
			category, keyword,
			typeCode1List, typeCode2List, saleTypeList,
			facilityOptionList, mbrCd,
			parkingYn, minFloor, maxFloor, minArea, maxArea
		);

		return ResponseEntity.ok(result == null ? Collections.emptyList() : result);
	}

	
	@GetMapping("/detail")
	public ResponseEntity<?> getListingDetalList(
			@RequestParam("lstgId") String lstgId,
			@RequestParam(value = "mbrCd", required = false) String mbrCd
	) {
		List<Map<String, Object>> detailList = service.selectListingDetailList(lstgId, mbrCd);

		if (detailList == null || detailList.isEmpty()) {
			return ResponseEntity.noContent().build();
		}

		// Map 변수 선언
		Map<String, Object> result = detailList.get(0);

		// 옵션 추가
		result.put("facilityOptions", service.selectFacilityOptionsByListingId(lstgId));

		return ResponseEntity.ok(result);
	}
	
	@PostMapping("/wishlist/toggle")
	public ResponseEntity<Integer> toggleWishlist(
	        @RequestParam String lstgId,
	        @RequestParam String mbrCd) {

	    ListingWishlistVO vo = new ListingWishlistVO();
	    vo.setLstgId(lstgId);
	    vo.setMbrCd(mbrCd);

	    int affected = 0;
	    if (service.deleteWishList(vo) == 0) {
	        affected = service.insertWishList(vo);
	    }

	    return ResponseEntity.ok(service.countWishListByLstgId(lstgId));
	}


	
}
