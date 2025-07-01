package kr.or.ddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class ProductAddController {
	@GetMapping("/building/")
	public String brokerProduct() {
		return "product/productAdd/brokerProductAdd";
	}
}
