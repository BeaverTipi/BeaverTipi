/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영          최초 생성
 * 2025. 7. 11.     		김찬영          패키지 고침.
 * 2025. 7. 17.				김찬영			계약등록 프로세스 완료^0^
 *
 * </pre>
 */
package kr.or.ddit.broker.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerContractService;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.TenancyVO;
import lombok.RequiredArgsConstructor;

/**
 * @author developer_KCY
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

	@Override
	public List<TenancyVO> readTenancyList(String rentalPtyId) {
		List<TenancyVO> tenancyList = null;
		tenancyList = mapper.selectTenancyInfo(rentalPtyId);
		return tenancyList;
	};

	
	/**
	 *	@param principal 내에서 불러온 Broker의 mbrCd
	 *	@return Broker가 가진 계약(CONTRACT)의 리스트
	 */
	@Override
	public List<ContractVO> readContractList(String mbrCd) {
		List<ContractVO> contractList = null;
		
		return contractList;
	}
	
	/**
	 * @return 계약ID를 돌려줘서 그걸로 계약파일 이름을 짓는 게 낫지 않나
	 */
	@Override
	public String createProceedingContract(ContractVO contract) {
		int rec = mapper.insertProceedingContract(contract);
		if(rec == 0) return "failed";
		return contract.getContId();
	};
	
	/**
	 * @param contId: 방금 계약 등록된 매물의 상태 비활성화로 변경
	 * @return
	 */
	public String modifyListingProdStat(String contId) {
		int rec = mapper.updateListingProdStat(contId);
		if(rec == 1) return "SUCCESS";
		else return "FAILED";
	}

	@Override
	public List<ContractVO> readProceedingContractsList(String mbrCd) {
		List<ContractVO> proceedingContractsList = null;
		proceedingContractsList = mapper.selectProceedingContractsList(mbrCd);
		return proceedingContractsList;
	}


	@Override
	public ResponseEntity<?> processOfCreatingContract() {
		// TODO 컨트롤러 다이어트 들어가야지...
		return null;
	}
}
