package kr.or.ddit.building.managed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ManagedListController {
	@GetMapping("/building/managed/list")
	public String managedList() {
		return "building/managed/managedList";
	}
}
