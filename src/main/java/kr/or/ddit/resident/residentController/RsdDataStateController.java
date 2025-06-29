package kr.or.ddit.resident.residentController;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/resident")
public class RsdDataStateController {

	@GetMapping("/dataState/energy")
	public String showEnergyUsage() {
	    return "resident/dataState/EnergyUsage";
	}

	@GetMapping("/dataState/bill")
	public String showUtilityBill() {
	    return "resident/dataState/UtilityBillList";
	}

}
