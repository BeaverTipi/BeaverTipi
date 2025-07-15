
package kr.or.ddit.building.managed.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.BuildingVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.TenancyAccountVO;
import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/building/managed")
public class ManagedListingController {

    @Autowired
    private BuildingManagedService managedService;

    @GetMapping("/listing/select")
    public String selectListingForBuilding(Model model, 
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

    	MemberVO memberVO = principal.getRealUser();
    	String rentalPtyId = null;
    	if (memberVO != null && memberVO.getTenancy() != null) {
    	    rentalPtyId = memberVO.getTenancy().getRentalPtyId();
    	}

        List<ListingVO> listingList = managedService.selectListingsByRentalPtyId(rentalPtyId);
        model.addAttribute("listingList", listingList);

        return "jsonView"; // 혹은 JSP로 처리 시 "/building/managed/listingSelect"
    }
    
    @GetMapping("/listing/detail")
    @ResponseBody
    public ListingVO getListingDetail(@RequestParam String lstgId) {
        return managedService.selectListingById(lstgId);
    }
}

