//
//package kr.or.ddit.building.managed.controller;
//
//import java.util.Collections;
//import java.util.List;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.security.core.annotation.AuthenticationPrincipal;
//import org.springframework.stereotype.Controller;
//import org.springframework.ui.Model;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//
//import kr.or.ddit.building.managed.service.ManagedService;
//import kr.or.ddit.util.security.auth.RealUserWrapper;
//import kr.or.ddit.vo.BuildingVO;
//import kr.or.ddit.vo.MemberVO;
//
//@Controller
//@RequestMapping("/building/managed")
//public class ManagedListController {
//
//    @Autowired
//    private ManagedService managedService;
//
//    @GetMapping("/list")
//    public String buildingList(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
//        MemberVO memberVO = principal.getRealUser();
//        String rentalPtyId = memberVO.getTenancy().getRentalPtyId();
//
//        List<BuildingVO> buildingList = managedService.selectBuildingListByBldgId(rentalPtyId);
//        model.addAttribute("buildingList", buildingList);
//        return "building/managed/managedList";
//    }
//
//}
//
