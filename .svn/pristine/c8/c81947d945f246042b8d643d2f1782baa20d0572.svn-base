package kr.or.ddit.broker;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import kr.or.ddit.broker.mapper.BrokerAuthUnpackingMapper;
import kr.or.ddit.vo.BrokerVO;

@Component
public class BrokerAuthUnpackingUtility implements HandlerInterceptor {
	
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
}
