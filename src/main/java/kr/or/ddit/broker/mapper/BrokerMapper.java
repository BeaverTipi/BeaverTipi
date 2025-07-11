/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. 9.     			김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import kr.or.ddit.vo.BrokerVO;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.MemberVO;
import kr.or.ddit.vo.TenancyVO;

/**
 * @author developer_KCY
 */
@Mapper
public interface BrokerMapper {

	public TenancyVO selectTenancyInfo(String rentalPtyId);
	public BrokerVO selectBrokerInfo(String mbrCd);
	public MemberVO selectLesseeInfo(String mbrCd);

	/**
	 * @param mbrCd :중개인의 멤버코드
	 * @return 중개인의 매물 리스트
	 */
	public List<ListingVO> selectLstgList(String mbrCd);
	/**
	 * @param mbrCd :중개인의 멤버코드
	 * @return 중개 가능한 상태의 매물 리스트
	 */
	public List<ListingVO> selectLstgListForContract(String mbrCd);
	/**
	 * @param lstgId
	 * @return 한 매물에 대한 상세정보 (중개인 본인이 올린 경우에만 조회)
	 */
	public ListingVO selectLstgDetails(Map<String, String> param);
	public List<ListingWishlistVO> selectWishlistForLessee(String lstgId);
	
	
	public List<CommonCodeVO> selectBankList();
	public List<CommonCodeVO> selectLesserTypeList();
    public List<CommonCodeVO> selectCommonCodeByGroup(@Param("codeGroup") String codeGroup);

}
