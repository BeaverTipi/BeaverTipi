package kr.or.ddit.main.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.fail;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.SolutionSubscriptionVO;
import kr.or.ddit.vo.SolutionVO;
import kr.or.ddit.vo.TenancyVO;
@SpringBootTest
@Transactional
class SubscribeSubscriptionMapperTest {
	// 테스트 완료
	@Autowired
	SubscribeSubscriptionMapper mapper;
	
	@Test
	void testSelectSolutionList() {
		List<SolutionVO> solList = mapper.selectSolutionList();
		assertNotNull(solList);
	}

	@Test
	void testSelectSolution() {
		SolutionVO sol = mapper.selectSolution("SOL-A001");
		assertNotNull(sol);
	}

	@Test
	void testSelectSolutionSubscription() {
		SolutionSubscriptionVO solSub = mapper.selectSolutionSubscription("M2507000002");
		assertNotNull(solSub);
	}

	@Test
	void testSelectCommonCodeSolutionList() {
		List<SolutionVO> solList = mapper.selectCommonCodeSolutionList("001");
		assertNotNull(solList);
	}
	
	@Test
	void testInsertSolutionSubscription() {
		SolutionSubscriptionVO solSub = new SolutionSubscriptionVO();
		solSub.setMbrCd("M2507000015");
		solSub.setSolId("SOL-A001");
		int cnt = mapper.insertSolutionSubscription(solSub);
		assertEquals(cnt, 1);
	}
	
	@Test
	void testInsertBroker() {
		BrokerVO broker = new BrokerVO();
		broker.setMbrCd("M2507000015");
		int cnt = mapper.insertBroker(broker);
		assertEquals(cnt, 1);
	}

	@Test
	void testInsertTenancy() {
		TenancyVO tenancy = new TenancyVO();
		tenancy.setRentalPtyId("2025070077");
		tenancy.setRentalPtyBizRegNo("1234574404");
		tenancy.setRentalPtyRegBldgCnt(8);
		tenancy.setRentalPtyAcctNo("110658207596");
		tenancy.setLsrYnTypeCd("001");
		tenancy.setRentalPtyBankNm("하나은행");
		tenancy.setMbrCd("M2507000064");
		int cnt = mapper.insertTenancy(tenancy);
		assertEquals(cnt, 1);
	}

}
