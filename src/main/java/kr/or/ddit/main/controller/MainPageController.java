package kr.or.ddit.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainPageController {

	@GetMapping("/")
	public String mainPage() {
		return "main/mainPage";
	}
}
