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

import com.fasterxml.jackson.core.JsonProcessingException;

import jakarta.servlet.http.HttpServletRequest;
import kr.or.ddit.broker.dto.SignerDTO;
import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
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
	
	public List<Map<String, Object>> readContractPartyInfo(Map<String, String> partyInfoParams);
	public Map<String, SignerDTO> readContractPartyInfo2(Map<String, String> partyInfoParams, HttpServletRequest request);
	
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
	 * @throws JsonProcessingException 
	 */
	public ResponseEntity<?> processOfCreatingContract(String decryptedJson, Principal principal) throws JsonProcessingException;
	
	public FileVO readContractPDFFile(String contId);
	
	/**
	 * 계약 정보 단건 삭제
	 * @param selectedContractIds
	 * @return
	 */
	public int removeProceedingContractBulk(List<String> selectedContractIds);
	
	
	/**
	 * 서명페이지 개설
	 * @param contId
	 * @return
	 */
	public int openContractSignaturePage(String contId);
	/**
	 * 일정 시간 후 알아서 닫히게끔
	 * @param contId
	 * @return
	 */
	public int expireContractSignaturePage(String contId);
	/**
	 * 서명페이지 개설 여부 확인
	 * @param contId
	 */
	public String isSignPageOpened(String contId);
	
	/**
	 * 인가처리 후 계약페이지 넘어갈 때 만들어봄
	 * @param contId
	 * @return
	 */
	public boolean isContractExist(String contId);
	
	public ContractVO readContractInfo(String contId);
	
	public List<Map<String, Object>> validateSignerStatus(String contId);
	
	
	/**
	 * contId에 대한 전자서명 정보를 조회, List로 반환하는 단순 조회
	 * @param contId
	 * @return List<ContractDigitalSignVO>
	 */
	public List<ContractDigitalSignVO> readSignatureList(String contId);
}
