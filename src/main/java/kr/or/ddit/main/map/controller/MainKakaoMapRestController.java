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

import jakarta.servlet.http.HttpServletRequest;
import kr.or.ddit.main.map.service.MainKakaoMapService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.LstgViewLogVO;
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
		@RequestParam(required = false) Double maxArea,
		@RequestParam(required = false) Integer jeonseMin,
		@RequestParam(required = false) Integer jeonseMax,
		@RequestParam(required = false) Integer depositMin,
		@RequestParam(required = false) Integer depositMax,
		@RequestParam(required = false) Integer monthlyMin,
		@RequestParam(required = false) Integer monthlyMax,
		@RequestParam(required = false) Integer saleMin,
		@RequestParam(required = false) Integer saleMax
	) {
		List<ListingVO> result = service.selectLatLngMarkList(
			swLat, swLng, neLat, neLng,
			category, keyword,
			typeCode1List, typeCode2List, saleTypeList,
			facilityOptionList, mbrCd,
			parkingYn, minFloor, maxFloor, minArea, maxArea,
			jeonseMin, jeonseMax, depositMin, depositMax, monthlyMin, monthlyMax, saleMin, saleMax
		);

		return ResponseEntity.ok(result == null ? Collections.emptyList() : result);
	}

	
	@GetMapping("/detail")
	public ResponseEntity<ListingVO> getDetailInfo(
	    @RequestParam String lstgId,
	    @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
	) {
	    String mbrCd = (principal != null && principal.getRealUser() != null)
	        ? principal.getRealUser().getMbrCd()
	        : null;

	    ListingVO detail = service.selectListingDetailList(lstgId, mbrCd);

	    if (detail == null) {
	        return ResponseEntity.notFound().build();
	    }

	    return ResponseEntity.ok(detail);
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
	
	 @PostMapping("/viewLog")
	 public ResponseEntity<Void> insertListingViewLog(
	    @RequestParam("lstgId") String lstgId,
	    @RequestParam(value = "mbrCd", required = false) String mbrCd,
	    HttpServletRequest request
	 ) {
		 if (mbrCd == null || mbrCd.trim().isEmpty()) {
	         mbrCd = request.getRemoteAddr(); // 비회원이면 IP로 대체
	     }

	     LstgViewLogVO vo = new LstgViewLogVO();
	     vo.setLstgId(lstgId);
	     vo.setMbrCd(mbrCd);

	     service.countListingView(vo);
	     return ResponseEntity.ok().build();
	    }
}
