package kr.or.ddit.resident.chargebill.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import kr.or.ddit.resident.chargebill.service.PaymentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/resident/payment/success")
public class PaymentSuccessController {

    @Autowired
    private PaymentService paymentService;

    @GetMapping
    public String updateAfterPayment(
    		@AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        MemberVO member = principal.getRealUser();
        String mbrCd = member.getMbrCd();
        
        
        log.info("성공 URL 타고 들어옴 ^0^");
        log.info("✅ Toss API 응답 결과: {}");
        // ✅ 실제 납부 처리
        paymentService.payChargeBill(mbrCd);

        return "redirect:/resident/payment?success=true";
    }
}