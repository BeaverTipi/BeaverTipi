package kr.or.ddit.building.managed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.building.managed.service.ManagedService;
import kr.or.ddit.vo.BuildingVO;
@Controller
@RequestMapping("/building/managed")
public class ManagedUpdateController {
	
	@Autowired
	private ManagedService managedService; 
	
	
	
	 @GetMapping("/edit")
	 public String editForm(@RequestParam("unitId") String unitId, Model model) {
	     BuildingVO unit = managedService.selectUnitById(unitId);
	     model.addAttribute("unit", unit);
	     return "building/managed/managedEdit";
	}
	 
	 @PostMapping("/edit")
	 public String editUnit(@ModelAttribute BuildingVO unitVO) {
	     managedService.updateUnit(unitVO);
	     return "redirect:/building/managed/list?bldgId=" + unitVO.getBldgId();
	    }
}
