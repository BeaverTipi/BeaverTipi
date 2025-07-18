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

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.ListingVO;

@RestController
@RequestMapping("/rest/broker/myoffice/cont/proc")
public class RestBrokerContractProceedingController {

	@Autowired
	BrokerAuthUnpackingService authService;
	@Autowired
	AES256Util aes256Util;
	@Autowired
	BrokerContractService contService;
	
//	@PostMapping("/list")
//	public List<ContractVO> contractList(
//			Principal principal
//			, @RequestBody Map<String, String> payload
//	) {
//		String iv = payload.get("iv");
//		String encrypted = payload.get("encrypted");
//		if (encrypted == null)
//			throw new IllegalArgumentException("암호화된 요청 없음");
////		String decryptedJson = aes256Util.decryptWithDynamicIV(encrypted, iv);
//		
//		List<ContractVO> proceedingContractsList = null;
//		proceedingContractsList = contService.readProceedingContractsList(principal.getName());
//		responseBody = aes256Util.encryptWithDynamicIV(proceedingContractsList);
//
//		return contractList;
//	}
}
