//package kr.or.ddit.building.managed.controller;
//
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import kr.or.ddit.building.managed.service.BuildingManagedService;
//import kr.or.ddit.util.security.auth.RealUserWrapper;
//import kr.or.ddit.vo.BuildingVO;
//import kr.or.ddit.vo.MemberVO;
//import kr.or.ddit.vo.TenancyAccountVO;
//
//@Controller
//@RequestMapping("/building/product")
//public class ManagedUpdateController {
//
//    @Autowired
//    private BuildingManagedService managedService;
//
//    // 수정 폼 진입
//    @GetMapping("/managedEdit")
//    public String editForm(@RequestParam("bldgId") String bldgId,
//                           Model model,
//                           @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
//    	
//    	
//
//        MemberVO memberVO = principal.getRealUser();
//        String rentalPtyId = memberVO.getTenancy().getRentalPtyId();
//      
//        BuildingVO buildingVO = managedService.selectBuildingById(bldgId);
//        buildingVO.setRentalPtyId(rentalPtyId);  // 혹시 몰라 재셋팅
//    	List<TenancyAccountVO> accList = managedService.selectAccountsByRentalPtyId(rentalPtyId);
//    	buildingVO.setAccList(accList);
//        if (!accList.isEmpty() && buildingVO.getAccNum() == null) {
//            buildingVO.setAccNum(accList.get(0).getAccNum());
//        }
//       
//    	model.addAttribute("buildingVO", buildingVO);
//        model.addAttribute("mode", "edit"); // JSP에서 <c:choose>로 등록/수정 버튼 구분
//        
//        return "building/managed/managedAdd";
//    }
//
//    // 수정 처리
//    @PostMapping("/managedEdit")
//    public String editUnit(@ModelAttribute("buildingVO") BuildingVO buildingVO) {
//        
//    	if (buildingVO.getBldgTypeCode() == null || buildingVO.getBldgTypeCode().isBlank()) {
//            // 오류 로그 찍고, 폼으로 다시 보내거나 예외 처리 필요
//            return "redirect:/building/managed/edit?bldgId=" + buildingVO.getBldgId(); // 혹은 에러 페이지
//        }
//    	
//    	managedService.updateBuilding(buildingVO);
//        
//        return "redirect:/building/managed/list?bldgId=" + buildingVO.getBldgId();
//    }
//}
