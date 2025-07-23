package kr.or.ddit.main.member.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class MemberReadController {
	private final MemberService service;
    private final SubscribeSubsriptionService subService;

	@GetMapping("/account/read")
	public String mypage(Model model, Authentication auth, /*^0^*/Principal restAuth) {
		Object principal = null; //^0^
		String username = null; //^0^
		if(auth!=null) {principal = auth.getPrincipal(); username = auth.getName();} //^0^
		else {principal = restAuth; username = restAuth.getName();} //^0^
//		Object principal = auth.getPrincipal();
//		String username = auth.getName();
		String logInfo = "LOCAL";

		MemberVO member = service.readMemberByAll(username);
		List<SolutionSubscriptionVO> solSub = subService.checkedSolutionSubscriptionList(member.getMbrCd());

		boolean showUpdateCancelBtnTenancy = false;
		boolean showPaymentBtnTenancy = false;
		boolean showUpdateCancelBtnBroker = false;
		boolean showPaymentBtnBroker = false;

		if (solSub != null && !solSub.isEmpty()) {
			for (SolutionSubscriptionVO sub : solSub) {
				if ("001".equals(sub.getSubsStatus()) && "001".equals(sub.getSolution().getSolCcCd())) {
					showUpdateCancelBtnTenancy = true;
				}
				if ("001".equals(sub.getSubsStatus()) && "002".equals(sub.getSolution().getSolCcCd())) {
					showUpdateCancelBtnBroker = true;
				}
			}

			if (!showUpdateCancelBtnTenancy) {
				for (SolutionSubscriptionVO sub : solSub) {
					if (!"001".equals(sub.getSubsStatus()) && "001".equals(sub.getSolution().getSolCcCd())) {
						showPaymentBtnTenancy = true;
						break;
					}
				}
			}

			if (!showUpdateCancelBtnBroker) {
				for (SolutionSubscriptionVO sub : solSub) {
					if (!"001".equals(sub.getSubsStatus()) && "002".equals(sub.getSolution().getSolCcCd())) {
						showPaymentBtnBroker = true;
						break;
					}
				}
			}
		} else {
			showPaymentBtnTenancy = member.getTenancy() != null && "Y".equals(member.getTenancy().getAuthApprYn());
			showPaymentBtnBroker = member.getBroker() != null && "Y".equals(member.getBroker().getAuthApprYn());
		}

		if (solSub != null) {
			model.addAttribute("solutionSubscriptionList", solSub);
		}

		if (principal instanceof OAuth2User) {
			logInfo = "KAKAO";
		} else if (principal instanceof OidcUser) {
			logInfo = "GOOGLE";
		}

		model.addAttribute("member", member);
		model.addAttribute("logInfo", logInfo);
		model.addAttribute("showUpdateCancelBtnTenancy", showUpdateCancelBtnTenancy);
		model.addAttribute("showPaymentBtnTenancy", showPaymentBtnTenancy);
		model.addAttribute("showUpdateCancelBtnBroker", showUpdateCancelBtnBroker);
		model.addAttribute("showPaymentBtnBroker", showPaymentBtnBroker);

		return "main/member/memberPage";
	}
}
