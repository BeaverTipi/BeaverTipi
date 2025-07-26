package kr.or.ddit.broker.controller;

import java.util.Map;

import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerDashBoardService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/rest/broker/myoffice/dashboard")
@RequiredArgsConstructor
public class RestBrokerDashBoardController {
    private final BrokerDashBoardService dashboardService;
    private final BrokerAuthUnpackingService authUnpack;

    @PostMapping("/overview")
    public ResponseEntity<Map<String, String>> getDashboardOverviewEncrypted(
        Principal principal,
        @RequestBody Map<String, String> payload
    ) {
        // 1. 복호화
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        
        // 2. 인증 정보로부터 회원 코드 추출
        String mbrId = principal.getName(); // Spring Security Principal
        String mbrCd = authUnpack.getMbrCd(mbrId);

        // 3. 대시보드 데이터 구성
        Map<String, Object> result = dashboardService.readDashboardOverview(mbrCd);

        // 4. 암호화 후 응답
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(result));
    }
}
