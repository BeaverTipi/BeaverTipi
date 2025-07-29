package kr.or.ddit.resident.chargebill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;

@RestController
@RequestMapping("/ajax/resident/api")
public class PaymentRestController {

    @Autowired
    private UnitResidentService unitResidentService;

    
    @GetMapping("/units")
    public List<UnitResidentVO> getUnitsByBuilding(
        @RequestParam("bldgId") String bldgId,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> user
    ) {
        String mbrCd = user.getRealUser().getMbrCd();
        return unitResidentService.selectMyUnitsInBuilding(mbrCd, bldgId);
    }
}
