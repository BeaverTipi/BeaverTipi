package kr.or.ddit.building.managed.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;

import kr.or.ddit.building.managed.service.BuildingManagedService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;


@Controller
@RequestMapping("/building/managed")
public class ManagedDeleteController {

    @Autowired
    private BuildingManagedService managedService;

    @PostMapping("/delete")
    public String delete(@RequestParam("bldgId") String bldgId,
                         @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        String rentalPtyId = principal.getRealUser().getTenancy().getRentalPtyId();
        managedService.deleteBuilding(bldgId, rentalPtyId);
        return "redirect:/building/managed/list";
    }

}
