package kr.or.ddit.resident.chargebill.controller;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import kr.or.ddit.resident.mapper.ChargeBillMapper;
import kr.or.ddit.vo.PaymentTosspamentsRawVO;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/payment")
@Slf4j
public class TossPaymentTestController {

    @Autowired
    private ChargeBillMapper chargeBillMapper;

    @PostMapping("/toss/insert")
    public String testInsertTossPayment(@RequestBody PaymentTosspamentsRawVO vo) {
        log.info("🔔 테스트 insert 요청: {}", vo);
        int result = chargeBillMapper.insertTossPaymentInfo(vo);
        return result > 0 ? "✅ TossPayments 저장 성공" : "❌ 저장 실패";
    }
}
