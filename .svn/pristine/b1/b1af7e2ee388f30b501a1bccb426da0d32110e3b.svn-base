package kr.or.ddit.building.product.controller;

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
public class RentalOwnerProductAddController {

    @Autowired
    private RentalOwnerProductService productService;

    @GetMapping("/add")
    public String showAddForm(Model model,
                              @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO loginUser = principal.getRealUser();
        ProductVO productVO = new ProductVO();
        if (loginUser != null) {
            productVO.setMbrCd(loginUser.getMbrCd());
        }

        model.addAttribute("productVO", productVO);
        return "building/product/rentalOwnerProductAdd";
    }

    @PostMapping("/add")
    public String processAdd(@ModelAttribute("productVO") ProductVO productVO,
                             @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO loginUser = principal.getRealUser();
        if (loginUser != null) {
            productVO.setMbrCd(loginUser.getMbrCd());
        }

        productService.insertProduct(productVO);
        return "redirect:/building/product/list";
    }
}





