package kr.or.ddit.resident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resident")
public class RsdCalendarController {
	@GetMapping("/calendar")
	public String noticeMyHousePage() {
		 return "resident/calendar/Calendar"; 
	}
}
