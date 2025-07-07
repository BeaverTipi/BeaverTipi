package kr.or.ddit.building.managed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class OutsourcingListController {
	@GetMapping("/building/managed/outsourcing")
	public String outsourcingList() {
		return "building/managed/outsourcingList";
	}
}
