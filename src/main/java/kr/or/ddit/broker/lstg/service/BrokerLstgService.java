package kr.or.ddit.broker.lstg.service;

import java.util.List;

import kr.or.ddit.vo.ListingPackVO;

/**
 * @author developer_KCY
 */
public interface BrokerLstgService {
	public List<ListingPackVO> readLstgListByMbrCd(String mbrCd);
}
