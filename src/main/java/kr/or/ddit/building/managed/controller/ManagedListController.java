package kr.or.ddit.building.managed.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.building.managed.service.ManagedService;
import kr.or.ddit.vo.BuildingVO;
@Controller
@RequestMapping("/building/managed")
public class ManagedListController {

	
	@Autowired
	private ManagedService managedService; 
	
	@GetMapping("/list")
	public String unitList(@RequestParam(value = "bldgId", required = false) String bldgId, Model model) {
	    List<BuildingVO> unitList = 
	        (bldgId != null) ? managedService.selectUnitListByBldgId(bldgId) : List.of();
	    model.addAttribute("unitList", unitList);
	    return "building/managed/managedList";
	}
	

}
