package kr.or.ddit.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.main.map.service.MainKakaoMapService;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ListingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
public class MainPageController {
	
	private final CommonCodeService commonCode;
	private final MainKakaoMapService kakaoService;
	
	@GetMapping("/")
	public String mainPage(Model model) {
		List<CommonCodeVO> categoryList = commonCode.readCommonCodeList("LSTG1");
		model.addAttribute("categoryList", categoryList);
		return "main/mainPage";
	}
	
	@GetMapping("/ajax/broker/mainPageRooms/load")
	@ResponseBody
	public List<ListingVO> mainPageRooms() {
		log.info("/ajax/broker/mainPageRooms/load 접근 성공: ");
		List<ListingVO> list = null;
		try {
			list = kakaoService.readListingDetailListWithoutNothing();			
		}
		catch(RuntimeException e) {
			log.error("메인페이지 매물 가져오기 실패");
		}
		return list;
	}
}
