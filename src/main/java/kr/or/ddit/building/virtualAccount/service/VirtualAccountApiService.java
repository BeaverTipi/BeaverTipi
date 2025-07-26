package kr.or.ddit.building.virtualAccount.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.or.ddit.vo.VirtualAccountVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class VirtualAccountApiService {
    @Value("${tosspayments.secret-key}")
    private String secretKey;
    private final RestTemplate restTemplate = new RestTemplate();

    public VirtualAccountVO issueVirtualAccount(VirtualAccountVO param) throws Exception {
        log.info("★★★ 실제 tossSecretKey = {}", secretKey);

        // 은행코드 null/공백 체크 (없으면 명확히 Exception)
        if(param.getBankCode() == null || param.getBankCode().trim().isEmpty()) {
            throw new RuntimeException("은행코드가 누락되었습니다. 올바른 은행코드를 선택하세요.");
        }

        String url = "https://api.tosspayments.com/v1/virtual-accounts";
        ObjectMapper om = new ObjectMapper();
        
        JsonNode body = om.createObjectNode()
        		.put("amount", param.getVirtualAccountAmount() != null ? param.getVirtualAccountAmount() : 10000)
                .put("orderId", "test-order-" + System.currentTimeMillis())
                .put("orderName", "테스트 월세")
                .put("customerName", param.getCustomerName())
                .put("bank", param.getBankCode()) 
                .put("validHours", 24);
        log.info("은행코드={}", param.getBankCode());
        String auth = secretKey + ":";
        String encodedAuth = java.util.Base64.getEncoder().encodeToString(auth.getBytes());

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.set("Authorization", "Basic " + encodedAuth);

        HttpEntity<String> req = new HttpEntity<>(body.toString(), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(url, req, String.class);

        if (response.getStatusCode().is2xxSuccessful()) {
//            JsonNode root = om.readTree(response.getBody());
        	// 응답에서 한글 깨질 때!
        	String rawBody = response.getBody();		// 여기서부터
        	String utf8Body = new String(rawBody.getBytes("ISO-8859-1"), "UTF-8");
        	JsonNode root = om.readTree(utf8Body);		//여기까지가 임시로 UTF-8 인코딩 해버리는 코드얌.
        												// 이 세줄을 주석처리하고 그 위에 있는걸 주석해제하면
        												// 강제 인코딩은 사라진단다.

            
            JsonNode va = root.get("virtualAccount");
            log.info("이름 잘나와? api 서비스임 {}", va.get("customerName").asText());
            VirtualAccountVO vo = new VirtualAccountVO();
            vo.setVirtualAccountId(root.has("orderId") ? root.get("orderId").asText() : "");
            vo.setAccountType(param.getAccountType());
            vo.setVirtualAccountAmount(param.getVirtualAccountAmount());
            vo.setAccountNumber(va.get("accountNumber").asText());
            vo.setBankCode(va.get("bankCode").asText());
            vo.setCustomerName(va.get("customerName").asText());
            vo.setDueDate(va.get("dueDate").asText().substring(0,10));
            vo.setExpired(va.get("expired").asBoolean() ? "Y" : "N");
            vo.setSettlementStatus("READY");
            vo.setSecret(root.has("secret") ? root.get("secret").asText() : "");
            vo.setMbrCd(param.getMbrCd());
            
            log.info("DB 저장 전 customerName={}", vo.getCustomerName());

            
            
            return vo;
        } else {
            throw new RuntimeException("토스 테스트 API 호출 실패: " + response.getBody());
        }
    }
}
