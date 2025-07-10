package kr.or.ddit.broker.mapper;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.ListingPackVO;
import kr.or.ddit.vo.LstgFacOptVO;
import kr.or.ddit.vo.TenancyVO;
import lombok.extern.slf4j.Slf4j;

/**
 * 	@author developer_KCY
 * 	BrokerListingMapper CLASS의 테스트케이스.
 */
@Slf4j
@SpringBootTest
class BrokerListingMapperTest {

	@Autowired
	BrokerListingMapper mapper;
	
	/**
	 * 	selectLstgListByMbrCd() 메소드
	 * 	2025-07-03 16:01 성공.
	 * 
	 */
	@Test
	void testSelectLstgListByMbrCd() {
		String mbrCd = "M2507000110";
		List<ListingPackVO> lstgList = mapper.selectLstgListByMbrCd(mbrCd);
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
			List<LstgFacOptVO> lstgFacOptList = lstg.getLstgFacOptInfo();
			log.info("시설/옵션 리스트: {}", lstgFacOptList);
			lstgFacOptList.forEach( lfo -> {
				log.info("시설정보");
				log.info("- {}, {}, {}", lfo.getFacOptId(), lfo.getFacOptNm(), lfo.getFacOptDesc());
			});
			log.info("------------------------");
		});
	}

}
