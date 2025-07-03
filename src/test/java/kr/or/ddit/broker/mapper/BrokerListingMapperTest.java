package kr.or.ddit.broker.mapper;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.TenancyVO;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
class BrokerListingMapperTest {

	@Autowired
	BrokerListingMapper mapper;
	
	@Test
	void testSelectLstgListByMbrCd() {
		String mbrCd = "M2507000110";
		List<ListingVO> lstgList = mapper.selectLstgListByMbrCd(mbrCd);
		log.error("{}", lstgList);
		log.error("{}개의 매물 출현;", lstgList.size());
		lstgList.forEach(lstg -> {
			log.info("{}", lstg.getLstgId());
			log.info("{}", lstg.getLstgNm());
			TenancyVO tenancy = lstg.getTenancyInfo();
			log.info("{}", tenancy.getMbrNm());
			log.info("{}", tenancy.getMbrCd());
			log.info("{}", tenancy.getRentalPtyId());
			BrokerVO broker = lstg.getBrokerInfo();
			log.info("{}", broker.getBrokNm());
			log.info("{}", broker.getBrokRegNo());
			log.info("{}", broker.getMbrNm());
			log.info("{}", broker.getMbrCd());
			log.info("------------------------");
		});
	}

}
