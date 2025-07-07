package kr.or.ddit.broker.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BrokerProductListController {
	@GetMapping("/broker/product/list")
	public String brokerProductList() {
		return "broker/product/productList/brokerProductList";
		}
	}
