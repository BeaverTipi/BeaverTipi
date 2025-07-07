package kr.or.ddit.resident.main.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resident/myhouse")
public class RsdMyhouseMainController {
	
	 @GetMapping("")
	    public String showMyHousePage() {
	        return "resident/myHouse/MyHouseMainpage";  
	    }

}
