package kr.or.ddit.building.product.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.exception.FileIOException;
import kr.or.ddit.util.validate.exception.ListingException;
import kr.or.ddit.util.validate.exception.ListingOptionException;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RentalOwnerProductAddController {
	private static final String MODELNAME = "listingVO";
    @Autowired
    private RentalOwnerProductService service;
    @Autowired
    private CommonCodeService commonService;

    /**
     * [GET] 매물 등록 폼 진입
     * - 로그인된 사용자 정보에서 mbrCd 추출
     * - 시설 옵션 목록 조회 후 그룹핑하여 모델 전달
     * - 로그인 안 되어있으면 회원가입 페이지로 리다이렉트
     */
    @GetMapping("/building/product/add")
    public String addForm(Model model,
                          @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        log.info("[GET] /building/product/add 진입");

        // 1. 로그인 정보 확인
        MemberVO member = principal.getRealUser();
        if (member == null || member.getMbrCd() == null) {
            log.warn("로그인 정보 없음 → /member/register 리다이렉트");
            return "redirect:/member/register";
        }

        String mbrCd = member.getMbrCd();
        log.info("로그인된 사용자 mbrCd: {}", mbrCd);
        List<CommonCodeVO> lstg1List = commonService.readCommonCodeList("LSTG1");        
        List<CommonCodeVO>  lstgTypeSaleList= commonService.readCommonCodeList("TRDST");        
        // 2. 시설 옵션 전체 조회
        List<FacilityOptionVO> optionList = service.selectAllFacilityOptions();

        // 3. 그룹핑 (FAC_TYPE_GROUP_CD 기준)
        Map<String, List<FacilityOptionVO>> facilityMap = optionList.stream()
            .collect(Collectors.groupingBy(FacilityOptionVO::getFacTypeCcCd));

        
        // 4. 모델에 추가
        model.addAttribute("facilityMap", facilityMap);
        model.addAttribute("lstg1List", lstg1List);
        model.addAttribute("lstgTypeSaleList", lstgTypeSaleList);

        return "building/product/rentalOwnerProductAdd";
    }

   
    @PostMapping("/building/product/add")
    public String processAdd(@Validated(InsertGroup.class) @ModelAttribute("listingVO") ListingVO listingVO,
    						 BindingResult errors,
                             @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
                             @RequestParam(name = "brokerIds") List<String> brokerIds,
                             RedirectAttributes redirectAttributes,
                             @RequestParam(value = "imageUpload", required = false) List<MultipartFile> imageFiles
                             
    ) {
    	String lvn = "redirect:/building/product/tabList";
    	if(errors.hasErrors()) {
    		String errorName = BindingResult.MODEL_KEY_PREFIX+MODELNAME;
			redirectAttributes.addFlashAttribute(MODELNAME, listingVO);
			redirectAttributes.addFlashAttribute(errorName, errors);
			log.error("errors : {}", errors.toString());
			lvn = "redirect:/building/product/add";

    	}else {
	        MemberVO member = principal.getRealUser();
	
	        if (member.getTenancy() != null) {
	            listingVO.setRentalPtyId(member.getTenancy().getRentalPtyId());
	        }
	        
	        log.info("등록 요청 데이터: {}", listingVO);
	
	        try {
	         service.insertProduct(listingVO, brokerIds,imageFiles);
	         redirectAttributes.addFlashAttribute("message", "매물 등록이 완료되었습니다.");
		    } catch (ListingOptionException e) {
		        log.error("옵션 처리 오류", e);
		        lvn = "redirect:/building/product/add";
		        redirectAttributes.addFlashAttribute("message", e.getMessage());
		    } catch (ListingException e) {
		        log.error("매물 등록 실패", e);
		        lvn = "redirect:/building/product/add";
		        redirectAttributes.addFlashAttribute("message", e.getMessage());
		    } catch (FileIOException e) {
		        log.error("파일 업로드 오류", e);
		        lvn = "redirect:/building/product/add";
		        redirectAttributes.addFlashAttribute("message", e.getMessage());
		    } catch (Exception e) {
		        log.error("알 수 없는 오류", e);
		        lvn = "redirect:/building/product/add";
		        redirectAttributes.addFlashAttribute("message", e.getMessage());
		    }
    	}
        return lvn;
    }
    
    @PostMapping("/ajax/building/broker/list")
    @ResponseBody
    public ResponseEntity<List<BrokerVO>> getNearbyBrokers(@RequestBody Map<String, Object> params) {
        double lat = Double.parseDouble(params.get("lat").toString());
        double lng = Double.parseDouble(params.get("lng").toString());
        double radiusKm = params.get("radiusKm") != null
            ? Double.parseDouble(params.get("radiusKm").toString())
            : 1.0;

        List<BrokerVO> list = service.findNearbyBrokers(lat, lng, radiusKm);
        return ResponseEntity.ok(list);
    }
    
    @GetMapping("/ajax/building/myList")
    public ResponseEntity<List<BuildingVO>> myBuildingList(
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO member = principal.getRealUser();
        String rentalPtyId = member.getTenancy().getRentalPtyId();

        List<BuildingVO> buildingList = service.readBuildingList(rentalPtyId);

        return ResponseEntity.ok(buildingList);
    }

    @GetMapping("/ajax/building/{bldgId}/units")
    public ResponseEntity<List<BuildingVO>> myBuildingUnitList(
            @PathVariable String bldgId,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
    ) {
        MemberVO member = principal.getRealUser();
        String rentalPtyId = member.getTenancy().getRentalPtyId();

        List<BuildingVO> buildingUnitList = service.readBuildingUnitList(bldgId, rentalPtyId);

        return ResponseEntity.ok(buildingUnitList);
    }


    // 매물등록 시, 소분류 목록 가져오기
    @GetMapping("/ajax/building/product/selectLstg2List")
    @ResponseBody
    public List<CommonCodeVO> commonCodeLstg2List(@RequestParam("lstg1") String lstg1){
    	CommonCodeVO code = new CommonCodeVO();
    	code.setParentCodeGroup("LSTG1");
    	code.setCodeGroup("LSTG2");
    	code.setParentCodeValue(lstg1);
    	return commonService.readCommonCodeList(code);
    }
}















