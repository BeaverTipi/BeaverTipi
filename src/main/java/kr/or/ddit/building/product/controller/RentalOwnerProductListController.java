package kr.or.ddit.building.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.vo.ListingVO;

@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductListController {

    @Autowired
    private RentalOwnerProductService service;

    // 매물 목록 화면
    @GetMapping("/list")
    public String productList(Model model, HttpSession session) {
        String mbrCd = (String) session.getAttribute("mbrCd");

        // mbrCd가 세션에 없으면 로그인 페이지 등으로 redirect 처리 가능
        if (mbrCd == null) {
            return "redirect:/member/login";
        }

        List<ListingVO> productList = service.selectProductList(mbrCd);
        model.addAttribute("productList", productList);

        return "building/product/rentalOwnerProductList"; // jsp 경로
    }
}
