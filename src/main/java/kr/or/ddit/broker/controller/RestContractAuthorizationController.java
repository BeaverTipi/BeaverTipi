package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.ddit.broker.dto.SignatureDTO;
import kr.or.ddit.broker.dto.SignatureStateDTO;
import kr.or.ddit.broker.dto.SignerDTO;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 도메인 레벨 인가 SPRING Security의 전역 권한(ROLE)과는 별도로, 특정 계약에서의 개인별 접근 권한 체크.
 * 
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
	@Autowired
	private FileService fileService;

	@PostMapping
	public ResponseEntity<?> contractSignPageAuthorize(
			@RequestBody Map<String, String> payload
			, @AuthenticationPrincipal Authentication auth
			, Principal principal
			, HttpServletRequest request
	) throws Exception {
		String resultJson = "";
		try {
			/** 1. 복호화 */
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			log.debug("💬 수신된 encrypted: {}", encrypted);
			log.debug("💬 수신된 iv: {}", iv);
			
			if (encrypted == null || iv == null) {
				resultJson = objectMapper
						.writeValueAsString(Map.of("success", false, "message", "암호화된 요청 또는 IV 누락", "signYn", "N"));
				return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
			}

			try {
				String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
				Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
				});
				log.debug("{} --- {}",parsedRequest.get("_method"), parsedRequest.get("encryptedContId"));
				String method = String.valueOf(parsedRequest.get("_method"));
				if (!"GET".equalsIgnoreCase(method)) {
					resultJson = objectMapper.writeValueAsString(
							Map.of("success", false, "message", "지원하지 않는 요청 방식입니다.", "signYn", "N"));
					return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
				}

				String encryptedContId = String.valueOf(parsedRequest.get("encryptedContId"));
				String contId = aes256Util.decrypt(encryptedContId);
				// String contId = String.valueOf(parsedRequest.get("contId"));
				log.debug("-----<> 계약 ID 출력:: {}", contId);
				ContractVO contract = contService.readContractInfo(contId);
				log.debug("-----<> 계약 VO 출력:: {}", contract);

				/** 2. 회원 인가처리 */
				if ((auth == null || !auth.isAuthenticated()) && principal == null)
					return reject("로그인이 필요합니다.");

				String username = auth != null ? auth.getName() : principal.getName();
				MemberVO user = memService.readMember(username);
				String userTelno = user.getMbrTelno();
				log.debug("-----<><><> 접근한 회원의 전화번호:: {}", userTelno);
				log.debug("-----<><><><><>  contLesseeTelNo:: {}", contract.getContLesseeTelno());
				log.debug("-----<><><><><>  contTenancyTelNo:: {}", contract.getContTenancyTelno());
				log.debug("-----<><><><><>  contBrokerTelNo:: {}", contract.getContBrokerTelno());

				String userRole = null;
				if (userTelno.equals(contract.getContLesseeTelno()))
					userRole = "LESSEE";
				else if (userTelno.equals(contract.getContTenancyTelno()))
					userRole = "LESSOR";
				else if (userTelno.equals(contract.getContBrokerTelno()))
					userRole = "AGENT";

				if (userRole == null)
					return reject("접근 권한이 없습니다.");
				log.debug("-----<><><><><>  role:: {}", userRole);

				/** 3. 계약 참여자 정보(signers) 구성 */
				String lesseeTelno = contract.getContLesseeTelno();
				String lessorTelno = contract.getContTenancyTelno();
				String agentTelno = contract.getContBrokerTelno();
				Map<String, String> partyTelnoParam = Map.of(
						"lesseeTelno", lesseeTelno,
						"lessorTelno", lessorTelno,
						"agentTelno", agentTelno,
						"userRole", userRole,
						"contId", contId);
//				List<Map<String, Object>> signers = contService.readContractPartyInfo(partyTelnoParam);
				Map<String, SignerDTO> signers = contService.readContractPartyInfo2(partyTelnoParam, request);
				
				FileVO contractFile = contService.readContractPDFFile(contId);
				SignatureDTO signature = SignatureDTO.builder()
						.contId(contId)
						.signatureStatus("P_NOT_YET")
						.originalPdfData("")///
						.lessorSignedPdfData("")
						.lessorSignedPdfId("")
						.lessorSignedPdfPath("")
						.lesseeSignedPdfData("")
						.lesseeSignedPdfId("")
						.lesseeSIgnedPdfPath("")
						.agendSignedPdfId("")
						.agentSignedPdfData("")
						.agentSignedPdfPath("")
						.build();
				
				/** 4. 응답 데이터 구성 */
				Map<String, Object> responseMap = new LinkedHashMap<>();
				responseMap.put("myRole", userRole);
				responseMap.put("globalContId", contId);
				responseMap.put("success", true);
//				responseMap.put("contId", contId);
//				responseMap.put("role", userRole);
//				responseMap.put("code", user.getMbrCd());
//				responseMap.put("name", user.getMbrNm());
//				responseMap.put("telno", user.getMbrTelno());
//				responseMap.put("id", user.getMbrId());
//				responseMap.put("ipAddr", request.getRemoteAddr());
//				responseMap.put("isValid", true);
//			    responseMap.put("tempPdfUrl", null);
//				responseMap.put("signers", signers);
				responseMap.put("signature", signature);
				responseMap.put("signers", signers);
				log.debug("^ㅂ^^ㅂ^^ㅂ^^ㅂ^^ㅂ^^ㅂ^ {}", user.getMbrTelno());
				resultJson = objectMapper.writeValueAsString(responseMap);
				log.debug("----<><> {}", resultJson);
				return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
			} catch (Exception e) {
				log.error("❌ 복호화 실패 - 암호문 또는 IV 문제", e);
				return reject("복호화 실패 - 전송 중 문제가 발생했습니다.");
			}
		} catch (Exception e) {
			log.error("[서명페이지 접근 인가 실패]", e);
			return reject("시스템 오류 발생");
		}

	}

	private ResponseEntity<?> reject(String message) throws Exception {
		String errorJson = objectMapper.writeValueAsString(Map.of("success", false, "signYn", "N", "message", message));
		return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(errorJson));
	}
}
