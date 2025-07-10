package kr.or.ddit.broker.lstg.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import kr.or.ddit.broker.mapper.BrokerListingMapper;
import kr.or.ddit.vo.CommonCodeVO;
import kr.or.ddit.vo.ListingPackVO;

@Service
public class BrokerLstgServiceImpl implements BrokerLstgService{

	@Autowired
	BrokerListingMapper mapper;
	
	@Override
	public List<ListingPackVO> readLstgListByMbrCd(String mbrCd) {
		List<ListingPackVO> lstgList =  mapper.selectLstgListByMbrCd(mbrCd);
		List<CommonCodeVO> lstgTypeCode = mapper.selectLstgTypeCode();
		return lstgList;
	}

	@Override
	public ListingPackVO readLstgDetails(Map<String, String> lstgDetailParams) {
		ListingPackVO lstgDetails = mapper.selectLstgDetails(lstgDetailParams);
		return lstgDetails;
	}

	@Override
	public List<ListingPackVO> readListingList(String mbrCd) {
		//Listing TABLE의 MBR_CD는 매물을 올린 회원을 가리킨다.
		//중개인이 자신이 올린 매물을 조회할 때 다른 검색조건은 필요 없다고 생각. 0708 15:30
		return null;
	}
	

}
