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
public class PaymentPayController {

	@Autowired
	private PaymentService paymentService;
	@Autowired
	private UnitResidentService unitResidentService;
	
	@GetMapping
	public String paymentList(
			Model model
			, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal
			, @RequestParam(required = false) String bldgIdParam
			, @ModelAttribute("search") SimpleSearch simpleSearch
			) {
		MemberVO member = principal.getRealUser();
		List<UnitResidentVO> units = unitResidentService.getUnitsByMember(member.getMbrCd());
		if(units == null || units.isEmpty()) {
			return "resident:/member/register";
		}
		
		 String selectedBldgId = bldgIdParam;
	        if(selectedBldgId == null || selectedBldgId.isBlank()) {
	        	selectedBldgId = units.stream()
	        		.min(Comparator.comparing(UnitResidentVO::getMoveInDt))
	        		.map(UnitResidentVO::getBldgId)
	        		.orElse(units.get(0).getBldgId());
	        }
	        simpleSearch.setBldgId(selectedBldgId);
	        log.info("📌 simpleSearch.bldgId (after): {}", simpleSearch.getBldgId());
	        log.info("▶ Search: bldgId={}, brdCode={}, noticeType={}, keyword={}",
	        		  simpleSearch.getBldgId(),
	        		  simpleSearch.getBrdCode(),
	        		  simpleSearch.getNoticeType(),
	        		  simpleSearch.getSearchWord()
	        		);
	        
	        
	        
	    model.addAttribute("units", units);    
		model.addAttribute("bldgIdParam",bldgIdParam);
		model.addAttribute("selectedBldgId", selectedBldgId);
		
		return "resident/payment/Payment";
	}
}
