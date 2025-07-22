package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.batch.contract.ContractSignatureExpireJob;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/cont/proc")
public class RestBrokerContractProceedingController {

	@Autowired
	BrokerAuthUnpackingService authService;
	@Autowired
	AES256Util aes256Util;
	@Autowired
	BrokerContractService contService;
	@Autowired
	ObjectMapper objectMapper; 
	@Autowired
	private Scheduler scheduler;
	
	@PostMapping("/list")
	public Map<String, String> contractList(
			Principal principal
			, @RequestBody Map<String, String> payload
	) {
		List<ContractVO> proceedingContractsList = null;
		
		BrokerVO broker = authService.getRealUser(principal);
		proceedingContractsList = contService.readProceedingContractsList(broker.getMbrCd());
		
	    try {
	        String resultJson = objectMapper.writeValueAsString(proceedingContractsList);
	        Map<String, String> encryptedResponse = aes256Util.encryptWithDynamicIV(resultJson);
	        return encryptedResponse;
	    } catch (Exception e) {
	        throw new RuntimeException("응답 암호화 실패", e);
	    }
	}
	
	/**
	| 항목                  | 설명                                  
	| --------------------- | ----------------------------------- 
	| `ResponseEntity` 사용 | 상태 코드와 메시지 명확하게 전달                  
	| 암호화 필드 검사      | `null`이면 즉시 400 응답                  
	| `try-catch` 정리      | 에러 발생 시 정상 흐름 중단 및 명확한 메시지 전달       
	| `_method` 체크        | `POST`로 왔지만 실제는 `DELETE`임을 명시적으로 처리 
	| 서비스 분리 호출      | 실제 삭제 책임은 `Service`가 가지도록           
	| `deletedCount` 반환   | 클라이언트에서 몇 건 삭제됐는지 알 수 있음
	 */
	@PostMapping("delete")
	public ResponseEntity<?> deleteBulk(
	        Principal principal,
	        @RequestBody Map<String, String> payload
	) {
	    try {
	        /** 1. 복호화 */
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (encrypted == null || iv == null) return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");
	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

	        /** 2. JSON -> POJO 매핑 */
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
	        String method = String.valueOf(parsedRequest.get("_method"));
	        if (!"DELETE".equalsIgnoreCase(method)) return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

	        Object rawData = parsedRequest.get("selectedContracts");
	        List<String> selectedContractIds = objectMapper.convertValue(rawData, new TypeReference<List<String>>() {});
	        if (selectedContractIds == null || selectedContractIds.isEmpty()) return ResponseEntity.badRequest().body("삭제할 계약 ID가 없습니다.");

	        /** 3. 실제 삭제 처리 */
	        int deletedCount = contService.removeProceedingContractBulk(selectedContractIds);

	        /** 4. ResponseEntity */
	        return ResponseEntity.ok(Map.of("message", "삭제 완료", "deletedCount", deletedCount));
	    }
	    catch (JsonProcessingException e) {log.error("JSON 파싱 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");}
	    catch (IllegalArgumentException | IllegalStateException e) {log.error("삭제 처리 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());}
	    catch (Exception e) {log.error("서버 오류", e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");}
	}

	@PostMapping("/open-signpage")
	public ResponseEntity<?> signPage(
			Principal principal
			, @RequestBody Map<String, String> payload
	) {
		try {
			/** 1. 복호화 */
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (encrypted == null || iv == null) return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");
	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
	        
	        /** 2. JSON -> POJO 매핑 */
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
	        String method = String.valueOf(parsedRequest.get("_method"));
	        if (!"UPDATE".equalsIgnoreCase(method)) return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");
	        
	        String contId = String.valueOf(parsedRequest.get("contId"));
	        if (contId == null || contId.isEmpty()) return ResponseEntity.badRequest().body("서명 페이지를 개설할 계약 ID가 없습니다.");
	        
	        /** 3. 실제 업데이트 처리 */
	        int updatedCount = contService.openContractSignaturePage(contId);
	        if (updatedCount == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 계약을 찾을 수 없거나 이미 개설된 상태입니다.");
	        
	        /** 4. Quartz Job 예약 (10분 후 서명 만료) */
	        JobDetail jobDetail = JobBuilder.newJob(ContractSignatureExpireJob.class)
	            .withIdentity("expireSignPage-" + contId, "signpage") // 중복 방지
	            .usingJobData("contId", contId)
	            .build();

	        Trigger trigger = TriggerBuilder.newTrigger()
	            .forJob(jobDetail)
	            .withIdentity("expireSignPageTrigger-" + contId, "signpage")
	            .startAt(Date.from(Instant.now().plus(10, ChronoUnit.MINUTES)))
	            .withSchedule(SimpleScheduleBuilder.simpleSchedule()
	                .withMisfireHandlingInstructionFireNow()) // 누락 시 즉시 실행
	            .build();

	        log.debug("🕒 서명 만료 Job 예약 완료 → contId: {}, 실행시각: {}", contId, trigger.getStartTime());
	        scheduler.scheduleJob(jobDetail, trigger);
	        
	        /** 5. ResponseEntity */
	        return ResponseEntity.ok(Map.of("message", "서명 페이지가 성공적으로 개설되었습니다.", "updatedCount", updatedCount));   
		}
		catch (JsonProcessingException e) {log.error("JSON 파싱 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");}
		catch (Exception e) {log.error("서명 페이지 개설 중 예외 발생", e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 페이지 개설 중 오류가 발생했습니다.");}
	}
}
