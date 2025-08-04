package kr.or.ddit.broker.controller;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerDashBoardService;
import kr.or.ddit.util.security.auth.RealUserWrapper;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/rest/broker/myoffice/dashboard")
@RequiredArgsConstructor
@Slf4j
public class RestBrokerDashBoardController {
    private final BrokerDashBoardService dashboardService;
    private final BrokerAuthUnpackingService authUnpack;

    @PostMapping("/overview")
    public ResponseEntity<Map<String, String>> getDashboardOverviewEncrypted(
            @AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
            @RequestBody Map<String, String> payload) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        String mbrCd = principal.getRealUser().getMbrCd();
        String period = decrypted.getOrDefault("period", "month");

        log.info("mbrCd : {}, period : {}, mbrId : {}, principal : {}", mbrCd, period, principal);

        validatePeriod(period);
        Map<String, Object> result = dashboardService.readDashboardOverview(mbrCd, period);
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(result));
    }

    @PostMapping("/commission-total")
    public ResponseEntity<?> getCommissionTotal(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal, @RequestBody Map<String, String> payload) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        String mbrCd = principal.getRealUser().getMbrCd();
        String period = decrypted.getOrDefault("period", "month");

        validatePeriod(period);
        Long total = dashboardService.readCommissionTotal(mbrCd, period);
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(Map.of("total", total)));
    }

    @PostMapping("/commission-trend")
    public ResponseEntity<?> getCommissionTrend(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal, @RequestBody Map<String, String> payload) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        String mbrCd = principal.getRealUser().getMbrCd();
        String period = decrypted.getOrDefault("period", "month");

        validatePeriod(period);
        List<Map<String, Object>> trend = dashboardService.readCommissionTrend(mbrCd, period);
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(trend));
    }

    @PostMapping("/contract-summary")
    public ResponseEntity<?> getContractSummary(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal, @RequestBody Map<String, String> payload) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        String mbrCd = principal.getRealUser().getMbrCd();
        String period = decrypted.getOrDefault("period", "month");

        validatePeriod(period);
        Map<String, Object> summary = dashboardService.readContractStatusSummary(mbrCd, period);
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(Map.of("series", summary)));
    }

    @PostMapping("/contract-trend")
    public ResponseEntity<?> getContractTrend(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal, @RequestBody Map<String, String> payload) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        String mbrCd = principal.getRealUser().getMbrCd();
        String period = decrypted.getOrDefault("period", "month");

        validatePeriod(period);
        Map<String, Object> trend = dashboardService.readContractTrend(mbrCd, period);
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(trend));
    }
    @PostMapping("/listing-stats")
    public ResponseEntity<?> getListingStats(@AuthenticationPrincipal RealUserWrapper<MemberVO> principal,
                                             @RequestBody Map<String, String> payload) {
        Map<String, String> decrypted = BrokerCryptUtil.decryptRequestPayload(payload);
        String mbrCd = principal.getRealUser().getMbrCd();
        String period = decrypted.getOrDefault("period", "month");
        
        validatePeriod(period);

        Map<String, Object> stats = dashboardService.readListingStats(mbrCd, period);
        return ResponseEntity.ok(BrokerCryptUtil.encryptResponsePayload(stats));
    }

    private void validatePeriod(String period) {
        List<String> validPeriods = List.of("today","week", "month", "quarter", "year");
        if (!validPeriods.contains(period)) {
            throw new IllegalArgumentException("Invalid period value: " + period);
        }
    }
}