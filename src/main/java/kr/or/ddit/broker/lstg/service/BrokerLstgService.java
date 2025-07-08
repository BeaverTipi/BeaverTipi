package kr.or.ddit.broker.lstg.service;

import java.util.List;
import java.util.Map;

import kr.or.ddit.vo.ListingPackVO;

/**
 * @author developer_KCY
 */
public interface BrokerLstgService {

	public List<ListingPackVO> readLstgListByMbrCd(String mbrCd);
	public ListingPackVO readLstgDetails(Map<String, String> lstgDetailParams);
	
	
	//유기하겠슴
	public List<ListingPackVO> readListingList(String mbrCd);
}
