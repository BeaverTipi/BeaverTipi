package kr.or.ddit.broker.lstg.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.BrokerAuthUnpackingUtility;
import kr.or.ddit.broker.lstg.service.BrokerLstgService;
import kr.or.ddit.vo.ListingPackVO;
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
	public List<ListingPackVO> lstgList(
			Principal principal
	) {
		String username = principal.getName();
		log.error("{}", username);
		String mbrCd = authUnpack.getMbrCd(username);
		List<ListingPackVO> lstgList= service.readLstgListByMbrCd(mbrCd);
		log.error("{}", lstgList);
		return lstgList;
	}
	
	@GetMapping("/listing-details/{lstgId}")
	public ListingPackVO lstgDetails(
			Principal principal,
			@PathVariable String lstgId
	) {
		
		String username = principal.getName();
		log.error("Handler::lstgDetails() -> username: {}", username);
		String mbrCd = authUnpack.getMbrCd(username);
		
		Map<String, String> lstgDetailsParams = Map.of("mbrCd", mbrCd, "lstgId", lstgId);
		ListingPackVO lstgDetails = service.readLstgDetails(lstgDetailsParams);
		log.error("{}", lstgDetails);
		return lstgDetails;
	}
}
