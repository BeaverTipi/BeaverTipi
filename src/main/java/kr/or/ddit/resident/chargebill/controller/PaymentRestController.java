package kr.or.ddit.resident.chargebill.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.or.ddit.resident.chargebill.service.PaymentService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.vo.UnitResidentVO;

@RestController
@RequestMapping("/ajax/resident/api")
public class PaymentRestController {

    @Autowired
    private UnitResidentService unitResidentService;

    @Autowired
    private PaymentService paymentService;
    
    @GetMapping("/units")
    public List<UnitResidentVO> getUnitsByBuilding(
            @RequestParam String bldgId,
            @RequestParam String mbrCd
    ) {
        return unitResidentService.selectMyUnitsInBuilding(mbrCd, bldgId);
    }
    
}
