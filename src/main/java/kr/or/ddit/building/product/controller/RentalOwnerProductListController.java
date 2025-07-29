package kr.or.ddit.building.product.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.util.validate.exception.NoLoginException;
import kr.or.ddit.vo.ListingSearchFormVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
public class RentalOwnerProductListController {

    @Autowired
    private RentalOwnerProductService service;

    /**
     * 내 매물 목록 + 검색 + 페이징
     */
    @GetMapping("/building/product/list")
    public String listPage(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
                           @ModelAttribute("searchForm") ListingSearchFormVO searchForm,
                           @RequestParam(name = "page", defaultValue = "1") int currentPage,
                           Model model) {

        MemberVO loginUser = principal.getRealUser();
        if (loginUser == null || loginUser.getMbrCd() == null) {
            return "redirect:/member/register";
        }

        searchForm.setMbrCd(loginUser.getMbrCd());

        // 🔥 핵심: 서비스에서 전체 페이징 + 리스트 다 만든 PaginationInfo 자체를 가져온다.
        Map<String, Object> resultMap = service.readPagingAndListing(searchForm, currentPage);

        model.addAttribute("listingProductList", resultMap.get("dataList"));
        model.addAttribute("pagingVO", resultMap.get("pagingVO"));
        model.addAttribute("pagingHTML", resultMap.get("pagingHTML"));
        model.addAttribute("typeSaleCodeList", resultMap.get("typeSaleCodeList"));
        model.addAttribute("statusCodeList", resultMap.get("statusCodeList"));

        return "building/product/rentalOwnerProductList";
    }

    @PostMapping("/ajax/building/listing/rooms")
    @ResponseBody
    public List<ListingVO> listRooms(
    		@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
    		@RequestParam("address") String lstgAdd
    ){
    	 MemberVO loginUser = principal.getRealUser();
    	 log.info("***** loginUser : {}", loginUser);
         if (loginUser == null || loginUser.getMbrCd() == null) {
             throw new NoLoginException();
         }
         String rentalPtyId = loginUser.getTenancy().getRentalPtyId();
         
         ListingVO listing = new ListingVO();
 		listing.setRentalPtyId(rentalPtyId);
 		listing.setLstgAdd(lstgAdd);
 		
 		List<ListingVO> rooms = service.readRoomsList(listing);
 		
 		log.info("***** rooms : {}", rooms);
         return rooms;
    }


    /**
     * 상세 모달 Ajax 조회
     */
//    @GetMapping("/ajax/detailModal")
//    @ResponseBody
//    public ResponseEntity<ListingVO> getListingDetail(@RequestParam("lstgId") String lstgId) {
//        try {
//            ListingVO detail = service.selectListingDetail(lstgId);
//            if (detail != null) {
//                return ResponseEntity.ok(detail);
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
//            }
//        } catch (Exception e) {
//            log.error("매물 상세 조회 오류", e);
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
//        }
//    }

    // 이후 삭제도 이렇게 구성 가능 (POST or DELETE)
    /*
    @PostMapping("/ajax/delete")
    @ResponseBody
    public ResponseEntity<?> deleteListing(@RequestBody Map<String, String> body) {
        String lstgId = body.get("lstgId");
        int result = service.deleteListing(lstgId);
        if (result > 0) {
            return ResponseEntity.ok(Map.of("success", true));
        } else {
            return ResponseEntity.status(500).body(Map.of("success", false));
        }
    }
    */
}
