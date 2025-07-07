package kr.or.ddit.building.product.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;

@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductDeleteController {

    @Autowired
    private RentalOwnerProductService productService;

    @PostMapping("/delete")
    public String delete(@RequestParam("lstgId") String lstgId,
                         @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO loginUser = principal.getRealUser();
        String mbrCd = loginUser != null ? loginUser.getMbrCd() : null;

        productService.deleteProduct(lstgId);
        return "redirect:/building/product/list";
    }
}
