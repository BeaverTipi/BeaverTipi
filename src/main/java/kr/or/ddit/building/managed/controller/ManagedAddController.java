
package kr.or.ddit.building.managed.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
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
	private BuildingManagedService managedService; 
	
	@Autowired
	private CommonCodeService commonCodeService;
    // 1. 등록 폼 진입
	  @GetMapping("/add")
	    public String addForm(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
	        MemberVO memberVO = principal.getRealUser();

	        BuildingVO buildingVO = new BuildingVO();
	        
	        String rentalPtyId = memberVO.getTenancy().getRentalPtyId();
	        List<TenancyAccountVO> accList = managedService.selectAccountsByRentalPtyId(rentalPtyId);
	        buildingVO.setAccList(accList);
	        if (!accList.isEmpty()) {
	            buildingVO.setAccNum(accList.get(0).getAccNum());
	        }

	        // Tenancy 정보에서 rentalPtyId 꺼내서 셋팅
	        if (memberVO != null && memberVO.getTenancy() != null) {
	            buildingVO.setRentalPtyId(memberVO.getTenancy().getRentalPtyId());
	        }
	        
	        List<ListingVO> listingList = managedService.selectListingsByRentalPtyId(rentalPtyId);
			
	        List<CommonCodeVO> bldgTypeList = commonCodeService.readCommonCodeList("BLDG");
	        model.addAttribute("bldgTypeList", bldgTypeList);
	        
	        model.addAttribute("listingList", listingList);
	        model.addAttribute("buildingVO", buildingVO);
	        
	        
	        
	        return "building/managed/managedAdd";
	  }

    // 2. 등록 처리
    @PostMapping("/add")
    public String addUnit(@ModelAttribute("buildingVO") BuildingVO buildingVO) {
    	
        //근데 이걸 넣으면서 든 생각이.. 그냥 층수를 Nullable 하면 되지 않을까? 히히
    	if (buildingVO.getBldgFlrCnt() == null) {
            buildingVO.setBldgFlrCnt(1);
        }
        if (buildingVO.getBldgUnitCnt() == null) {
            buildingVO.setBldgUnitCnt(1);
        }
    	
        managedService.insertBuilding(buildingVO);
        System.out.println("여기를봐라 멍청이들아" + buildingVO);
        return "redirect:/building/unitManaged/add?bldgId=" + buildingVO.getBldgId()
        + "&rentalPtyId=" + buildingVO.getRentalPtyId();
    }
	
}



//
//
