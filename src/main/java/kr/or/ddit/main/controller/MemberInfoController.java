package kr.or.ddit.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberInfoController {

	@GetMapping("/account/update")
	public String info() {
		return "member/info";
	}
}
