package kr.or.ddit.broker.lstg.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerListingMapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ListingVO;

@Service
public class BrokerLstgServiceImpl implements BrokerLstgService{

	@Autowired
	BrokerListingMapper mapper;
	
	@Override
	public List<ListingVO> readLstgListByMbrCd(String mbrCd) {
		List<ListingVO> lstgList =  mapper.selectLstgListByMbrCd(mbrCd);
		List<CommonCodeVO> lstgTypeCode = mapper.selectLstgTypeCode();
		return lstgList;
	}

	@Override
	public ListingVO readLstgDetails(Map<String, String> lstgDetailParams) {
		ListingVO lstgDetails = mapper.selectLstgDetails(lstgDetailParams);
		return lstgDetails;
	}


}
