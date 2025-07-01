package kr.or.ddit.member.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class VerificationController {
	@PostMapping("/member/verification")
	public void verification(String phone) {
		
	}
}
