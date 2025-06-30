package kr.or.ddit.building;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
@Controller
public class mainController {
	@GetMapping("/building/myhouse")
	public String main() {
		return "building/main";
	}
}
