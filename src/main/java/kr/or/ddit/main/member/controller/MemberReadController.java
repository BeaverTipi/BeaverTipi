package kr.or.ddit.main.member.controller;

import java.security.Principal;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberReadController {
	private final MemberService service;

	@GetMapping("/account/read")
	public String mypage(Model model,
			Principal principal) {
		String username = principal.getName();
		MemberVO member = service.readMember(username);
		model.addAttribute("member", member);
		return "main/member/memberPage";
	}
}
