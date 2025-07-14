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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import kr.or.ddit.resident.checkPage.dto.CheckComparisonDto;
import kr.or.ddit.resident.checkPage.service.PaymentCheckService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
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

    @GetMapping("/dataState/energy")
    public String showEnergyUsage() {
        return "resident/dataState/EnergyUsage";
    }

    @GetMapping("/dataState/bill")
    public String showUtilityBill(
        Model model,
        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
        @RequestParam(required = false) String bldgIdParam,
        @RequestParam(required = false) String yearSelect,
        @RequestParam(required = false) String monthSelect
    ) {
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        if (units == null || units.isEmpty()) {
            return "resident:/member/register";
        }

        String chargeMonth;
        if (yearSelect != null && monthSelect != null &&
            !yearSelect.equals("년도 선택") && !monthSelect.equals("월 선택")) {
            
            String paddedMonth = String.format("%02d", Integer.parseInt(monthSelect));
            chargeMonth = yearSelect + paddedMonth;  // ex: "202507"
        } else {
            chargeMonth = getCurrentMonth();
        }
        
        String tempBldgId = (bldgIdParam == null || bldgIdParam.isBlank())
            ? units.stream().min(Comparator.comparing(UnitResidentVO::getMoveInDt))
                .map(UnitResidentVO::getBldgId).orElse(units.get(0).getBldgId())
            : bldgIdParam;

        final String selectedBldgId = tempBldgId;
        String unitId = units.stream()
            .filter(u -> selectedBldgId.equals(u.getBldgId()))
            .findFirst().map(UnitResidentVO::getUnitId)
            .orElse(units.get(0).getUnitId());

        // ⏰ 당월, 전월 계산
        String currentMonth = getCurrentMonth();
        String previousMonth = getPreviousMonth();

        // 📊 공과금 비교
        List<CheckComparisonDto> chargeComparison =
            paymentCheckService.getMonthlyComparison(unitId, currentMonth, previousMonth);

        // 📊 에너지 비교
        Map<String, Map<String, Object>> energyComparison =
            paymentCheckService.getEnergyComparison(unitId, currentMonth, previousMonth);
        
        log.info("📊 이전 달 에너지 데이터: {}", energyComparison.get(previousMonth));
        
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", selectedBldgId);
        model.addAttribute("bldgIdParam", bldgIdParam);
        model.addAttribute("chargeMonth", currentMonth); // ⬅️ 기준은 최신 달
        model.addAttribute("chargeComparison", chargeComparison);
        model.addAttribute("energyComparison", energyComparison);
        model.addAttribute("previousMonth", previousMonth);
        model.addAttribute("yearSelect",yearSelect);
        model.addAttribute("monthSelect",monthSelect);
        
        return "resident/dataState/UtilityBillList"; // 💡 이 JSP에서 비교값 출력
    }

	// 날짜 포맷 유틸
	private String getCurrentMonth() {
	    return LocalDate.now().format(DateTimeFormatter.ofPattern("yyyyMM"));
	}

	private String getPreviousMonth() {
	    return LocalDate.now().minusMonths(1).format(DateTimeFormatter.ofPattern("yyyyMM"));
	}

	
}
