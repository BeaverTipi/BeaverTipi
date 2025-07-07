//
//package kr.or.ddit.building.managed.controller;
//
//
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//
//import kr.or.ddit.building.managed.service.ManagedService;
//import kr.or.ddit.util.security.auth.RealUserWrapper;
//import kr.or.ddit.vo.BuildingVO;
//import kr.or.ddit.vo.MemberVO;
//@Controller
//@RequestMapping("/building/managed")
//public class ManagedAddController {
//	
//	@Autowired
//	private ManagedService managedService; 
//	
//	
//    // 1. 등록 폼 진입
//	  @GetMapping("/add")
//	    public String addForm(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
//	        MemberVO memberVO = principal.getRealUser();
//
//	        BuildingVO buildingVO = new BuildingVO();
//	    
//
//	        // Tenancy 정보에서 rentalPtyId 꺼내서 셋팅
//	        if (memberVO != null && memberVO.getTenancy() != null) {
//	            buildingVO.setRentalPtyId(memberVO.getTenancy().getRentalPtyId());
//	        }
//	        
//	        
//	        model.addAttribute("buildingVO", buildingVO);
//	        return "building/managed/managedAdd";
//	  }
//
//    // 2. 등록 처리
//    @PostMapping("/add")
//    public String addUnit(@ModelAttribute("buildingVO") BuildingVO buildingVO) {
//        managedService.insertBuilding(buildingVO);
//        System.out.println("여기를봐라 멍청이들아" + buildingVO);
//        return "redirect:/building/managed/list?bldgId=" + buildingVO.getBldgId();
//    }
//	
//}
//
//
//
//
//
