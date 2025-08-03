package kr.or.ddit.broker.controller;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import kr.or.ddit.broker.mapper.BrokerAuthUnpackingMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.crypto.UrlSafeBase64;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.ByteArrayMultipartFile;
import kr.or.ddit.util.file.ToMultipartFileUtil;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.notifications.service.NotificationsService;
import kr.or.ddit.util.pdf.SignerRole;
import kr.or.ddit.util.pdf.service.PDFService;
import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.NotificationVO;
import kr.or.ddit.broker.dto.SignatureDTO;
import kr.or.ddit.broker.dto.SignerDTO;
import kr.or.ddit.broker.dto.SignerStatusAssembler;
import kr.or.ddit.broker.dto.SignerStatusDTO;
import kr.or.ddit.broker.dto.StandardLeaseFormDTO;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Slf4j
@RestController
@RequestMapping("/rest/contract")
public class RestContractSignatureController {

	@Autowired
	private BrokerAuthUnpackingService authUnpack;
	@Autowired
	private AES256Util aes256Util;
	@Autowired
	private BrokerContractService contService;
	@Autowired
	private ObjectMapper objectMapper;
	@Autowired
	private Validator validator;
	@Autowired
	private FileService fileService;
	@Autowired
	private SignerStatusAssembler dtoAssembler;
	@Autowired
	private PDFService pdfService;
	@Autowired
	private BrokerListingService lstgService;
	@Autowired
	private NotificationsService notifService;

	@GetMapping("/{encryptedContId}")
	public ResponseEntity<Void> redirectToReactSignaturePage(@PathVariable String encryptedContId) {
		String frontendUrl = "https://dev.beavertipi.com/contract/" + encryptedContId;
		log.debug("여기를 들렸다.>!!!! [RestContractSignatureController]::", frontendUrl);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl)).build();
	}

//	@PostMapping("/signature/upload")
//	public ResponseEntity<?> uploadSignature(@RequestBody Map<String, String> payload) {
//		log.debug("----><><>< [POST:: /REST/CONTRACT/SIGNATURE/UPLOAD] {}", payload);
//		String resultJson = "";
//		try {
//			/** 1. 복호화 */
//			String iv = payload.get("iv");
//			String encrypted = payload.get("encrypted");
//			if (encrypted == null || iv == null) {
//				resultJson = objectMapper
//						.writeValueAsString(Map.of("success", false, "message", "암호화된 요청 또는 IV 누락", "signYn", "N"));
//				return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
//			}
//			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
//
//			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
//			});
//			String method = String.valueOf(parsedRequest.get("_method"));
//			if (!"POST".equalsIgnoreCase(method))
//				return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");
//
//			/// RESTAPI에서 파싱한 객체에 대한 검증
//			ContractDigitalSignVO digitalSign = objectMapper.convertValue(parsedRequest.get("contractDigitalSign"),
//					ContractDigitalSignVO.class);
//			Set<ConstraintViolation<ContractDigitalSignVO>> violations = validator.validate(digitalSign);
//			if (!violations.isEmpty()) {
//				StringBuilder errorMsg = new StringBuilder("검증 실패 항목:\n");
//				for (ConstraintViolation<ContractDigitalSignVO> violation : violations) {
//					errorMsg.append("- ").append(violation.getPropertyPath()).append(": ")
//							.append(violation.getMessage()).append("\n");
//				}
//				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg.toString());
//			}
//
//			/// contDtBaseData로 이미지의 multipartFile을 만들어서, 아래 로직에 넘겨줄 거야.
//			String contDtBaseData = digitalSign.getContDtBaseData();
//			String base64 = contDtBaseData.contains(",") ? contDtBaseData.split(",")[1] : contDtBaseData; // ✅ base64
//			String contDtSignId = digitalSign.getContDtSignId(); // ✅ 파일명에 활용
//			String savedFileName = "sign_" + contDtSignId + ".png";
//			String contentType = "image/png"; // 또는 base64 헤더에서 파싱
//
//			byte[] data = Base64.getDecoder().decode(base64);
//			MultipartFile multipartFile = new Base64DecodedMultipartFile(data, savedFileName, contDtSignId,
//					contentType);
//
//			FileVO result = fileService.uploadAndSave(multipartFile, "contract", "CODTS", contDtSignId,
//					multipartFile.getContentType());
//
//			// 해시 검증
//			String rawData = contDtBaseData + digitalSign.getMbrCd() + digitalSign.getContId()
//					+ digitalSign.getContDtSignType() + digitalSign.getContDtSignDtm();
//
//			String serverHash = DigestUtils.sha256Hex(rawData);
//			if (!serverHash.equals(digitalSign.getContDtSignHashVal())) {
//				throw new IllegalStateException("전자서명 데이터 위변조 의심");
//			}
//			return ResponseEntity.ok().body(Map.of("success", true));
//		} catch (JsonProcessingException e) {
//			log.error("JSON 파싱 실패", e);
//			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");
//		} catch (IllegalArgumentException e) {
//			// convertValue 실패 시 (예: 타입 불일치 등)
//			log.error("❌ contractDigitalSign 변환 실패", e);
//			return ResponseEntity.badRequest().body("서명 데이터 파싱 실패");
//		} catch (Exception e) {
//			log.error("서명 페이지 개설 중 예외 발생", e);
//			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 페이지 개설 중 오류가 발생했습니다.");
//		}
//	}
	private String extractBase64(String dataUri) {
		return dataUri.contains(",") ? dataUri.split(",")[1] : dataUri;
	}

	private void validate(ContractDigitalSignVO vo) {
		Set<ConstraintViolation<ContractDigitalSignVO>> violations = validator.validate(vo);
		if (!violations.isEmpty()) {
			throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "서명 데이터 검증 실패");
		}
	}

	
	@PostMapping("/signature/upload/two/{signerRole}")
	public ResponseEntity<?> uploadSignautreTwo(
			Principal principal
			, @RequestBody Map<String, String> payload
			, @PathVariable(name = "signerRole") String signerRole
			, HttpServletRequest request
	) {
		String resultJson = "";
		try {
			// 1. 복호화
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (iv == null || encrypted == null)
				return badRequest("암호화된 요청 또는 IV 누락");

			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			if (!"POST".equalsIgnoreCase((String) parsedRequest.get("_method")))
				return badRequest("지원하지 않는 요청 방식입니다.");

			ContractDigitalSignVO digitalSign = objectMapper.convertValue(parsedRequest.get("contractDigitalSign"),
					ContractDigitalSignVO.class);
			validate(digitalSign);
			log.debug("<><><><><> DIGITALSIGN:: {}", digitalSign);

			String telno = authUnpack.getSigner(principal).getMbrTelno();
			log.debug(
					"hashVal 생성한당 ^0^^0^^0^^)^ baseData: {} \n telno: {}, contId: {}, contDtSignType: {}, contDtSignDtm: {}",
					digitalSign.getContDtBaseData(), telno, digitalSign.getContId(), digitalSign.getContDtSignType(),
					digitalSign.getContDtSignDtm());

			String rawBase64 = digitalSign.getContDtBaseData(); // data:image/png;base64,...
			String base64 = rawBase64.contains(",") ? rawBase64.split(",")[1] : rawBase64;
			byte[] imageBytes = Base64.getDecoder().decode(base64);

			String rawHash = digitalSign.getContDtBaseData() + telno + digitalSign.getContId()
					+ signerRole + digitalSign.getContDtSignDtm();
			String serverHash = DigestUtils.sha256Hex(rawHash);
			if (!serverHash.equals(digitalSign.getContDtSignHashVal())) {
				throw new IllegalStateException("전자서명 데이터 위변조 의심");
			}

			
			// 3. 원본 PDF 로드
			//먼저 tempContr을 검색 있으면 그거 불러오고, 가져다 서명 입혀서 update
			//	public FileVO updateFile(String fileId, MultipartFile newFile);
			FileVO originalFile = contService.readContractPDFFile(digitalSign.getContId());
			if (originalFile == null) {
				log.warn("📁 원본 파일이 존재하지 않음. contId: {}", digitalSign.getContId());
				return badRequest("원본 계약서를 찾을 수 없습니다.");
			}
			String fileId = originalFile.getFileId();
			
			ResponseEntity<Resource> response = fileService.downloadContractFile(originalFile.getFileId());
			Resource resource = response.getBody();
			byte[] originalPdfBytes = resource != null ? StreamUtils.copyToByteArray(resource.getInputStream()) : null;
			if (originalPdfBytes == null)
				return badRequest("원본 계약서를 불러올 수 없습니다.");

			// 4. enum 역할 변환 및 좌표 템플릿 추출
			SignerRole role = SignerRole.from(/* digitalSign.getContDtSignType() */signerRole);
//			PDFService.SignaturePosition pos = pdfService.getPositionForRole(role);
			// 다중 좌표 추출
			List<PDFService.SignaturePosition> positions = pdfService.getPositionForRole(role);


			// 5. 서명 PDF 생성
			List<PDFService.SignatureInfo> signList = positions.stream()
			        .map(pos -> new PDFService.SignatureInfo(
			                role,
			                imageBytes,
			                pos.pageNumber,
			                pos.x,
			                pos.y,
			                pos.width,
			                pos.height
			        ))
			        .toList();

			byte[] signedPdfBytes = pdfService.insertMultipleSignaturesToPDF(originalPdfBytes, signList);

			// 6. 새 PDF를 S3에 업로드
			MultipartFile signedPdf = new ByteArrayMultipartFile("contract-" + digitalSign.getContId() + "-signed.pdf",
					signedPdfBytes, "application/pdf");
			FileVO tempContr = fileService.uploadAndSaveTempSignedContract(signedPdf, digitalSign, fileId);

			
			String contId = digitalSign.getContId();
			
			String signDateTime = digitalSign.getContDtSignDtm();
			LocalDateTime dateTime = Instant.parse(signDateTime)
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();
			
			// 6-0. SIGNER 업데이트
			SignerDTO signer = contService.readSigner(Map.of("contId", contId, "role", signerRole));
			signer.setIsSigned(true);
			signer.setIsValid(true);
			signer.setIpAddr(request.getRemoteAddr());
			signer.setSignedAt(dateTime);
			signer.setIsJoined(false);
			contService.updateSigner(signer);
			
			// 6-1. 계약 체결
			if("AGENT".equals(signerRole)) {
				ContractVO contract = contService.readContractInfo(contId);
				contService.conclusionContract(contId);
			}
			
			// 6-2. 알림메시지

			ContractVO contract = contService.readContractInfo(contId);
			String lesseeTelno = contract.getContLesseeTelno();
			String lessorTelno = contract.getContTenancyTelno();
			String agentTelno = contract.getContBrokerTelno();
			String userRole = "AGENT";
			
			ListingVO lstg = lstgService.readLstgDetailsById(contract.getLstgId());
			
			Map<String, String> partyTelnoParam = Map.of(
					"lesseeTelno", lesseeTelno,
					"lessorTelno", lessorTelno,
					"agentTelno", agentTelno,
					"userRole", userRole,
					"contId", contId);
			Map<String, SignerDTO> signers = contService.readContractPartyInfo2(partyTelnoParam, request);
	        SignerDTO lessee = signers.get("LESSEE");
	        SignerDTO lessor = signers.get("LESSOR");
	        SignerDTO agent = signers.get("AGENT");
	        String lesseeCd = lessee.getCode();
	        String lessorCd = lessor.getCode();
	        String agentCd = agent.getCode();
			
	        String lstgName = lstg.getLstgNm();
			String encodedEncryptedContId = UrlSafeBase64.encode(aes256Util.encrypt(contId));
			String notifRefUrl = String.format("https://dev.beavertipi.com/contract/%s", encodedEncryptedContId);
			String notifTitle = "";
	        String notifMsg = "";
	        
			NotificationVO notif = new NotificationVO();
			
			switch(signerRole) {
			case "LESSOR":
				notifTitle = "[전자서명 진행 중]";
				notifMsg = String.format("임대인이 서명을 완료했습니다. 임차인의 서명을 기다리는 중... 매물 '%s'.", lstgName);
				
				notif.setNotifTitle(notifTitle);
				notif.setNotifMsg(notifMsg);
				notif.setNotifRefUrl(notifRefUrl);
				notif.setNotifTypeCd("092");
				break;
			case "LESSEE":
				notifTitle = "[전자서명 진행 중]";
				notifMsg = String.format("임차인이 서명을 완료했습니다. 중개인의 확인을 기다리는 중... 매물 '%s'.", lstgName);
				
				notif.setNotifTitle(notifTitle);
				notif.setNotifMsg(notifMsg);
				notif.setNotifRefUrl(notifRefUrl);
				notif.setNotifTypeCd("093");
				break;
			case "AGENT":
				notifTitle = "[전자계약 체결]";
				notifMsg = String.format("중개인이 계약 체결을 확인했습니다. 매물 '%s'.", lstgName);
				
				notif.setNotifTitle(notifTitle);
				notif.setNotifMsg(notifMsg);
				notif.setNotifRefUrl(notifRefUrl);
				notif.setNotifTypeCd("094");
				break;
			default: break;
			}
			
			notif.setMbrCd(lessorCd);
	        notifService.createNotificationSignautrePageOpened(notif);
	        notif.setMbrCd(lesseeCd);
	        notifService.createNotificationSignautrePageOpened(notif);
	        notif.setMbrCd(agentCd);
	        notifService.createNotificationSignautrePageOpened(notif);

	        // 6-3. 서명상태 저장
	        
	        
			
			// 7. 성공 응답
			resultJson = objectMapper.writeValueAsString(
					Map.of("success", true, "fileUrl", tempContr.getFilePathUrl(), "fileId", tempContr.getFileId()));
			return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));

		} catch (Exception e) {
			log.error("[서명 업로드 실패]", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("success", false, "message", "서명 업로드 중 오류 발생"));
		}
	}
	

	/**
	 * 1.contId로 CONTRACT_DIGITAL_SIGN 테이블 조회 2.LESSEE|LESSOR|AGENT 역할 별로 서명 여부를 판단
	 * 3.각 RECORD에 대한 HASH검증 4.SignerStatusDTO 변환
	 * 
	 * @param payload
	 * @return
	 */
	@PostMapping("/signature/status")
	public ResponseEntity<?> getSignatureStatus(Principal principal, @RequestBody Map<String, String> payload,
			HttpServletRequest request) {
		String resultJson = "";
		try {
			// 1. 요청 페이로드 복호화
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (encrypted == null || iv == null) {
				resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "암호화된 요청 또는 IV 누락"));
				return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
			}

			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			String method = String.valueOf(parsedRequest.get("_method"));
			if (!"GET".equalsIgnoreCase(method))
				return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

			String contId = String.valueOf(parsedRequest.get("contId"));

			// 2. 계약 정보 + 서명자 목록 조회
			ContractVO contract = contService.readContractInfo(contId);
			List<ContractDigitalSignVO> signs = contService.readSignatureList(contId);
			log.debug("-------<><><><><>^ㅂ^쓰바 서명 레코드 수: {}", signs.size());

			// map(Function<? super T,? extends R>) 타입추론 실패 지점
//	        List<SignerStatusDTO> dtoList = signs.stream()
//	        		.map(vo -> {
			/****** 이 부분을 Function<T,R> 함수명 = LAMBDA로 선언 **/
			/** React에서 컴포넌트에 함수를 prop 넘겨주듯이 사용 ****/


			// 3. 현재 로그인한 사용자 정보
			MemberVO user = authUnpack.getSigner(principal);
			String role = "";
			if (user.getMbrTelno().equals(contract.getContLesseeTelno()))
				role = "LESSEE";
			if (user.getMbrTelno().equals(contract.getContTenancyTelno()))
				role = "LESSOR";
			if (user.getMbrTelno().equals(contract.getContBrokerTelno()))
				role = "AGENT";

			// 4. 기본 SignerStatusDTO 리스트 구성
			List<SignerStatusDTO> defaultSigners = new ArrayList<>();
			defaultSigners
					.add(dtoAssembler.makeDefaultSigner("AGENT", contract.getContBrokerTelno(), null, null, null));
			defaultSigners
					.add(dtoAssembler.makeDefaultSigner("LESSOR", contract.getContTenancyTelno(), null, null, null));
			defaultSigners
					.add(dtoAssembler.makeDefaultSigner("LESSEE", contract.getContLesseeTelno(), null, null, null));

			// 5. 인가된 권한에 해당하는 SignerStatus 갱신
			for (SignerStatusDTO dto : defaultSigners) {
				if (dto.getRole().equals(role)) {
					dto.setMbrNm(user.getMbrNm());
					dto.setMbrCd(user.getMbrCd());
					dto.setIpAddr(request.getRemoteAddr());
				}
			}

			// 5. 실제 서명 레코드 반영 (있는 경우 덮어쓰기)
			for (ContractDigitalSignVO sign : signs) {
				for (SignerStatusDTO dto : defaultSigners) {
					if (dto.getRole().equals(sign.getContDtSignType())) {
						dto.setSignedAt(LocalDateTime.parse(sign.getContDtSignDtm(), SignerStatusAssembler.formatter));
						dto.setHashVal(sign.getContDtSignHashVal());
//	                    dto.setTempPdfUrl(null);
//						dto.setIsRejected("Y".equalsIgnoreCase(sign.getContDtSignStat()));

						// 🔐 hash 검증
						String raw = sign.getContDtBaseData() + user.getMbrTelno() + sign.getContId()
								+ sign.getContDtSignType() + sign.getContDtSignDtm();
						String serverHash = DigestUtils.sha256Hex(raw);
						dto.setIsValid(serverHash.equals(sign.getContDtSignHashVal()));
					}
				}
			}

			
//			log.debug("129842389nfqcuyhyjuhm ----<><><<> {}", defaultSigners.toString());
			// 4. 응답 JSON
			resultJson = objectMapper.writeValueAsString(Map.of("success", true, "signers", defaultSigners));
			return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));

		} catch (Exception e) {
			log.error("[서명 상태 조회 실패]", e);
			try {
				resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", "서명 상태 조회 중 오류 발생"));
				return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
			} catch (Exception ex) {
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복호화 실패");
			}
		}
	}

	@PostMapping("/pdf/download")
	public ResponseEntity<?> downloadSignedPdf(@RequestBody Map<String, String> payload) {
		String resultJson = "";
		try {
			/** 1. 복호화 */
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (encrypted == null || iv == null)
				return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");
			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

			/** 2. JSON -> POJO 매핑 */
			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			String method = String.valueOf(parsedRequest.get("_method"));
			if (!"GET".equalsIgnoreCase(method))
				return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

			String contId = String.valueOf(parsedRequest.get("contId"));
			if (contId == null || contId.isEmpty())
				return ResponseEntity.badRequest().body("서명 페이지를 개설할 계약 ID가 없습니다.");
			String role = String.valueOf(parsedRequest.get("role"));
			if(role ==null || role.isBlank())
				return ResponseEntity.badRequest().body("넌 누구야!");

			
			
//			// PDF 파일 조회 (sourceRef는 "CONTRACT_TEMP" 또는 상황에 따라 "CONTRACT" 등)
//			List<FileVO> files = fileService.readFileList("CONTR", contId);
//			if (files == null || files.isEmpty()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "서명된 PDF 파일이 존재하지 않습니다."));
//			}
//			FileVO signedPdf = files.get(0); // 첫 번째 파일 사용
			
			
			//원본이 아니라, 최신 서명파일을 불러와야지 ㅇㅇ
			log.debug("컨트랙트 ID: {}", contId);
			FileVO latestFile = contService.readContractPDFFile(contId);
			log.debug("가져온 파일 정보: {}", latestFile);
//			String fileUrl = latestFile.getFilePathUrl(); // 커스텀 서비스 메소드
//			
//			if (fileUrl == null || fileUrl.isBlank()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "원본 PDF URL 없음"));
//			}
			log.debug("--<><<><> 이건 뭘까 ^0^   {}",latestFile.getFileId());
			InputStream is = fileService.getContractFileStream(latestFile.getFileId());
			byte[] fileBytes = is.readAllBytes();
			String base64Pdf = Base64.getEncoder().encodeToString(fileBytes);
//			ResponseEntity<Resource> is = fileService.downloadFile(latestFile.getFileId());
//			
//			InputStream inputStream = is.getBody().getInputStream();
//			byte[] fileBytes = inputStream.readAllBytes();
//			
//			String base64Pdf = Base64.getEncoder().encodeToString(fileBytes);
			

			return ResponseEntity.ok(Map.of("base64", base64Pdf));
		} catch (Exception e) {
			log.error("PDF 다운로드 중 오류 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "서명된 PDF 로드 실패", "detail", e.getMessage()));
		}
	}


	@PostMapping("/pdf/url")
	public ResponseEntity<?> getContractOriginalPdfUrl(
			@RequestBody Map<String, String> payload
	) {
		try {
			// 🔓 1. 복호화
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (iv == null || encrypted == null)
				return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");

			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

			// 📦 2. JSON → Map 변환
			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			String method = String.valueOf(parsedRequest.get("_method"));
			if (!"GET".equalsIgnoreCase(method))
				return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

			String contId = String.valueOf(parsedRequest.get("contId"));
			if (contId == null || contId.isBlank())
				return ResponseEntity.badRequest().body("계약 ID 누락");
			String role = String.valueOf(parsedRequest.get("role"));
			if(role ==null || role.isBlank())
				return ResponseEntity.badRequest().body("넌 누구야!");

//			// 📄 3. 파일 서비스에서 원본 계약서 PDF URL 조회
//			FileVO file = contService.readContractPDFFile(contId);
//			if (file == null || file.getFilePathUrl() == null || file.getFilePathUrl().isBlank()) {
//				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "원본 PDF가 존재하지 않습니다."));
//			}
			
			//원본이 아니라, 최신 서명파일을 불러와야지 ㅇㅇ
			FileVO latestSignedFile = contService.readLatestSignedContractPdf(contId);
			if(latestSignedFile.getFileSavedname() == null || latestSignedFile.getFileSavedname().isBlank()) {
				FileVO lstestSignedFile = contService.readContractPDFFile(contId);
			}
			
			String fileUrl = latestSignedFile.getFilePathUrl(); // 커스텀 서비스 메소드

			if (fileUrl == null || fileUrl.isBlank()) {
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "원본 PDF URL 없음"));
			}

			return ResponseEntity.ok(Map.of("success", true, "pdfUrl", fileUrl));
		} catch (Exception e) {
			log.error("PDF URL 조회 실패", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "PDF URL 조회 중 오류", "detail", e.getMessage()));
		}
	}
	
	@PostMapping("/pdf/meta")
	public ResponseEntity<?> getContractPdfMeta(Principal principal, @RequestBody Map<String, String> payload) {
		try {
			// 🔓 1. 복호화
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (iv == null || encrypted == null)
				return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");

			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

			// 📦 2. JSON → Map 변환
			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			String method = String.valueOf(parsedRequest.get("_method"));
			if (!"GET".equalsIgnoreCase(method))
				return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

			String contId = String.valueOf(parsedRequest.get("contId"));
			if (contId == null || contId.isBlank())
				return ResponseEntity.badRequest().body("계약 ID 누락");

			// ✅ 3. 서비스 통해 계약 정보 조회
			ContractVO contract = contService.readContractInfo(contId);
			if (contract == null)
				return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "계약 정보 없음"));

			MemberVO signer = authUnpack.getSigner(principal);

			// 📝 4. 필요한 정보만 추려서 응답
			return ResponseEntity.ok(Map.of("success", true, "contId", contract.getContId(), "contType",
					contract.getContTypeCode(), "signedAt", contract.getContDtm(), "signers", signer.getMbrNm() // VO에
																												// 포함되어
																												// 있다고
																												// 가정
			));
		} catch (Exception e) {
			log.error("PDF 메타 정보 조회 실패", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body(Map.of("error", "계약 정보 조회 중 오류", "detail", e.getMessage()));
		}
	}


	@PostMapping("/pdf/base64")
	public ResponseEntity<?> getPdfBase64(@RequestBody Map<String, String> payload) {
		try {
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (iv == null || encrypted == null)
				return badRequest("암호화된 요청 또는 IV 누락");

			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
			Map<String, Object> parsed = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			if (!"GET".equalsIgnoreCase((String) parsed.get("_method")))
				return badRequest("지원하지 않는 요청 방식");

			String contId = (String) parsed.get("contId");

			FileVO signedFile = contService.readContractPDFFile(contId);
			byte[] pdfBytes = fileService.getFileStream(signedFile.getFileId()).readAllBytes();

			String base64 = Base64.getEncoder().encodeToString(pdfBytes);
			return ResponseEntity.ok(Map.of("base64", base64));

		} catch (Exception e) {
			log.error("❌ PDF base64 반환 실패", e);
			return ResponseEntity.status(500).body(Map.of("error", "PDF 불러오기 실패"));
		}
	}

	@PostMapping("/pdf/base64/viewer")
	public ResponseEntity<?> getSignedPdfForViewer(@RequestBody Map<String, String> payload) {
		try {
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (iv == null || encrypted == null)
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "iv 또는 암호화 본문 누락"));

			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
			Map<String, Object> parsed = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			if (!"GET".equalsIgnoreCase((String) parsed.get("_method")))
				return ResponseEntity.badRequest().body(Map.of("success", false, "message", "_method가 GET이 아님"));

			String contId = (String) parsed.get("contId");

			FileVO signedFile = contService.readContractPDFFile(contId);
			byte[] pdfBytes = fileService.getFileStream(signedFile.getFileId()).readAllBytes();

			String base64 = Base64.getEncoder().encodeToString(pdfBytes);
			return ResponseEntity.ok(Map.of("success", true, "base64", base64));
		} catch (Exception e) {
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "PDF 로딩 실패"));
		}
	}

	@PostMapping("/pdf/delete-temp")
	public ResponseEntity<?> deleteTempSignedPdf(@RequestBody Map<String, String> payload) throws JsonMappingException, JsonProcessingException {
		String iv = payload.get("iv");
		String encrypted = payload.get("encrypted");
		if (iv == null || encrypted == null)
			return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");

		String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

		// 📦 2. JSON → Map 변환
		Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
		});
		String method = String.valueOf(parsedRequest.get("_method"));
		if (!"DELETE".equalsIgnoreCase(method))
			return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

//#######################   Unchecked Cast   ###############################################
//		List<String> tempPdfFileIds = (List<String>) parsedRequest.get("tempPdfFileIds");
//		Type safety: Unchecked cast from Object to List<String>
		Object tempPdfFileIdsObj = parsedRequest.get("tempPdfFileIds");
		List<String> tempPdfFileIds = null;
		if (tempPdfFileIdsObj instanceof List<?> list) {
			boolean allStrings = list.stream().allMatch(item -> item instanceof String);
			if (allStrings) {
				tempPdfFileIds = list.stream().map(item -> (String) item).toList(); // Java 16+ toList() returns
																					// unmodifiable List
			} else {
				throw new IllegalArgumentException("tempPdfFileIds must be a list of strings.");
			}
		} else {
			throw new IllegalArgumentException("tempPdfFileIds is not a list.");
		}
//##########################################################################################
		if (tempPdfFileIds == null || tempPdfFileIds.isEmpty()) {
			return ResponseEntity.badRequest().body(Map.of("success", false, "message", "fileIds 누락"));
		}

		try {
			for (String fileId : tempPdfFileIds) {
				fileService.deleteFile(fileId);
			}
			return ResponseEntity.ok(Map.of("success", true));
		} catch (Exception e) {
			log.error("임시 PDF 삭제 실패", e);
			return ResponseEntity.status(500).body(Map.of("success", false, "message", "서버 오류"));
		}
	}

	private ResponseEntity<?> badRequest(String message) {
		try {
			String resultJson = objectMapper.writeValueAsString(Map.of("success", false, "message", message));
			return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
		} catch (Exception e) {
			return ResponseEntity.badRequest().body(message);
		}
	}

}
