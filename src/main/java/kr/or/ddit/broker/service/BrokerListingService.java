/** 
 * <pre>
 * << 개정이력(Modification Information) >>
 *   
 *   수정일      			수정자           수정내용
 *  -----------   	-------------    ---------------------------
 * 2025. 7. ?.     			김찬영            최초 생성
 * 2025. 7. 11.     		김찬영            패키지 고침.
 *
 * </pre>
 */
package kr.or.ddit.broker.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.ListingVO;

/**
 * @author developer_KCY
 */
public interface BrokerListingService {

	/**
	 * @param Broker의 mbrCd
	 * @return
	 */
	public List<ListingVO> readLstgList(String mbrCd);
	
	public ListingVO readLstgDetails(Map<String, String> param);
}
