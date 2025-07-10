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
package kr.or.ddit.broker.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ListingVO;
import kr.or.ddit.vo.ListingWishlistVO;
import kr.or.ddit.vo.MemberVO;

/**
 * 
 * @author developer_KCY
 * @since
 * @see
 *
 *
 */
public interface BrokerContractService {
	/**
	 * @param principal 내에서 불러온 Broker의 mbrCd
	 * @return Broker가 가진 매물(LSTG)의 리스트
	 */
	public List<ListingVO> readLstgListForContract(String mbrCd);
	/**
	 * @param partyInfoParams :Map.of("lstgId",lstgId,"lesseeCd",lesseeCd);
	 * @return 중개인, 임대인, 임차인 세 명에 대한 정보를 담은 Map
	 */
	public Map<String, Object> readContractPartyInfo(Map<String, String> partyInfoParams /*lstgId, mbrCd(lessee)*/);
	
	/**
	 * @param lstgId: 한 매물에 좋아요 누른 사람들
	 * @return 
	 */
	public List<ListingWishlistVO> readLesseeVolunteerList(String lstgId);
	

}
