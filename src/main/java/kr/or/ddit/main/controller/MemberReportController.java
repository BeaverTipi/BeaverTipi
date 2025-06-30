package kr.or.ddit.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MemberReportController {

	@GetMapping("/report/userReport")
	public String info() {
		return "report/userReport";
	}
}
