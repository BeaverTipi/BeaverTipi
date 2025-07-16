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
import org.springframework.web.servlet.view.RedirectView;

import kr.or.ddit.main.subscribe.service.SubscribeSubsriptionService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.CardVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import kr.or.ddit.vo.RoleAchievedVO;
import kr.or.ddit.vo.SolutionSubscriptionPaymentVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.SolutionnSubscriptionAutopayMethodVO;
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

    private final String billingSuccessURL = "/ajax/toss/billing-success";
    private final String successURL = "http://localhost/ajax/toss/payment-success";

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

        String base64Credentials = Base64.getEncoder().encodeToString((secretKey + ":").getBytes());
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.add("Authorization", "Basic " + base64Credentials);

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
        String url = "https://api.tosspayments.com/v1/billing/authorizations/" + cardVO.getBillingKey();
        return restTemplate.exchange(url, HttpMethod.POST, request, String.class);
    }

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

    @PostMapping("/billing-ready")
    public PaymentTosspamentsRawVO prepareBillingRegistration(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
                                                              @RequestBody SolutionVO solution) {
        MemberVO member = principal.getRealUser();
        String customerKey = "CK-" + member.getMbrCd();
        SolutionVO sol = service.readSolution(solution.getSolId());
        String orderId = "ORD" + System.currentTimeMillis() + principal.getRealUser().getMbrCd();

        PaymentTosspamentsRawVO vo = new PaymentTosspamentsRawVO();
        vo.setOrderId(orderId);
        vo.setClientKey(clientKey);
        vo.setCustomerKey(customerKey);
        vo.setOrderName(sol.getSolName());
        vo.setAmount(sol.getSolPrice());
        vo.setSuccessUrl(billingSuccessURL);

        return vo;
    }

    @PostMapping("/billing-success")
    public ResponseEntity<?> handleTossSuccess(@RequestBody Map<String, String> payload,
                                               @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        try {
            String mbrCd = principal.getRealUser().getMbrCd();
            String billingKey = payload.get("billingKey");
            String approvedAtRaw = payload.get("approvedAt");
            
            String customerKey = payload.get("customerKey");
            String role = payload.get("role");
            OffsetDateTime offsetDateTime = OffsetDateTime.parse(approvedAtRaw);
            LocalDate approvedAt = offsetDateTime.toLocalDate();

            // 자동결제 수단 정보
            SolutionnSubscriptionAutopayMethodVO methodVO = new SolutionnSubscriptionAutopayMethodVO();
            methodVO.setMbrCd(mbrCd);
            methodVO.setAutomethBillingkey(billingKey);
            methodVO.setAutomethProvider("toss");
            methodVO.setAutomethPaytype("001");
            methodVO.setAutomethIsActive("Y");
            methodVO.setAutomethPaytypeGrpCd("PAYTP");
            methodVO.setAutomethIsActiveGrpCd("YNFG");
            methodVO.setAutomethStartedAt(approvedAt);

            // 결제 정보
            SolutionSubscriptionPaymentVO paymentVO = new SolutionSubscriptionPaymentVO();
            paymentVO.setBillingKey(billingKey);
            paymentVO.setCustomerKey(customerKey);
            paymentVO.setMbrCd(mbrCd);
            RoleAchievedVO roleAchievedVO = new RoleAchievedVO();
            roleAchievedVO.setMbrCd(mbrCd);
            roleAchievedVO.setUserRoleId(role);

            service.saveAutopayAndFirstPayment(methodVO, paymentVO, roleAchievedVO);

            return ResponseEntity.ok(Map.of("redirectUrl", "/account/read?success=true"));

        } catch (Exception e) {
            log.error("billing-success 처리 오류", e);
            return ResponseEntity.status(500).body(Map.of("redirectUrl", "/account/read?fail=true"));
        }
    }




    @GetMapping("/payment-success")
    public RedirectView handleNormalPaymentSuccess(@RequestParam String paymentKey,
                                                   @RequestParam String orderId,
                                                   @RequestParam int amount,
                                                   @RequestParam("role") String role,
                                                   @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
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
                String method = (String) data.get("method");
                String approvedAtRaw = (String) data.get("approvedAt");
                LocalDate approvedAt = LocalDate.parse(approvedAtRaw.substring(0, 10));

                Map<String, Object> card = (Map<String, Object>) data.get("card");
                String cardNumber = (String) card.get("number");
                String cardCompany = (String) card.get("company");

                SolutionSubscriptionPaymentVO paymentVO = new SolutionSubscriptionPaymentVO();
                paymentVO.setSubsId("SUBS001");
                paymentVO.setCustomerKey("CUST-" + principal.getRealUser().getMbrCd());
                paymentVO.setMbrCd(principal.getRealUser().getMbrCd());
                RoleAchievedVO roleAchievedVO = new RoleAchievedVO();
                roleAchievedVO.setMbrCd(principal.getRealUser().getMbrCd());
                roleAchievedVO.setUserRoleId(role);

                service.savePaymentResult(paymentVO, roleAchievedVO);

                // 리다이렉트 URL 설정
                String redirectUrl = "/account/read?success=true";  // 성공 리다이렉트 URL 설정
                return new RedirectView(redirectUrl);  // 리다이렉트 처리
            } else {
                // 실패 시 리다이렉트 처리
                String redirectUrl = "/account/read?fail=true";
                return new RedirectView(redirectUrl);
            }
        } catch (Exception e) {
            log.error("일반결제 처리 중 오류", e);
            // 예외 처리 후 리다이렉트
            String redirectUrl = "/account/read?fail=true";
            return new RedirectView(redirectUrl);
        }
    }

}
