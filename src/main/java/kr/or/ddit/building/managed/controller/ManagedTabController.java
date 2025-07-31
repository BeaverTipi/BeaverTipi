package kr.or.ddit.building.managed.controller;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.building.unitManaged.service.UnitManagedService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.exception.ListingException;
import kr.or.ddit.util.validate.exception.NoLoginException;
import kr.or.ddit.vo.*;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/building/product")
public class ManagedTabController {
    @Autowired
    private BuildingManagedService buildingService;

    @Autowired
    private RentalOwnerProductService listingService;

    @Autowired
    private CommonCodeService commonCodeService;
    
    @Autowired
    private UnitManagedService unitService;

    @Autowired
    private UnitResidentService unitResidentService;

    @GetMapping("/tabList")
    public String tabList(
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
        Model model,
        @ModelAttribute("searchForm") ListingSearchFormVO searchForm,
        @RequestParam Map<String, String> paramMap // 추가 검색 파라미터가 있다면
    ) {
        MemberVO memberVO = principal.getRealUser();
        if (memberVO == null || memberVO.getMbrCd() == null) {
            return "redirect:/member/register";
        }
        String rentalPtyId = memberVO.getTenancy().getRentalPtyId();

        // 로그인 정보 바인딩
        searchForm.setMbrCd(memberVO.getMbrCd());

        // 파라미터 바인딩(옵션)
        if(paramMap != null) {
            searchForm.setSearchBuildingName(paramMap.get("searchBuildingName"));
            searchForm.setSearchRoomNum(paramMap.get("searchRoomNum"));
            searchForm.setSearchStatus(paramMap.get("searchStatus"));
            searchForm.setSearchType(paramMap.get("searchType"));
        }

        // 매물 리스트
        Map<String, Object> resultMap = listingService.readPagingAndListing(searchForm, 1);
        model.addAttribute("listingProductList", resultMap.get("dataList"));
        model.addAttribute("pagingVO", resultMap.get("pagingVO"));
        model.addAttribute("pagingHTML", resultMap.get("pagingHTML"));
        model.addAttribute("typeSaleCodeList", resultMap.get("typeSaleCodeList"));
        model.addAttribute("statusCodeList", resultMap.get("statusCodeList"));

        // 검색 폼 값 유지용 (필수)
        model.addAttribute("searchForm", searchForm);

        // 건물 리스트
        BuildingSearchFormVO buildingSearchForm = new BuildingSearchFormVO();
        buildingSearchForm.setRentalPtyId(rentalPtyId);
        List<BuildingVO> buildingList = buildingService.searchBuildingList(rentalPtyId, buildingSearchForm);
        for (BuildingVO building : buildingList) {
            List<TenancyAccountVO> accList = buildingService.selectAccountsByRentalPtyId(building.getRentalPtyId());
            building.setAccList(accList);
        }
        model.addAttribute("buildingList", buildingList);

        return "building/product/rentalOwnerProductList";
    }

 

}
