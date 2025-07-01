package kr.or.ddit.admin.ads.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdcListController {
	@GetMapping("/adcList")
	public String noticeList() {
		 return "admin/adc/adcList"; 
	}
}
