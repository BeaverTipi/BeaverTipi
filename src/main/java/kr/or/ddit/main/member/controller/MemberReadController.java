package kr.or.ddit.main.member.controller;

import java.security.Principal;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
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
			Authentication auth) {
		Object principal = auth.getPrincipal();
		String username =auth.getName();
		String logInfo = "LOCAL";
		MemberVO member = service.readMember(username);
	
		if (principal instanceof OAuth2User) {
			logInfo = "KAKAO";
		}else if(principal instanceof OidcUser) {
			logInfo="GOOGLE";
		}else {
		}
		model.addAttribute("member", member);
		model.addAttribute("logInfo", logInfo);
		return "main/member/memberPage";
	}
}
