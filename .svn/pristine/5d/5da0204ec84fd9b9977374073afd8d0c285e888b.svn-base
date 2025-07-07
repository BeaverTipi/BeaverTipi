package kr.or.ddit.building.product.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.ProductVO;

@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductListController {

    @Autowired
    private RentalOwnerProductService productService;

    @GetMapping("/list")
    public String list(Model model,
                       @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO loginUser = principal.getRealUser();
        String mbrCd = loginUser != null ? loginUser.getMbrCd() : null;

        List<ProductVO> productList = productService.getProductListByMember(mbrCd);
        model.addAttribute("productList", productList);
        return "building/managed/productList";
    }
}


