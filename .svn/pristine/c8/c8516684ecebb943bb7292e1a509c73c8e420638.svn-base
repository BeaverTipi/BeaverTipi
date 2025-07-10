package kr.or.ddit.broker.lstg.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.BrokerAuthUnpackingUtility;
import kr.or.ddit.broker.lstg.service.BrokerLstgService;
import kr.or.ddit.vo.ListingVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/lstg")
public class RestBrokerLstgController {

	@Autowired
	BrokerLstgService service;
	
	@Autowired
	BrokerAuthUnpackingUtility authUnpack;
	
	@GetMapping("/list")
	public List<ListingVO> lstgList(
			Principal principal
	) {
		String username = principal.getName();
		log.error("{}", username);
		String mbrCd = authUnpack.getMbrCd(username);
		
		
		List<ListingVO> lstgList= service.readLstgListByMbrCd(mbrCd);
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
