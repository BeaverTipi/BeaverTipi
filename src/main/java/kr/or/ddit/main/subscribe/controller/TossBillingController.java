package kr.or.ddit.main.subscribe.controller;

import java.time.LocalDate;
import java.time.OffsetDateTime;
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
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.EasyPayVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.RoleAchievedVO;
import kr.or.ddit.vo.SolutionSubscriptionPaymentVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.SolutionnSubscriptionAutopayMethodVO;
import kr.or.ddit.vo.VirtualAccountVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/ajax/toss")
@RequiredArgsConstructor
@Slf4j
public class TossBillingController {

    private final SubscribeSubsriptionService service;

    @Value("${tosspayments.secret-key}")
    private String secretKey;

    @Value("${tosspayments.client-key}")
    private String clientKey;

    private final RestTemplate restTemplate = new RestTemplate();

    private static final String BILLING_SUCCESS_URL = "http://localhost/ajax/toss/billing-success";
    private static final String PAYMENT_SUCCESS_URL = "http://localhost/ajax/toss/payment-success";

    @PostMapping("/billing-key")
    public ResponseEntity<Map> issueBillingKey(@RequestBody CardVO cardVO,
                                               @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        Map<String, Object> cardRequest = new HashMap<>();
        cardRequest.put("cardNumber", cardVO.getCardNumber());
        cardRequest.put("cardExpirationYear", cardVO.getCardExpirationYear());
        cardRequest.put("cardExpirationMonth", cardVO.getCardExpirationMonth());
        cardRequest.put("cardPassword", cardVO.getCardPassword());
        cardRequest.put("customerIdentityNumber", cardVO.getCustomerIdentityNumber());
        String customerKey = "CK-" + principal.getRealUser().getMbrCd();
        cardRequest.put("customerKey", customerKey);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(cardRequest, headers);
        return restTemplate.exchange(
            "https://api.tosspayments.com/v1/billing/authorizations/card",
            HttpMethod.POST,
            request,
            Map.class
        );
    }

    @PostMapping("/billing")
    public ResponseEntity<String> doRecurringPayment(@RequestBody CardVO cardVO,
                                                     @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        Map<String, Object> billingRequest = new HashMap<>();
        billingRequest.put("customerKey", "CK-" + principal.getRealUser().getMbrCd());
        billingRequest.put("billingKey", cardVO.getBillingKey());
        billingRequest.put("amount", cardVO.getAmount());
        billingRequest.put("orderId", "ORDER-" + System.currentTimeMillis());
        billingRequest.put("orderName", "정기결제");
        billingRequest.put("customerEmail", principal.getRealUser().getMbrEmlAddr());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBasicAuth(secretKey, "");

        HttpEntity<Map<String, Object>> request = new HttpEntity<>(billingRequest, headers);
        String url = "https://api.tosspayments.com/v1/billing/" + cardVO.getBillingKey();
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

    @PostMapping("/ready")
    public PaymentTosspamentsRawVO formProcess(@RequestBody SolutionVO solution,
                                               @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        SolutionVO sol = service.readSolution(solution.getSolId());
        String orderId = "ORD" + System.currentTimeMillis() + principal.getRealUser().getMbrCd();
        String customerKey = "CK-" + principal.getRealUser().getMbrCd();
        
        PaymentTosspamentsRawVO toss = new PaymentTosspamentsRawVO();
        toss.setOrderId(orderId);
        toss.setAmount(sol.getSolPrice());
        toss.setOrderName(sol.getSolName());
        toss.setCustomerName(principal.getRealUser().getMbrNm());
        toss.setSuccessUrl(PAYMENT_SUCCESS_URL);
        toss.setClientKey(clientKey);
        toss.setCustomerKey(customerKey);
        return toss;
    }

    @PostMapping("/billing-ready")
    public PaymentTosspamentsRawVO prepareBillingRegistration(
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @RequestBody SolutionVO solution) {

        MemberVO member = principal.getRealUser();
        String customerKey = "CK-" + member.getMbrCd();

        if (solution == null || solution.getSolId() == null) {
            throw new IllegalArgumentException("solId가 없습니다.");
        }

        SolutionVO sol = service.readSolution(solution.getSolId());
        if (sol == null) {
            throw new IllegalArgumentException("해당 solId에 대한 솔루션 정보를 찾을 수 없습니다.");
        }

        String orderId = "ORD" + System.currentTimeMillis() + member.getMbrCd();

        PaymentTosspamentsRawVO vo = new PaymentTosspamentsRawVO();
        vo.setOrderId(orderId);
        vo.setClientKey(clientKey);
        vo.setCustomerKey(customerKey);
        vo.setOrderName(sol.getSolName());
        vo.setAmount(sol.getSolPrice());
        vo.setCustomerName(member.getMbrNm());
        vo.setSuccessUrl(BILLING_SUCCESS_URL);
        vo.setFailUrl("/fail"); // 필요 시
        vo.setCustomerEmail(member.getMbrEmlAddr());

        return vo;
    }
    @GetMapping("/billing-success")
    public RedirectView handleTossBillingSuccess(@RequestParam String customerKey,
                                                 @RequestParam String authKey,
                                                 @RequestParam("role") String role,
                                                 @RequestParam("solId") String solId,
                                                 @RequestParam("current") String current,
                                                 @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
                                                 RedirectAttributes redirectAttributes) {
        try {
            // 💡 Toss 인증 헤더 수동 처리
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            String encodedSecret = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
            headers.set("Authorization", "Basic " + encodedSecret);

            Map<String, String> requestBody = new HashMap<>();
            requestBody.put("customerKey", customerKey);
            requestBody.put("authKey", authKey);

            HttpEntity<Map<String, String>> request = new HttpEntity<>(requestBody, headers);
            ResponseEntity<Map> response = restTemplate.exchange(
                "https://api.tosspayments.com/v1/billing/authorizations/issue",
                HttpMethod.POST,
                request,
                Map.class
            );

            if (!response.getStatusCode().is2xxSuccessful()) {
                return new RedirectView(current +"?fail=true");
            }

            Map<String, Object> result = response.getBody();
            log.info("✅ Toss API 응답 결과: {}", result);
            String billingKey = (String) result.get("billingKey");
            String approvedAtRaw = (String) result.get("authenticatedAt");

            OffsetDateTime offsetDateTime = OffsetDateTime.parse(approvedAtRaw);
            LocalDate approvedAt = offsetDateTime.toLocalDate();

            // 2. 사용자 정보 추출
            String mbrCd = principal.getRealUser().getMbrCd();


            // 3. DB 저장 처리
            SolutionnSubscriptionAutopayMethodVO methodVO = new SolutionnSubscriptionAutopayMethodVO();
            methodVO.setMbrCd(mbrCd);
            methodVO.setAutomethBillingkey(billingKey);
            methodVO.setAutomethProvider("toss");
            methodVO.setAutomethPaytype("001");
            methodVO.setAutomethIsActive("Y");
            methodVO.setAutomethPaytypeGrpCd("PAYTP");
            methodVO.setAutomethIsActiveGrpCd("YNFG");
            methodVO.setAutomethStartedAt(approvedAt);

            SolutionSubscriptionPaymentVO paymentVO = new SolutionSubscriptionPaymentVO();
            paymentVO.setBillingKey(billingKey);
            paymentVO.setCustomerKey(customerKey);
            paymentVO.setMbrCd(mbrCd);

            RoleAchievedVO roleAchievedVO = new RoleAchievedVO();
            roleAchievedVO.setMbrCd(mbrCd);
            roleAchievedVO.setUserRoleId("ROLE_"+role);
            
            CardVO cardVO =new CardVO();
            Map<String,String> map = (Map<String,String>)result.get("card");
            cardVO.setIssuerCode(map.get("issuerCode").toString());
            cardVO.setAcquirerCode(map.get("acquirerCode").toString());
            cardVO.setCardNumber(map.get("number").toString());
            cardVO.setCardType(map.get("cardType").toString());
            cardVO.setOwnerType(map.get("ownerType").toString());
            cardVO.setMbrCd(mbrCd);
            
            SolutionSubscriptionVO subscriptionVO = new SolutionSubscriptionVO();
            subscriptionVO.setMbrCd(mbrCd);
            subscriptionVO.setSolId(solId);
            SolutionVO solutionVO = new SolutionVO();
            solutionVO.setSolCcCd("BROKER".equalsIgnoreCase(role) ? "002" : "001");
            subscriptionVO.setSolution(solutionVO);

            service.saveAutopayAndFirstPayment(methodVO, paymentVO, roleAchievedVO, subscriptionVO, cardVO);
            redirectAttributes.addFlashAttribute("message", "결제에 성공했습니다.");
            return new RedirectView("/account/read?success=true");

        } catch (Exception e) {
            log.error("billing-success 처리 오류", e);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return new RedirectView(current+"?fail=true");
        }
    }



    @GetMapping("/payment-success")
    public RedirectView handleNormalPaymentSuccess(@RequestParam String paymentKey,
                                                   @RequestParam String orderId,
                                                   @RequestParam int amount,
                                                   @RequestParam("role") String role,
                                                   @RequestParam("solId") String solId,
                                                   @RequestParam("current") String current,
                                                   @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
                                                   RedirectAttributes redirectAttributes) {
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
           
                SolutionSubscriptionPaymentVO paymentVO = new SolutionSubscriptionPaymentVO();
                paymentVO.setCustomerKey("CUST-" + mbrCd);
                paymentVO.setMbrCd(mbrCd);

                RoleAchievedVO roleAchievedVO = new RoleAchievedVO();
                roleAchievedVO.setMbrCd(mbrCd);
                roleAchievedVO.setUserRoleId("ROLE_" + role);

                SolutionSubscriptionVO subscriptionVO = new SolutionSubscriptionVO();
                subscriptionVO.setMbrCd(mbrCd);
                subscriptionVO.setSolId(solId);
                SolutionVO solutionVO = new SolutionVO();
                solutionVO.setSolCcCd("BROKER".equalsIgnoreCase(role) ? "002" : "001");
                subscriptionVO.setSolution(solutionVO);

                service.savePaymentResult(paymentVO, roleAchievedVO, subscriptionVO);
                
                Map<String, Object> cardMap = (Map<String, Object>) data.get("card");
                Map<String, Object> easyPayMap = (Map<String, Object>) data.get("easyPay");
                Map<String, Object> vaMap = (Map<String, Object>) data.get("virtualAccount");
                if( cardMap !=null && !cardMap.isEmpty() ) {
                	CardVO card = new CardVO();
                	card.setIssuerCode(cardMap.get("issuerCode").toString());
                	card.setAcquirerCode(cardMap.get("acquirerCode").toString());
                	card.setCardNumber(cardMap.get("number").toString());
                	card.setCardType(cardMap.get("cardType").toString());
                	card.setOwnerType(cardMap.get("ownerType").toString());
                	card.setMbrCd(mbrCd);
                	service.createCard(card);
                }
                if(easyPayMap != null && !easyPayMap.isEmpty()) {
                	EasyPayVO easyPay = new EasyPayVO();	
                	easyPay.setProvider(easyPayMap.get("provider").toString());
                	easyPay.setAmount((Integer)easyPayMap.get("amount"));
                	easyPay.setDiscountAmount((Integer)easyPayMap.get("discountAmount"));
                	easyPay.setMbrCd(mbrCd);
                	service.createEasyPay(easyPay);
                }
                if (vaMap != null && !vaMap.isEmpty()) {
                	VirtualAccountVO va = new VirtualAccountVO();
                	va.setAccountNumber((String) vaMap.get("accountNumber"));
                	va.setBankCode((String) vaMap.get("bankCode"));
                	va.setCustomerName((String) vaMap.get("customerName"));
                	va.setAccountType((String) vaMap.get("accountType"));
                	va.setDueDate((String) vaMap.get("dueDate"));
                	va.setExpired((String) vaMap.get("expired"));
                	va.setSettlementStatus((String) vaMap.get("settlementStatus"));
                	va.setSecret((String) vaMap.get("secret"));
                	va.setMbrCd(mbrCd);
                	service.createVirtualAccount(va);
                }
                redirectAttributes.addFlashAttribute("message", "결제에 성공했습니다.");
                return new RedirectView("/account/read?success=true");
            } else {
            	redirectAttributes.addFlashAttribute("message", "결제에 실패했습니다.");
                return new RedirectView(current+"?fail=true");
            }
        } catch (Exception e) {
            log.error("일반결제 처리 중 오류", e);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return new RedirectView(current+"?fail=true");
        }
    }
}
