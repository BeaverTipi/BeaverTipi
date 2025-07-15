package kr.or.ddit.broker.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.mapper.BrokerAuthUnpackingMapper;
import kr.or.ddit.broker.service.BrokerAuthUnpackingService;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.ContractVO;

@RestController
@RequestMapping("/rest/broker/myoffice/cont/mng")
public class RestBrokerContractMngController {

	@Autowired
	BrokerAuthUnpackingService authUnpack;
	@Autowired
	AES256Util aes256Util;
	@Autowired
	BrokerContractService service;
	
	@GetMapping("/list")
	public List<ContractVO> contractList() {
		List<ContractVO> contractList = null;
		return contractList;
	}
}
