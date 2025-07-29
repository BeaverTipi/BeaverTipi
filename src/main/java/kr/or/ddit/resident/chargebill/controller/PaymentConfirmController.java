package kr.or.ddit.resident.chargebill.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.resident.chargebill.dto.PaymentConfirmRequest;
import kr.or.ddit.resident.chargebill.service.PaymentService;
import kr.or.ddit.resident.unitResident.service.UnitResidentService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;

@RestController
@RequestMapping("/ajax/payment")
public class PaymentConfirmController {

	@Autowired
    private PaymentService paymentService;

	@Autowired
    private UnitResidentService unitResidentService;
    
    @PostMapping("/confirm")
    public ResponseEntity<?> confirmPayment(@RequestBody PaymentConfirmRequest dto,
                                            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal) {
        try {
            // 현재 로그인 사용자 정보
            MemberVO user = principal.getRealUser();
            
            if (!unitResidentService.isMyUnit(user.getMbrCd(), dto.getUnitId())) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN)
                                     .body("❌ 납부 권한이 없는 유닛입니다.");
            }
            
            return ResponseEntity.ok(Map.of("message","✅ 납부 완료"));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("❌ 납부 실패: " + e.getMessage());
        }
        
    }
}

