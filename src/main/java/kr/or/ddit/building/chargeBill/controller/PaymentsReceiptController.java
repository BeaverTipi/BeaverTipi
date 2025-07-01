package kr.or.ddit.building.chargeBill.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PaymentsReceiptController {
	@GetMapping("/building/payments/receipt/list")
	public String paymentsProofList() {
		return "building/managed/paymentsReceiptList";
	}
}
