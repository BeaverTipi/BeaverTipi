/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영          최초 생성
 * 2025. 7. 11.     		김찬영          패키지 고침.
 * 2025. 7. 15.				김찬영			insert계약
 * 2025. 7. 17.				김찬영			계약등록 완료^0^
 * </pre>
 */
package kr.or.ddit.broker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.broker.dto.SignerDTO;
import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ContractDigitalSignVO;
import kr.or.ddit.vo.ContractVO;
import kr.or.ddit.vo.FileVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.SignatureVO;
import kr.or.ddit.vo.SignerVO;
import kr.or.ddit.vo.TenancyVO;

/**
 * @author developer_KCY
 */
/**
 * 
 * @author 
 * @since
 * @see
 *
 *
 */
@Mapper
public interface BrokerMapper {

	/** 안 쓰고 싶었는데 필요해졌다.
	 * @param rentalPtyId
	 * @return
	 */
	public List<TenancyVO> selectTenancyInfo(String rentalPtyId);
	

	/**
	 * @param mbrCd :중개인의 멤버코드
	 * @return 중개인의 매물 리스트
	 */
	public List<ListingVO> selectLstgList(String mbrCd);
	public List<ListingVO> selectLstgListWithoutMbrCd();
	/**
	 * @param mbrCd :중개인의 멤버코드
	 * @return 중개 가능한 상태의 매물 리스트
	 */
	public List<ListingVO> selectLstgListForContract(String mbrCd);
	/**
	 * @param lstgId
	 * @return 한 매물에 대한 상세정보 (중개인 본인이 올린 경우에만 조회)
	 */
	public ListingVO selectLstgDetails(ListingVO listing);
	public List<ListingWishlistVO> selectWishlistForLessee(String lstgId);
	public ListingVO selectLstgDetailsById(String lstgId);
	
	/**
	 * @param mbrCd: 중개인의 멤버코드
	 * @return 중개인이 관리하는 계약의 기본정보 리스트
	 */
	public List<ContractVO> selectContractList(String mbrCd);
	
	/**
	 * @param contract: [새 전자계약 진행]에서 정상적으로 등록한 계약 정보
	 * @return 성공: 1, 실패: 0
	 */
	public int insertProceedingContract(ContractVO contract);
	
	/**
	 * @param lstgId: 방금 계약 등록한 매물을 비활성화 상태로 변경하기 위한 식별자
	 * @return 성공: 1, 실패: 0
	 */
	public int updateListingProdStat(String lstgId);
	
	
	/**
	 * @param params: mbrCd
	 * @return 그냥 계약 리스트
	 */
	public List<ContractVO> selectMngContractsList(String mbrCd);
	
	/**
	 * @param params: mbrCd && contStatCd "001"
	 * @return 진행중인 계약 리스트
	 */
	public List<ContractVO> selectProceedingContractsList(String mbrCd);
	
	/**
	 * 단건 삭제 매퍼
	 * @param contId
	 * @return
	 */
	public int deleteProceedingContract(String contId);
	public int updateProceedingContractSignYnToY(String contId);
	public int updateProceedingContractSignYnToN(String contId);
	/**
	 * 서명페이지 개설 여부 컬럼값 반환
	 * @param contId
	 * @return
	 */
	public String selectContractSignatureYn(String contId);
	public ContractVO selectContractInfo(String contId);
	public boolean isContractExist(@Param("contId") String contId);
	
	public FileVO selectContractFile(String contId); 
	
	public SignatureVO selectSignature(String contId);
	public int existsSignature(String contId);
	public int insertSignature(SignatureVO signature);
	public int updateSignature(SignatureVO signature);
	public SignerDTO selectSigner(Map<String, String> contIdAndRole);
	public int existsSigner(Map<String, String> contIdAndRole);
	public int insertSigner(SignerDTO singer);
	public int updateSigner(SignerDTO signer);
	
	public FileVO selectLatestSignedContractPdf(String contId);
	
	public List<CommonCodeVO> selectBankList();
	public List<CommonCodeVO> selectLesserTypeList();
    public List<CommonCodeVO> selectCommonCodeByGroup(@Param("codeGroup") String codeGroup);
    
    public BrokerVO selectBrokerInfo(String mbrCd);
    public MemberVO selectLesseeInfo(String mbrCd);
    public MemberVO selectMemberByTelno(String telno);


	/**
	 * contId로 계약에 속한 전자서명들(ContractDigitalSignVO)을 모두 조회
	 * @param contId
	 * @return
	 */
	public List<ContractDigitalSignVO> selectDtSignList(String contId);
	
	public int updateConclusedContract(String contId);
	
}
