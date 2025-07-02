package kr.or.ddit.resident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resident")
public class RsdPaymentController {

	@GetMapping("/chargeBillResident")
	public String noticeMyHousePage() {
		 return "resident/chargeBillResident/Payment"; 
	}
}
