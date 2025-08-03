package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Map;

import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.Trigger.TriggerState;
import org.quartz.TriggerBuilder;
import org.quartz.TriggerKey;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.ddit.broker.dto.SignerDTO;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.util.batch.contract.ContractSignatureExpireJob;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.crypto.UrlSafeBase64;
import kr.or.ddit.util.notifications.service.NotificationsService;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.NotificationVO;
import kr.or.ddit.vo.SignerVO;
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
	@Qualifier("customQuartzScheduler")
	private Scheduler scheduler;
	@Autowired
	private NotificationsService notifService;
	@Autowired
	private BrokerListingService lstgService;
	
	
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
	        Map<String, String> parsedRequest = BrokerCryptUtil.decryptRequestPayload(payload);
	        
	        /** JSON -> POJO 매핑 */
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
//	    catch (JsonProcessingException e) {log.error("JSON 파싱 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");}
	    catch (IllegalArgumentException | IllegalStateException e) {log.error("삭제 처리 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());}
	    catch (Exception e) {log.error("서버 오류", e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");}
	}

	@PostMapping("/open-signpage")
	public ResponseEntity<?> signPage(
			Principal principal
			, @RequestBody Map<String, String> payload
			, HttpServletRequest request
	) {
		String resultJson = "";
//		log.debug("🔍 현재 Scheduler 인스턴스의 JobFactory 클래스: {}", scheduler.getJobFactory().getClass().getName());
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
	        if(updatedCount == 0) return ResponseEntity.status(HttpStatus.NOT_FOUND).body("해당 계약을 찾을 수 없거나 이미 개설된 상태입니다.");
	        
	        /** 4. Quartz Job 예약 (10_0분 후 서명 만료) */
//	        log.debug("✅ [QuartzConfig] Scheduler가 사용하는 JobFactory: {}", scheduler.getJobFactory().getClass().getName());
	        JobKey jobKey = JobKey.jobKey("expireSignPage-" + contId, "signpage");
	        TriggerKey triggerKey = TriggerKey.triggerKey("expireSignPageTrigger-" + contId, "signpage");

	        // 🔁 기존 Job/Trigger 제거
	        if (scheduler.checkExists(triggerKey)) {
	            scheduler.unscheduleJob(triggerKey); // 트리거만 제거
	        }
	        if (scheduler.checkExists(jobKey)) {
	            scheduler.deleteJob(jobKey); // JobDetail도 제거 (옵션)
	        }
	        
	        JobDetail jobDetail = JobBuilder.newJob(ContractSignatureExpireJob.class)
        	    .withIdentity(jobKey)
        	    .usingJobData("contId", contId)
        	    .storeDurably()
        	    .build();
        	scheduler.addJob(jobDetail, true);  // << 명시적으로 Job 먼저 등록


        	Trigger trigger = TriggerBuilder.newTrigger()
        	    .forJob(jobDetail)
        	    .withIdentity(triggerKey)
//        	    .startAt(new Date(System.currentTimeMillis() + 5000))
        	    .startAt(new Date(System.currentTimeMillis() + 1_200_000))
        	    .withSchedule(SimpleScheduleBuilder.simpleSchedule()
        	        .withMisfireHandlingInstructionFireNow())
        	    .build();
        	scheduler.scheduleJob(trigger);

	        log.debug("🕒 서명 만료 Job 예약 완료 → contId: {}, 실행시각: {}", contId, trigger.getStartTime());
	        log.debug("🔧 JobDetail: {}", jobDetail);
	        log.debug("🔧 Trigger: {}", trigger);

	        ContractVO contract = contService.readContractInfo(contId);
			String lesseeTelno = contract.getContLesseeTelno();
			String lessorTelno = contract.getContTenancyTelno();
			String agentTelno = contract.getContBrokerTelno();
			String userRole = "AGENT";
			
			ListingVO lstg = lstgService.readLstgDetailsById(contract.getLstgId());
			String lstgName = lstg.getLstgNm();
			String notifTitle = "[계약페이지 개설]";
			String notifMsg = String.format("문의하신 매물 '%s'의 계약이 진행 중입니다. 임대인의 서명을 기다리는 중...", lstgName);
			String encodedEncryptedContId = UrlSafeBase64.encode(aes256Util.encrypt(contId));
			String notifRefUrl = String.format("https://dev.beavertipi.com/contract/%s", encodedEncryptedContId);
			
			Map<String, String> partyTelnoParam = Map.of(
					"lesseeTelno", lesseeTelno,
					"lessorTelno", lessorTelno,
					"agentTelno", agentTelno,
					"userRole", userRole,
					"contId", contId);
//			List<Map<String, Object>> signers = contService.readContractPartyInfo(partyTelnoParam);
			Map<String, SignerDTO> signers = contService.readContractPartyInfo2(partyTelnoParam, request);
	        SignerDTO lessee = signers.get("LESSEE");
	        SignerDTO lessor = signers.get("LESSOR");
	        SignerDTO agent = signers.get("AGENT");
	        String lesseeCd = lessee.getCode();
	        String lessorCd = lessor.getCode();
	        String agentCd = agent.getCode();
	        
	        NotificationVO notifToLessee = new NotificationVO();
	        NotificationVO notifToLessor = new NotificationVO();
	        NotificationVO notifToAgent = new NotificationVO();
	        notifToLessee.setMbrCd(lesseeCd);
	        notifToLessee.setNotifTitle(notifTitle);
	        notifToLessee.setNotifMsg(notifMsg);
	        notifToLessee.setNotifRefUrl(notifRefUrl);
	        notifToLessee.setNotifTypeCd("091");
	        notifToLessor.setMbrCd(lessorCd);
	        notifToLessor.setNotifTitle(notifTitle);
	        notifToLessor.setNotifMsg(notifMsg);
	        notifToLessor.setNotifRefUrl(notifRefUrl);
	        notifToLessor.setNotifTypeCd("091");
	        notifToAgent.setMbrCd(agentCd);
	        notifToAgent.setNotifTitle(notifTitle);
	        notifToAgent.setNotifMsg(notifMsg);
	        notifToAgent.setNotifRefUrl(notifRefUrl);
	        notifToAgent.setNotifTypeCd("091");
	        notifService.createNotificationSignautrePageOpened(notifToLessee);
	        notifService.createNotificationSignautrePageOpened(notifToLessor);
	        notifService.createNotificationSignautrePageOpened(notifToAgent);
	        
	        //DEBUG
	        Instant now = Instant.now();
	        Instant fireTime = now.plus(90, ChronoUnit.SECONDS);
	        log.debug("🕒 현재 시각: {}, 예약 시각: {}", now, fireTime);
	        TriggerState state = scheduler.getTriggerState(trigger.getKey());
	        log.debug("📌 트리거 상태: {}", state);  // BLOCKED, COMPLETE, PAUSED, NONE, NORMAL 중 하나
	        Date prevFire = scheduler.getTrigger(trigger.getKey()).getPreviousFireTime();
	        log.debug("📅 이전 실행 시각: {}", prevFire);
	        log.debug("🟢 Quartz 시작 여부: {}, 상태: {}", scheduler.isStarted(), scheduler.isInStandbyMode());

	        
	        /** 5. ResponseEntity */
	        
	        resultJson = objectMapper.writeValueAsString(Map.of("message", "서명 페이지가 성공적으로 개설되었습니다.", "updatedCount", updatedCount));
	        return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));

		}
		catch (JsonProcessingException e) {log.error("JSON 파싱 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");}
		catch (Exception e) {log.error("서명 페이지 개설 중 예외 발생", e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 페이지 개설 중 오류가 발생했습니다.");}
	}
	
	@PostMapping("/sign-status")
	public ResponseEntity<?> signPageStatus(
			Principal principal
			, @RequestBody Map<String, String> payload
	) throws JsonProcessingException {
        String resultJson = "";
		try {
		/** 1. 복호화 */
        String iv = payload.get("iv");
        String encrypted = payload.get("encrypted");
        if (encrypted == null || iv == null) {
        	resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "암호화된 요청 또는 IV 누락", "signYn", "N"));
        	return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
        }
        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
        
        /** 2. JSON -> POJO 매핑 */
        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
        String method = String.valueOf(parsedRequest.get("_method"));
        if (!"GET".equalsIgnoreCase(method)) {
        	resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "지원하지 않는 요청 방식입니다.", "signYn", "N"));
        	return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
        }
        
        String contId = String.valueOf(parsedRequest.get("contId"));
        if (contId == null || contId.isEmpty()) {
        	resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "서명 페이지가 개설된 계약 ID가 없습니다.", "signYn", "N"));
        	return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
        }
        
        /** 3. 개설 여부 조회 */
        String signYn = contService.isSignPageOpened(contId); // Y/N 조회
        
        /** 4. ResponseEntity */
        if ("N".equals(signYn)) resultJson = objectMapper.writeValueAsString(
        		Map.of("success", false, "message", "이미 만료된 접근입니다.", "signYn", "N")); 
        else if("Y".equals(signYn))  resultJson = objectMapper.writeValueAsString(
        		Map.of("success", true, "message", "서명 페이지로 이동합니다.", "signYn", "Y"));
        else resultJson = objectMapper.writeValueAsString(
        		Map.of("success", false, "message", "contSignYn 값이 누락되어 있습니다."));
        return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
		}
		catch(JsonProcessingException e) {log.error("JSON 파싱 실패", e); return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");}
		catch(Exception e) {log.error("서명 페이지 개설 중 예외 발생", e); return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 페이지 개설 중 오류가 발생했습니다.");}
	}
}
