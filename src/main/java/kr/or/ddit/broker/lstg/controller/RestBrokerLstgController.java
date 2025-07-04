package kr.or.ddit.broker.lstg.controller;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
	
	@GetMapping("/list/{mbrCd}")
	public List<ListingPackVO> lstgList(
//			@PathVariable String mbrCd
			Principal principal
	) {
		String username = principal.getName();
		log.error("{}", username);
//		String mbrCd = "M2507000110";
//		log.error("요청한 BROKER의 MEMBER CODE: {}", mbrCd);
		
//		List<ListingVO> lstgList = service.readLstgListByMbrCd(mbrCd);
//		log.error("BROKER의 LSTG LIST: {}", lstgList);
		return null;
	}
}
