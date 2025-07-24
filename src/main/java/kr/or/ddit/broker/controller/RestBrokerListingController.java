/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  8.     		김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.util.validate.exception.FileIOException;
import kr.or.ddit.util.validate.exception.ListingException;
import kr.or.ddit.util.validate.exception.ListingOptionException;
import kr.or.ddit.vo.FacilityOptionVO;
import kr.or.ddit.vo.ListingOptionVO;
import kr.or.ddit.vo.ListingVO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/lstg")
@RequiredArgsConstructor
public class RestBrokerListingController {

	private final BrokerListingService  service;
	private final BrokerAuthUnpackingService authUnpack;
	private final ObjectMapper mapper;
	private final AES256Util aes256Util;
	
	@GetMapping("/list")
	public List<ListingVO> lstgList(
			Principal principal
	) {
		String username = principal.getName();
		log.debug("{}", username);
		String mbrCd = authUnpack.getMbrCd(username);
		
		
		List<ListingVO> lstgList= service.readLstgList(mbrCd);
		log.debug("{}", lstgList);
		return lstgList;
	}
	
	@PostMapping("/listing-details")
	public Map<String, String> lstgDetails(Principal principal, @RequestBody Map<String, String> payload) {
	    Map<String, String> parsedRequest = decryptRequestPayload(payload);
	    String lstgId = parsedRequest.get("lstgId");
	    if (lstgId == null) throw new IllegalArgumentException("lstgId 누락");

	    String mbrCd = authUnpack.getMbrCd(principal.getName());
	    ListingVO listing = new ListingVO();
	    listing.setLstgId(lstgId);
	    listing.setMbrCd(mbrCd);

	    ListingVO lstgDetails = service.readLstgDetails(listing);
	    return encryptResponsePayload(lstgDetails);
	}

	
	@PostMapping("/facilityOption")
	public Map<String, String> facilityOption() {
	    List<FacilityOptionVO> facilityOptionList = service.readFacilityOptionList();
	    return encryptResponsePayload(facilityOptionList);
	}

	
	private Map<String, String> decryptRequestPayload(Map<String, String> payload) {
	    String iv = payload.get("iv");
	    String encrypted = payload.get("encrypted");
	    if (encrypted == null) throw new IllegalArgumentException("암호화된 요청 없음");

	    String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
	    try {
	        return mapper.readValue(decryptedJson, new TypeReference<>() {});
	    } catch (Exception e) {
	        throw new RuntimeException("요청 JSON 파싱 실패", e);
	    }
	}
	
	private Map<String, String> encryptResponsePayload(Object responseData) {
	    try {
	        String resultJson = mapper.writeValueAsString(responseData);
	        return aes256Util.encryptWithDynamicIV(resultJson);
	    } catch (Exception e) {
	        throw new RuntimeException("응답 암호화 실패", e);
	    }
	}

	@PostMapping("/product/add")
	public ResponseEntity<?> addListing(
	    @ModelAttribute ListingVO listing,
	    @RequestParam("facilities") List<String> facOptIds,
	    @RequestParam(value = "imageUpload", required = false) List<MultipartFile> imageFiles,
	    Principal principal
	) {
	    try {
	        String username = principal.getName();
	        String mbrCd = authUnpack.getMbrCd(username);
	        listing.setMbrCd(mbrCd);

	        List<ListingOptionVO> optionList = facOptIds.stream().map(id -> {
	            ListingOptionVO vo = new ListingOptionVO();
	            vo.setLstgId(listing.getLstgId());
	            vo.setFacOptId(id);
	            return vo;
	        }).toList();

	        service.createListing(listing, imageFiles, optionList);
	        return ResponseEntity.ok(Map.of("success", true, "message", "매물 등록이 완료되었습니다."));

	    } catch (ListingOptionException e) {
	        log.error("옵션 처리 오류", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "선택한 옵션 처리 중 오류가 발생했습니다."));
	    } catch (ListingException e) {
	        log.error("매물 등록 실패", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "매물 등록 처리 중 오류가 발생했습니다."));
	    } catch (FileIOException e) {
	        log.error("파일 업로드 오류", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "파일 업로드 중 오류가 발생했습니다."));
	    } catch (Exception e) {
	        log.error("알 수 없는 오류", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "예기치 못한 오류가 발생했습니다."));
	    }
	}



	@PostMapping("/product/update")
	public ResponseEntity<?> updateListing(
	    @ModelAttribute ListingVO listing,
	    @RequestParam("facilities") List<String> facOptIds,
	    @RequestParam(value = "imageUpload", required = false) List<MultipartFile> imageFiles,
	    Principal principal
	) {
	    try {
	        String username = principal.getName();
	        String mbrCd = authUnpack.getMbrCd(username);
	        listing.setMbrCd(mbrCd);

	        List<ListingOptionVO> optionList = facOptIds.stream().map(id -> {
	            ListingOptionVO vo = new ListingOptionVO();
	            vo.setLstgId(listing.getLstgId());
	            vo.setFacOptId(id);
	            return vo;
	        }).toList();

	        service.modifyListing(listing, imageFiles, optionList);
	        return ResponseEntity.ok(Map.of("success", true, "message", "매물 수정이 완료되었습니다."));

	    } catch (ListingOptionException e) {
	        log.error("옵션 수정 오류", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "옵션 수정 처리 중 오류가 발생했습니다."));
	    } catch (ListingException e) {
	        log.error("매물 수정 실패", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "매물 수정 처리 중 오류가 발생했습니다."));
	    } catch (FileIOException e) {
	        log.error("파일 처리 오류", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "파일 처리 중 오류가 발생했습니다."));
	    } catch (Exception e) {
	        log.error("알 수 없는 오류", e);
	        return ResponseEntity.status(500).body(Map.of("success", false, "message", "예기치 못한 오류가 발생했습니다."));
	    }
	}

	@PostMapping("/product/delete")
	public Map<String, String> deleteListing(
	    Principal principal,
	    @RequestBody Map<String, String> encryptedPayload
	) {
	    Map<String, String> parsed = decryptRequestPayload(encryptedPayload);
	    String lstgId = parsed.get("lstgId");

	    if (lstgId == null || lstgId.isBlank()) {
	        return encryptResponsePayload(Map.of("success", false, "message", "lstgId 누락"));
	    }

	    String mbrCd = authUnpack.getMbrCd(principal.getName());

	    ListingVO listing = new ListingVO();
	    listing.setLstgId(lstgId);
	    listing.setMbrCd(mbrCd);
	    try {
	        service.removeListing(listing);
	        return encryptResponsePayload(Map.of("success", true, "message", "삭제되었습니다."));
	    } catch (ListingException e) {
	        log.warn("삭제 실패 - 매물 없음 or 삭제 대상 불일치: {}", e.getMessage());
	        return encryptResponsePayload(Map.of("success", false, "message", e.getMessage()));
	    } catch (Exception e) {
	        log.error("매물 삭제 실패 (알 수 없는 오류)", e);
	        return encryptResponsePayload(Map.of("success", false, "message", "삭제 중 오류 발생"));
	    }
	}



}
