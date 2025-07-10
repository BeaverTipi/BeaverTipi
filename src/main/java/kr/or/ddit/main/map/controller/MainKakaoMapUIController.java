package kr.or.ddit.main.map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/main/map")
public class MainKakaoMapUIController{
	
	private final KakaoApiKeyProvider apiKeyProvider;
	
	@GetMapping
	public String kakaoMapUI(Model model) {
		model.addAttribute("jsApiKey", apiKeyProvider.getJsApiKey());
		return "main/mainMap/mainKakaoMapUI";
	}
	
//	@ResponseBody
//	@PostMapping(value="/pointList")
//	public ResponseEntity<List<ListingVO>> categoryList(@RequestParam Map<String, String> param){
//		
//		String category = param.get("category");
//		log.info("category : " + category);
//  		List<ListingVO> listingList = service.selectCategoryByListingList();
//		return new ResponseEntity<List<ListingVO>>(listingList, HttpStatus.OK);
//	}

//	@GetMapping("/data")
//	@ResponseBody
//	public List<Map<String, String>> getBuildingData() {
//		List<BuildingVO> listings = service.selectNotLatLngList();
//			return list.stream()
//					   .filter(b -> b.getBldgAddr() != null)
//					   .map(b -> Map.of(
//							"id", String.valueOf(b.getBldgId()),
//							"address", b.getBldgAddr() + " " + (b.getBldgDtlAddr() != null ? b.getBldgDtlAddr() : "")
//					   ))
//					   .collect(Collectors.toList());
//	}

}
