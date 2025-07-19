/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7.  9.     		김찬영          최초 생성
 * 2025. 7. 11.     		김찬영          수정
 * 2025. 7. 17.				김찬영			계약등록 프로세스 완료 ^0^
 * </pre>
 */
package kr.or.ddit.broker.service;

import java.security.Principal;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;

import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.TenancyVO;

/**
 * @author developer_KCY
 */
public interface BrokerContractService {
	/**
	 * @param Broker의 mbrCd
	 * @return Broker가 가진 매물(LSTG)의 리스트
	 */
	public List<ListingVO> readLstgListForContract(String mbrCd);

	/**
	 * @param lstgId
	 * @return 매물에 좋아요 누른 회원의 리스트
	 */
	public List<ListingWishlistVO> readLesseeVolunteerList(String lstgId);
	
	public List<TenancyVO> readTenancyList(String rentalPtyId);
	
	public Map<String, Object> readContractPartyInfo(Map<String, String> partyInfoParams);
	
	public List<ContractVO> readContractList(String mbrCd);

	/**
	 * @return 계약ID를 돌려줘서 그걸로 계약파일 이름을 짓는 게 낫지 않나
	 */
	public String createProceedingContract(ContractVO contract);
	
	/**
	 * @param contId: 방금 계약 등록된 매물의 상태 비활성화로 변경
	 * @return
	 */
	public String modifyListingProdStat(String contId);
	
	
	public List<ContractVO> readProceedingContractsList(String mbrCd);
	
	
	
	/** 컨트롤러 다이어트 들어가야지...
	 * @return
	 */
	public ResponseEntity<?> processOfCreatingContract(String decryptedJson, Principal principal);
}
