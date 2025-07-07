package kr.or.ddit.main.member.controller.product.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;


@Controller
public class MemberProductAddController {
	@GetMapping("/member/product/add")
	public String memberProductAdd() {
		return "main/product/productAdd/memberProductAdd";
		}
	}
