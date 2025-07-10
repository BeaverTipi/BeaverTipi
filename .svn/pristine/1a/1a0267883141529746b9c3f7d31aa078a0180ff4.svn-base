package kr.or.ddit.building.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductListController {

    @Autowired
    private RentalOwnerProductService service;

    /**
     * 매물 목록 조회
     */
    @GetMapping("/list")
    public String listPage(Model model,
                           @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

        // 로그인 사용자 꺼냄
        MemberVO loginUser = principal.getRealUser();
        log.info("[GET] /building/product/rentalOwnerProductList - 로그인 사용자: {}", loginUser);

        if (loginUser == null || loginUser.getMbrCd() == null) {
            log.warn("로그인 정보 없음 → /member/register 리다이렉트");
            return "redirect:/member/register";
        }

        String mbrCd = loginUser.getMbrCd();
        log.info("로그인된 mbrCd: {}", mbrCd);

        // 매물 목록 조회
        List<ListingVO> listingList = service.selectProductList(mbrCd);
        log.info("조회된 매물 수: {}", (listingList != null ? listingList.size() : 0));

        // 모델에 담기
        model.addAttribute("productList", listingList);

        return "building/product/rentalOwnerProductList";
    }
}
