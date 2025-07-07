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
public class RentalOwnerProductUpdateController {

    @Autowired
    private RentalOwnerProductService productService;

    @GetMapping("/update")
    public String showUpdateForm(@RequestParam("lstgId") String lstgId, Model model) {
        ProductVO product = productService.getProductById(lstgId);
        model.addAttribute("productVO", product);
        return "building/managed/productEdit";
    }

    @PostMapping("/update")
    public String processUpdate(@ModelAttribute("productVO") ProductVO productVO,
                                @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO loginUser = principal.getRealUser();
        if (loginUser != null) {
            productVO.setMbrCd(loginUser.getMbrCd());
        }

        productService.updateProduct(productVO);
        return "redirect:/building/product/list";
    }
}
