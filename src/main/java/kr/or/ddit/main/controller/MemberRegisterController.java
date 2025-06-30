package kr.or.ddit.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberRegisterController {

	@GetMapping("/member/register")
	public String registerPage() {
		return "main/member/register";
	}
	
}
