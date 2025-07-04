package kr.or.ddit.main.member.controller;

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
public class MemberUpdateController {
	private final MemberService service;
	private static final String MODELNAME ="member";	

	
	@GetMapping("/account/update")
	public String formUI(
			Authentication authentication
			, Model model
	) {
		String lvn = "main/member/updateForm";
		Object principal = authentication.getPrincipal();
		if(!model.containsAttribute(MODELNAME)) {
		    String username = authentication.getName();
		    MemberVO member = service.readMember(username);
			model.addAttribute(MODELNAME, member);
		}
		
		if (principal instanceof OAuth2User || principal instanceof OidcUser) {
			lvn = "main/member/updateOAuth2Form";
		}
		
		return lvn;
	}
}
