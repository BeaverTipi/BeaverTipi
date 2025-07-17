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
package kr.or.ddit.broker.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerMapper;
import kr.or.ddit.broker.service.BrokerListingService;
import kr.or.ddit.vo.ListingVO;

/**
 * @author developer_KCY
 */
@Service
public class BrokerListingServiceImpl implements BrokerListingService{

	@Autowired
	BrokerMapper mapper;

	@Override
	public List<ListingVO> readLstgList(String mbrCd) {
		List<ListingVO> lstgList = mapper.selectLstgList(mbrCd);
		return lstgList;
	}
	
	public ListingVO readLstgDetails(ListingVO listing) {
		ListingVO lstg = mapper.selectLstgDetails(listing);
		return lstg;
	}
	
}
