package kr.or.ddit.broker.mapper;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.BrokerVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BrokerAuthUnpackingMapperTest {

	@Autowired
	BrokerAuthUnpackingMapper mapper;
	
	@Test
	void testSelectMbrCdByUsername() {
		String mbrId = "openthatsodapop";
		String mbrCd = mapper.selectMbrCdByUsername(mbrId);
		log.info("ID -> CD : {}", mbrCd);
	}
	
	@Test
	void testSelectBrokerByUsername() {
		String mbrId = "openthatsodapop";
		BrokerVO broker = mapper.selectBrokerByUsername(mbrId);
		log.info("{}", broker);
		log.info("{}", broker.getMbrNm());
	}

}
