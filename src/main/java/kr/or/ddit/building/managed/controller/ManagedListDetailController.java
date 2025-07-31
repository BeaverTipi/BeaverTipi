package kr.or.ddit.building.managed.controller;

import java.util.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.building.unitManaged.service.UnitManagedService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.vo.*;

@Controller
@RequestMapping("/building/managed")
public class ManagedListDetailController {

    @Autowired
    private BuildingManagedService buildingService;
    @Autowired
    private UnitManagedService unitService;
    @Autowired
    private UnitResidentService unitResidentService;

    @GetMapping("/detail")
    public String buildingDetail(
        @RequestParam("bldgId") String bldgId,
        Model model
    ) {
        BuildingVO buildingVO = buildingService.selectBuildingById(bldgId);
        List<UnitVO> unitList = unitService.selectUnitListByBldgId(bldgId);

        // 어휴
        String selectedUnitId = unitList.isEmpty() ? null : unitList.get(0).getUnitId();
        List<UnitResidentVO> residentList = selectedUnitId != null ?
                unitResidentService.selectUnitResidentListByUnitId(selectedUnitId)
                : Collections.emptyList();

        model.addAttribute("buildingVO", buildingVO);
        model.addAttribute("unitList", unitList);
        model.addAttribute("selectedUnitId", selectedUnitId);
        model.addAttribute("residentList", residentList);
        return "building/managed/managedDetailList";
        
    }
    

    @GetMapping("/detail/quick/{bldgId}")
    public String buildingQuickDetail(@PathVariable String bldgId, Model model) {
       
        BuildingVO buildingVO = buildingService.selectBuildingById(bldgId);
        List<UnitVO> unitList = unitService.selectUnitListByBldgId(bldgId);
        String selectedUnitId = unitList.isEmpty() ? null : unitList.get(0).getUnitId();
        List<UnitResidentVO> residentList = selectedUnitId != null
                ? unitResidentService.selectUnitResidentListByUnitId(selectedUnitId)
                : Collections.emptyList();

        model.addAttribute("buildingVO", buildingVO);
        model.addAttribute("unitList", unitList);
        model.addAttribute("selectedUnitId", selectedUnitId);
        model.addAttribute("residentList", residentList);

        
        return "building/managed/managedDetailView";
    }

    

    @GetMapping("/detail/residentList")
    @ResponseBody
    public List<UnitResidentVO> getResidentListByUnitId(@RequestParam("unitId") String unitId) {
        return unitResidentService.selectUnitResidentListByUnitId(unitId);
    }
}
