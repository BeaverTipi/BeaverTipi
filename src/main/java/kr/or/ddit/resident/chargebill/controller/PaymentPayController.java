package kr.or.ddit.resident.chargebill.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.resident.chargebill.dto.ChargeComparisonDto;
import kr.or.ddit.resident.chargebill.service.PaymentService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident/payment")
@RequiredArgsConstructor
public class PaymentPayController {

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private UnitResidentService unitResidentService;

    @Autowired
    private CommonCodeService service;
    
    @GetMapping
    public String paymentList(
            Model model,
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @RequestParam(required = false) String bldgIdParam,
            @ModelAttribute("search") SimpleSearch simpleSearch
    ) {
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        if (units == null || units.isEmpty()) {
            return "resident:/member/register";
        }

        String tempBldgId = bldgIdParam;
        if (tempBldgId == null || tempBldgId.isBlank()) {
            tempBldgId = units.stream()
                .min(Comparator.comparing(UnitResidentVO::getMoveInDt))
                .map(UnitResidentVO::getBldgId)
                .orElse(units.get(0).getBldgId());
        }
        final String selectedBldgId = tempBldgId;

        simpleSearch.setBldgId(selectedBldgId);
        log.info("📌 selectedBldgId: {}", selectedBldgId);

        String unitId = units.stream()
            .filter(u -> selectedBldgId.equals(u.getBldgId()))
            .findFirst()
            .map(UnitResidentVO::getUnitId)
            .orElse(units.get(0).getUnitId());

        String currentMonth = getCurrentMonth();
        String previousMonth = getPreviousMonth();

        log.info("unitId={}, currentMonth={}, previousMonth={}", unitId, currentMonth, previousMonth);

        // ✅ DTO 기반 비교 결과 조회
        List<ChargeComparisonDto> comparisonList =
                paymentService.getChargeComparisonList(unitId, currentMonth, previousMonth);
        
        Map<String, Map<String, Object>> energySummary =
                paymentService.getEnergyUsageSummary(unitId, currentMonth, previousMonth);

        List<CommonCodeVO> payment = service.readCommonCodeList("PAY")
        	    .stream()
        	    .collect(Collectors.collectingAndThen(
        	        Collectors.toMap(CommonCodeVO::getCodeName, c -> c, (a, b) -> a),
        	        map -> map.values().stream().toList()
        	    ));
        
        model.addAttribute("payment", payment);
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", selectedBldgId);
        model.addAttribute("bldgIdParam", bldgIdParam);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("previousMonth", previousMonth);
        model.addAttribute("chargeBillComparisonList", comparisonList);
        model.addAttribute("energySummary", energySummary);

        log.info("📊 비교형 청구 리스트 size: {}", comparisonList.size());
        return "resident/payment/Payment";
    }


    private String getCurrentMonth() {
        return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private String getPreviousMonth() {
        return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

}