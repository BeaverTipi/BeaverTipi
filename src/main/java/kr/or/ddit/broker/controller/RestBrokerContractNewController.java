/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  9.     		김찬영            최초 생성
 * 2025. 7. 10.     		김찬영            수정.
 * 2025. 7. 11.     		김찬영            패키지 고침.
 * 2025. 7. 17.				김찬영			  계약생성 끝나기 일보직전^0^
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.FileToMultipartFileUtil;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.parse.SafeParse;
import kr.or.ddit.util.pdf.service.PDFService;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.StandardLeaseFormDTO;
import kr.or.ddit.vo.TenancyVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/cont/new")
public class RestBrokerContractNewController {

	@Autowired
	BrokerAuthUnpackingService authService;
	@Autowired
	BrokerContractService contService;
	@Autowired
	AES256Util aes256Util;
	@Autowired
	PDFService pdfService;
	@Autowired
	FileService fileService;
	@Autowired
	BrokerAuthUnpackingService authUnpack;

	@GetMapping("/listing")
	public List<ListingVO> lstgListForContract(Principal principal) {
		BrokerVO broker = authService.getRealUser(principal);
		log.error("{}", broker);
		List<ListingVO> lstgList = contService.readLstgListForContract(broker.getMbrCd());
		return lstgList;
	}

	@PostMapping("/listing")
	public Map<String, String> listingList(
			Principal principal,
			@RequestBody Map<String, String> payload
	) {
		String iv = payload.get("iv");
		BrokerVO broker = authService.getRealUser(principal);
		List<ListingVO> lstgList = contService.readLstgListForContract(broker.getMbrCd());

		ObjectMapper mapper = new ObjectMapper();
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

		try {
			String resultJson = mapper.writeValueAsString(lstgList);
			Map<String, String> encryptedResponse = aes256Util.encryptWithDynamicIV(resultJson);
			return encryptedResponse;
		} catch (Exception e) {
			throw new RuntimeException("응답 암호화 실패", e);
		}
	}

	@PostMapping("/lessee")
	public List<ListingWishlistVO> lesseeForContract(@RequestBody Map<String, String> requestBody) {
		String lstgId = requestBody.get("lstgId");
		List<ListingWishlistVO> wishlist = contService.readLesseeVolunteerList(lstgId);
		return wishlist;
	}
	
	@PostMapping("/lessor")
	public List<TenancyVO> lessorForContract(@RequestBody Map<String, String> payload) {
		String iv = payload.get("iv");
		String encrypted = payload.get("encrypted");
		if (encrypted == null)
			throw new IllegalArgumentException("암호화된 요청 없음");
		String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		String rentalPtyId = "";
		try {
			Map<String, String> parsedRequest = mapper.readValue(decryptedJson, new TypeReference<>() {});
			rentalPtyId = String.valueOf(parsedRequest.get("rentalPtyId"));
		} catch(JsonProcessingException e) { e.printStackTrace(); }
		
		log.debug("------<><><><> {}", rentalPtyId);
		
		return contService.readTenancyList(rentalPtyId);
	}
	

	@PostMapping("/submit")
	public ResponseEntity<?> encryptedNewContract(Principal principal,
			@RequestBody Map<String, String> payload) {
		try {
			/** 1. 복호화 */
			String iv = payload.get("iv");
			String encrypted = payload.get("encrypted");
			if (encrypted == null)
				throw new IllegalArgumentException("암호화된 요청 없음");
			String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);

			/** 2. JSON -> POJO 매핑 */
			ObjectMapper mapper = new ObjectMapper();
			mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
			mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
			mapper.registerModule(new JavaTimeModule());
			mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

			Map<String, Object> parsedRequest = mapper.readValue(decryptedJson, new TypeReference<>() {
			});
			StandardLeaseFormDTO contractInfo = mapper.convertValue(parsedRequest.get("contractInfo"),
					StandardLeaseFormDTO.class);
			List<Map<String, String>> base64Files = mapper.convertValue(parsedRequest.get("base64Files"),
					new TypeReference<List<Map<String, String>>>() {
					});
			List<FileVO> files = mapper.convertValue(parsedRequest.get("files"), new TypeReference<List<FileVO>>() {
			});
			log.info(">ㅂ< contractInfo {}", contractInfo);
			log.info(">ㅂ< files {}", files);

			/** 3. 계약서류 통합PDF 만들기 */
			List<MultipartFile> multipartFiles = new ArrayList<>();
			for (Map<String, String> fileMap : base64Files) {
				String fileName = fileMap.get("name");
				String content = fileMap.get("content");
				String savedFileName = "";

				// Base64 문자열에서 MIME 헤더(data URI scheme) 제거 후
				// -> BINARY 데이터로 디코딩
				// -> MultipartFile 객체로 감싸 파일화.
				String base64 = content.contains(",") ? content.split(",")[1] : content;
				byte[] data = Base64.getDecoder().decode(base64);
				MultipartFile multipartFile = new Base64DecodedMultipartFile(data, savedFileName, fileName,
						Files.probeContentType(Paths.get(fileName)));
				multipartFiles.add(multipartFile);
			}
			File merged = pdfService.mergeToSinglePdf(multipartFiles);
			MultipartFile multipartMerged = FileToMultipartFileUtil.convert(merged);
			

			
			/** 4. DB에 계약정보 레코드 입력 */
			ContractVO contract = ContractVO.builder()
					.mbrCd(String.valueOf(contractInfo.getLesseeMbrCd()))
					.mbrCdBrok(authUnpack.getMbrCd(principal.getName()))
					.lstgId(contractInfo.getListingId())
					.contTypeCode(contractInfo.getListingTypeCode1())
					.contDeposit(/*deposit*/
							SafeParse.safeParseLong(
								Optional.ofNullable(contractInfo.getListingTypeCode1())
							    .map(type -> {
							        // 전세 계약일 경우
							        if ("001".equals(type)) {
							            return contractInfo.getListingLease(); // 전세금 (String)
							        }
							        // 월세 계약일 경우
							        if ("002".equals(type)) {
							            return contractInfo.getListingLeaseAmt(); // 보증금 (String)
							        }
							        // 매칭되는 타입이 없으면 null 반환
							        return null;
							    })
							    .orElse("0") // null이면 "0"으로 대체
							)
					)
					.contTaxAmount(null)
					.contAmount(SafeParse.safeParseLong(contractInfo.getListingLeaseM()))
					.contStatCd("001")
					.contDtm(null)
					.contTypeGroupCd(null)
					.contStatGroupCd(null)
					.build();
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingTypeCode1());
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingLease());
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingLeaseAmt());
			log.debug("(ಥ﹏ಥ) {}", contractInfo.getListingLeaseM());
			log.debug("(ಥ﹏ಥ) {}", contract);
			String contId = contService.createProceedingContract(contract);
			
			/** 5. S3 파일서버에 통합PDF 업로드 및 DB에 파일정보 레코드 입력 */
			FileVO result = fileService.uploadAndSave(multipartMerged, "contract", "CONTR", contId,
					multipartMerged.getContentType());
			
			/** DEBUG__병합된 파일 디렉토리에서 확인하기 ^0^ */
			File debugCopy = new File("D:/debug/merged-" + System.currentTimeMillis() + ".pdf");
			Files.copy(merged.toPath(), debugCopy.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			
			/** 0. 응답신호 내보내기 */
	        return ResponseEntity.ok(Map.of(
	            "success", true,
	            "mergedPath", merged.getAbsolutePath(),
	            "debugPath", debugCopy.getAbsolutePath(),
	            "contId", contId
	        ));
		} catch (Exception e) {
			e.printStackTrace();
			/*  0. 응답신호 내보내기 */
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
	            .body(Map.of("success", false, "error", e.getMessage()));
		}
	}
	
	
	//나중에 이런 식으로 리팩토링 하고 싶다.
	//아니 차라리 DB를 다시 짠다.
	/**
	 * 계약 유형(listingTypeCode1)에 따라 보증금 값을 결정함.
	 * - "001" → 전세금(listingLease)
	 * - "002" → 보증금(listingLeaseAmt)
	 * - 나머지 또는 값 없음 → 0
	 *
	 * @param contractInfo 계약정보 DTO
	 * @return 보증금(Long)
	 */
	private Long resolveDepositFromContractInfo(StandardLeaseFormDTO contractInfo) {
	    return Optional.ofNullable(contractInfo.getListingTypeCode1())
	        .map(type -> {
	            if ("001".equals(type)) return contractInfo.getListingLease();
	            if ("002".equals(type)) return contractInfo.getListingLeaseAmt();
	            return null;
	        })
	        .map(SafeParse::safeParseLong)
	        .orElse(0L);
	}


}
