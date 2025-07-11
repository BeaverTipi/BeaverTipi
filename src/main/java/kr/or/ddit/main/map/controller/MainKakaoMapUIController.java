package kr.or.ddit.main.map.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequiredArgsConstructor
@RequestMapping("/main/map")
public class MainKakaoMapUIController{
	
	private final KakaoApiKeyProvider apiKeyProvider;
	
	@GetMapping
	public String kakaoMapUI(Model model) {
		model.addAttribute("jsApiKey", apiKeyProvider.getJsApiKey());
		return "main/mainMap/mainKakaoMapUI";
	}

}
