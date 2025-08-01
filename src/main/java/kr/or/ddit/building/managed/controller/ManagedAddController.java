package kr.or.ddit.building.managed.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile; // 추가
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.exception.BuildingException;
import kr.or.ddit.util.validate.exception.BuildingUnitException;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.TenancyAccountVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/building/managed")
public class ManagedAddController {

	@Autowired
	private BuildingManagedService buildingService;

	@Autowired
	private CommonCodeService commonCodeService;

	// 건물 등록 폼 (GET)
	@GetMapping("/add")
	public String addBuildingForm(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		MemberVO memberVO = principal.getRealUser();
		String rentalPtyId = memberVO.getTenancy().getRentalPtyId();

		BuildingVO buildingVO = new BuildingVO();
		log.info("삘딩쁘이오!! : {}", buildingVO);
		buildingVO.setRentalPtyId(rentalPtyId);
		buildingVO.setDelYn("N");

		// 계좌 리스트
		List<TenancyAccountVO> accList = buildingService.selectAccountsByRentalPtyId(rentalPtyId);
		buildingVO.setAccList(accList);
		if (!accList.isEmpty()) {
			buildingVO.setAccNum(accList.get(0).getAccNum());
		}

		// 내 매물 리스트
		List<ListingVO> listingList = buildingService.selectListingsByRentalPtyId(rentalPtyId);

		// 건물 유형 코드
		List<CommonCodeVO> bldgTypeList = commonCodeService.readCommonCodeList("BLDG");

		model.addAttribute("buildingVO", buildingVO);
		model.addAttribute("listingList", listingList);
		model.addAttribute("bldgTypeList", bldgTypeList);

		return "building/managed/managedAdd";
	}

	// 건물 등록 처리 (POST)
	@PostMapping("/add")
	public String addBuilding(@ModelAttribute("buildingVO") @Validated(InsertGroup.class) BuildingVO buildingVO,
			BindingResult errors, RedirectAttributes redirectAttributes,
			@RequestParam(value = "bldgImgFile", required = false) MultipartFile bldgImgFile // 추가!
	) {
		String lvn = "redirect:/building/managed/add";
		if (!errors.hasErrors()) {
			// 1. 건물 등록 (bldgId 생성)
			try {
				buildingService.insertBuilding(buildingVO, bldgImgFile);
				// 등록 후 상세/유닛 입력화면으로 이동
				lvn = "redirect:/building/product/tabList";
			} catch (BuildingException e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			} catch (BuildingUnitException e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			} catch (Exception e) {
				redirectAttributes.addFlashAttribute("message", e.getMessage());
			}
		}
		return lvn;
	}

	@GetMapping("/listing/detail")
	@ResponseBody
	public ListingVO getListingDetail(@RequestParam String lstgId) {
		return buildingService.selectListingById(lstgId);
	}
}
