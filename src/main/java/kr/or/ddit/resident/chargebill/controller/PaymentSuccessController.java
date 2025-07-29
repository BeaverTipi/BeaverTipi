package kr.or.ddit.resident.chargebill.controller;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.view.RedirectView;

import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.resident.chargebill.service.PaymentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.SolutionVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/ajax/resident/payment")
public class PaymentSuccessController {
	
    @Autowired
    private PaymentService paymentService;
    
    @Value("${tosspayments.secret-key}")
    private String secretKey;

    @Value("${tosspayments.client-key}")
    private String clientKey;

    private final RestTemplate restTemplate = new RestTemplate();
    
    private static final String PAYMENT_SUCCESS_URL = "http://localhost/ajax/resident/payment/success";
    
    @PostMapping("/ready")
    public PaymentTosspamentsRawVO formProcess(@RequestBody PaymentTosspamentsRawVO paymentTosspamentsRawVO,
    			
                                               @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
    	
        String orderId = "ORD" + System.currentTimeMillis() + principal.getRealUser().getMbrCd();
        String customerKey = "CK-" + principal.getRealUser().getMbrCd();
        
        paymentTosspamentsRawVO.setOrderId(orderId);
        paymentTosspamentsRawVO.setCustomerName(principal.getRealUser().getMbrNm());
        paymentTosspamentsRawVO.setSuccessUrl(PAYMENT_SUCCESS_URL);
        paymentTosspamentsRawVO.setClientKey(clientKey);
        paymentTosspamentsRawVO.setCustomerKey(customerKey);
        return paymentTosspamentsRawVO;
    }
    
    
    @GetMapping("/success")
    public RedirectView updateAfterPayment(
    		  @RequestParam String paymentKey,
    		  @RequestParam String orderId,
    		  @RequestParam int amount,
    		  @RequestParam("current") String current,
    		  @RequestParam("unitId")String unitId,
    		  @RequestParam("chgbillChargeMonth")String chgbillChargeMonth,
    		  @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO member = principal.getRealUser();
        
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBasicAuth(secretKey, "");
            
            Map<String, Object> payload = new HashMap<>();
            payload.put("paymentKey", paymentKey);
            payload.put("orderId", orderId);
            payload.put("amount", amount);

            HttpEntity<Map<String, Object>> request = new HttpEntity<>(payload, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/payments/confirm",
                HttpMethod.POST,
                request,
                Map.class
            );
            
            if (response.getStatusCode().is2xxSuccessful()) {
                Map<String, Object> data = response.getBody();
                String approvedAtRaw = (String) data.get("approvedAt");
                LocalDate approvedAt = LocalDate.parse(approvedAtRaw.substring(0, 10));
                String mbrCd = principal.getRealUser().getMbrCd();
                
                Map<String, Object> cardMap = (Map<String, Object>) data.get("card");
                Map<String, Object> easyPayMap = (Map<String, Object>) data.get("easyPay");
                Map<String, Object> vaMap = (Map<String, Object>) data.get("virtualAccount");
                
                paymentService.confirmAndPayFromToss(data, approvedAtRaw, approvedAt, mbrCd, cardMap, easyPayMap, vaMap, unitId, chgbillChargeMonth);
                
                log.info("성공 URL 타고 들어옴 ^0^");
                log.info("✅ Toss API 응답 결과: {}");
                return new RedirectView("/resident/dataState/bill");
                
            } else {
                return new RedirectView(current+"?fail=true");
            }
        } catch (Exception e) {
            log.error("일반결제 처리 중 오류", e);
            return new RedirectView(current+"?fail=true");
        

    }
}
}