package kr.or.ddit.building.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RentalOwnerProductListController {
	@GetMapping("/building/product/list")
	public String rentalOwnerProductList() {
		return "building/product/productList/rentalOwnerProductList";
	}


}
