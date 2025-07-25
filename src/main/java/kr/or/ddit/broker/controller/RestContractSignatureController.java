package kr.or.ddit.broker.controller;

import java.io.InputStream;
import java.net.URI;
import java.nio.file.Files;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import kr.or.ddit.broker.mapper.BrokerAuthUnpackingMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.main.member.service.MemberService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.ToMultipartFileUtil;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.StandardLeaseFormDTO;
import lombok.extern.slf4j.Slf4j;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.Principal;

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
	private MemberService memService;
//	@GetMapping("/{encryptedContId}")
//	public ResponseEntity<?> redirectToSignPage(@PathVariable String encryptedContId){
//		try {
//	        String contId = aes256Util.decrypt(encryptedContId); // 예시
//	        if (!contService.isContractExist(contId)) {
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//	                .body("존재하지 않는 계약입니다.");
//	        }
//
//	        String frontendUrl = "https://dev.beavertipi.com/contract/" + encryptedContId;
//	        return ResponseEntity.status(HttpStatus.FOUND)
//	                .location(URI.create(frontendUrl)).build();
//
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//	            .body("잘못된 접근입니다.");
//	    }
//	}

	@GetMapping("/{encryptedContId}")
	public ResponseEntity<Void> redirectToReactSignaturePage(@PathVariable String encryptedContId) {
		String frontendUrl = "https://dev.beavertipi.com/contract/" + encryptedContId;
		log.debug("여기를 들렸다.>!!!! [RestContractSignatureController]::", frontendUrl);
		return ResponseEntity.status(HttpStatus.FOUND).location(URI.create(frontendUrl)).build();
	}

	@PostMapping("/signature/upload")
	public ResponseEntity<?> uploadSignature(@RequestBody Map<String, String> payload) {
		log.debug("----><><>< [POST:: /REST/CONTRACT/SIGNATURE/UPLOAD] {}", payload);
		String resultJson = "";
		try {
			/** 1. 복호화 */
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (encrypted == null || iv == null) {
				resultJson = objectMapper
						.writeValueAsString(Map.of("success", false, "message", "암호화된 요청 또는 IV 누락", "signYn", "N"));
				return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
			}
			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

			Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {
			});
			String method = String.valueOf(parsedRequest.get("_method"));
			if (!"POST".equalsIgnoreCase(method))
				return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

			/// RESTAPI에서 파싱한 객체에 대한 검증
			ContractDigitalSignVO digitalSign = objectMapper.convertValue(parsedRequest.get("contractDigitalSign"),
					ContractDigitalSignVO.class);
			Set<ConstraintViolation<ContractDigitalSignVO>> violations = validator.validate(digitalSign);
			if (!violations.isEmpty()) {
				StringBuilder errorMsg = new StringBuilder("검증 실패 항목:\n");
				for (ConstraintViolation<ContractDigitalSignVO> violation : violations) {
					errorMsg.append("- ").append(violation.getPropertyPath()).append(": ")
							.append(violation.getMessage()).append("\n");
				}
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, errorMsg.toString());
			}

			/// contDtBaseData로 이미지의 multipartFile을 만들어서, 아래 로직에 넘겨줄 거야.
			String contDtBaseData = digitalSign.getContDtBaseData();
			String base64 = contDtBaseData.contains(",") ? contDtBaseData.split(",")[1] : contDtBaseData; // ✅ base64
			String contDtSignId = digitalSign.getContDtSignId(); // ✅ 파일명에 활용
			String savedFileName = "sign_" + contDtSignId + ".png";
			String contentType = "image/png"; // 또는 base64 헤더에서 파싱

			byte[] data = Base64.getDecoder().decode(base64);
			MultipartFile multipartFile = new Base64DecodedMultipartFile(data, savedFileName, contDtSignId,
					contentType);

			FileVO result = fileService.uploadAndSave(multipartFile, "contract", "CODTS", contDtSignId,
					multipartFile.getContentType());

			
			// 해시 검증
			String rawData = contDtBaseData + digitalSign.getMbrCd() + digitalSign.getContId()
					+ digitalSign.getContDtSignType() + digitalSign.getContDtSignDtm();

			String serverHash = DigestUtils.sha256Hex(rawData);
			if (!serverHash.equals(digitalSign.getContDtSignHashVal())) {
				throw new IllegalStateException("전자서명 데이터 위변조 의심");
			}
			return ResponseEntity.ok().body(Map.of("success", true));
		} catch (JsonProcessingException e) {
			log.error("JSON 파싱 실패", e);
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("잘못된 요청 형식입니다.");
		} catch (IllegalArgumentException e) {
			// convertValue 실패 시 (예: 타입 불일치 등)
			log.error("❌ contractDigitalSign 변환 실패", e);
			return ResponseEntity.badRequest().body("서명 데이터 파싱 실패");
		} catch (Exception e) {
			log.error("서명 페이지 개설 중 예외 발생", e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서명 페이지 개설 중 오류가 발생했습니다.");
		}
	}
	
	@PostMapping("/signature/status")
	public ResponseEntity<?> getSignatureStatus(@RequestBody Map<String, String> payload) {
	    String resultJson = "";
	    try {
	        // ✅ 1. 복호화
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (encrypted == null || iv == null) {
	            resultJson = objectMapper.writeValueAsString(Map.of(
	                "success", false,
	                "message", "암호화된 요청 또는 IV 누락"
	            ));
	            return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
	        }

	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
	        String method = String.valueOf(parsedRequest.get("_method"));
	        if (!"GET".equalsIgnoreCase(method))
	            return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");
	        
	        String contId = String.valueOf(parsedRequest.get("contId"));


	        List<Map<String, Object>> signerStatusList = contService.validateSignerStatus(contId);


	        resultJson = objectMapper.writeValueAsString(Map.of(
	            "success", true,
	            "signers", signerStatusList
	        ));
	        return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));

	    } catch (Exception e) {
	        log.error("[서명 상태 조회 실패]", e);
	        try {
	            resultJson = objectMapper.writeValueAsString(Map.of(
	                "success", false,
	                "message", "서명 상태 조회 중 오류 발생"
	            ));
	            return ResponseEntity.ok(aes256Util.encryptWithDynamicIV(resultJson));
	        } catch (Exception ex) {
	            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("복호화 실패");
	        }
	    }
	}

	@PostMapping("/pdf/download")
	public ResponseEntity<?> downloadSignedPdf(
			@RequestBody Map<String, String> payload) {
		String resultJson = "";
	    try {
			/** 1. 복호화 */
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (encrypted == null || iv == null) return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");
	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
	        
	        /** 2. JSON -> POJO 매핑 */
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
	        String method = String.valueOf(parsedRequest.get("_method"));
	        if (!"POST".equalsIgnoreCase(method)) return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");
	        
	        String contId = String.valueOf(parsedRequest.get("contId"));
	        if (contId == null || contId.isEmpty()) return ResponseEntity.badRequest().body("서명 페이지를 개설할 계약 ID가 없습니다.");
	        
	        // PDF 파일 조회 (sourceRef는 "CONTRACT_TEMP" 또는 상황에 따라 "CONTRACT" 등)
	        List<FileVO> files = fileService.readFileList("CONTR", contId);
	        if (files == null || files.isEmpty()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "서명된 PDF 파일이 존재하지 않습니다."));
	        }

	        FileVO signedPdf = files.get(0); // 첫 번째 파일 사용
	        InputStream is = fileService.getFileStream(signedPdf.getFileId());
	        byte[] fileBytes = is.readAllBytes();
	        String base64Pdf = Base64.getEncoder().encodeToString(fileBytes);

	        return ResponseEntity.ok(Map.of("base64", base64Pdf));
	    } catch (Exception e) {
	        log.error("PDF 다운로드 중 오류 발생", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "서명된 PDF 로드 실패", "detail", e.getMessage()));
	    }
	}
	
	@PostMapping("/pdf/meta")
	public ResponseEntity<?> getContractPdfMeta(
			Principal principal
			, @RequestBody Map<String, String> payload
	) {
	    try {
	        // 🔓 1. 복호화
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (iv == null || encrypted == null)
	            return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");

	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

	        // 📦 2. JSON → Map 변환
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
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
	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "contId", contract.getContId(),
	            "contType", contract.getContTypeCode(),
	            "signedAt", contract.getContDtm(),
	            "signers", signer.getMbrNm() // VO에 포함되어 있다고 가정
	        ));
	    } catch (Exception e) {
	        log.error("PDF 메타 정보 조회 실패", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "계약 정보 조회 중 오류", "detail", e.getMessage()));
	    }
	}

	@PostMapping("/pdf/url")
	public ResponseEntity<?> getContractOriginalPdfUrl(@RequestBody Map<String, String> payload) {
	    try {
	        // 🔓 1. 복호화
	        String iv = payload.get("iv");
	        String encrypted = payload.get("encrypted");
	        if (iv == null || encrypted == null)
	            return ResponseEntity.badRequest().body("암호화된 요청 또는 IV 누락");

	        String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

	        // 📦 2. JSON → Map 변환
	        Map<String, Object> parsedRequest = objectMapper.readValue(decryptedJson, new TypeReference<>() {});
	        String method = String.valueOf(parsedRequest.get("_method"));
	        if (!"GET".equalsIgnoreCase(method))
	            return ResponseEntity.badRequest().body("지원하지 않는 요청 방식입니다.");

	        String contId = String.valueOf(parsedRequest.get("contId"));
	        if (contId == null || contId.isBlank())
	            return ResponseEntity.badRequest().body("계약 ID 누락");

	        // 📄 3. 파일 서비스에서 원본 계약서 PDF URL 조회
	        FileVO file = fileService.readFile(contId);
	        String fileUrl = file.getFilePathUrl(); // 커스텀 서비스 메소드

	        if (fileUrl == null || fileUrl.isBlank()) {
	            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "원본 PDF URL 없음"));
	        }

	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "pdfUrl", fileUrl
	        ));
	    } catch (Exception e) {
	        log.error("PDF URL 조회 실패", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	                .body(Map.of("error", "PDF URL 조회 중 오류", "detail", e.getMessage()));
	    }
	}



}
