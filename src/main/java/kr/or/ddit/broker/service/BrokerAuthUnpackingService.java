/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. ?.     			김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.service;

import java.security.Principal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.or.ddit.broker.mapper.BrokerAuthUnpackingMapper;
import kr.or.ddit.vo.BrokerVO;

/**
 * @author developer_KCY 
 */
@Component
public class BrokerAuthUnpackingService implements HandlerInterceptor {
	
	@Autowired
	BrokerAuthUnpackingMapper mapper;
	
	public String getMbrCd(String username) {
		String mbrCd = "";
		mbrCd = mapper.selectMbrCdByUsername(username);
		return mbrCd;
	}
	
	public BrokerVO getBroker(String username) {
		return mapper.selectBrokerByUsername(username);
	}
	
	public BrokerVO getRealUser(Principal principal) {
		return mapper.selectBrokerByUsername(principal.getName());
	}
	
}
