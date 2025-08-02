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
import java.util.Set;

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

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.building.account.service.TenancyAccountService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.file.Base64DecodedMultipartFile;
import kr.or.ddit.util.file.ToMultipartFileUtil;
import kr.or.ddit.util.file.service.FileService;
import kr.or.ddit.util.parse.SafeParse;
import kr.or.ddit.util.pdf.service.PDFService;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.TenancyAccountVO;
import kr.or.ddit.broker.dto.StandardLeaseFormDTO;
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
	@Autowired
	ObjectMapper objectMapper;
	@Autowired
	TenancyAccountService taService;

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
		log.debug("-------<><><><><><><><>---------<><><><> {}", wishlist);
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
		
//		log.debug("------<><><><> {}", rentalPtyId);
		return contService.readTenancyList(rentalPtyId);
	}
	
	@PostMapping("/lessorAcc")
	public TenancyAccountVO lessorAccount(@RequestBody Map<String, String> payload) {
		String iv = payload.get("iv");
		String encrypted = payload.get("encrypted");
		if(encrypted==null) { throw new IllegalArgumentException("암호화된 요청 없음");}
		String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
		
		ObjectMapper mapper = new ObjectMapper();
		mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
		mapper.configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);
		mapper.registerModule(new JavaTimeModule());
		mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
		
		String rentalPtyId = "";
		TenancyAccountVO vo = null;
		try {
			Map<String, String> parsedRequest = mapper.readValue(decryptedJson, new TypeReference<>() {});
			rentalPtyId = String.valueOf(parsedRequest.get("rentalPtyId"));
			List<TenancyVO> tenancyList = contService.readTenancyList(rentalPtyId);
//			TenancyVO tenancy = tenancyList.get(0);
			String mbrCd = tenancyList.get(0).getMbrCd();
			List<TenancyAccountVO> list = taService.retrieveAccountList(mbrCd);
			vo = list.get(0);
		} catch(JsonProcessingException e) { e.printStackTrace(); }
		
		return vo;
		
	}
	
	@PostMapping("/submit")
	public ResponseEntity<?> encryptedNewContract(
			Principal principal,
			@RequestBody Map<String, String> payload
	) {
		/** 1. 복호화 */
		String iv = payload.get("iv");
		String encrypted = payload.get("encrypted");
		if (encrypted == null)
			throw new IllegalArgumentException("암호화된 요청 없음");
		String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
		
		try {
	        return contService.processOfCreatingContract(decryptedJson, principal);
	    } catch (JsonProcessingException e) {
	        log.error("[Controller] JSON 파싱 실패", e);
	        return ResponseEntity.badRequest().body("잘못된 JSON 데이터입니다.");
	    } catch (Exception e) {
	        log.error("[Controller] 계약 처리 실패", e);
	        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("서버 오류 발생");
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
