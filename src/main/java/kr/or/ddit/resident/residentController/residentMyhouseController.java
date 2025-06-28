package kr.or.ddit.resident.residentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resident")
public class residentMyhouseController {
	
	 @GetMapping("/myhouse")
	    public String showMyHousePage() {
	        return "resident/myHouse/myHouseMainpage";  
	    }

}
