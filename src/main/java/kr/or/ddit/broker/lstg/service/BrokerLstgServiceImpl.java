package kr.or.ddit.broker.lstg.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerListingMapper;
import kr.or.ddit.vo.ListingVO;

@Service
public class BrokerLstgServiceImpl implements BrokerLstgService{

	@Autowired
	BrokerListingMapper mapper;
	
	@Override
	public List<ListingVO> readLstgListByMbrCd(String mbrCd) {
		List<ListingVO> lstgList =  mapper.selectLstgListByMbrCd(mbrCd);
		return lstgList;
	}
	

}
