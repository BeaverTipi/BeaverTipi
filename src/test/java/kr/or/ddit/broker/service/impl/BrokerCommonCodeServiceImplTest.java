package kr.or.ddit.broker.service.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.broker.service.BrokerCommonCodeService;
import kr.or.ddit.vo.CommonCodeVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BrokerCommonCodeServiceImplTest {

	@Autowired
	BrokerCommonCodeService service;
	
	@Test
	@DisplayName("[성공]: 은행계좌 입력 시 공통코드로 form 생성하기 위한 테스트")
	void testReadBankList() {
		List<CommonCodeVO> bankList = service.readBankList();
		bankList.forEach(vo -> log.info("{} {} {}", vo.getCodeGroup(), vo.getCodeValue(), vo.getCodeName()));
	}
}
