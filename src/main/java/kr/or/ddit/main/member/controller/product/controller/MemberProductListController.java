package kr.or.ddit.main.member.controller.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberProductListController {
	@GetMapping("/member/product/list")
	public String memberProductAdd() {
		return "member/product/productList/memberProductList";
		}
	}


