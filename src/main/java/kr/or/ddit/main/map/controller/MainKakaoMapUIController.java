package kr.or.ddit.main.map.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/main/map")
public class MainKakaoMapUIController{
	
	private final KakaoApiKeyProvider apiKeyProvider;
	
	@GetMapping
	public String kakaoMapUI(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		model.addAttribute("jsApiKey", apiKeyProvider.getJsApiKey());

		if (principal != null && principal.getRealUser() != null) {
			MemberVO loginMember = principal.getRealUser();
			model.addAttribute("loginMember", loginMember);
		}

		return "main/mainMap/mainKakaoMapUI";
	}


}
