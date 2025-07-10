package kr.or.ddit.broker.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.ListingVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BrokerMapperTest {

	@Autowired
	BrokerMapper mapper;
	
	/**
	 * 	계약을 생성하기 위해 활성화된 매물 리스트 불러오는 시나리오.
	 */
	@Test
	void testSelectLstgListForContract() {
		String mbrCdBrok = "M2507000110";
		List<ListingVO> lstgList = mapper.selectLstgListForContract(mbrCdBrok);
		log.info("조회된 매물 수: {}", lstgList.size());
		lstgList.forEach(lstg -> {
			log.info("-------------------");
			log.info("매물명: {}", lstg.getLstgNm());
			log.info("중개인: {}", lstg.getBrokerInfo().getMbrNm());
			if(lstg.getTenancyInfo()!=null) log.info("임대인: {}", lstg.getTenancyInfo().getMbrNm()); else log.info("임대인: 정보없음");
			log.debug("{}", lstg.getBrokerInfo());
			if(lstg.getTenancyInfo()!=null) log.debug("{}", lstg.getTenancyInfo()); else log.info("임대인: 정보없음");
			log.debug("{}", lstg.getFacOptions());
		});
	}

	@Test
	void testSelectTenancyInfo() {
		fail("Not yet implemented");
	}

	@Test
	void testSelectLesseeInfo() {
		fail("Not yet implemented");
	}

	@Test
	void testSelectBrokerInfo() {
		fail("Not yet implemented");
	}

	@Test
	void testSelectLstgInfo() {
		fail("Not yet implemented");
	}

}
