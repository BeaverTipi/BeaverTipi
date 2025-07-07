package kr.or.ddit.building.product.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import jakarta.servlet.http.HttpSession;
import kr.or.ddit.building.product.service.RentalOwnerProductService;
import kr.or.ddit.vo.ProductVO;

@Controller
@RequestMapping("/building/product")
public class RentalOwnerProductAddController {

    @Autowired
    private RentalOwnerProductService productService;

    // 등록 폼 페이지 진입
    @GetMapping("/add")
    public String rentalOwnerProductAdd() {
        return "building/product/productAdd/rentalOwnerProductAdd";
    }

    // 폼 제출 처리
    @PostMapping("/add")
    public String rentalOwnerProductInsert(ProductVO product, HttpSession session) {
        String mbrCd = (String) session.getAttribute("mbrCd");
        product.setMbrCd(mbrCd);

        int result = productService.insertProduct(product);

        if (result > 0) {
            return "redirect:/building/product/list";
        } else {
            return "redirect:/building/product/add?error";
        }
    }
}
