package kr.or.ddit.broker.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class BrokerProductAddController {
	@GetMapping("/broker/product/add")
	public String borkerProductAdd() {
		return "broker/product/productAdd/brokerProductAdd";
		}
	
}
