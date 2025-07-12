/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 10.     		김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.controller;

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

import kr.or.ddit.broker.service.BrokerCommonCodeService;
import kr.or.ddit.util.crypto.AES256Util;
import kr.or.ddit.vo.CommonCodeVO;
import lombok.extern.slf4j.Slf4j;

/**
 * @author developer_KCY
 */
@Slf4j
@RestController
@RequestMapping("/rest/broker/myoffice/form")	//form uri는 read와 동일
public class RestBrokerFormUIController {

	@Autowired
	BrokerCommonCodeService codeService;
	@Autowired
	AES256Util aes256Util;
	
	/** /rest/broker/myoffice/form/bankList
	 * @return List<'BANK'>
	 */
	@GetMapping("/bankList")
	public List<CommonCodeVO> bankList() {
		log.debug("GET/rest/broker/myoffice/form/bankList 실행...");
		
		return codeService.readBankList();   
	}
	//fsdaf
	
	/** /rest/broker/myoffice/form/lesserTypeList
	 * @return List<'LSR'>
	 */
	@GetMapping("/lesserTypeList")
	public List<CommonCodeVO> lesserTypeList() {
		log.debug("GET/rest/broker/myoffice/form/lesserTypeList 실행...");
		return codeService.readLesserTypeList();
	}
	
	/**	/rest/broker/myoffice/form
	 * @param encrypted-payload with iv
	 * @return
	 */
	@PostMapping
	public Map<String, String> encryptedCommonCode(
			@RequestBody Map<String, String> payload
	) {
		Map<String, Object> parsedRequest = aes256Util.decryptPayloadToMap(payload);
		
		Object obj = parsedRequest.get("codeGroup");
		if( !(obj instanceof Map)) throw new IllegalArgumentException("codeGroup 누락 또는 형식 오류");
		Map<String, String> codeGroupParams = (Map<String, String>) parsedRequest.get("codeGroup");
		log.debug("---------------> {}", codeGroupParams);
	    
	    Map<String, List<CommonCodeVO>> resultMap = codeService.sortCommonCodes(codeGroupParams);

	    return aes256Util.encryptObjectToPayload(resultMap);
	}
}
