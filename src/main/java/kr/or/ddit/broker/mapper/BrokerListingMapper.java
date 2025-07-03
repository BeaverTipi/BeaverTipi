package kr.or.ddit.broker.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;

import kr.or.ddit.vo.ListingVO;

@Mapper
public interface BrokerListingMapper {

	
	public List<ListingVO> selectLstgListByMbrCd(String mbrCd);
}
