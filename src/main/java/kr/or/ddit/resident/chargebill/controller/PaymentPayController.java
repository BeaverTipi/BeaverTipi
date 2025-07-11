/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 10.     	권성운            최초 생성
 * 
 * </pre>
 */
package kr.or.ddit.resident.chargebill.controller;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.resident.chargebill.service.PaymentService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.page.SimpleSearch;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.ChargeBillVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.UnitResidentVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * @author 
 * @since
 * @see
 * 
 *
 */
@Slf4j
@Controller
@RequestMapping("/resident/payment")
@RequiredArgsConstructor
public class PaymentPayController {

    @Autowired
    private PaymentService paymentService;
    @Autowired
    private UnitResidentService unitResidentService;
    
    
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
        log.info("📌 simpleSearch.bldgId (after): {}", simpleSearch.getBldgId());
        log.info("▶ Search: bldgId={}", simpleSearch.getBldgId());
        
        String unitId = units.stream()
        	    .filter(u -> selectedBldgId.equals(u.getBldgId()))
        	    .findFirst()
        	    .map(UnitResidentVO::getUnitId)
        	    .orElse(units.get(0).getUnitId()); // fallback 처리
        
        String currentMonth = getCurrentMonth(); // 현재 월을 가져옵니다.
        String previousMonth = getPreviousMonth(); // 전월을 가져옵니다.
        String beforeLastMonth = getBeforeLastMonth(); // 전전월을 가져옵니다.

        log.info("unitId={}, currentMonth={}, previousMonth={}, beforeLastMonth={}", unitId, currentMonth, previousMonth, beforeLastMonth); // 로그 추가

        // 청구 내역 가져오기
        List<ChargeBillVO> chargeBillList = paymentService.retrieveChargeBillListForMonths(unitId, previousMonth, beforeLastMonth);
        log.info("chargeBillList ==== {}", chargeBillList);
        
        List<ChargeBillVO> chargeBillListLastMonth = chargeBillList.stream()
        	    .filter(bill -> previousMonth.equals(bill.getChgbillChargeMonth()))
        	    .toList();

        	List<ChargeBillVO> chargeBillListBeforeLastMonth = chargeBillList.stream()
        	    .filter(bill -> beforeLastMonth.equals(bill.getChgbillChargeMonth()))
        	    .toList();

        	// 모델에 데이터 추가
       	model.addAttribute("chargeBillListLastMonth", chargeBillListLastMonth);
       	model.addAttribute("chargeBillListBeforeLastMonth", chargeBillListBeforeLastMonth);
        model.addAttribute("previousMonth", previousMonth);
        log.info("previousMonth=========={}",previousMonth);
        model.addAttribute("beforeLastMonth", beforeLastMonth);
        model.addAttribute("chargeBillList", chargeBillList);
        model.addAttribute("unitList", units);
        model.addAttribute("bldgIdParam", bldgIdParam);
        model.addAttribute("selectedBldgId", selectedBldgId);
        

        log.info("chargeBillListLastMonth.size={}", chargeBillListLastMonth.size());
        log.info("chargeBillListBeforeLastMonth.size={}", chargeBillListBeforeLastMonth.size());
        return "resident/payment/Payment";
    }
    
    @GetMapping("/detail")
    @ResponseBody
    public List<ChargeBillVO> getChargeBillDetail(
    		@RequestParam String unitId,
    		@RequestParam String chargeMonth
    		){
    	
    	
    	return paymentService.retrieveChargeBillListForMonths(unitId, chargeMonth, chargeMonth);
    }
    

    private String getCurrentMonth() {
        LocalDate currentDate = LocalDate.now();
        return currentDate.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private String getPreviousMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate previousMonth = currentDate.minusMonths(1); // 전월 계산
        return previousMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }

    private String getBeforeLastMonth() {
        LocalDate currentDate = LocalDate.now();
        LocalDate beforeLastMonth = currentDate.minusMonths(2); // 전전월 계산
        return beforeLastMonth.format(DateTimeFormatter.ofPattern("yyyyMM"));
    }
    
    


}
