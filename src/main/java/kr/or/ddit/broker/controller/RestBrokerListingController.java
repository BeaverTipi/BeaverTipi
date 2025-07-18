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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.FacilityOptionVO;
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
		log.error("{}", username);
		String mbrCd = authUnpack.getMbrCd(username);
		
		
		List<ListingVO> lstgList= service.readLstgList(mbrCd);
		log.error("{}", lstgList);
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


}
