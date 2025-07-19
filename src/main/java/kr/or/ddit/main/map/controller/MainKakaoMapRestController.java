package kr.or.ddit.main.map.controller;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.main.map.service.MainKakaoMapService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.MemberVO;
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
	public ResponseEntity<Map<String, Object>> getDetailInfo(
	    @RequestParam String lstgId,
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
	) {
	    String mbrCd = (principal != null && principal.getRealUser() != null)
	        ? principal.getRealUser().getMbrCd()
	        : null;

	    List<Map<String, Object>> detailList = service.selectListingDetailList(lstgId, mbrCd);
	    if (detailList.isEmpty()) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok(detailList.get(0));
	}



	
	@PostMapping("/wishlist/toggle")
	public ResponseEntity<Integer> toggleWishlist(
	        @RequestParam String lstgId,
	        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

	    if (principal == null || principal.getRealUser() == null) {
	        return ResponseEntity.status(401).build(); // 로그인 필요
	    }

	    String mbrCd = principal.getRealUser().getMbrCd();

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
