package kr.or.ddit.main.map.controller;

import java.util.Collections;
import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.main.map.service.MainKakaoMapService;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
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
			@RequestParam(required = false) List<Integer> typeCode1List,
			@RequestParam(required = false) List<Integer> typeCode2List,
			@RequestParam(required = false) List<Integer> saleTypeList,
			@RequestParam(required = false) List<String> facilityOptionList,
			@RequestParam(required = false) String mbrCd
	) {
		List<ListingVO> result = service.selectLatLngMarkList(
				swLat, swLng, neLat, neLng,
				category, keyword,
				typeCode1List, typeCode2List, saleTypeList,
				facilityOptionList, mbrCd);
		
		return ResponseEntity.ok(result == null ? Collections.emptyList() : result);
	}
	
	@GetMapping("/category")
	public ResponseEntity<List<ListingVO>> getListingByCategory() {
		List<ListingVO> categoryList = service.selectCategory();
		
		return categoryList == null || categoryList.isEmpty()
			? ResponseEntity.noContent().build()
			: ResponseEntity.ok(categoryList);
	}
	
	@GetMapping("/detail")
	public ResponseEntity<?> getListingDetalList(@RequestParam("lstgId") String lstgId){
		List<ListingVO> detailList = service.selectListingDetailList(lstgId);

		if (detailList == null || detailList.isEmpty()) {
			return ResponseEntity.noContent().build(); // 204 → JS에서 catch 블럭으로 감
		}

		return ResponseEntity.ok(detailList.get(0));
	}
	
}
