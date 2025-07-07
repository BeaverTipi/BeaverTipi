package kr.or.ddit.building.chargeBill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SavingAccountController {
	@GetMapping("/building/savingAccount")
	public String savingAccount() {
		return "building/payments/savingAccount";
	}
	
}
