package kr.or.ddit.building.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
@Controller
@RequestMapping("/building/myhouse")
public class BuildingDashboardController {
	
	@GetMapping
	public String main() {
		return "building/main";
	}
	
	
	
	
	
}
