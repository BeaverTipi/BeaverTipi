package kr.or.ddit.broker.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.util.crypto.AES256Util;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/contract")
public class RestContractSignatureController {

	@Autowired
	private AES256Util aes256Util;
	@Autowired
	private BrokerContractService contService;
	
//	@GetMapping("/{encryptedContId}")
//	public ResponseEntity<?> redirectToSignPage(@PathVariable String encryptedContId){
//		try {
//	        String contId = aes256Util.decrypt(encryptedContId); // 예시
//	        if (!contService.isContractExist(contId)) {
//	            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//	                .body("존재하지 않는 계약입니다.");
//	        }
//
//	        String frontendUrl = "https://dev.beavertipi.com/contract/" + encryptedContId;
//	        return ResponseEntity.status(HttpStatus.FOUND)
//	                .location(URI.create(frontendUrl)).build();
//
//	    } catch (Exception e) {
//	        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
//	            .body("잘못된 접근입니다.");
//	    }
//	}
	
	@GetMapping("/{encryptedContId}")
	public ResponseEntity<Void> redirectToReactSignaturePage(@PathVariable String encryptedContId) {
	    String frontendUrl = "https://dev.beavertipi.com/contract/" + encryptedContId;
	    log.debug("여기를 들렸다.>!!!! [RestContractSignatureController]::", frontendUrl);
	    return ResponseEntity.status(HttpStatus.FOUND)
	        .location(URI.create(frontendUrl))
	        .build();
	}
}
