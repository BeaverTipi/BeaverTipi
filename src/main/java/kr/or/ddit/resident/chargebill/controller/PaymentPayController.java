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
            @RequestParam(required = false) String unitIdParam,
            @ModelAttribute("search") SimpleSearch simpleSearch
    ) {
        MemberVO member = principal.getRealUser();
        List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
        if (units == null || units.isEmpty()) {
        	log.warn("유닛 정보가 없습니다. 회원 등록이 필요합니다.");
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

        log.info("선택한 건물 : {}",selectedBldgId);
        
        simpleSearch.setBldgId(selectedBldgId);
        log.info("📌 selectedBldgId: {}", selectedBldgId);

        List<UnitResidentVO> unitsInBuilding = unitResidentService.selectMyUnitsInBuilding(member.getMbrCd(), selectedBldgId);
        
        if (unitsInBuilding.isEmpty()) {
            log.warn("해당 건물에 계약된 유닛이 없습니다.");
            return "resident:/member/register";
        }
        
        String unitId = (unitIdParam != null && !unitIdParam.isBlank())
        	    ? unitIdParam
        	    : unitsInBuilding.get(0).getUnitId();

        log.info("선택된 유닛 ID:{}",unitId);
        
        String currentMonth = getCurrentMonth();
        String previousMonth = getPreviousMonth();
        String twoMonthsAgo = getTwoMonthsAgo();
        
        log.info("전월 월: {}, 전전월 월: {}", previousMonth,twoMonthsAgo);
        

        // ✅ DTO 기반 비교 결과 조회
        List<ChargeComparisonDto> comparisonList =
                paymentService.getChargeComparisonList(unitId, currentMonth, previousMonth);
        
        log.info("청구 비교 리스트 사이즈: {}", comparisonList.size());
        
        Map<String, Map<String, Object>> energySummary =
                paymentService.getEnergyUsageSummary(unitId, currentMonth, previousMonth);

        List<CommonCodeVO> payment = service.readCommonCodeList("PAY")
        	    .stream()
        	    .collect(Collectors.collectingAndThen(
        	        Collectors.toMap(CommonCodeVO::getCodeName, c -> c, (a, b) -> a),
        	        map -> map.values().stream().toList()
        	    ));
        
        log.info("unitId: {}", unitId);
        log.info("currentMonth: {}, previousMonth: {}", currentMonth, previousMonth);
        log.info("energySummary: {}", energySummary);
        
        Long currentChargeAmount = paymentService.getCurrentChargeAmount(unitId,currentMonth);
        log.info("getCurrentChargeAmount : ", currentChargeAmount);
        model.addAttribute("currentChargeAmount", currentChargeAmount);
        
        log.info("납부 관련 공통 코드: {}", payment);
        
        model.addAttribute("unitsInBuilding", unitsInBuilding);
        model.addAttribute("selectedUnitId", unitId);
        model.addAttribute("payment", payment);
        model.addAttribute("unitList", units);
        model.addAttribute("selectedBldgId", selectedBldgId);
        model.addAttribute("bldgIdParam", bldgIdParam);
        model.addAttribute("currentMonth", currentMonth);
        model.addAttribute("previousMonth", previousMonth);
        model.addAttribute("twoMonthsAgo", twoMonthsAgo);
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
    private String getTwoMonthsAgo() {
        return LocalDate.now().minusMonths(2).format(DateTimeFormatter.ofPattern("yyyyMM")); // 202505
    }
    
}