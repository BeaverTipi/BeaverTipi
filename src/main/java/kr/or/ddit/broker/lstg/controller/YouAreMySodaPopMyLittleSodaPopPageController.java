package kr.or.ddit.broker.lstg.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author developer_KCY
 */
@Controller
@RequestMapping("/youaremysodapop")
public class YouAreMySodaPopMyLittleSodaPopPageController {
	
	@GetMapping
	public String mylittlesodapop() {
		return "broker/youAreMySodaPopMyLittleSodaPop";
	}

}
