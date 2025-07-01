package kr.or.ddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductListController {
	@GetMapping("/building/brokerProduct/list")
	public String brokerProductList() {
		return "/product/productList/brokerProductList";
	}
	@GetMapping("/building/rentalOwnerProduct/list")
	public String rentalOwnerProductList() {
		return "/product/productList/rentalOwnerProductList.jsp";
	}
	@GetMapping("/building/userProduct/list")
	public String userProductList() {
		return "/product/productList/userProductList";
	}

}
