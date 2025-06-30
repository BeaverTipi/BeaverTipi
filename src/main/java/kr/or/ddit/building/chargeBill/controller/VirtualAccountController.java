package kr.or.ddit.building.chargeBill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class VirtualAccountController {
	@GetMapping("/building/virtualAccount")
	public String virtualAccount(){
		return "building/payments/virtualAccount";
	}
}
