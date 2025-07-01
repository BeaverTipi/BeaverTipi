package kr.or.ddit.building.managed.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class ManagedAddController {
	@GetMapping("/building/managed/add")
	public String managedformUI() {
		return "building/managed/managedAdd";
	}
}
