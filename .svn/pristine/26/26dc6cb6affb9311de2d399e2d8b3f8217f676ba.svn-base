package kr.or.ddit.main.subscribe.controller;

import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import kr.or.ddit.admin.code.service.CommonCodeService;
import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.util.validate.BrokerInsertGroup;
import kr.or.ddit.util.validate.InsertGroup;
import kr.or.ddit.util.validate.TenancyInsertGroup;
import kr.or.ddit.util.validate.exception.FileIOException;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.TenancyVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequiredArgsConstructor
public class SubscribeSubsriptionController {
	private final SubscribeSubsriptionService service;
	private final CommonCodeService commonService;
	private final String successURL = "http://localhost/account/read?success=true";
	private final String TENANCY = "tenancy";
	private final String BROKER = "broker";

	@ModelAttribute(TENANCY)
	public TenancyVO tenancy() {
		return new TenancyVO();
	}

	@ModelAttribute(BROKER)
	public BrokerVO broker() {
		return new BrokerVO();
	}

	@GetMapping("/subscribe/subscription")
	public String formUI(Model model, @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		String username = principal.getRealUser().getMbrCd();
		SolutionSubscriptionVO solutionSubscription = service.readSolutionSubscription(username);
		List<SolutionVO> solutionList = service.readSolutionList();
		List<CommonCodeVO> commonCodeList = commonService.readCommonCodeList("PAY");

		model.addAttribute("solutionSubscription", solutionSubscription);
		model.addAttribute("solutionList", solutionList);
		model.addAttribute("commonCodeList", commonCodeList);
		return "main/subscribe/subscribe";
	}

	@GetMapping("/apply/broker")
	public String brokerForm(Model model) {
		List<SolutionVO> solutionList = service.readCommonCodeSolutionList("002");

		model.addAttribute("solutionList", solutionList);
		return "main/subscribe/brokerForm";
	}

	@PostMapping("/apply/broker")
	public String brokerFormProcess(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
	                                @RequestParam(name = "solution", required = false) String solutionId,
	                                @Validated(BrokerInsertGroup.class) @ModelAttribute(BROKER) BrokerVO broker,
	                                BindingResult errors,
	                                RedirectAttributes redirectAttributes) {
	    String lvn = "redirect:/apply/broker";

	    if (!errors.hasErrors()) {
	        try {
	            String mbrCd = principal.getRealUser().getMbrCd();
	            SolutionSubscriptionVO sol = new SolutionSubscriptionVO();
	            sol.setSolId(solutionId);
	            sol.setMbrCd(mbrCd);
	            SolutionVO solution = new SolutionVO();
	            solution.setSolCcCd("002");
	            sol.setSolution(solution);
	            service.createSolutionSubscription(sol);

	            broker.setMbrCd(mbrCd);
	            service.createBroker(broker);

	            lvn = "redirect:/account/read";

	        } catch (Exception e) {
	            log.error("▶▶ 예외 발생: ", e);
	            redirectAttributes.addFlashAttribute("message", e.getMessage());
	            redirectAttributes.addFlashAttribute("broker", broker);
	            redirectAttributes.addFlashAttribute("solutionId", solutionId);
	        }
	    } else {
	        String errorName = BindingResult.MODEL_KEY_PREFIX + "broker";
	        redirectAttributes.addFlashAttribute("broker", broker);
	        redirectAttributes.addFlashAttribute("solutionId", solutionId);
	        redirectAttributes.addFlashAttribute(errorName, errors);
	    }

	    return lvn;
	}

	@GetMapping("/apply/tenancy")
	public String tenancyForm(Model model) {
		List<SolutionVO> solutionList = service.readCommonCodeSolutionList("001");
		
		model.addAttribute("solutionList", solutionList);
		return "main/subscribe/tenancyForm";
	}

	@PostMapping("/apply/tenancy")
	public String tenancyFormProcess(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
			@RequestParam(name = "solution", required = false) String solutionId,
			@Validated(TenancyInsertGroup.class) @ModelAttribute(TENANCY) TenancyVO tenancy, BindingResult errors,
			RedirectAttributes redirectAttributes) {
		String lvn = "redirect:/apply/tenancy";
		// 검증 통과
		if (!errors.hasErrors()) {
			try {
				String mbrCd = principal.getRealUser().getMbrCd();
				
				SolutionSubscriptionVO sol = new SolutionSubscriptionVO();
				sol.setSolId(solutionId);
				sol.setMbrCd(mbrCd);
				service.createSolutionSubscription(sol);
				
				tenancy.setMbrCd(mbrCd);
				service.createTenancy(tenancy);
				// 수정 성공 후? 새 mypage로 이동
				lvn = "redirect:/account/read";
			} catch (FileIOException e) {
				// 인증 실패? 수정양식으로 redirect, 비번오류 메시지, 기존 입력 데이터
				redirectAttributes.addFlashAttribute("message", e.getMessage());
				redirectAttributes.addFlashAttribute(TENANCY, tenancy);
				redirectAttributes.addFlashAttribute("solutionId", solutionId);
			}
		} else {
			// 검증 실패? 수정양식으로 redirect, 검증 에러 메시지, 기존 입력데이터
			String errorName = BindingResult.MODEL_KEY_PREFIX + TENANCY;
			redirectAttributes.addFlashAttribute(TENANCY, tenancy);
			redirectAttributes.addFlashAttribute("solutionId", solutionId);
			redirectAttributes.addFlashAttribute(errorName, errors);
		}
		return lvn;
	}

	@PostMapping("/ajax/payment/ready")
	@ResponseBody
	public PaymentTosspamentsRawVO formProcess(@RequestBody SolutionVO solution,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		SolutionVO sol = service.readSolution(solution.getSolId());
		String orderId = "ORD" + System.currentTimeMillis() + principal.getRealUser().getMbrCd();
		PaymentTosspamentsRawVO toss = new PaymentTosspamentsRawVO();
		toss.setOrderId(orderId);
		toss.setAmount(sol.getSolPrice());
		toss.setOrderName(sol.getSolName());
		toss.setCustomerName(principal.getRealUser().getMbrNm());
		toss.setSuccessUrl(successURL);
		return toss;
	}
}
