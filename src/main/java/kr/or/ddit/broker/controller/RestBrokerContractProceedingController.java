package kr.or.ddit.broker.controller;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.ObjectMapper;

import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ContractVO;

@RestController
@RequestMapping("/rest/broker/myoffice/cont/proc")
public class RestBrokerContractProceedingController {

	@Autowired
	BrokerAuthUnpackingService authService;
	@Autowired
	AES256Util aes256Util;
	@Autowired
	BrokerContractService contService;
	
	@PostMapping("/list")
	public Map<String, String> contractList(
			Principal principal
			, @RequestBody Map<String, String> payload
	) {
		List<ContractVO> proceedingContractsList = null;
		
		BrokerVO broker = authService.getRealUser(principal);
		proceedingContractsList = contService.readProceedingContractsList(broker.getMbrCd());
		
	    ObjectMapper mapper = new ObjectMapper();
	    try {
	        String resultJson = mapper.writeValueAsString(proceedingContractsList);
	        Map<String, String> encryptedResponse = aes256Util.encryptWithDynamicIV(resultJson);
	        return encryptedResponse;
	    } catch (Exception e) {
	        throw new RuntimeException("응답 암호화 실패", e);
	    }
	}
}
