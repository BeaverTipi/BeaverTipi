package kr.or.ddit.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberInfoController {

	@GetMapping("/account/update")
	public String info() {
		return "member/info";
	}
}
