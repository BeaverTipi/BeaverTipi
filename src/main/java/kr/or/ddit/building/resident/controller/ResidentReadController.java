package kr.or.ddit.building.resident.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ResidentReadController {
	@GetMapping("/building/resident/read")
	public String residentform() {
		return "building/move-in/residentList";
	}
}
