package kr.or.ddit.resident.chargebill.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import lombok.extern.slf4j.Slf4j;
@RestController
@Slf4j
public class PaymentController {
	
	private final String successURL = "http://localhost/resident/payment";
	
	@PostMapping("/ajax/payment/resident")
	public PaymentTosspamentsRawVO formProcess(
			@RequestBody PaymentTosspamentsRawVO rawVO,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
		
		String orderId = "ORD" + System.currentTimeMillis() + principal.getRealUser().getMbrCd();
		rawVO.setOrderId(orderId);
		rawVO.setCustomerName(principal.getRealUser().getMbrNm());
		rawVO.setSuccessUrl(successURL);
		return rawVO;
	}
    
}
