package kr.or.ddit.building.resident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MoveInDetailController {
	@GetMapping("/building/moveIn") 
	public String moveIn(){
		return "building/move-in/moveInDetail";
	}
}
