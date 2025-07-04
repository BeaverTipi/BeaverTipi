package kr.or.ddit.building.managed.controller;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;


import kr.or.ddit.building.managed.service.ManagedService;
import kr.or.ddit.vo.BuildingVO;
@Controller
@RequestMapping("/building/managed")
public class ManagedAddController {
	
	@Autowired
	private ManagedService managedService; 
	
	
	@GetMapping("/add")
	public String addForm(Model model) {
	    model.addAttribute("buildingVO", new BuildingVO());
	    return "building/managed/managedAdd";
	}
	
	@PostMapping("/add")
	public String addUnit(@ModelAttribute BuildingVO unitVO) {
		managedService.insertUnit(unitVO);
		return "redirect:/building/managed/list?bldgId=" + unitVO.getBldgId();
	}
	
}
