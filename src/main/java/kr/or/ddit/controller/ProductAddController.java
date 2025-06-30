package kr.or.ddit.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.service.annotation.GetExchange;

@Controller
public class ProductAddController {
	@GetMapping
	public String brokerProduct() {
		return "product/productAdd/brokerProductAdd";
	}
}
