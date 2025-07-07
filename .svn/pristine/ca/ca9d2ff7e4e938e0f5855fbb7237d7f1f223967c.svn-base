//
//package kr.or.ddit.building.managed.controller;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import kr.or.ddit.building.managed.service.ManagedService;
//import kr.or.ddit.vo.BuildingVO;
//import kr.or.ddit.vo.UnitVO;
//@Controller
//@RequestMapping("/building/managed")
//public class ManagedUpdateController {
//	   @Autowired
//	    private ManagedService managedService;
//
//	   @GetMapping("/edit")
//	   public String editForm(@RequestParam("bldgId") String bldgId, Model model) {
//	       BuildingVO buildingVO = managedService.selectBuildingById(bldgId); // bldgId 기준 조회
//	       model.addAttribute("buildingVO", buildingVO);
//	       model.addAttribute("mode", "edit"); // JSP에서 등록/수정 구분용
//	       return "building/managed/managedAdd"; // 등록폼과 JSP 공유
//	   }
//
//
//	   @PostMapping("/edit")
//	   public String updateBuilding(@ModelAttribute("buildingVO") BuildingVO buildingVO) {
//	       managedService.updateBuilding(buildingVO); // 실제 BUILDING 테이블 수정
//	       return "redirect:/building/managed/list?bldgId=" + buildingVO.getBldgId(); // 수정 후 리스트로 리다이렉트
//	   }
//
//}
//
