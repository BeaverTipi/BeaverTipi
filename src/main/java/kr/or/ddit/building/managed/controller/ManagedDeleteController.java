package kr.or.ddit.building.managed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.building.managed.service.ManagedService;

@Controller
@RequestMapping("/building/managed")
public class ManagedDeleteController {


	@Autowired
	private ManagedService managedService;

	@PostMapping("/delete")
	public String deleteUnit(@RequestParam("unitId") String unitId, @RequestParam("bldgId") String bldgId) {
		managedService.deleteUnit(unitId);
		return "redirect:/building/managed/list?bldgId=" + bldgId;
	}
}
