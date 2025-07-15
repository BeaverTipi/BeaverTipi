package kr.or.ddit.main.subscribe.controller;

import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.SolutionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ajax/toss")
@RequiredArgsConstructor
@Slf4j
public class TossBillingController {
	private final SubscribeSubsriptionService service;
	private final String successURL = "http://localhost/account/read?success=true";

	@Value("${tosspayments.secret-key}")
	private String secretKey;

	@Value("${tosspayments.client-key}")
	private String clientKey;

	private final RestTemplate restTemplate = new RestTemplate();

	/**
	 * ✅ 1. 카드 등록 (billingKey 발급)
	 */
	@PostMapping("/billing-key")
	public ResponseEntity<Map> issueBillingKey(@RequestBody CardVO cardVO,
	        @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

	    Map<String, Object> cardRequest = new HashMap<>();
	    cardRequest.put("cardNumber", cardVO.getCardNumber());
	    cardRequest.put("cardExpirationYear", cardVO.getCardExpirationYear());
	    cardRequest.put("cardExpirationMonth", cardVO.getCardExpirationMonth());
	    cardRequest.put("cardPassword", cardVO.getCardPassword());
	    cardRequest.put("customerIdentityNumber", cardVO.getCustomerIdentityNumber());
	    String customerKey = "CUST-" + principal.getRealUser().getMbrCd();
	    cardRequest.put("customerKey", customerKey);
	    log.info("cardRequest ===> {}",cardRequest.toString());

	    // 🔐 인증 헤더 생성 (Base64 인코딩)
	    String plainCredentials = secretKey + ":";
	    String base64Credentials = Base64.getEncoder().encodeToString(plainCredentials.getBytes());
	    log.info("plainCredentials ===> {}",plainCredentials);
	    log.info("base64Credentials ===> {}",base64Credentials);
	    HttpHeaders headers = new HttpHeaders();
	    headers.setContentType(MediaType.APPLICATION_JSON);
	    headers.add("Authorization", "Basic " + base64Credentials);

	    HttpEntity<Map<String, Object>> request = new HttpEntity<>(cardRequest, headers);

	    ResponseEntity<Map> response = restTemplate.exchange(
	            "https://api.tosspayments.com/v1/billing/authorizations/card",
	            HttpMethod.POST,
	            request,
	            Map.class
	    );

	    return response;
	}


	/**
	 * ✅ 2. billingKey를 이용한 정기결제
	 */
	@PostMapping("/billing")
	public ResponseEntity<String> doRecurringPayment(@RequestBody CardVO cardVO,
			@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {

		Map<String, Object> billingRequest = new HashMap<>();
		billingRequest.put("customerKey", "CUST-" + principal.getRealUser().getMbrCd());
		billingRequest.put("billingKey", cardVO.getPaymentKey());
		billingRequest.put("amount", cardVO.getAmount());
		billingRequest.put("orderId", "ORDER-" + System.currentTimeMillis());
		billingRequest.put("orderName", "정기결제"); // or cardVO.getOrderName()
		billingRequest.put("customerEmail", principal.getRealUser().getMbrEmlAddr());

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBasicAuth(secretKey, "");

		HttpEntity<Map<String, Object>> request = new HttpEntity<>(billingRequest, headers);
		 String url = "https://api.tosspayments.com/v1/billing/authorizations/" + cardVO.getPaymentKey();
		ResponseEntity<String> response = restTemplate.exchange(
				url,
				HttpMethod.POST,
				request,
				String.class
		);

		return response;
	}

	/**
	 * ✅ 3. 일반 결제 준비 API (orderId 및 클라이언트키 포함)
	 */
	@PostMapping("/ready")
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
		toss.setClientKey(clientKey);

		return toss;
	}
}
