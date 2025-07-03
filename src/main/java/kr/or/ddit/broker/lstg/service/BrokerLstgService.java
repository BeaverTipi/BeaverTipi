package kr.or.ddit.broker.lstg.service;

import java.util.List;

import kr.or.ddit.vo.ListingVO;

public interface BrokerLstgService {
	public List<ListingVO> readLstgListByMbrCd(String mbrCd);
}
