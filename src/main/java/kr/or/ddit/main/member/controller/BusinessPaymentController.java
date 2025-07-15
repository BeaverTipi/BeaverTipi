package kr.or.ddit.main.member.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class BusinessPaymentController {
	private final SubscribeSubsriptionService service;
	private final CommonCodeService commonService;
	
	
	@GetMapping("/payment/business/broker")
	public String brokerUI(Model model,@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		List<SolutionVO> solList = service.readCommonCodeSolutionList("002");
		MemberVO member = principal.getRealUser();
		SolutionSubscriptionVO solutionSubscription = service.checkedSolutionSubscription(member.getMbrCd(),"002");
		CommonCodeVO common = new CommonCodeVO();
		common.setCodeGroup("PAY");
		common.setParentCodeGroup("RCPAY");
		common.setParentCodeValue("Y");
		List<CommonCodeVO> commonCodeRcPayList =  commonService.readCommonCodeList(common);
		common.setParentCodeValue("N");
		List<CommonCodeVO> commonCodePayList =  commonService.readCommonCodeList(common);
		model.addAttribute("member", member);
		model.addAttribute("solutionList", solList);
		model.addAttribute("solutionSubscription", solutionSubscription);
		model.addAttribute("commonCodeRcPayList", commonCodeRcPayList);
		model.addAttribute("commonCodePayList", commonCodePayList);
		
		// 자동 결제 카드 값 미리 넣어두기^0^
	    CardVO card = new CardVO();
	    card.setCardNumber("4455123456789010");
	    card.setCardExpirationYear("26");
	    card.setCardExpirationMonth("07");
	    card.setCardPassword("12");
	    card.setCustomerIdentityNumber("900101"); 
		model.addAttribute("billingCard", card);
		return "main/subscribe/payments";
	}
	@GetMapping("/payment/business/tenancy")
	public String tenancyUI(Model model,@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		List<SolutionVO> solList = service.readCommonCodeSolutionList("001");
		MemberVO member = principal.getRealUser();
		SolutionSubscriptionVO solutionSubscription = service.checkedSolutionSubscription(member.getMbrCd(),"001");
		model.addAttribute("solutionList", solList);
		model.addAttribute("member", member);
		model.addAttribute("solutionSubscription", solutionSubscription);
		model.addAttribute("card", new CardVO());
		return "main/subscribe/payments";
	}
}
