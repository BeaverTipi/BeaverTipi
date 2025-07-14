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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.vo.ListingVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/lstg")
public class RestBrokerListingController {

	@Autowired
	BrokerListingService service;
	
	@Autowired
	BrokerAuthUnpackingService authUnpack;
	
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
	
	@GetMapping("/listing-details")
	public ListingVO lstgDetails(
			Principal principal,
			@RequestBody String lstgId
	) {
		
		String username = principal.getName();
		log.error("Handler::lstgDetails() -> username: {}", username);
		String mbrCd = authUnpack.getMbrCd(username);
		
//		Map<String, String> lstgDetailsParams = Map.of("mbrCd", mbrCd, "lstgId", lstgId);
//		ListingPackVO lstgDetails = service.readLstgDetails(lstgDetailsParams);
		
		
		log.error("{}", lstgId);
		return null;
	}
}
