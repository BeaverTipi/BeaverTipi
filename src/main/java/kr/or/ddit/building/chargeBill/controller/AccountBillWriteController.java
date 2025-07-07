package kr.or.ddit.building.chargeBill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccountBillWriteController {
	@GetMapping("/building/accountBill/write")
	public String giro() {
		return "building/payments/giro";
	}
}
