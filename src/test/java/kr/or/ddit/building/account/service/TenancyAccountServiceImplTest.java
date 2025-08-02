package kr.or.ddit.building.account.service;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.TenancyAccountVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class TenancyAccountServiceImplTest {
	
	@Autowired
	private TenancyAccountService service;

	@Test
	void testRetrieveAccountList() {
		TenancyAccountVO vo = null;
		List<TenancyAccountVO> list = service.retrieveAccountList("M2508000003");
		vo = list.get(0);
		log.warn("---------<><><> < {} >", vo);
	}

}
