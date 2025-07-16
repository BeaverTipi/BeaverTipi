package kr.or.ddit.main.brokerChat.controller;


import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.main.brokerChat.service.BrokerChatServiceImpl;
import kr.or.ddit.vo.ChatMessageVO;
import lombok.extern.slf4j.Slf4j;

@SpringBootTest
@Slf4j
class BrokerChatControllerTest {

	@Autowired
	BrokerChatServiceImpl service;
	
	@Test
	void getMessage() {
		List<ChatMessageVO> cmVO = service.getMessages("CR00000044");
		cmVO.forEach(m-> {
			log.info("보낸이 : {}, 내용 : {}" , m.getMbrCd(), m.getCmCont());
		});
	}

}
