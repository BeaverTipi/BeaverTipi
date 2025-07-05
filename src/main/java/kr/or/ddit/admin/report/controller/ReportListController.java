package kr.or.ddit.admin.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class ReportListController {
	@GetMapping("/reportList")
	public String noticeList() {
		 return "admin/report/reportList"; 
	}
}
