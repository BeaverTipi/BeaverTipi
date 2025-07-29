package kr.or.ddit.resident.checkPage.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.checkPage.dto.CheckComparisonDto;
import kr.or.ddit.resident.checkPage.service.PaymentCheckService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident")
public class RsdDataStateController {

    @Autowired
    private UnitResidentService unitResidentService;

    @Autowired
    private PaymentCheckService paymentCheckService; // ✅ 새 서비스 연결

    @GetMapping("/dataState/bill")
    public String showUtilityBill(
        Model model,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
        @RequestParam(required = false) String bldgIdParam,
        @RequestParam(required = false) String unitIdParam,
        @RequestParam(required = false) String chargeMonth,
        @ModelAttribute("search") SimpleSearch simpleSearch
    ) {
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> allUnits = unitResidentService.getUnitsByMember(member.getMbrCd());
        
        if (allUnits == null || allUnits.isEmpty()) {
            return "resident:/member/register";
        }
        
        if (bldgIdParam != null && (unitIdParam == null || chargeMonth == null)) {
            List<UnitResidentVO> myUnits = paymentCheckService.getMyUnitsInBuilding(member.getMbrCd(), bldgIdParam);
            if (!myUnits.isEmpty()) {
                String firstUnitId = myUnits.get(0).getUnitId();
                List<String> months = paymentCheckService.getAvailableChargeMonths(firstUnitId);
                String latestMonth = months.isEmpty() ? getCurrentMonth() : months.get(0);
                return "redirect:/resident/dataState/bill?bldgIdParam=" + bldgIdParam +
                       "&unitIdParam=" + firstUnitId +
                       "&chargeMonth=" + latestMonth;
            }
        }

        String selectedBldgId = (bldgIdParam == null || bldgIdParam.isBlank())
            ? allUnits.stream().min(Comparator.comparing(UnitResidentVO::getMoveInDt))
                .map(UnitResidentVO::getBldgId).orElse(allUnits.get(0).getBldgId())
            : bldgIdParam;

        List<UnitResidentVO> units = paymentCheckService.getMyUnitsInBuilding(member.getMbrCd(), selectedBldgId);

        String selectedUnitId = (unitIdParam == null || unitIdParam.isBlank())
            ? units.stream().filter(u -> selectedBldgId.equals(u.getBldgId()))
                .findFirst().map(UnitResidentVO::getUnitId).orElse(units.get(0).getUnitId())
            : unitIdParam;

        String baseMonth = (chargeMonth != null && !chargeMonth.isBlank())
        		? chargeMonth
        				: getCurrentMonth();
        
        // 기준 월에서 전월, 전전월 계산
        LocalDate baseDate = LocalDate.of(
            Integer.parseInt(baseMonth.substring(0, 4)),
            Integer.parseInt(baseMonth.substring(4, 6)),
            1
        );
        
        String thisMonth = baseMonth;
        String previousMonth = baseDate.minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM")); 
        
        List<CheckComparisonDto> chargeComparison =
            paymentCheckService.getMonthlyComparison(selectedUnitId, previousMonth, thisMonth);

        List<CheckComparisonDto> currentCharges =
        		paymentCheckService.getMonthlyCharges(selectedUnitId, thisMonth);
        
        Map<String, Map<String, Object>> energyComparison =
            paymentCheckService.getEnergyComparison(selectedUnitId, previousMonth, thisMonth);

        List<String> availableMonths = paymentCheckService.getAvailableChargeMonths(selectedUnitId);
        
        
        log.info("🟠 기준월 (baseMonth): {}", baseMonth);
        log.info("📦 당월 (thisMonth): {}", thisMonth);  // ✅ 명확하게 표시
        log.info("🔸 전월 (previousMonth): {}", previousMonth);
        log.info("selectedUnitId: {}", selectedUnitId);
        log.info("📊 chargeComparison size: {}", chargeComparison.size());
        log.info("📦 currentCharges size: {}", currentCharges.size());
        for (CheckComparisonDto dto : currentCharges) {
            log.info("➡️ currentCharge: {} - {}", dto.getFeeName(), dto.getChargeAmount());
        }
        log.info("📈 energyComparison keys: {}", energyComparison.keySet());
        
        model.addAttribute("availableMonths", availableMonths);
        model.addAttribute("unitList", allUnits);
        model.addAttribute("unitsInBuilding", units);
        model.addAttribute("selectedBldgId", selectedBldgId);
        model.addAttribute("selectedUnitId", selectedUnitId);
        model.addAttribute("chargeMonth", baseMonth); // 기준 월 표시용
        model.addAttribute("previousMonth", previousMonth);
        model.addAttribute("chargeComparison", chargeComparison);
        model.addAttribute("currentCharges", currentCharges);
        model.addAttribute("energyComparison", energyComparison);

        return "resident/dataState/UtilityBillList";
    }

	// 날짜 포맷 유틸
	private String getCurrentMonth() {
	    return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
	}

	private String getPreviousMonth() {
	    return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
	}

	  private String getTwoMonthsAgo() {
	        return LocalDate.now().minusMonths(2).format(DateTimeFormatter.ofPattern("yyyyMM")); 
	    }
}
