//package kr.or.ddit.building.managed.controller;
//
//import java.util.List;
//import java.util.Map;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.*;
//
//import kr.or.ddit.building.managed.service.BuildingManagedService;
//import kr.or.ddit.building.product.service.RentalOwnerProductService;
//import kr.or.ddit.util.security.auth.RealUserWrapper;
//import kr.or.ddit.util.validate.exception.NoLoginException;
//import kr.or.ddit.vo.BuildingVO;
//import kr.or.ddit.vo.ListingSearchFormVO;
//import kr.or.ddit.vo.ListingVO;
//import kr.or.ddit.vo.MemberVO;
//import kr.or.ddit.vo.TenancyAccountVO;
//import lombok.extern.slf4j.Slf4j;
//
//@Slf4j
//@Controller
//@RequestMapping("/building/managed")
//public class ManagedTabController {
//
//    @Autowired
//    private BuildingManagedService managedService;
//    @Autowired
//    private RentalOwnerProductService productService;
//
//    // 한 번에 두 탭 데이터 모두 model에
//    @GetMapping("/list")
//    public String managedTabPage(
//            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
//            @ModelAttribute("searchForm") ListingSearchFormVO searchForm,
//            @RequestParam(name = "page", defaultValue = "1") int currentPage,
//            Model model) {
//
//        MemberVO memberVO = principal.getRealUser();
//        if (memberVO == null || memberVO.getMbrCd() == null) {
//            return "redirect:/member/register";
//        }
//        String rentalPtyId = memberVO.getTenancy().getRentalPtyId();
//
//        // 1. 건물 리스트 (accList 바인딩 포함)
//        List<BuildingVO> buildingList = managedService.selectBuildingListByRentalPtyId(rentalPtyId);
//        for (BuildingVO building : buildingList) {
//            List<TenancyAccountVO> accList = managedService.selectAccountsByRentalPtyId(building.getRentalPtyId());
//            building.setAccList(accList);
//        }
//        model.addAttribute("buildingList", buildingList);
//
//        // 2. 매물(내 매물 관리) 리스트+검색+페이징 등 (model 값 전부)
//        searchForm.setMbrCd(memberVO.getMbrCd());
//        Map<String, Object> resultMap = productService.readPagingAndListing(searchForm, currentPage);
//
//        model.addAttribute("listingProductList", resultMap.get("dataList"));
//        model.addAttribute("pagingVO", resultMap.get("pagingVO"));
//        model.addAttribute("pagingHTML", resultMap.get("pagingHTML"));
//        model.addAttribute("typeSaleCodeList", resultMap.get("typeSaleCodeList"));
//        model.addAttribute("statusCodeList", resultMap.get("statusCodeList"));
//
//        return "building/managed/managedList";
//    }
//
//    // 기존 방 AJAX 조회 (url은 바꿔서 사용)
//    @PostMapping("/ajax/listing/rooms")
//    @ResponseBody
//    public List<ListingVO> listRooms(
//            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
//            @RequestParam("address") String lstgAdd
//    ) {
//        MemberVO loginUser = principal.getRealUser();
//        log.info("***** loginUser : {}", loginUser);
//        if (loginUser == null || loginUser.getMbrCd() == null) {
//            throw new NoLoginException();
//        }
//        String rentalPtyId = loginUser.getTenancy().getRentalPtyId();
//
//        ListingVO listing = new ListingVO();
//        listing.setRentalPtyId(rentalPtyId);
//        listing.setLstgAdd(lstgAdd);
//
//        List<ListingVO> rooms = productService.readRoomsList(listing);
//
//        log.info("***** rooms : {}", rooms);
//        return rooms;
//    }
//
//    // (※ 필요하면 아래 상세/삭제 등도 그대로 추가 가능)
//}
