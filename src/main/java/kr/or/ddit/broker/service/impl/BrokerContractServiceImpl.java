/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영            최초 생성
 *
 * </pre>
 */
package kr.or.ddit.broker.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import lombok.RequiredArgsConstructor;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
@Service
@RequiredArgsConstructor
public class BrokerContractServiceImpl implements BrokerContractService {

	private final BrokerMapper mapper;
	
	/**
	 * @param principal 내에서 불러온 Broker의 mbrCd
	 * @return Broker가 가진 매물(LSTG)의 리스트
	 */
	@Override
	public List<ListingVO> readLstgListForContract(String mbrCd) {
		List<ListingVO> lstgList = mapper.selectLstgListForContract(mbrCd);
		return lstgList;
	}

	/**
	 * @param partyInfoParams :Map.of("lstgId",lstgId,"lesseeCd",lesseeCd);
	 * @return 중개인, 임대인, 임차인 세 명에 대한 정보를 담은 Map
	 */
	@Override
	public Map<String, Object> readContractPartyInfo(Map<String, String> partyInfoParams) {
		Map<String, Object> contractPartyInfo = null;
		return contractPartyInfo;
	}

	/**
	 *
	 */
	@Override
	public List<ListingWishlistVO> readLesseeVolunteerList(String lstgId) {
		List<ListingWishlistVO> lesseeVolunteerList = null;
		lesseeVolunteerList = mapper.selectWishlistForLessee(lstgId);
		return lesseeVolunteerList;
	}

}
