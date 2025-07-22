package kr.or.ddit.main.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.vo.CommonCodeVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MainPageController {
	
	private final CommonCodeService commonCode;
	
	@GetMapping("/")
	public String mainPage(Model model) {
		List<CommonCodeVO> categoryList = commonCode.readCommonCodeList("LSTG1");
		model.addAttribute("categoryList", categoryList);
		return "main/mainPage";
	}
}
