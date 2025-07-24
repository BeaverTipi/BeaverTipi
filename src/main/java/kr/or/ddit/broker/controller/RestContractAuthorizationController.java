package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.MemberVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * 도메인 레벨 인가
 * SPRING Security의 전역 권한(ROLE)과는 별도로,
 * 특정 계약에서의 개인별 접근 권한 체크.
 * @author 
 * @since
 * @see
 *
 *
 */
@Slf4j
@RestController
@RequestMapping("/rest/contract/authorize")
public class RestContractAuthorizationController {

	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private AES256Util aes256Util;
	@Autowired
	private BrokerContractService contService;
	@Autowired
	private MemberService memService;
	
	@PostMapping
	public ResponseEntity<?> contractSignPageAuthorize(
			@RequestBody Map<String, String> payload
			, @AuthenticationPrincipal Authentication auth
			, Principal principal
	) throws Exception {
		String resultJson= "";
		try {
			/** 1. 복호화 */
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (encrypted == null || iv == null) {
	        	resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "암호화된 요청 또는 IV 누락", "signYn", "N"));
	        	return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
	        }
	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
	        String method = String.valueOf(parsedRequest.get("_method"));
	        if (!"GET".equalsIgnoreCase(method)) {
	        	resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "지원하지 않는 요청 방식입니다.", "signYn", "N"));
	        	return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
	        }
	        
	        String contId = String.valueOf(parsedRequest.get("contId"));
	        log.debug("-----<> 계약 ID 출력:: {}", contId);
	        ContractVO contract =  contService.readContractInfo(contId);

			/** 2. 회원 로그인상태 확인 및 인가처리 밑작업 */
			if ((auth == null || !auth.isAuthenticated()) && principal == null)
			    return reject("로그인이 필요합니다.");		
	        String username = null; //^0^
			if(auth!=null) {username = auth.getName();} //^0^
			else {username = principal.getName();} //^0^
			
			MemberVO user = memService.readMember(username);
			String userTelno = user.getMbrTelno();
			log.debug("-----<><><> 접근한 회원의 전화번호:: {}", userTelno);
			String userRole = null;
			log.debug("-----<><><><><>  contLesseeTelNo:: {}", contract.getContLesseeTelno());
			log.debug("-----<><><><><>  contTenancyTelNo:: {}", contract.getContTenancyTelno());
			log.debug("-----<><><><><>  contBrokerTelNo:: {}", contract.getContBrokerTelno());
	        
	        if(userTelno.equals(contract.getContLesseeTelno())) userRole = "LESSEE";
	        else if(userTelno.equals(contract.getContTenancyTelno())) userRole = "LESSOR";
	        else if(userTelno.equals(contract.getContBrokerTelno())) userRole = "AGENT";
	        
	        if(userRole == null) return reject("접근 권한이 없습니다.");
	        String response = objectMapper.writeValueAsString(Map.of(
                "success", true,
                "signYn", "Y",
                "role", userRole,
                "contId", contId
            ));
            return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(response));
		}
		catch(Exception e) {log.error("[서명페이지 접근 인가 실패]", e); return reject("시스템 오류 발생");}
		
	}
	private ResponseEntity<?> reject(String message) throws Exception {
	    String errorJson = objectMapper.writeValueAsString(Map.of(
	        "success", false,
	        "signYn", "N",
	        "message", message
	    ));
	    return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(errorJson));
	}
}
